package fit;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

import org.apache.commons.lang3.ArrayUtils;

import com.google.common.collect.*;

import fit.compile.*;

import struct.inst.*;
import temp.*;

public class OutputModel {
	private final Output output;
	private final OutputFunction outFunction;

	private final List<Dataset> datasets;
	private Dataset simulation;

	private final BiMap<String, String> dimsToCols;
	private final BiMap<String, String> exosToCols;
	private final BiMap<String, String> outsToCols;

	private int[] dimsToColsIndex;
	private int[] exosToColsIndex;
	private int[] statesToColsIndex;

	private double[] modelParameters;
	private double[] outputParameters;

	private double[] dimensions;
	private double[] exogenous;
	private double[] state;

	private double[] outputs;
	
	int ndata;

	public OutputModel(Output output, List<Dataset> datasets, Dataset simulation, BiMap<String, String> dimsToCols, BiMap<String, String> exosToCols, BiMap<String, String> outsToCols, int... ndataset)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException {
		this.output = output;
		this.outFunction = ModelCompiler.outputToFunction(output);

		this.datasets = datasets;
		this.simulation = simulation;

		this.dimsToCols = dimsToCols;
		this.exosToCols = exosToCols;
		this.outsToCols = outsToCols;
		
		if (ndataset.length == 0){
			ndata = 0;
		} else {
			ndata = ndataset[0];
		}

		// Model Parameters
		this.modelParameters = new double[output.graph.reachParameters.size()];
		for (int i = 0; i < this.modelParameters.length; i++) {
			IC ic = output.graph.reachParameters.get(i).cons;
			if (ic.value != null) {
				this.modelParameters[i] = ic.value;
			} else {
				this.modelParameters[i] = Double.NaN;
			}
		}

		// Output Parameters
		this.outputParameters = new double[output.fitted.size()];
		for (int i = 0; i < this.outputParameters.length; i++) {
			OutputCons cons = output.fitted.get(i);
			if (cons.value != null) {
				this.outputParameters[i] = output.fitted.get(i).value;
			} else {
				this.outputParameters[i] = Double.NaN;
			}
		}

		// Dimensions Index
		this.dimsToColsIndex = new int[dimsToCols.size()];
		int j = 0;
		for (Entry<String, String> dim : dimsToCols.entrySet()) {
			int dimColIndex = datasets.get(ndata).getColIndex(dim.getValue());
			this.dimsToColsIndex[j] = dimColIndex;

			j++;
		}

		// Exogenous Index
		this.exosToColsIndex = new int[output.graph.reachExogenous.size()];
		for (int i = 0; i < this.exosToColsIndex.length; i ++) {
			int exoColIndex;
			if(output.graph.reachExogenous.get(i).var.getFullName().endsWith(".t")){
				exoColIndex = -1;
			}else{
				exoColIndex = datasets.get(ndata).getColIndex(exosToCols.get(output.graph.reachExogenous.get(i).var.getFullName()));
			}
			this.exosToColsIndex[i] = exoColIndex;
		}

		
		// State Variables Index
		
		//idea from ODEmodel
		//time is the first column
		
		j=1;
		BiMap<String, Integer> stateIndex = HashBiMap.create();
		for (IVNode state : output.graph.diffDifferential.valueList()) {
			stateIndex.put(state.var.getFullName(), j);
			j++;
		}
		
		
		//FIXME: Get statesToCols without simulation
		//Note: done but should be rechecked
		this.statesToColsIndex = new int[output.graph.diffDifferential.size()];
		for (int i = 0; i < this.statesToColsIndex.length; i++) {
			//int stateColIndex = simulation.getColIndex(output.graph.diffDifferential.get(i).var.getFullName());
			int stateColIndex = stateIndex.get(output.graph.diffDifferential.get(i).var.getFullName());
			this.statesToColsIndex[i] = stateColIndex;
		}

	
		
		this.dimensions = new double[this.dimsToColsIndex.length];
		this.exogenous = new double[this.exosToColsIndex.length];
		this.state = new double[this.statesToColsIndex.length];

		this.outputs = new double[this.output.variables.size()];

	}

	public void reset(Dataset simulation, double[] modelParameters, double[] outputParameters) {
		this.simulation = simulation;

		System.arraycopy(modelParameters, 0, this.modelParameters, 0, modelParameters.length);
		System.arraycopy(outputParameters, 0, this.outputParameters, 0, outputParameters.length);
	}
	
	public void setOutputParameters(double[] outputParameters){
		System.arraycopy(outputParameters, 0, this.outputParameters, 0, outputParameters.length);
	}

	public Dataset compute() {
		Double[][] simulated = new Double[dimensions.length + output.variables.size()][simulation.getNRows()];
		double[] outputs = new double[output.variables.size()];
		for (int i = 0; i < simulation.getNRows(); i++) {
			this.apply(i, outputs);
			for (int j = 0; j < dimensions.length; j++) {
				simulated[j][i] = dimensions[j];
			}
			for (int j = 0; j < outputs.length; j++) {
				simulated[j + dimensions.length][i] = outputs[j];
			}
		}

		String[] colNames = new String[dimensions.length + output.variables.size()];
		int k = 0;
		for (String dimName : dimsToCols.keySet()) {
			colNames[k] = dimName;

			k++;
		}
		for (int i = 0; i < output.variables.size(); i++) {
			colNames[i + dimensions.length] = output.variables.getKey(i);
		}
		Dataset allOutputs = new Dataset(simulated, colNames);

		return allOutputs;
	}
	public void apply(int index, double[] outputs) {
		// Dimensions
		for (int i = 0; i < this.dimensions.length; i++) {
			this.dimensions[i] = this.datasets.get(ndata).getElem(index, this.dimsToColsIndex[i]);
		}

		// Exogenous
		for (int i = 0; i < this.exogenous.length; i++) {
			if(this.exosToColsIndex[i] == -1){
				//WARNING: hardcoded time
				int timeColIndex = datasets.get(ndata).getColIndex(dimsToCols.get("time"));
				this.exogenous[i] = this.datasets.get(ndata).getElem(index, timeColIndex);
			}
			else{
				this.exogenous[i] = this.datasets.get(ndata).getElem(index, this.exosToColsIndex[i]);
			}
		}

		// State
		for (int i = 0; i < this.state.length; i++) {
			this.state[i] = this.simulation.getElem(index, this.statesToColsIndex[i]);
		}

		this.outFunction.output(dimensions, modelParameters, exogenous, state, outputParameters, outputs);

	}

	public OutputFunction getOutputFunction() {
		return this.outFunction;
	}

	public double[] getModelParameters() {
		return this.modelParameters;
	}

	public double[] getOutputParameters() {
		return this.outputParameters;
	}
}

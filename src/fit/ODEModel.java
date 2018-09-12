package fit;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

import org.bridj.*;

import com.google.common.collect.*;

import fit.compile.*;
import fit.jvode.bridj.cvode.*;
import fit.jvode.bridj.nvector.*;
import fit.jvode.bridj.sundials.*;

import struct.inst.*;
import temp.*;
import util.*;

/**
 * Contains all necessary information needed to simulate a system of ODEs
 * 
 * @author Darko
 * 
 */
public class ODEModel extends CvodeLibrary.CVRhsFn {
	private long tc1;
	private long tc2;
	private long tc3;
	private long tc4;
	private long tc5;
	private long tc6;
	private long tc7;

	private long tc;

	// Mappings

	// NameToIndex mappings for all model arguments
	private BiMap<String, Integer> dimensionsIndex = HashBiMap.create();
	private BiMap<String, Integer> parametersIndex = HashBiMap.create();
	private BiMap<String, Integer> exogenousIndex = HashBiMap.create();
	private BiMap<String, Integer> stateIndex = HashBiMap.create();

	// don't change during the simulation of the ODEModel
	private double[] initial;
	private double[] parameters;

	// change during the simulation of the ODEModel
	private double[] dimensions;
	private double[] exogenous;
	private double[] state;

	private double[] yDot; // derivatives returned from CVODE

	// mapping from index of exogenous variable to index of column in the
	// dataset
	private int[] exoToCols;

	// data sets and mappings to get values for exogenous variables
	private List<Dataset> datasets;
	private BiMap<String, String> dimsToCols;
	private BiMap<String, String> exosToCols;

	private double[] timeCol;
	private String timeName;

	private Pointer<_generic_N_Vector> yVector;
	public Pointer<Double> yVectorData;

	private boolean isFirst = true;
	private Pointer<Double> ydotData;

	/** The size of the ODE model in terms of number of equations **/
	private int problemSize;
	private ODEFunction ode;

	private int ndata;

	public ODEModel(IQGraph graph, List<Dataset> datasets,
			BiMap<String, String> dimsToCols, BiMap<String, String> exosToCols,
			int... ndataset) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, IOException {

		if (ndataset.length == 0) {
			ndata = 0;
		} else {
			ndata = ndataset[0];
		}

		this.problemSize = graph.diffDifferential.size() + graph.algAlgebraic.size();
		this.ode = ModelCompiler.iqGraphToODEFunction(graph);

		// Dimension ordering & initial values
		int i = 0;
		this.dimensions = new double[dimsToCols.size()];
		for (Entry<String, String> dim : dimsToCols.entrySet()) {
			this.dimensionsIndex.put(dim.getKey(), i);
			dimensions[i] = datasets.get(ndata).getElem(0, dim.getValue());

			i++;
		}

		// Parameter ordering & values
		i = 0;
		this.parameters = new double[graph.reachParameters.size()];
		for (Entry<String, ICNode> param : graph.reachParameters.entrySet()) {
			this.parametersIndex.put(param.getKey(), i);
			IC ic = param.getValue().cons;
			if (ic.value != null) {
				parameters[i] = ic.value;
			} else {
				parameters[i] = Double.NaN;
			}

			i++;
		}

		// Exogenous ordering
		i = 0;
		this.exogenous = new double[graph.reachExogenous.size()];
		for (Entry<String, IVNode> exo : graph.reachExogenous.entrySet()) {
			this.exogenousIndex.put(exo.getKey(), i);

			i++;
		}

		// State ordering and initial & state values
		i = 0;
		this.initial = new double[graph.diffDifferential.size()];
		this.state = new double[graph.diffDifferential.size()];
		for (IVNode state : graph.diffDifferential.valueList()) {
			this.stateIndex.put(state.var.getFullName(), i);
			if (state.var.initial != null) {
				this.initial[i] = state.var.initial;
				this.state[i] = state.var.initial;
			} else {
				this.initial[i] = Double.NaN;
				this.state[i] = Double.NaN;
			}

			i++;
		}

		this.datasets = datasets;
		this.dimsToCols = dimsToCols;
		this.exosToCols = exosToCols;

		this.exogenous = new double[exogenousIndex.size()];

		this.exoToCols = new int[exogenousIndex.size()];
		for (Entry<String, Integer> exoToIndex : exogenousIndex.entrySet()) {
			int exoIndex = exoToIndex.getValue();
			boolean istime = false;
			String colName = exosToCols.get(exoToIndex.getKey());
			if (colName == null) {
				if (exoToIndex.getKey().endsWith(".t")) {
					istime = true;
				} else {
					throw new ParsingException(String.format(Errors.ERRORS[80],
							exoToIndex.getKey()));
				}
			}
			Integer columnIndex;
			if (istime) {
				columnIndex = -1;
			} else {
				columnIndex = datasets.get(ndata).getColIndex(colName);
			}
			if (columnIndex == null) {
				throw new ParsingException(String.format(Errors.ERRORS[79],
						exoToIndex.getKey(), colName));
			}

			this.exoToCols[exoIndex] = columnIndex;
		}

		int nRow = this.datasets.get(ndata).getNRows();
		int tColIndex = this.datasets.get(ndata).getColIndex(
				dimsToCols.get("time"));

		timeName = dimsToCols.get("time");
		timeCol = new double[nRow];
		for (int j = 0; j < nRow; j++) {
			timeCol[j] = this.datasets.get(ndata).getElem(j, tColIndex);
		}

		this.yDot = new double[graph.diffDifferential.size()];

		this.yVector = NvectorLibrary.N_VNew_Serial(this.problemSize);
		_generic_N_Vector.arrayToNVector(initial, yVector);
		this.yVectorData = NvectorLibrary.N_VGetArrayPointer_Serial(yVector);

	}
	

	public void reset() {
		int i = 0;
		for (Entry<String, String> dim : dimsToCols.entrySet()) {
			dimensions[i] = datasets.get(ndata).getElem(0, dim.getValue());

			i++;
		}

		for (i = 0; i < this.initial.length; i++) {
			this.state[i] = this.initial[i];
		}

		_generic_N_Vector.arrayToNVector(initial, yVector);
		this.yVectorData = NvectorLibrary.N_VGetArrayPointer_Serial(yVector);

		this.isFirst = true;

	}

	public void reset(double[] parameters) {
		reset();
		System.arraycopy(parameters, 0, this.parameters, 0, parameters.length);
	}

	@Override
	public int apply(double t, Pointer<_generic_N_Vector> y,
			Pointer<_generic_N_Vector> ydot, Pointer<?> user_data) {
		long tt0 = System.nanoTime();

		if (isFirst) {
			this.ydotData = NvectorLibrary.N_VGetArrayPointer_Serial(ydot);
			isFirst = false;
		}

		// Dimensions
		this.dimensions[0] = t; // hard-coded time dimension. Should expand for
								// arbitrary dimensions.

		long tt1 = System.nanoTime();

		// State
		_generic_N_Vector.pointerToArray(this.yVectorData, state);

		long tt2 = System.nanoTime();
		// at this moment we assume that the parameters exist

		// compute the values of the exogenous
		int tIndex = Arrays.binarySearch(timeCol, t);

		long tt3 = System.nanoTime();
		if (tIndex >= 0) { // exact time exists
			for (int i = 0; i < this.exogenous.length; i++) {
				if (exoToCols[i] == -1) {
					this.exogenous[i] = this.dimensions[0];
				} else {
					this.exogenous[i] = datasets.get(ndata).getElem(tIndex,
							exoToCols[i]);
				}
			}

		} else if (-tIndex - 1 == timeCol.length) { // extrapolate after last
													// time point (by truncating
													// to last)
			int tIndexLast = -tIndex - 2;
			for (int i = 0; i < this.exogenous.length; i++) {
				if (exoToCols[i] == -1) {
					this.exogenous[i] = this.dimensions[0];
				} else {
					this.exogenous[i] = datasets.get(ndata).getElem(tIndexLast,
							exoToCols[i]);
				}
			}

		} else {// linear interpolation
			int tIndex1 = -(tIndex + 1) - 1;
			int tIndex2 = tIndex1 + 1;

			double t1 = timeCol[tIndex1];
			double t2 = timeCol[tIndex2];

			double ratio = (t - t1) / (t2 - t1);

			for (int i = 0; i < this.exogenous.length; i++) {
				if (exoToCols[i] == -1) {
					this.exogenous[i] = this.dimensions[0];
				} else {
					double exoT1 = datasets.get(ndata).getElem(tIndex1,
							exoToCols[i]);
					double exoT2 = datasets.get(ndata).getElem(tIndex2,
							exoToCols[i]);

					this.exogenous[i] = exoT1 + (exoT2 - exoT1) * ratio;
				}
			}
		}

		long tt4 = System.nanoTime();

		ode.yprime(dimensions, parameters, exogenous, state, this.yDot);

		long tt5 = System.nanoTime();
		this.ydotData = NvectorLibrary.N_VGetArrayPointer_Serial(ydot);
		_generic_N_Vector.arrayToPointer(this.yDot, ydotData);

		long tt6 = System.nanoTime();

		long tt7 = System.nanoTime();

		this.tc1 += tt1 - tt0;
		this.tc2 += tt2 - tt1;
		this.tc3 += tt3 - tt2;
		this.tc4 += tt4 - tt3;
		this.tc5 += tt5 - tt4;
		this.tc6 += tt6 - tt5;
		this.tc7 += tt7 - tt6;

		this.tc += tt6 - tt0;

		return 0;
	}

	public int size() {
		return this.problemSize;
	}

	public double getT0() {
		return timeCol[0];
	}

	public Pointer<_generic_N_Vector> getY() {
		return this.yVector;
	}

	public double[] getTimeCol() {
		return this.timeCol;
	}

	public String getTimeName() {
		return this.timeName;
	}

	public String[] getStateNames() {
		String[] toReturn = new String[this.state.length];
		for (int i = 0; i < toReturn.length; i++) {
			toReturn[i] = this.stateIndex.inverse().get(i);
		}
		return toReturn;
		// return this.stateIndex.keySet().toArray(new
		// String[stateIndex.size()]);
	}

	public ODEFunction getODEFunction() {
		return this.ode;
	}

	public double[] getParameters() {
		return this.parameters;
	}

	public void reset(double[] initialValues, double[] parameters) {

		int i = 0;
		for (Entry<String, String> dim : dimsToCols.entrySet()) {
			dimensions[i] = datasets.get(ndata).getElem(0, dim.getValue());

			i++;
		}

		System.arraycopy(initialValues, 0, this.initial, 0,
				initialValues.length);
		for (i = 0; i < this.initial.length; i++) {
			this.state[i] = this.initial[i];
		}

		_generic_N_Vector.arrayToNVector(initial, yVector);
		this.yVectorData = NvectorLibrary.N_VGetArrayPointer_Serial(yVector);

		this.isFirst = true;

		System.arraycopy(parameters, 0, this.parameters, 0, parameters.length);
	}
}

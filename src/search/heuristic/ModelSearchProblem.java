package search.heuristic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.logging.Logger;

import org.antlr.runtime.RecognitionException;
import org.apache.commons.lang3.ArrayUtils;

import com.google.common.collect.BiMap;

import fit.CVodeSimulator;
import fit.LinearSolver;
import fit.NonlinearSolver;
import fit.ODEModel;
import fit.ODESolver;
import fit.OutputModel;
import fit.objective.TrajectoryObjectiveFunction;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.util.JMException;
import struct.inst.IV;
import struct.inst.Model;
import temp.Dataset;
import temp.ExtendedModel;
import temp.IQGraph;
import temp.Output;
import util.FailedSimulationException;
import xml.CVODESpec;
import xml.FitterSpec;
import xml.InitialValuesSpec;
import xml.OutputSpec;

class ModelSearchProblem extends Problem {
	
	protected ExtendedModel extendedModel;
	
	protected HeuristicCodec codec;
	protected List<Dataset> datasets;
	protected OutputSpec outputSpec;
	protected BiMap<String, String> dimsToCols;
	protected BiMap<String, String> endosToCols;
	protected BiMap<String, String> exosToCols;
	protected BiMap<String, String> outsToCols;
	protected BiMap<String, String> weightsToCols;


	protected CVODESpec spec;
	protected FitterSpec fitterSpec;
	protected InitialValuesSpec initialValuesSpec;
	
	protected int populationSize;

	protected TrajectoryObjectiveFunction objFunction;

	protected static int count = 1;
	protected Logger logger = Logger.getLogger("fitPerformance");
	protected double minerror = Double.POSITIVE_INFINITY;
	
	
	protected TreeSet<PlateauModel> plateau;

	public ModelSearchProblem(ExtendedModel incompleteModel, OutputSpec outputSpec, List<Dataset> datasets,
			BiMap<String, String> dimsToCols, BiMap<String, String> endosToCols, BiMap<String, String> exosToCols,
			BiMap<String, String> outsToCols, BiMap<String, String> weightsToCols, CVODESpec spec,
			FitterSpec fitterSpec, InitialValuesSpec initialValuesSpec) {
		// things needed by the evaluation function
		this.datasets = datasets;
		this.outputSpec = outputSpec;
		this.dimsToCols = dimsToCols;
		this.endosToCols = endosToCols;
		this.exosToCols = exosToCols;
		this.outsToCols = outsToCols;
		this.weightsToCols = weightsToCols;
		this.fitterSpec = fitterSpec;
		this.spec = spec;
		this.initialValuesSpec = initialValuesSpec;
		
		this.extendedModel = incompleteModel;
		
		// Take care of the integer part of the problem
		codec = new GeneticCodec();
		codec.encode(incompleteModel, outputSpec);
		
		problemName_ = incompleteModel.getModel().id; // Name of the incomplete model
		
		plateau = new TreeSet<PlateauModel>();

	}

	@Override
	public void evaluate(Solution solution) throws JMException {
		throw new JMException("Evaluation not implemented. Specify a problem.");
	}
	
	
	//Utilities
	
	//modified from task
	protected List<Dataset> simulateModel(ExtendedModel em, List<Dataset> datasets)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, RecognitionException,
			FailedSimulationException, IOException {

		Model model = em.getModel();

		List<Dataset> outputDatas = new ArrayList<Dataset>();

		try {
			for (int i = 0; i < datasets.size(); i++) {

				for (String name : model.allVars.keySet()) {
					IV var = model.allVars.get(name);
					if (initialValuesSpec.usedatasetvalues) {
						String dsColName = endosToCols.get(name);
						if (dsColName != null) {
							var.initial = datasets.get(i).getElem(0, dsColName);
							model.allVars.put(name, var);
						}
					}
				}

				IQGraph graph = new IQGraph(model);
				Output output = new Output(outputSpec, graph);

				ODEModel odeModel = new ODEModel(graph, datasets, dimsToCols, exosToCols, i);
				CVodeSimulator simulator = new CVodeSimulator(ODESolver.BDF, NonlinearSolver.NEWTON);
				simulator.initialize(odeModel);
				simulator.setTolerances(spec.reltol, spec.abstol);
				simulator.setLinearSolver(LinearSolver.SPGMR);
				simulator.setMaxNumSteps(spec.steps);

				Dataset simulation = simulator.simulate();

				OutputModel outputModel = new OutputModel(output, datasets, simulation, dimsToCols, exosToCols,
						outsToCols, i);

				Double[] outputs = new Double[em.getOutputConstants().values().size()];
				em.getOutputConstants().values().toArray(outputs);
				outputModel.setOutputParameters(ArrayUtils.toPrimitive(outputs));

				outputDatas.add(outputModel.compute());

			}
			return outputDatas;
		} catch (FailedSimulationException e) {
			System.out.println("Simulation error: " + e.getMessage());
			return null;
		}

	}
	
	
	protected void filterPlateau() {
		
		Iterator<PlateauModel> pIterator = plateau.iterator();
		PlateauModel best = pIterator.next();
		while (pIterator.hasNext()) {
			PlateauModel next = pIterator.next();
			Double bestVal = best.eModel.getFitnessMeasures().get(objFunction.getName());
			Double nextVal = next.eModel.getFitnessMeasures().get(objFunction.getName());
			if (bestVal * 1.1 < nextVal) {
				break;
			}
			best = next;
		}
		
		//Create custom headview because treeset uses compareto instead of equals for this
		TreeSet<PlateauModel> plateau2 = new TreeSet<PlateauModel>();
		pIterator = plateau.iterator();
		while(pIterator.hasNext()) {
			PlateauModel p = pIterator.next();
			plateau2.add(p);
			if(p.equals(best)) break;
		}
		
		plateau = new TreeSet<PlateauModel>(plateau2);
	}
	
	public ArrayList<ExtendedModel> getPlateau() {
		ArrayList<ExtendedModel> toReturn = new ArrayList<ExtendedModel>();
		for (PlateauModel p : plateau)
			toReturn.add(p.eModel);
		return toReturn;
	}
	
	protected static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
		List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
			@Override
			public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});

		Map<K, V> result = new LinkedHashMap<>();
		for (Map.Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}
	
	public void setPopulationSize(int populationSize) {
		this.populationSize = populationSize;
	}
}

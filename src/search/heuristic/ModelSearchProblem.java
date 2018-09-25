package search.heuristic;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Logger;

import org.antlr.runtime.RecognitionException;
import org.apache.commons.io.FileUtils;
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
import jmetal.core.Variable;
import jmetal.encodings.variable.Int;
import jmetal.util.JMException;
import struct.inst.IV;
import struct.inst.Model;
import task.Task;
import temp.Dataset;
import temp.ExtendedModel;
import temp.IQGraph;
import temp.Output;
import util.FailedSimulationException;
import xml.CVODESpec;
import xml.FitterSpec;
import xml.InitialValuesSpec;
import xml.OutputSpec;
import xml.SearchSpec;

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
	protected SearchSpec searchSpec;
	
	protected int populationSize;

	protected TrajectoryObjectiveFunction objFunction;

	protected static int count = 1;
	protected Logger logger = Logger.getLogger("fitPerformance");
	protected static double minerror = Double.POSITIVE_INFINITY;
	
	protected File outdir;
	
	private double modifier;
	
	//Supporting two types of plateau structures temporarily
	//The Lite version is more memory efficient but operations with it are cpu intensive
	//Lite is used for all two level searches, Regular for single level
	
	protected final TreeSet<PlateauModel> plateau;
	protected final TreeSet<PlateauModelLite> plateauLite;
	
	

	public ModelSearchProblem(ExtendedModel incompleteModel, OutputSpec outputSpec, List<Dataset> datasets,
			BiMap<String, String> dimsToCols, BiMap<String, String> endosToCols, BiMap<String, String> exosToCols,
			BiMap<String, String> outsToCols, BiMap<String, String> weightsToCols, CVODESpec spec,
			FitterSpec fitterSpec, InitialValuesSpec initialValuesSpec, SearchSpec searchSpec, boolean enumerate) {
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
		this.searchSpec = searchSpec;
		
		this.extendedModel = incompleteModel;
		
		// Take care of the integer part of the problem
		codec = new GeneticCodec();
		codec.enumerate = enumerate;
		codec.encode(incompleteModel, outputSpec);
		
		problemName_ = incompleteModel.getModel().id; // Name of the incomplete model
		

		plateau = new TreeSet<PlateauModel>();
		
		
		plateauLite = new TreeSet<PlateauModelLite>();
		
		modifier = searchSpec.plateau;
		if(modifier<1) modifier += 1;

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
		
		//double modifier = searchSpec.plateau;
		//if(modifier<1) modifier += 1;
		
		int counter= 0;
		while (pIterator.hasNext()) {
			PlateauModel next = pIterator.next();

			if(modifier > 1 && modifier < 2) {
				Double bestVal = best.eModel.getFitnessMeasures().get(objFunction.getName());
				Double nextVal = next.eModel.getFitnessMeasures().get(objFunction.getName());
				if (bestVal * modifier < nextVal) {
					break;
				}
			} else {
				if(++counter > modifier) {
					break;
				}
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
		
		plateau.clear();
		plateau.addAll(plateau2);
	
	}
	
	
	protected void plateauFilterAndAdd(PlateauModel plateauModel) {
		
		double error = plateauModel.eModel.getFitnessMeasures().get(objFunction.getName());
		
		if(plateau.isEmpty()) plateau.add(plateauModel);
		else {
			double minError = plateau.first().eModel.getFitnessMeasures().get(objFunction.getName());
			double maxError = plateau.last().eModel.getFitnessMeasures().get(objFunction.getName());
			
			if (error <= minError) {
				//add at the beginning of the plateau
				if (error*modifier < minError) plateau.clear();
				plateau.add(plateauModel);
			} else {
				//add in the middle or at the end
				if(error <= maxError*modifier) plateau.add(plateauModel);
			}
		}
		
	}
	
	protected void plateauFilterAndAdd(PlateauModelLite plateauModel) {
			
			double error = plateauModel.error;
			
			if(plateauLite.isEmpty()) plateauLite.add(plateauModel);
			else {
				double minError = plateauLite.first().error;
				double maxError = plateauLite.last().error;
				
				if (error <= minError) {
					//add at the beginning of the plateau
					if (error*modifier < minError) plateauLite.clear();
					plateauLite.add(plateauModel);
				} else {
					//add in the middle or at the end
					if(error <= maxError*modifier) plateauLite.add(plateauModel);
				}
				
				//Make it so there is no more than 10000 models in the plateau at a time by removing the worst models
				while(plateauLite.size() > 10000) plateauLite.pollLast();
			}
			
		}
	
	
	public ArrayList<ExtendedModel> getPlateau() {
		ArrayList<ExtendedModel> toReturn = new ArrayList<ExtendedModel>();
		if(plateau.isEmpty()) {
			for(PlateauModelLite p: plateauLite) {
				Variable[] s = new Variable[p.structure.length];
				for(int i=0; i<p.structure.length; i++)	s[i] = new Int(p.structure[i], p.structure[i], p.structure[i]);
				
				ExtendedModel eModel = codec.decode(s);
				for(String ivName : p.initials.keySet()) {
					if(eModel.getModel().allVars.containsKey(ivName))
						eModel.getModel().allVars.get(ivName).initial = p.initials.get(ivName);
				}
				for(String icName : p.params.keySet()) {
					if(eModel.getModel().allConsts.containsKey(icName))
						eModel.getModel().allConsts.get(icName).value = p.params.get(icName);
				}
				
				eModel.setOutputConstants(p.outputConsts);
				
				toReturn.add(eModel);
			}
		} else {
			for (PlateauModel p : plateau)
				toReturn.add(p.eModel);
		}
		return toReturn;
	}
	
	protected <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
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
	
	public void setTempOut(File outdir) {
		this.outdir = outdir;
	}
	
	protected void writePlateau() {
		if(outdir != null) {
			int counter = 1;
			PrintStream out = null;
			try {
				out = new PrintStream(FileUtils.openOutputStream(new File(outdir + "/Models_temp.out")));
				
				if(plateau.isEmpty()) {
					
					if (plateauLite.size() > 10000) {
						out.println("Plateau size too large " + plateauLite.size());
					} else {
					
						for(PlateauModelLite p: plateauLite) {
							Variable[] s = new Variable[p.structure.length];
							for(int i=0; i<p.structure.length; i++)	s[i] = new Int(p.structure[i], p.structure[i], p.structure[i]);
							
							ExtendedModel eModel = codec.decode(s);
							for(String ivName : p.initials.keySet()) {
								if(eModel.getModel().allVars.containsKey(ivName))
									eModel.getModel().allVars.get(ivName).initial = p.initials.get(ivName);
							}
							for(String icName : p.params.keySet()) {
								if(eModel.getModel().allConsts.containsKey(icName))
									eModel.getModel().allConsts.get(icName).value = p.params.get(icName);
							}
							
							
							eModel.setOutputConstants(p.outputConsts);
							
							out.println("// Model" + (counter++));
							out.println(eModel);
							out.flush();
						}
					}
					
				} else {
			
					for (ExtendedModel model : getPlateau()) {
						out.println("// Model" + (counter++));
						out.println(model);
						out.flush();
					}
				}
				
				out.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			finally {
				if(out != null) out.close();
			}
			
		}
	}
	
	
}

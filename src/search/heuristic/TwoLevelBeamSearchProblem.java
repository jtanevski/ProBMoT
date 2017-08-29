package search.heuristic;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import java.util.concurrent.TimeUnit;

import org.antlr.runtime.RecognitionException;

import com.google.common.collect.BiMap;

import fit.objective.ObjectiveProblem;
import fit.objective.RMSEMultiDataset;
import fit.objective.TrajectoryObjectiveFunction;
import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.SolutionSet;
import jmetal.core.Variable;
import jmetal.encodings.variable.Int;
import jmetal.metaheuristics.singleObjective.differentialEvolution.DE;
import jmetal.operators.crossover.CrossoverFactory;
import jmetal.operators.selection.SelectionFactory;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import task.Task;
import temp.Dataset;
import temp.ExtendedModel;
import temp.IQGraph;
import temp.IVNode;
import temp.Output;
import util.FailedSimulationException;
import xml.CVODESpec;
import xml.DESpec;
import xml.FitterSpec;
import xml.InitialValuesSpec;
import xml.OutputSpec;
import xml.SearchSpec;

public class TwoLevelBeamSearchProblem extends ModelSearchProblem{
	
	private int cLength;
	private int beamWidth;
	
	private TreeSet<VariableArray> seen;
	
	
	//TODO: Might be buggy. Is the pruning sufficient?
	public TwoLevelBeamSearchProblem(ExtendedModel extendedModel, OutputSpec outputSpec, List<Dataset> datasets,
			BiMap<String, String> dimsToCols, BiMap<String, String> endosToCols, BiMap<String, String> exosToCols,
			BiMap<String, String> outsToCols, BiMap<String, String> weightsToCols, CVODESpec spec,
			FitterSpec fitterSpec, InitialValuesSpec initialValuesSpec, SearchSpec searchSpec, boolean enumerate) {

		super(extendedModel,outputSpec,datasets,dimsToCols,endosToCols, exosToCols, outsToCols, weightsToCols, spec, fitterSpec, initialValuesSpec, searchSpec, enumerate);
		
		cLength = codec.code.size();

		numberOfVariables_ = cLength;

		// Limits and ordering
		lowerLimit_ = new double[numberOfVariables_];
		upperLimit_ = new double[numberOfVariables_];

		// model variables
		for (int i = 0; i < cLength; i++) {
			lowerLimit_[i] = 1;
			upperLimit_[i] = codec.code.get(i);
		}
		
		//default beam width
		beamWidth = cLength;

		seen = new TreeSet<VariableArray>();
		
	}
	
	public void execute() throws JMException, InterruptedException{
		//generate random solution
		Variable[] initialState = new Variable[numberOfVariables_];
		
		//the initial solution is used to generate a set of neighbors to be considered at the start of the search. not seen.
		for(int i=0; i<numberOfVariables_; i++) {
			initialState[i] = new Int(PseudoRandom.randInt((int)lowerLimit_[i], (int)upperLimit_[i]),(int)lowerLimit_[i], (int)upperLimit_[i]);
		}
		
		LinkedList<Variable[]> initialList = new LinkedList<Variable[]>();
		initialList.add(initialState);
		
		search(initialList);
		
	}
	
	private void search(LinkedList<Variable[]> states) throws JMException, InterruptedException {
		
		//shared memory
		final Map<Variable[], Double> beam = Collections.synchronizedMap(new HashMap<Variable[], Double>());
		
		LinkedList<Variable[]> nextStep = states;
		PlateauModel best = null;
		
		boolean useConvergence = true;
		
		int noImprovement = 0;
		int convergenceStop = 10;
		ExecutorService executor;
		
		while(true) {
			//breadth first
			beam.clear();
			executor = Executors.newFixedThreadPool(Math.max(2,searchSpec.threads));
			
			for(Variable[] state: nextStep) {
				LinkedList<Variable[]> neighbors = generateNeighbors(state);
				
							
				for(final Variable[] model : neighbors) {
					executor.execute(new Runnable() {

						@Override
						public void run(){
							Double heuristic = Double.MAX_VALUE;
							try {
							 heuristic = evaluate(model);
							} catch (JMException e) {}

							beam.put(model, heuristic);
							
						}
					});
					
				}
				
			}
			
			//Better implementation would require Java 8
			executor.shutdown();
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
			
			//fully explored stop criterion
			if(beam.size() == 0) { logger.info("Search completed. Beam is empty."); break; }
			
			//convergence stop criterion
			if (best != null && best.equals(plateau.first())) {
				noImprovement++;
				logger.info("Search reports no improvement in the last " + noImprovement + " search step(s).");
				if(useConvergence && (noImprovement >= convergenceStop)) {
					logger.info("Search completed. No improvement in " + convergenceStop + " conscutive search steps.");
					break;
				}
			} else {
				noImprovement = 0;
				best = plateau.first();
			}
			
			//concurrency reasons
			Map<Variable[], Double> beams = sortByValue(beam);
			beam.clear();
			beam.putAll(beams);
			
			nextStep.clear();
	
			int c=0;
			for(Variable[] candidate : beam.keySet()){
				nextStep.add(candidate);
				if((++c)>=beamWidth) break;
			}
			
			
		}
		
	}
	
	//refinement operator
	//we define immediate neighbors as genotypes with manhattan distance 1
	private LinkedList<Variable[]> generateNeighbors(Variable[] state) throws JMException {
		LinkedList<Variable[]> neighbors = new LinkedList<Variable[]>();
		
		for(int i=0; i<state.length; i++) {
			Variable v = state[i];
			if((v.getValue()-1) >= v.getLowerBound()) {
				Variable[] neighbor = Arrays.copyOf(state, state.length);
				neighbor[i] = new Int((int)v.getValue()-1, (int)v.getLowerBound(), (int)v.getUpperBound());
				
				VariableArray vaCandidate = new VariableArray(neighbor);
				if(!seen.contains(vaCandidate)) {
					neighbors.add(neighbor);
					seen.add(vaCandidate);
				}
			}
			 
			if((v.getValue()+1) <= v.getUpperBound()) {
				Variable[] neighbor = Arrays.copyOf(state, state.length);
				neighbor[i] = new Int((int)v.getValue()+1, (int)v.getLowerBound(), (int)v.getUpperBound());
				
				VariableArray vaCandidate = new VariableArray(neighbor);
				if(!seen.contains(vaCandidate)) {
					neighbors.add(neighbor);
					seen.add(vaCandidate);
				}
			}
		}
		
		return neighbors;
	}
	
	public double evaluate(Variable[] structure) throws JMException {
		
		ExtendedModel model = codec.decode(structure);
		boolean failed = false;

		Output output = null;
		Double error = 0.0;
		
		Double toReturn = null;
		
		try {

			IQGraph graph = new IQGraph(model.getModel());

			output = new Output(outputSpec, graph);

			Algorithm algorithm = null;
			DESpec spec = (DESpec) fitterSpec;

			TrajectoryObjectiveFunction objectiveFun;
			if (spec.objectives.size() == 0) {
				objectiveFun = new RMSEMultiDataset(datasets, outsToCols);
			} else if (spec.objectives.get(0).contains("WRMSE")) {
				objectiveFun = (TrajectoryObjectiveFunction) (Class.forName("fit.objective." + spec.objectives.get(0))
						.getConstructor(List.class, BiMap.class, BiMap.class)
						.newInstance(datasets, outsToCols, weightsToCols));
			} else {
				objectiveFun = (TrajectoryObjectiveFunction) (Class.forName("fit.objective." + spec.objectives.get(0))
						.getConstructor(List.class, BiMap.class).newInstance(datasets, outsToCols));
			}

			this.objFunction = objectiveFun;

			ObjectiveProblem problem = new ObjectiveProblem(output, objectiveFun, datasets, dimsToCols, endosToCols,
					exosToCols, outsToCols, this.spec, initialValuesSpec);

			algorithm = new DE(problem);
			problem.setPopulationSize(spec.population);

			HashMap<String, Object> parameters;
			Operator crossover; // Crossover operator
			Operator selection; // Selection operator

			algorithm.setInputParameter("populationSize", spec.population);

			// problem.getNumberOfVariables() contains the number of all variables
			// that need fitting: initial + output + model parameters. no need to
			// recalculate!
			Integer max_evals = spec.evaluations * (problem.getNumberOfVariables());

			algorithm.setInputParameter("maxEvaluations", max_evals);

			// Crossover operator
			parameters = new HashMap<String, Object>();
			parameters.put("CR", spec.Cr);
			parameters.put("F", spec.F);
			parameters.put("DE_VARIANT", spec.strategy.toString());

			crossover = CrossoverFactory.getCrossoverOperator("DifferentialEvolutionCrossover", parameters);

			// Add the operators to the algorithm
			parameters = null;
			selection = SelectionFactory.getSelectionOperator("DifferentialEvolutionSelection", parameters);

			algorithm.addOperator("crossover", crossover);
			algorithm.addOperator("selection", selection);

			// Execute the optimization

			SolutionSet population = null;
			if(max_evals>0) {
				population = algorithm.execute();
				Variable[] variables = population.get(0).getDecisionVariables();
	
				for (int i = 0; i < problem.initialIndexes.size(); i++) {
					int indexval = problem.initialIndexes.get(i).intValue();
					String ivName = problem.initialFitted.get(indexval).id;
					Double iVal = variables[i + problem.initialIndexes.size()].getValue();
					model.getModel().allVars.get(ivName).initial = iVal;
				}
	
				for (int i = 0; i < problem.modelFitted.size(); i++) {
					String icName = problem.modelFitted.get(i).id;
					Double icValue = variables[problem.totalInitialToFit + i].getValue();
					model.getModel().allConsts.get(icName).value = icValue;
				}
	
				Map<String, Double> outputConsts = new LinkedHashMap<String, Double>();
				for (int i = 0; i < problem.outputFitted.size(); i++) {
					String outputName = output.fitted.getKey(i);
					Double outputValue = variables[problem.totalInitialToFit + problem.modelFitted.size() + i].getValue();
	
					outputConsts.put(outputName, outputValue);
				}
	
				model.setOutputConstants(outputConsts);
			}
			
			List<Dataset> simulations = null;
			try {
				simulations = simulateModel(model, datasets);
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException
					| FailedSimulationException | RecognitionException | IOException e) {
				logger.warning("Fitted model simulation failed");
			}
			model.setSimulations(simulations);

			Map<String, Double> fitnessMeasures = new HashMap<String, Double>();

			if (max_evals > 0) {
				fitnessMeasures.put(objectiveFun.getName(), population.get(0).getObjective(0));
				error = population.get(0).getObjective(0);
			} else {
				if (simulations == null) {
					fitnessMeasures.put(objectiveFun.getName(), Double.POSITIVE_INFINITY);
				} else {
					for (int i = 0; i < simulations.size(); i++) {
						error += objectiveFun.evaluateTrajectory(simulations.get(i), i);
					}
					fitnessMeasures.put(objectiveFun.getName(), error);
				}
			}

			model.setFitnessMeasures(fitnessMeasures);


		} catch (Exception e) {
			logger.warning("Something went wrong. A model evaluation failed.");
			failed = true;
		}
		

		if (failed) {
			error = Double.POSITIVE_INFINITY;
			toReturn = Double.POSITIVE_INFINITY;
		} else {
			// Regularize the objective function!
			double comp = 0;

			// using number of parameters
			if (searchSpec.regularization.contains("param")) {
				comp = output.graph.reachParameters.size();
				if (codec.enumerate) {
					comp /= codec.internalEnumeratingCodec.pCompHigh;
				}
			}

			if (searchSpec.regularization.contains("frag")) {
				for (IVNode var : output.graph.reachVariables.valueList())
					comp += var.inputIQs.size();
				if (codec.enumerate) {
					comp /= codec.internalEnumeratingCodec.fCompHigh;
				}
			}

			double lambda = searchSpec.lambda;
			if (lambda > 1 || lambda < 0)
				lambda = 0.5;
			error = lambda * error + (1 - lambda) * comp;

			toReturn = error;
			
			synchronized (plateau) {
				plateau.add(new PlateauModel(structure, model));
				filterPlateau();
				
				if (error < minerror) {
					minerror = error;
				}
			}
			

		}
		
		
		
		synchronized (plateau) {
			if (count % cLength == 0) {
				Task.logger.debug("Evaluation calls: " + count + " - " + "minerror=" + minerror + " - " + plateau.size()
						+ " models in the plateau");
			}

			if (count % cLength == 0) {
				logger.info("Evaluation calls: " + count + " - " + "minerror=" + minerror + " - " + plateau.size()
						+ " models in the plateau");
			}

			count++;
		}

		return toReturn;

	}

	public void setbeamWidth(int beamWidth) {
		this.beamWidth = beamWidth;
	}
	
	
	//Implemented due to the seen TreeSet
	class VariableArray implements Comparable<VariableArray>{

		Variable[] structure;
		
		public VariableArray(Variable[] structure) {
			this.structure = structure;
		}
		
		@Override
		public boolean equals(Object v) {
			return (this.compareTo((VariableArray)v) == 0);
		}
		
		@Override
		public int compareTo(VariableArray v) {
			for(int i=0; i<v.structure.length; i++) {
				try {
					if(this.structure[i].getValue() < v.structure[i].getValue()) return -1;
					if(this.structure[i].getValue() > v.structure[i].getValue()) return 1;
				} catch (JMException e) {
					return 1;
				}
			}
			return 0;
		}
	}

}

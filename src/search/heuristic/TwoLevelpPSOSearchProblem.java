package search.heuristic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.antlr.runtime.RecognitionException;
import org.apache.commons.lang3.tuple.Pair;

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

public class TwoLevelpPSOSearchProblem extends ModelSearchProblem{
	
	private int cLength;
	private int maxIterations;
	
	
	private ArrayList<PlateauModelLite> structureCache;
	private int qCapacity;
	
	private ArrayList<Variable[]> population;
	private ArrayList<Pair<Variable[],Double>> bestPopulation;
	private ArrayList<Double[]> velocities;
	
	private Variable[] globalBest;
	
	
	//TODO: Might be buggy. Is the pruning sufficient?
	public TwoLevelpPSOSearchProblem(ExtendedModel extendedModel, OutputSpec outputSpec, List<Dataset> datasets,
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
		
		//default population size
		populationSize = cLength;
		maxIterations = 100;
		qCapacity = 1000;

		
		structureCache = new ArrayList<PlateauModelLite>();
		population = new ArrayList<Variable[]>();
		bestPopulation = new ArrayList<Pair<Variable[],Double>>();
		velocities = new ArrayList<Double[]>();
		
		globalBest = null;
	}
	
	//contemporary inertia weighted PSO with default parameters, gbest
	public void execute() throws JMException, InterruptedException{
		
		for(int p=0; p<populationSize; p++) {
			//generate random solution
			Variable[] randomState = new Variable[numberOfVariables_];
			Double[] velocity = new Double[numberOfVariables_];
			
			for(int i=0; i<numberOfVariables_; i++) {
				randomState[i] = new Int(PseudoRandom.randInt((int)lowerLimit_[i], (int)upperLimit_[i]),(int)lowerLimit_[i], (int)upperLimit_[i]);
				velocity[i] = 0.0;
			}
			
			//add to population
			population.add(randomState);
			bestPopulation.add(Pair.of(randomState, Double.MAX_VALUE));
			velocities.add(velocity);
			
		}
		
		//constriction factor
		double omega = 0.729;
		double c = 2.05;
		
		
		
		ExecutorService executor;
		final List<Pair<Variable[], Double>> evaluatedPopulation = Collections.synchronizedList(new ArrayList<Pair<Variable[],Double>>(populationSize));
		//dummy elements
		for(int i=0; i<populationSize; i++) evaluatedPopulation.add(Pair.of(population.get(i),Double.MAX_VALUE));
		
		for(int i=0; i<maxIterations; i++) {
			executor = Executors.newFixedThreadPool(Math.max(2,searchSpec.threads));
			//evaluate population
			for(int p=0; p<populationSize; p++) {
				Variable[] model = population.get(p);
				int index = p;
				
				executor.execute(new Runnable() {
					@Override
					public void run(){
						Double heuristic = Double.MAX_VALUE;
						try {
						 heuristic = evaluate(model);
						} catch (JMException e) {}
						
						evaluatedPopulation.set(index, Pair.of(model, heuristic));
					}
				});
			}
			
			//Better implementation would require Java 8
			executor.shutdown();
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
			
			for(int p=0; p<populationSize; p++) {
				Pair<Variable[], Double> currentBest = bestPopulation.get(p);
				Pair<Variable[], Double> candidate = evaluatedPopulation.get(p);
				if(candidate.getValue() < currentBest.getValue()) bestPopulation.set(p, candidate);
			}
			
			
			//update velocities
			//update population
			for(int p=0; p<populationSize; p++) {
				Variable[] model = population.get(p);
				Double[] velocity = velocities.get(p);
				Variable[] localBest = bestPopulation.get(p).getKey();
				
				for(int v=0; v<numberOfVariables_; v++) {
					velocity[v] = omega*(velocity[v] + PseudoRandom.randDouble(0, c)*(localBest[v].getValue() - model[v].getValue()) + PseudoRandom.randDouble(0,c)* (globalBest[v].getValue() - model[v].getValue()));
					model[v].setValue(Math.round(model[v].getValue() + velocity[v]));
				}
				
				population.set(p, model);
				velocities.set(p, velocity);
			}
			
			
			
		}
	
		
	}
	

	public void setMaxIterations(int maxIterations) {
		this.maxIterations = maxIterations;
	}
	
	public void setQCapacity(int qCapacity) {
		this.qCapacity = qCapacity;
	}
	

	public double evaluate(Variable[] structure) throws JMException {
		Double toReturn = null;
		
		int found = -1;
		
		PlateauModelLite testLite = new PlateauModelLite(structure, null, null, null, Double.MAX_VALUE);
		for(int i=0; i<structureCache.size(); i++) {
			if(structureCache.get(i).equals(testLite)) {
				found = i;
				break;
			}
		}
		
		if(found > -1) {
			synchronized (structureCache) {
				PlateauModelLite fModel = structureCache.get(found); 
				toReturn = fModel.error;
				structureCache.remove(found);
				structureCache.add(fModel);
			}
		} else {

			ExtendedModel model = codec.decode(structure);
			boolean failed = false;
	
			Output output = null;
			Double error = 0.0;
			
			
			
			Map<String,Double> initials = new LinkedHashMap<String,Double>();
			Map<String,Double> params = new LinkedHashMap<String,Double>();
			Map<String, Double> outputConsts = new LinkedHashMap<String, Double>();
			
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
						initials.put(ivName, iVal);
					}
		
					for (int i = 0; i < problem.modelFitted.size(); i++) {
						String icName = problem.modelFitted.get(i).id;
						Double icValue = variables[problem.totalInitialToFit + i].getValue();
						model.getModel().allConsts.get(icName).value = icValue;
						params.put(icName, icValue);
					}
		
					
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
				System.out.println(e.getMessage());
				failed = true;
			}
			
	
			if (failed) {
				error = Double.POSITIVE_INFINITY;
				toReturn = Double.POSITIVE_INFINITY;
				
				Map<String, Double> fitnessMeasures = model.getFitnessMeasures();
				fitnessMeasures.put(objFunction.getName(), error);
				model.setFitnessMeasures(fitnessMeasures);
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
				
				
				//update fitness
				Map<String, Double> fitnessMeasures = model.getFitnessMeasures();
				fitnessMeasures.put(objFunction.getName(), error);
				model.setFitnessMeasures(fitnessMeasures);
	
				toReturn = error;
				
				synchronized(structureCache) {
					while(structureCache.size() > qCapacity) structureCache.remove(0);
					structureCache.add(new PlateauModelLite(structure, initials, params, outputConsts, error));
				}
				
				
				synchronized (plateauLite) {
					
					//plateauFilterAndAdd(new PlateauModel(structure, model));
					plateauFilterAndAdd(new PlateauModelLite(structure, initials, params, outputConsts, error));
					//plateau.add(new PlateauModel(structure, model));
					//filterPlateau();
					
					if (error < minerror) {
						minerror = error;
						Variable[] str = new Variable[cLength];
						for(int s=0; s<structure.length; s++) str[s] = new Int((int)structure[s].getValue(), (int)structure[s].getLowerBound(), (int)structure[s].getUpperBound());
						globalBest = str;
					}
				}
				
	
			}
		}
		
		synchronized (plateauLite) {
			if (count % cLength == 0) {
				Task.logger.debug("Evaluation calls: " + count + " - " + "minerror=" + minerror + " - " + plateauLite.size()
						+ " models in the plateau");
				logger.info("Evaluation calls: " + count + " - " + "minerror=" + minerror + " - " + plateauLite.size()
				+ " models in the plateau");
			}
			
			//save state every 100 evaluations
			//currently just the plateau
			if(count % 100 == 0) writePlateau();

			count++;
		}
		
		

		return toReturn;

	}

	
	//Implemented for queue operations
	class VariableArray implements Comparable<VariableArray>{

		Variable[] structure;
		double heuristic = Double.MAX_VALUE;
		
		public VariableArray(Variable[] structure) {
			this.structure = structure;
		}
		
		public VariableArray(Variable[] structure, double heuristic) {
			this.structure = structure;
			this.heuristic = heuristic;
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

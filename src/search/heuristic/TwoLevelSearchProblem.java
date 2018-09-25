package search.heuristic;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.antlr.runtime.RecognitionException;

import com.google.common.collect.BiMap;

import fit.objective.ObjectiveProblem;
import fit.objective.RMSEMultiDataset;
import fit.objective.TrajectoryObjectiveFunction;
import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.core.Variable;
import jmetal.encodings.solutionType.IntSolutionType;
import jmetal.metaheuristics.singleObjective.differentialEvolution.DE;
import jmetal.operators.crossover.CrossoverFactory;
import jmetal.operators.selection.SelectionFactory;
import jmetal.util.JMException;
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

public class TwoLevelSearchProblem extends ModelSearchProblem {

	private int cLength;

	private Map<PlateauModelLite, Double> seen;

	public TwoLevelSearchProblem(ExtendedModel extendedModel, OutputSpec outputSpec, List<Dataset> datasets,
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

		this.numberOfConstraints_ = 0; // No constraints
		this.numberOfObjectives_ = 1; // Single-objective optimization


		// Integer solution
		solutionType_ = new IntSolutionType(this);

		seen = Collections.synchronizedMap(new HashMap<PlateauModelLite, Double>());

	}

	@Override
	public void evaluate(Solution solution) throws JMException {
		Variable[] structure = solution.getDecisionVariables();
		ExtendedModel model = codec.decode(structure);
		boolean failed = false;

		// Careful! The seen map should not be sorted because the fitness of the empty
		// extendedmodel is null. It can be used to efficiently store seen structures
		// only.
		//PlateauModel isSeen = new PlateauModel(structure, model);
		
		Map<String,Double> initials = new LinkedHashMap<String,Double>();
		Map<String,Double> params = new LinkedHashMap<String,Double>();
		Map<String, Double> outputConsts = new LinkedHashMap<String, Double>();
		
		PlateauModelLite isSeen = new PlateauModelLite(structure, null, null, null, -1);
	
		if (seen.containsKey(isSeen)) {
			solution.setObjective(0, seen.get(isSeen));
			return;
		} else {
			seen.put(isSeen, Double.POSITIVE_INFINITY);
		}

		Output output = null;
		Double error = 0.0;
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
			failed = true;
		}

		if (failed) {
			error = Double.POSITIVE_INFINITY;
			solution.setObjective(0, Double.POSITIVE_INFINITY);
			
			//update fitness
			Map<String, Double> fitnessMeasures = model.getFitnessMeasures();
			fitnessMeasures.put(objFunction.getName(), error);
			model.setFitnessMeasures(fitnessMeasures);
		} else {
			// Regularize the objective function!
			double comp = 0;			
			
			//using number of parameters
			if(searchSpec.regularization.contains("param")) {
				comp = output.graph.reachParameters.size();
				if(codec.enumerate) {
					comp /= codec.internalEnumeratingCodec.pCompHigh;
				}
			}
			
			// or fragments
			if(searchSpec.regularization.contains("frag")) {
				for(IVNode var : output.graph.reachVariables.valueList()) comp += var.inputIQs.size();
				if(codec.enumerate) {
					comp /= codec.internalEnumeratingCodec.fCompHigh;
				}
			}
			
			double lambda = searchSpec.lambda;
			if(lambda > 1 || lambda < 0) lambda = 0.5;
			error = lambda*error+(1-lambda)*comp;
			
			solution.setObjective(0, error);
			
			//update fitness
			Map<String, Double> fitnessMeasures = model.getFitnessMeasures();
			fitnessMeasures.put(objFunction.getName(), error);
			model.setFitnessMeasures(fitnessMeasures);
			
			seen.replace(isSeen, error);

			

			synchronized (plateauLite) {
				
				plateauFilterAndAdd(new PlateauModelLite(structure, initials,params, outputConsts, error));
				
//				plateau.add(new PlateauModel(structure, model));
//			
//				filterPlateau();
			}

			if (error < minerror) {
				minerror = error;
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
			if(count % 10000 == 0) writePlateau();

			count++;
		}

	}

}

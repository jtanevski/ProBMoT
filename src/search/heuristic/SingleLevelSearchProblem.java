package search.heuristic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

import org.antlr.runtime.RecognitionException;

import com.google.common.collect.BiMap;

import fit.CVodeSimulator;
import fit.LinearSolver;
import fit.NonlinearSolver;
import fit.ODEModel;
import fit.ODESolver;
import fit.OutputModel;
import fit.objective.TrajectoryObjectiveFunction;
import temp.Dataset;
import temp.ExtendedModel;
import temp.ICNode;
import temp.IQGraph;
import temp.IVNode;
import temp.Output;
import temp.OutputCons;
import util.FailedSimulationException;
import xml.CVODESpec;
import xml.InitialValuesSpec;
import xml.OutputSpec;
import xml.SearchSpec;
import jmetal.core.Solution;
import jmetal.core.Variable;
import jmetal.encodings.solutionType.IntRealSolutionType;
import jmetal.util.JMException;
import task.Task;


//TODO: Migrate to jMetal 5.0 and make it multi-threaded?
//This version is unable to handle mixed integer problems well

public class SingleLevelSearchProblem extends ModelSearchProblem {
	
	private ArrayList<CVodeSimulator> simulators;
	private ArrayList<ODEModel> odeModels;
	private ArrayList<OutputModel> outputModels;
	
	private CVodeSimulator simulator;
	
	private int cLength, iLength, pLength, oLength;
	
	private Dataset simulation, outputData;
	
	public SingleLevelSearchProblem(ExtendedModel extendedModel, OutputSpec outputSpec, TrajectoryObjectiveFunction objFunction, List<Dataset> datasets,
			BiMap<String, String> dimsToCols, BiMap<String, String> exosToCols,
			BiMap<String, String> outsToCols, CVODESpec spec, InitialValuesSpec initialValuesSpec, SearchSpec searchSpec, boolean enumerate) {
		
		super(extendedModel,outputSpec,datasets,dimsToCols, null, exosToCols, outsToCols, null, spec, null, initialValuesSpec, searchSpec, enumerate);
		
		//things needed by the evaluation function
		cLength = codec.code.size();
		pLength = codec.internalEnumeratingCodec.params.size();
		oLength = codec.internalEnumeratingCodec.outputs.size();
		
		//Take care of the real part of the problem
		if(initialValuesSpec.sameforalldatasets){
			iLength = codec.internalEnumeratingCodec.initials.size();
		} else {
			iLength = codec.internalEnumeratingCodec.initials.size()*datasets.size();
		}
		
		numberOfVariables_ = cLength + iLength + pLength + oLength;
		
		//Limits and ordering
		lowerLimit_ = new double[numberOfVariables_];
		upperLimit_ = new double[numberOfVariables_];
		
		//model variables
		for(int i=0; i<cLength; i++) {
			lowerLimit_[i] = 1;
			upperLimit_[i] = codec.code.get(i);
		}
		
		//TODO: currently ignoring values from dataset setting
		//initial variables
		int mult = 1;
		if(!initialValuesSpec.sameforalldatasets){
			mult = datasets.size();
		}

		int t = 0;
		for (String s : codec.internalEnumeratingCodec.initials.keyList()) {
			for(int j=0; j<mult; j++){
				IVNode ivn = codec.internalEnumeratingCodec.initials.get(s);
				lowerLimit_[cLength + t + codec.internalEnumeratingCodec.initials.size()*j] = ivn.var.range.getLower();
				upperLimit_[cLength + t + codec.internalEnumeratingCodec.initials.size()*j] = ivn.var.range.getUpper();
				t++;
			}
		}
		
		
		//parameters
		t=cLength + codec.internalEnumeratingCodec.initials.size()*mult;
		for(String s : codec.internalEnumeratingCodec.params.keyList()) {
			ICNode icn = codec.internalEnumeratingCodec.params.get(s);
			lowerLimit_[t] = icn.cons.range.getLower();
			upperLimit_[t++] = icn.cons.range.getUpper();
		}
		
		//outputs
		for(String s: codec.internalEnumeratingCodec.outputs.keyList()) {
			OutputCons oc = codec.internalEnumeratingCodec.outputs.get(s);
			lowerLimit_[t] = oc.fit_range.getLower();
			upperLimit_[t++] = oc.fit_range.getUpper();
		}
		

		this.numberOfConstraints_ = 0; // No constraints
		this.numberOfObjectives_ = 1; // Single-objective optimization
		
		//Mixed integer solution
		solutionType_ = new IntRealSolutionType(this, cLength, iLength + pLength + oLength);
		
	}
	
	@Override
	public void evaluate(Solution solution) throws JMException {
		Variable[] genotype = solution.getDecisionVariables();
		Variable[] structure = Arrays.copyOfRange(genotype, 0, cLength);
		ExtendedModel phenotype = codec.decode(structure);

		IQGraph graph = new IQGraph(phenotype.getModel());

		Output output = null;
		
		double error = 0;
		boolean failed = false;
		
		
		try {
			
			output = new Output(outputSpec, graph);

			// Generate ODE models and simulators
			simulators = new ArrayList<CVodeSimulator>();
			odeModels = new ArrayList<ODEModel>();
			outputModels = new ArrayList<OutputModel>();

			for (int i = 0; i < datasets.size(); i++) {
				ODEModel lodeModel;

				lodeModel = new ODEModel(output.graph, datasets, dimsToCols, exosToCols, i);

				simulator = new CVodeSimulator(ODESolver.BDF, NonlinearSolver.NEWTON);
				simulator.initialize(lodeModel);

				simulator.setTolerances(spec.reltol, spec.abstol);
				simulator.setLinearSolver(LinearSolver.SPGMR);
				simulator.setMaxNumSteps(spec.steps);

				// FIXME: might need fixing see fixme in OutputModel
				OutputModel loutputModel = new OutputModel(output, datasets, null, dimsToCols, exosToCols, outsToCols,
						i);

				odeModels.add(lodeModel);
				outputModels.add(loutputModel);
				simulators.add(simulator);
			}
			
			//get and set model parameters for simulation
			double[] modelParameters = new double[output.graph.reachParameters.size()];
			for (int i = 0; i < modelParameters.length; i++) {
				String name = output.graph.reachParameters.get(i).id;
				Double value = output.graph.reachParameters.get(i).cons.value;
				if(value!= null) { 
					modelParameters[i] = value;
				} else {
					int pIndex = codec.internalEnumeratingCodec.params.indexOf(name);
					modelParameters[i] = output.graph.reachParameters.get(i).cons.value = genotype[cLength + iLength + pIndex].getValue();
				}
			}
			
			//get and set output parameters for simulation
			double[] outputParameters = new double[output.fitted.size()]; 
			for (int i = 0; i < outputParameters.length; i++) {
				String name = output.fitted.get(i).name;
				Double value = output.fitted.get(i).value;
				if(value != null) {
					outputParameters[i] = value;
				} else {
					int oIndex = codec.internalEnumeratingCodec.outputs.indexOf(name);
					outputParameters[i] = output.fitted.get(i).value = genotype[cLength + iLength + pLength + oIndex].getValue();
				}
			}
			

			//initials for each simulation and actual simulation
			for (int d = 0; d < datasets.size(); d++) {
				
				double[] initialValues = new double[output.graph.diffDifferential.size()];
				
					//initial values
					for (int i = 0; i < initialValues.length; i++) {
						Double value = output.graph.diffDifferential.get(i).var.initial;
						String name = output.graph.diffDifferential.get(i).id;
						if(value!=null) {
							initialValues[i] = value;
						} else {
							int iIndex = codec.internalEnumeratingCodec.initials.indexOf(name);
							if (initialValuesSpec.sameforalldatasets) {
								initialValues[i] = output.graph.diffDifferential.get(i).var.initial = genotype[cLength + iIndex].getValue();
							} else {
								initialValues[i] = output.graph.diffDifferential.get(i).var.initial = genotype[cLength + codec.internalEnumeratingCodec.initials.size()*d + iIndex].getValue();
							}
						}
					}
					
					odeModels.get(d).reset(initialValues, modelParameters);
					
					simulators.get(d).reinitialize(odeModels.get(d));
					
					simulation = simulators.get(d).simulate();
					
					outputModels.get(d).reset(simulation, modelParameters, outputParameters);
					outputData = outputModels.get(d).compute();
					
					phenotype.getSimulations().add(outputData);
					
					error += objFunction.evaluateTrajectory(outputData, d);
			}
			
			//clean up
			simulators.clear();
			odeModels.clear();
			outputModels.clear();
			

		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | IOException
				| RecognitionException e) {
			//Panic! 
			e.printStackTrace();
		} catch (FailedSimulationException ex) {
			failed = true;
		}
		
		
		if(failed) {
			solution.setObjective(0, Double.POSITIVE_INFINITY);
		} else {
			double comp = 0;
			//Regularize the objective function!
			
			//using number of parameters
			if(searchSpec.regularization.contains("param")) {
				comp = output.graph.reachParameters.size();
				if(codec.enumerate) {
					comp /= codec.internalEnumeratingCodec.pCompHigh;
				}
			}
			
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
			phenotype.getFitnessMeasures().put(objFunction.getName(), error);
			
			//keep only the best set of parameter values for each structure
			PlateauModel cModel = new PlateauModel(structure, phenotype);
			
			boolean pass = false;
			TreeSet<PlateauModel> plateau2 = new TreeSet<PlateauModel>();
			for(PlateauModel p : plateau) {
				if(p.equals(cModel)) {
					plateau2.add((p.compareTo(cModel) > 0) ? cModel : p);
					pass = true;
				} else {
					plateau2.add(p);
				}
			}
			
			plateau.clear();
			plateau.addAll(plateau2);
			if(!pass) plateau.add(cModel);
			
			
			//WARNING: Not optimal! See plateauFilterAndAdd function!
			filterPlateau();
			
			if(error < minerror){
				minerror = error;
			}
		}
		
		if (count % 100 == 0) {
			Task.logger.debug("Structure evaluation calls: " + count + " - " + "minerror="+ minerror + " - " + plateau.size() + " models in the plateau");
		}
		
		if (count % populationSize == 0 ){
			logger.info("Structure evaluation calls: " + count + " - " + "minerror="+ minerror + " - " + plateau.size() + " models in the plateau");
		}

		count++;
		
	}
	
	
}

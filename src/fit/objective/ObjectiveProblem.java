package fit.objective;

import java.io.IOException;
import java.util.*;

import java.util.logging.*;

import com.google.common.collect.*;

import fit.*;

import struct.inst.*;
import task.*;
import temp.*;
import util.*;
import xml.*;
import jmetal.core.*;
import jmetal.encodings.solutionType.*;
import jmetal.util.*;

public class ObjectiveProblem extends Problem {
	Output output;
	TrajectoryObjectiveFunction objFunction;

	List<Dataset> datasets;

	BiMap<String, String> dimsToCols;
	BiMap<String, String> endosToCols;
	BiMap<String, String> exosToCols;
	BiMap<String, String> outsToCols;

	public ListMap<String, IVNode> initialFitted;
	public ListMap<String, ICNode> modelFitted;
	public ListMap<String, OutputCons> outputFitted;
	
	InitialValuesSpec initialValuesSpec;

	public double[] initialValues;
	public double[] modelParameters;
	public double[] outputParameters;

	public ArrayList<Integer> initialIndexes;
	public BiMap<String,Integer> initialNamesToIndexes;
	public Integer totalInitialToFit;
	/**
	 * maps from index in modelFitted (Variable[] in Solution) to index in
	 * modelParameters (reachParameters)
	 */
	int[] modelFittedIndex;

	private ODEModel odeModel;
	private CVodeSimulator simulator;
	
	private ArrayList <CVodeSimulator> simulators;
	private ArrayList<ODEModel> odeModels;
	private ArrayList<OutputModel> outputModels;

	OutputModel outputModel;
	Dataset simulation;
	Dataset outputData;
	CVODESpec spec;

	public static int count = 1;
	
	private double minerror;
	public int populationSize;
	Logger logger = Logger.getLogger("fitPerformance");
	
	public boolean reg = false;

	public void setPopulationSize(int populationSize) {
		this.populationSize = populationSize;
	}

	public ObjectiveProblem(Output output,
			TrajectoryObjectiveFunction objFunction, List<Dataset> datasets,
			BiMap<String, String> dimsToCols, BiMap<String, String> endosToCols, BiMap<String, String> exosToCols,
			BiMap<String, String> outsToCols, CVODESpec spec, InitialValuesSpec initialValuesSpec)
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, IOException {
		this.output = output;
		this.objFunction = objFunction;

		this.datasets = datasets;

		this.dimsToCols = dimsToCols;
		this.exosToCols = exosToCols;
		this.outsToCols = outsToCols;
		this.endosToCols = endosToCols;
		
		this.initialValuesSpec = initialValuesSpec;

		this.spec = spec;

		this.modelFitted = output.graph.unknownParameters;
		this.outputFitted = output.fitted;

		this.initialFitted = output.graph.diffDifferential;

		initialIndexes = new ArrayList<Integer>();
		initialNamesToIndexes = HashBiMap.create();
		
		
		simulators = new ArrayList<CVodeSimulator>();
		odeModels = new ArrayList<ODEModel>();
		outputModels = new ArrayList<OutputModel>();

		this.initialValues = new double[output.graph.diffDifferential.size()];
		for (int i = 0; i < initialValues.length; i++) {
			Double value = output.graph.diffDifferential.get(i).var.initial;
			String name = output.graph.diffDifferential.get(i).var.getFullName();
			if (value != null) {
				this.initialValues[i] = value;
			} else {
				if(initialValuesSpec.usedatasetvalues){
					if(endosToCols.containsKey(name)){
						this.initialValues[i] = Double.NaN;
						initialNamesToIndexes.put(name, i);
					} else {
						System.out.println("WARNING: Endogenous variable " + name + " is not mapped to dataset column. Initial value will be fitted.");
						this.initialValues[i] = Double.NaN;
						initialIndexes.add(i);
					}
				} else {
					this.initialValues[i] = Double.NaN;
					initialIndexes.add(i);
				}
			}

		}

		this.modelParameters = new double[output.graph.reachParameters.size()];
		for (int i = 0; i < modelParameters.length; i++) {
			Double value = output.graph.reachParameters.get(i).cons.value;
			this.modelParameters[i] = (value != null) ? value : Double.NaN;
		}

		this.outputParameters = new double[output.fitted.size()];
		for (int i = 0; i < outputParameters.length; i++) {
			Double value = output.fitted.get(i).value;
			this.outputParameters[i] = (value != null) ? value : Double.NaN;
		}

		this.numberOfConstraints_ = 0; // No constraints
		this.numberOfObjectives_ = 1; // Single-objective optimization
		// this.numberOfVariables_ = modelFitted.size() + outputFitted.size();
		// // Model Parameters + Output Parameters
		
		if(initialValuesSpec.sameforalldatasets){
			totalInitialToFit = initialIndexes.size();
		} else {
			totalInitialToFit = initialIndexes.size() * datasets.size();
		}
		
		this.numberOfVariables_ = totalInitialToFit + modelFitted.size()
				+ outputFitted.size(); // Initial Values + Model Parameters +
										// Output Parameters
		this.problemName_ = output.graph.model.id;

		// Initialize Lower and Upper limits
		this.lowerLimit_ = new double[this.numberOfVariables_];
		this.upperLimit_ = new double[this.numberOfVariables_];

		// do the same as below for inital values keep in mind the indexes in
		// the fit vector
		
		int mult = 1;
		if(!initialValuesSpec.sameforalldatasets){
			mult = datasets.size();
		}

		for (int i = 0; i < initialIndexes.size(); i++) {
			for(int j=0; j<mult; j++){
				IIV iiv = (IIV) initialFitted.get(initialIndexes.get(i).intValue()).var;
				if(iiv.fit_range != null){
					this.lowerLimit_[i + initialIndexes.size()*j] = iiv.fit_range.getLower();
					this.upperLimit_[i + initialIndexes.size()*j] = iiv.fit_range.getUpper();
				} else {
					this.lowerLimit_[i + initialIndexes.size()*j] = iiv.range.getLower();
					this.upperLimit_[i + initialIndexes.size()*j] = iiv.range.getUpper();
				}
//				this.lowerLimit_[i + initialIndexes.size()*j] = initialFitted.get(initialIndexes.get(i)
//						.intValue()).var.range.getLower(); //get lower bound 
//				this.upperLimit_[i + initialIndexes.size()*j] = initialFitted.get(initialIndexes.get(i)
//						.intValue()).var.range.getUpper(); //get upper bound 
//			/*
//				 System.out.println("INITIAL:" +initialFitted.get(initialIndexes.get(i)
//							.intValue()).var.range.getUpper());*/
			}
		}

		for (int i = 0; i < modelFitted.size(); i++) {
			IIC iic = (IIC) modelFitted.get(i).cons;
			if (iic.fit_range != null) { // If Constant has fit_range, use it
				this.lowerLimit_[totalInitialToFit + i] = iic.fit_range
						.getLower();
				this.upperLimit_[totalInitialToFit + i] = iic.fit_range
						.getUpper();
			} else { // If not, use default range
				this.lowerLimit_[totalInitialToFit + i] = iic.range
						.getLower();
				this.upperLimit_[totalInitialToFit + i] = iic.range
						.getUpper();
			}
		}

		for (int i = 0; i < outputFitted.size(); i++) {
			OutputCons cons = outputFitted.get(i);
			this.lowerLimit_[totalInitialToFit + modelFitted.size() + i] = cons.fit_range
					.getLower();
			this.upperLimit_[totalInitialToFit + modelFitted.size() + i] = cons.fit_range
					.getUpper();			
		}

		this.modelFittedIndex = new int[modelFitted.size()];
		for (int i = 0; i < modelFittedIndex.length; i++) {
			modelFittedIndex[i] = output.graph.reachParameters
					.indexOf(modelFitted.getKey(i));
		}

		this.solutionType_ = new RealSolutionType(this);
		
		this.minerror = Double.POSITIVE_INFINITY;
		
		for (int i = 0; i < datasets.size(); i++) {
			try {

				ODEModel lodeModel = new ODEModel(output.graph, datasets, dimsToCols,exosToCols, i);

				simulator = new CVodeSimulator(ODESolver.BDF,
						NonlinearSolver.NEWTON);
				simulator.initialize(lodeModel);

				simulator.setTolerances(spec.reltol, spec.abstol);
				simulator.setLinearSolver(LinearSolver.SPGMR);
				simulator.setMaxNumSteps(spec.steps);
				
				
				//FIXME: might need fixing see fixme in OutputModel
				OutputModel loutputModel = new OutputModel(output, datasets, null, dimsToCols, exosToCols, outsToCols, i);
						
				odeModels.add(lodeModel);
				outputModels.add(loutputModel);

				simulators.add(simulator);
				
			} catch (InstantiationException ex) {
				throw new RuntimeException(ex);
			} catch (IllegalAccessException ex) {
				throw new RuntimeException(ex);
			} catch (ClassNotFoundException ex) {
				throw new RuntimeException(ex);
			}

		}
		
		

	}

	@Override
	public void evaluate(Solution solution) throws JMException,
			FailedSimulationException {

		Variable[] variables = solution.getDecisionVariables();

		for (int i = 0; i < initialIndexes.size(); i++) {
			initialValues[initialIndexes.get(i)] = variables[i].getValue();
		}
		

		for (int i = 0; i < modelFitted.size(); i++) {
			modelParameters[modelFittedIndex[i]] = variables[totalInitialToFit + i].getValue();
		}

		for (int i = 0; i < outputFitted.size(); i++) {
			outputParameters[i] = variables[totalInitialToFit
					+ modelFitted.size() + i].getValue();
		}

		double error = 0;
		boolean failed = false;
		
		
		for (int i = 0; i < datasets.size(); i++) {

			if (!initialValuesSpec.sameforalldatasets) {
				for (int j = 0; j < initialIndexes.size(); j++) {
					initialValues[initialIndexes.get(j)] = variables[j
							+ initialIndexes.size() * i].getValue();
				}
			}

			/*
			 * try { odeModel = new ODEModel(output.graph, datasets, dimsToCols,
			 * exosToCols, i); } catch (InstantiationException ex) { throw new
			 * RuntimeException(ex); } catch (IllegalAccessException ex) { throw
			 * new RuntimeException(ex); } catch (ClassNotFoundException ex) {
			 * throw new RuntimeException(ex); }
			 */

			// set initial values from dataset

			for (String name : initialNamesToIndexes.keySet()) {
				String dsColName = endosToCols.get(name);
				initialValues[initialNamesToIndexes.get(name)] = datasets
						.get(i).getElem(0, dsColName);
			}

			/*
			 * odeModel.reset(initialValues, modelParameters);
			 * 
			 * simulator = new CVodeSimulator(ODESolver.BDF,
			 * NonlinearSolver.NEWTON); simulator.initialize(odeModel);
			 * 
			 * simulator.setTolerances(spec.reltol, spec.abstol);
			 * simulator.setLinearSolver(LinearSolver.SPGMR);
			 * simulator.setMaxNumSteps(spec.steps);
			 */

			//odeModel.reset(initialValues, modelParameters);
			odeModels.get(i).reset(initialValues, modelParameters);
			

			// simulator.reinitialize();
			simulators.get(i).reinitialize(odeModels.get(i));

			try {
				// simulation = simulator.simulate();
				simulation = simulators.get(i).simulate();

				// why?
//				try {
//					outputModel = new OutputModel(output, datasets, simulation,
//							dimsToCols, exosToCols, outsToCols, i);
//				} catch (InstantiationException ex) {
//					throw new RuntimeException(ex);
//				} catch (IllegalAccessException ex) {
//					throw new RuntimeException(ex);
//				} catch (ClassNotFoundException ex) {
//					throw new RuntimeException(ex);
//				} catch (IOException ex) {
//					throw new RuntimeException(ex);
//				}

				outputModels.get(i)
						.reset(simulation, modelParameters, outputParameters);
				outputData = outputModels.get(i).compute();

				// exotic condition
				// if there are no exogenous variables and no different initial
				// values per dataset and no dataset values are used we do not
				// need to simulate for each dataset. one is enough
				// calculate for all datasets and break
				if (exosToCols.size() == 0
						&& (totalInitialToFit > initialIndexes.size())
						&& (initialNamesToIndexes.size() == 0)) {
					for (int j = 0; j < datasets.size(); j++) {
						error += objFunction.evaluateTrajectory(outputData, j);
					}
					break;
				}
				// if there are no exogenous variables this point wont be
				// reached
				error += objFunction.evaluateTrajectory(outputData, i);

			} catch (FailedSimulationException ex) {
				solution.setObjective(0, Double.POSITIVE_INFINITY);
				failed = true;
				Task.logger.debug("Evaluation " + count
						+ " failed. Assuming infinite error.");
				break;
			}
		}
		

		if (failed) {
			solution.setObjective(0, Double.POSITIVE_INFINITY);
		} else {
			//regularize if needed
			if(reg){
				double regTerm = 0.0;
				
//				//Ridge
//				for(double p : modelParameters){
//					regTerm += p*p;
//				}
//				
//				error += regTerm;
				
				//0-norm
				for(double p : modelParameters){
					if(Math.abs(p)>0.0) regTerm++;
				}
				
				error += 10*regTerm;
			}
			
			solution.setObjective(0, error);
			if(error < minerror){
				minerror = error;
			}
		}

		if (count % 1000 == 0) {
			Task.logger.debug("Evaluation: " + count);
		}
		
		//write hypercube points and minimal error
		if (count % populationSize == 0 ){
			logger.info(Double.toString(minerror));
		}

		count++;
		
		//simulator.free();
	}

}

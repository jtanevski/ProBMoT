package task;

import java.io.*;
import java.lang.reflect.*;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.xml.bind.*;

import jmetal.core.*;
import jmetal.metaheuristics.singleObjective.cmaes.CMAES;
import jmetal.metaheuristics.singleObjective.differentialEvolution.*;
import jmetal.metaheuristics.singleObjective.geneticAlgorithm.pgGA;
import jmetal.operators.crossover.*;
import jmetal.operators.mutation.MutationFactory;
import jmetal.operators.selection.*;
import jmetal.util.*;
import jmetal.util.parallel.IParallelEvaluator;
import jmetal.util.parallel.MultithreadedEvaluator;

import org.antlr.runtime.*;
import org.apache.commons.configuration.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.*;
import org.xml.sax.*;

import search.*;
import search.heuristic.TwoLevelBeamSearchProblem;
import search.heuristic.TwoLevelSearchProblem;
import search.heuristic.RandomSearchProblem;
import search.heuristic.SingleLevelSearchProblem;
import serialize.*;
import struct.inst.*;
import struct.temp.*;
import temp.*;
import traverse.*;
import util.*;
import xml.*;

import com.google.common.collect.*;
import fit.*;
import fit.compile.*;
import fit.objective.*;

public class Task {
    public static final org.slf4j.Logger logger = LoggerFactory.getLogger(Task.class);

    private TaskSpec ts;

    private Library library;
    private Model model;
    private IncompleteModel incompleteModel;

    private List<Dataset> datasets;
    private List<Dataset> trainDatasets;
    private List<Dataset> testDatasets;

    private Integer[] trainIDset;
    private Integer[] testIDset;

    private PrintStream out = System.out;
    private PrintStream eval_out = System.out;
    private PrintStream sim_out = System.out;

    private BiMap<String, String> dimsToCols = HashBiMap.create();
    private BiMap<String, String> endosToCols = HashBiMap.create();
    private BiMap<String, String> exosToCols = HashBiMap.create();
    private BiMap<String, String> outsToCols = HashBiMap.create();

    // Mappings of uncertainty columns
    private BiMap<String, String> weightsToCols = HashBiMap.create();


    private BiMap<String, Integer> exoIndex = HashBiMap.create();
    private BiMap<String, Integer> outIndex = HashBiMap.create();
    private BiMap<String, Integer> varIndex = HashBiMap.create(); // exoIndex + outIndex
    private BiMap<String, Integer> weightsIndex = HashBiMap.create(); // Uncertainties

    private String logDir = "log";

    private File outdir;
    private File evaldir;
    private File simdir;

	/* IO AND STATE MACHINE BLOCK */
    
	public Task(TaskSpec ts) throws IOException, RecognitionException, InstantiationException, IllegalAccessException,
			NoSuchFieldException, InvocationTargetException, NoSuchMethodException {
				
		this.ts = ts;

		if (ts.library == null) {
			throw new ParsingException(String.format(Errors.ERRORS[68]));
		}
		MDC.put("file", ts.library);
		this.library = Traverse.addLibrary(ts.library);

		if (ts.model != null) {
			MDC.put("file", ts.model);
			this.model = Traverse.addModel(ts.model);
		}
		if (ts.incomplete != null) {
			MDC.put("file", ts.incomplete);
			this.incompleteModel = Traverse.addIncompleteModel(ts.incomplete);
		}

		this.datasets = new ArrayList<Dataset>();
		this.testDatasets = new ArrayList<Dataset>();
		this.trainDatasets = new ArrayList<Dataset>();

		if (ts.settings.evaluation != null) {

			EvalSpec tts;
			if (ts.settings.evaluation instanceof LeaveOneOutSpec) {
				tts = new LeaveOneOutSpec();
				tts = (LeaveOneOutSpec) ts.settings.evaluation;
			} else if (ts.settings.evaluation instanceof TrainTestSpec) {
				tts = new TrainTestSpec();
				tts = (TrainTestSpec) ts.settings.evaluation;
			} else {// PANIC
				return;
			}

			organizeDatasets(tts);

		} else {// no eval
			if (ts.data != null) {
				for (DatasetSpec ds : ts.data) {
					this.datasets.add(new Dataset(ds));
					this.testDatasets = null;
				}
			}
		}

		// validate consistency of columns across all datasets
		if (datasets.size() > 0) {
			for (int i = 0; i < datasets.size() - 1; i++) {
				for (int j = i + 1; j < datasets.size(); j++) {
					Dataset d1 = datasets.get(i);
					Dataset d2 = datasets.get(j);
					LinkedList<String> d1names = new LinkedList<String>();
					for (String name : d1.getNames()) {
						d1names.add(name);
					}
					LinkedList<String> d2names = new LinkedList<String>();
					for (String name : d2.getNames()) {
						d2names.add(name);
					}
					if (!d1names.equals(d2names)) {
						throw new ParsingException(
								String.format(Errors.ERRORS[81], d1.getFilepath(), d2.getFilepath()));
					}

				}
			}
		}

		MDC.put("file", MDC.get("task"));
		if (ts.mappings != null) {
			// Create EXOGENOUS mappings
			for (Mapping exo : ts.mappings.exogenous) {
				if (datasets.get(0).getColIndex(exo.col) == null) {
					throw new ParsingException(
							String.format(Errors.ERRORS[67], exo.col, Arrays.toString(datasets.get(0).getNames())));
				}
				if (exosToCols.containsKey(exo.name)) {
					String oldCol = exosToCols.get(exo.name);
					throw new ParsingException(String.format(Errors.ERRORS[65], exo.name, exo.col, oldCol));
				}

				if (exosToCols.containsValue(exo.col)) {
					String oldVar = exosToCols.inverse().get(exo.col);
					throw new ParsingException(String.format(Errors.ERRORS[66], exo.col, exo.name, oldVar));
				}
				exosToCols.put(exo.name, exo.col);
			}
			for (String exo : exosToCols.keySet()) {
				exoIndex.put(exo, datasets.get(0).getColIndex(exosToCols.get(exo)));
			}

			// Create ENDOGENOUS mappings
			for (Mapping endo : ts.mappings.endogenous) {
				if (datasets.get(0).getColIndex(endo.col) == null) {
					throw new ParsingException(
							String.format(Errors.ERRORS[67], endo.col, Arrays.toString(datasets.get(0).getNames())));
				}
				if (endosToCols.containsKey(endo.name)) {
					String oldCol = endosToCols.get(endo.name);
					throw new ParsingException(String.format(Errors.ERRORS[65], endo.name, endo.col, oldCol));
				}

				if (endosToCols.containsValue(endo.col)) {
					String oldVar = endosToCols.inverse().get(endo.col);
					throw new ParsingException(String.format(Errors.ERRORS[66], endo.col, endo.name, oldVar));
				}
				endosToCols.put(endo.name, endo.col);
			}

			// Create DIMENSIONS mappings
			for (Mapping dim : ts.mappings.dimensions) {
				if (datasets.get(0).getColIndex(dim.col) == null) {
					throw new ParsingException(
							String.format(Errors.ERRORS[67], dim.col, Arrays.toString(datasets.get(0).getNames())));
				}
				if (dimsToCols.containsKey(dim.name)) {
					String oldCol = dimsToCols.get(dim.name);
					throw new ParsingException(String.format(Errors.ERRORS[70], dim.name, dim.col, oldCol));
				}

				if (dimsToCols.containsValue(dim.col)) {
					String oldDim = dimsToCols.inverse().get(dim.col);
					throw new ParsingException(String.format(Errors.ERRORS[71], dim.col, dim.name, oldDim));
				}
				dimsToCols.put(dim.name, dim.col);
			}

			// Create OUTPUT mappings
			for (Mapping out : ts.mappings.outputs) {
				if (datasets.get(0).getColIndex(out.col) == null) {
					throw new ParsingException(
							String.format(Errors.ERRORS[67], out.col, Arrays.toString(datasets.get(0).getNames())));
				}
				if (outsToCols.containsKey(out.name)) {
					String oldCol = outsToCols.get(out.name);
					throw new ParsingException(String.format(Errors.ERRORS[77], out.name, out.col, oldCol));
				}

				if (outsToCols.containsValue(out.col)) {
					String oldVar = outsToCols.inverse().get(out.col);
					throw new ParsingException(String.format(Errors.ERRORS[78], out.col, out.name, oldVar));
				}
				outsToCols.put(out.name, out.col);

				// Weights
				if (out.weight != null) {
					if (datasets.get(0).getColIndex(out.weight) == null) {
						throw new ParsingException(String.format(Errors.ERRORS[67], out.weight,
								Arrays.toString(datasets.get(0).getNames())));
					}
					if (weightsToCols.containsKey(out.name)) {
						String oldWeight = weightsToCols.get(out.name);
						throw new ParsingException(String.format(Errors.ERRORS[77], out.name, out.weight, oldWeight));
					}
					if (weightsToCols.containsValue(out.weight)) {
						String oldVar = weightsToCols.inverse().get(out.weight);
						throw new ParsingException(String.format(Errors.ERRORS[78], out.weight, out.name, oldVar));
					}
					weightsToCols.put(out.name, out.weight);
				}

			}
			for (String out : outsToCols.keySet()) {
				outIndex.put(out, datasets.get(0).getColIndex(outsToCols.get(out)));
			}
			// Weights
			for (String out : weightsToCols.keySet()) {
				weightsIndex.put(out, datasets.get(0).getColIndex(weightsToCols.get(out)));
			}

			if (ts.settings.initialvalues == null) {
				ts.settings.initialvalues = new InitialValuesSpec();
				ts.settings.initialvalues.sameforalldatasets = true;
				ts.settings.initialvalues.usedatasetvalues = false;
			}

			varIndex.putAll(exoIndex);
			varIndex.putAll(outIndex);

		}

		if (ts.outputFilepath != null) {
			if (ts.settings.evaluation != null) {
				if (ts.settings.evaluation instanceof LeaveOneOutSpec) {
					setPath(ts.outputFilepath + "/Iteration " + ts.getNumRuns());
				} else
					setPath(ts.outputFilepath);

			} else
				setPath(ts.outputFilepath);
		} else {
			this.out = System.out;
		}

		if (ts.command == null) {
			throw new ParsingException(String.format(Errors.ERRORS[69]));
		}
	}
	
	private void organizeDatasets(EvalSpec tts) {

		// Now just for Train->Validation->Test .Additionaly will be added for
		// Learning Curve and Leave One Out

		tts.train.trim();
		String[] trainTemp = tts.train.split(",");
		trainIDset = new Integer[trainTemp.length];

		for (int i = 0; i < trainTemp.length; i++) {
			try {
				trainIDset[i] = Integer.parseInt(trainTemp[i]);
			} catch (NumberFormatException nfe) {
			}
			;
		}

		if (tts.test != null && !tts.test.isEmpty()) {

			tts.test.trim();
			String[] testTemp = tts.test.split(",");
			testIDset = new Integer[testTemp.length];

			for (int i = 0; i < testTemp.length; i++) {
				try {
					testIDset[i] = Integer.parseInt(testTemp[i]);
				} catch (NumberFormatException nfe) {
				}
				;
			}
		} else
			testIDset = null;

		if (ts.data != null) {
			for (DatasetSpec ds : ts.data) {

				if (Arrays.asList(trainIDset).contains(ds.id))
					try {
						this.trainDatasets.add(new Dataset(ds));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				else if (testIDset != null)
					if (Arrays.asList(testIDset).contains(ds.id))
						try {
							this.testDatasets.add(new Dataset(ds));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				this.testDatasets = null;
			}
		}

		// For now just for TrainTest ..... LC
		if (tts instanceof TrainTestSpec)
			for (int i = 0; i < trainDatasets.size(); i++)
				this.datasets.add(trainDatasets.get(i));
		if (tts instanceof LeaveOneOutSpec) {
			for (int i = 0; i < trainDatasets.size(); i++)
				this.datasets.add(trainDatasets.get(i));
			if (ts.getNumRuns() < trainDatasets.size()) // FOR THE LAST ITTERATION AND WHT WHOLE MODEL
				datasets.remove(ts.getNumRuns());
		}
	}
    
	private void setPath(String s) {
		switch (ts.command) {
		case CLUSTERIFY:

			this.outdir = new File(s);
			if (!outdir.isDirectory()) {
				outdir.mkdir();
			}
			break;
		case SIMULATE:
		case SIMULATE_MODEL:
			this.outdir = new File(s);
			if (!outdir.isDirectory()) {
				outdir.mkdir();
			}

			this.simdir = new File(outdir + "/simulation");
			if (!this.simdir.isDirectory()) {
				this.simdir.mkdir();
			}
			break;
		case EVALUATE:
			this.outdir = new File(s);
			if (!outdir.isDirectory()) {
				outdir.mkdir();
			}
			this.evaldir = new File(outdir + "/evaluation");
			if (!evaldir.isDirectory()) {
				this.evaldir.mkdir();
			}
			break;
		case HEURISTIC_SEARCH:
		case EXHAUSTIVE_SEARCH:
			this.outdir = new File(s);
			if (!outdir.isDirectory()) {
				outdir.mkdir();
			}
			this.evaldir = new File(outdir + "/evaluation");
			if (!evaldir.isDirectory()) {
				this.evaldir.mkdir();
			}
			this.simdir = new File(outdir + "/simulation");
			if (!this.simdir.isDirectory()) {
				this.simdir.mkdir();
			}
			try {
				this.out = new PrintStream(FileUtils.openOutputStream(new File(outdir + "/Models.out")));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		default:
			this.outdir = new File(s);
			if (!outdir.isDirectory()) {
				outdir.mkdir();
			}
			try {
				this.out = new PrintStream(FileUtils.openOutputStream(new File(outdir + "/Models.out")));

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		}

	}
        
// This is the ONLY public method
	public void perform() throws IOException, InterruptedException, ConfigurationException, JAXBException, SAXException,
			InstantiationException, IllegalAccessException, ClassNotFoundException, RecognitionException, JMException,
			FailedSimulationException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
			SecurityException, URISyntaxException{

		Task.logger.info("Task started");
		switch (ts.command) {
		case SIMULATE:
		case SIMULATE_MODEL:
			simulateModels();
			break;
		case EVALUATE:
			evaluateModels();
			break;
		case WRITE_EQ:
			writeEQ();
			break;
		case HEURISTIC_SEARCH:
			heuristicSearch();
			break;
		case EXHAUSTIVE_SEARCH:
			enumerateModel(true);
			break;
		case ENUMERATE:
			enumerateModel(false);
			break;
		case ENUMERATE_C:
			enumerateModel(false, "c");
			break;
		case COUNT:
			count();
			break;
		case XML:
			writeXML();
			break;
		case JAVA:
			writeJava();
			break;
		case FIT_MODEL:
			fitModel();
			break;
		case CLUSTERIFY:
			clusterify();
			break;
		default:
			throw new IllegalStateException("invalid state");
		}
		Task.logger.info("Task ended");
		if (this.out != System.out) {
			this.out.close();
		}
	}


    /* EVALUATION BLOCK */
    

	//Evaluates a single model - see simulateModel
	private void evaluateModels() throws InstantiationException, IllegalAccessException, ClassNotFoundException,
			FailedSimulationException, RecognitionException, IOException {

		List<Dataset> outputData = simulateModel(testDatasets);
		for (int i = 0; i < testDatasets.size(); i++) {
			this.eval_out = new PrintStream(FileUtils.openOutputStream(new File(
					this.evaldir + "/" + model.getFullName() + "_datasetID_" + datasets.get(i).getId() + ".eval")));
			TrajectoryObjectiveFunction objectiveFun = new RMSEMultiDataset(testDatasets, outsToCols);
			this.eval_out.println(outputData.get(i));
			this.eval_out.println("TEST ERROR " + objectiveFun.getName() + " = "
					+ objectiveFun.evaluateTrajectory(outputData.get(i), i));

			this.eval_out.close();
		}

	}
    
	private Map<String, Double> evaluateModel(ExtendedModel ExModel, List<Dataset> DSs)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, RecognitionException,
			FailedSimulationException, IOException {

		Map<String, Double> evalMeasures = new HashMap<String, Double>();
		double error = 0;

		List<Dataset> outputData = simulateModel(ExModel, DSs);
		ExModel.setEvaluations(outputData);

		TrajectoryObjectiveFunction objectiveFun = new RelativeRMSEObjectiveFunctionMultiDataset(DSs, outsToCols);
		for (int i = 0; i < DSs.size(); i++) {

			if (outputData != null) {

				error += objectiveFun.evaluateTrajectory(outputData.get(i), i);

			} else {
				error = Double.POSITIVE_INFINITY;
				System.out.println("FAILED Simulation");
			}
		}

		evalMeasures.put(objectiveFun.getName(), error);

		return evalMeasures;
	}
    
    /* SIMULATION BLOCK */
    
	private void simulateModels() throws InstantiationException, IllegalAccessException, ClassNotFoundException,
			FailedSimulationException, RecognitionException, IOException {

		List<Dataset> outputData = simulateModel(datasets);
		for (int i = 0; i < outputData.size(); i++) {

			this.sim_out = new PrintStream(FileUtils.openOutputStream(new File(
					this.simdir + "/" + model.getFullName() + "_datasetID_" + datasets.get(i).getId() + ".sim")));
			sim_out.println(outputData.get(i));

		}

	}
    
	private List<Dataset> simulateModel(List<Dataset> datasets) throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, RecognitionException, FailedSimulationException, IOException {

		// incompleteModel cannot be simulated!
		// Model model = (this.model != null) ? this.model :
		// this.incompleteModel;
		Model model;
		if (this.model != null) {
			model = this.model;
		} else {
			throw new FailedSimulationException("No model available. Cannot simulate incomplete model.");
		}

		List<Dataset> outputDatas = new ArrayList<Dataset>();

		try {

			if (ts.settings != null) {
				if (ts.settings.simulator instanceof CVODESpec) {
					CVODESpec spec = (CVODESpec) ts.settings.simulator;
					for (int i = 0; i < datasets.size(); i++) {

						for (String name : model.allVars.keySet()) {
							IV var = model.allVars.get(name);
							if (ts.settings.initialvalues.usedatasetvalues) {
								String dsColName = endosToCols.get(name);
								if (dsColName != null) {
									var.initial = datasets.get(i).getElem(0, dsColName);
									model.allVars.put(name, var);
								}
							}
						}

						IQGraph graph = new IQGraph(model);
						Output output = new Output(ts.output, graph);

						ODEModel odeModel = new ODEModel(graph, datasets, dimsToCols, exosToCols, i);
						CVodeSimulator simulator = new CVodeSimulator(ODESolver.BDF, NonlinearSolver.NEWTON);
						simulator.initialize(odeModel);
						simulator.setTolerances(spec.reltol, spec.abstol);
						simulator.setLinearSolver(LinearSolver.SPGMR);
						simulator.setMaxNumSteps(spec.steps);

						Dataset simulation = simulator.simulate();

						OutputModel outputModel = new OutputModel(output, datasets, simulation, dimsToCols, exosToCols,
								outsToCols, i);

						outputDatas.add(outputModel.compute());

					}
				}
			}
			return outputDatas;
		} catch (FailedSimulationException e) {
			System.out.println("Simulation error: " + e.getMessage());
			return null;
		}

	}
    
	private List<Dataset> simulateModel(ExtendedModel em, List<Dataset> datasets)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, RecognitionException,
			FailedSimulationException, IOException {

		Model model = em.getModel();

		List<Dataset> outputDatas = new ArrayList<Dataset>();

		try {

			if (ts.settings != null) {
				if (ts.settings.simulator instanceof CVODESpec) {
					CVODESpec spec = (CVODESpec) ts.settings.simulator;
					for (int i = 0; i < datasets.size(); i++) {

						for (String name : model.allVars.keySet()) {
							IV var = model.allVars.get(name);
							if (ts.settings.initialvalues.usedatasetvalues) {
								String dsColName = endosToCols.get(name);
								if (dsColName != null) {
									var.initial = datasets.get(i).getElem(0, dsColName);
									model.allVars.put(name, var);
								}
							}
						}

						IQGraph graph = new IQGraph(model);
						Output output = new Output(ts.output, graph);

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
				}
			}
			return outputDatas;
		} catch (FailedSimulationException e) {
			System.out.println("Simulation error: " + e.getMessage());
			return null;
		}

	}
    
    
    /* CLUSTER BLOCK */
    
	// TODO: This method can be significantly improved. It should be
	// generalized!
	@SuppressWarnings("static-access")
	private void clusterify() throws ConfigurationException, IOException, InterruptedException, RecognitionException,
			JAXBException, SAXException, URISyntaxException {

		if (this.incompleteModel == null) {
			throw new RuntimeException(
					"Cannot perform search because an incomplete model was not provided. Use the <incomplete> tag to specify one");
		}

		ExtendedModel ext = new ExtendedModel(incompleteModel);

		String outputdir = ts.outputFilepath;

		// BufferedWriter rexp = new BufferedWriter(new FileWriter(new
		// File(outputdir + "runexperiments.sh")));
		// rexp.write("#!/bin/sh \n");

		ModelEnumerator search = new ModelEnumerator(ext);
		do {

			// change model name, change output done
			// create new task, change task variables, change task model
			// create new job_no.sh (hardcoded hack for dist\probmot.jar)

			ExtendedModel specificModel = search.nextModel();
			String nname = specificModel.getModel().id + search.getCounter();
			TaskSpec nts = TaskSpec.unmarshal(ts.taskPath);

			String IncompletefileName = nname + ".pbm";

			File jobDir = new File(outputdir + "/" + nname);
			if (!jobDir.isDirectory()) {
				jobDir.mkdir();
			}

			File resDir = new File(jobDir + "/res");
			if (!resDir.isDirectory()) {
				resDir.mkdir();
			}

			String LibfileName = new File(ts.library).getName();

			File jarFile = new File(run.TaskMain.class.getProtectionDomain().getCodeSource().getLocation().toURI());
			String jarFileName = jarFile.getName();

			// Copy the lib,library and jar file to the job folders
			FileUtils.copyDirectory(new File("lib"), new File(jobDir + "/lib"));
			FileUtils.copyFile(new File(ts.library), new File(resDir + "/" + LibfileName));
			FileUtils.copyFile(jarFile, new File(jobDir + "/" + jarFileName));

			// set the library and the incomplete model
			nts.library = "res/" + LibfileName;
			nts.incomplete = "res/" + IncompletefileName;

			// set the data
			File dataDir = new File(resDir + "/data");
			if (!dataDir.isDirectory()) {
				dataDir.mkdir();
			}
			for (int i = 0; i < nts.data.size(); i++) {
				String datafileName = new File(ts.data.get(i).datasetFilepath).getName();
				FileUtils.copyFile(new File(ts.data.get(i).datasetFilepath), new File(dataDir + "/" + datafileName));

				DatasetSpec dspec = nts.data.get(i);
				dspec.datasetFilepath = dspec.datasetFilepath.replace(ts.data.get(i).datasetFilepath,
						"res/data/" + datafileName);
			}

			// DatasetSpec dspec=ts.data.get(i);
			// dspec.datasetFilepath="res/data/"+datafileName;
			// nts.data.set(i, dspec);

			for (int i = 0; i < nts.mappings.endogenous.size(); i++) {
				Mapping mp = nts.mappings.endogenous.get(i);
				mp.name = mp.name.replace(specificModel.getModel().id, nname);
			}

			for (int i = 0; i < nts.mappings.exogenous.size(); i++) {
				Mapping mp = nts.mappings.exogenous.get(i);
				mp.name = mp.name.replace(specificModel.getModel().id, nname);
			}

			for (int i = 0; i < nts.output.variables.size(); i++) {
				ModelVar mvn = nts.output.variables.get(i);
				mvn.formula = mvn.formula.replace(specificModel.getModel().id, nname);
			}

			nts.command = nts.command.EXHAUSTIVE_SEARCH;

			// .pbm
			specificModel.getModel().setIdS(nname);

			this.out = new PrintStream(FileUtils
					.openOutputStream(new File(resDir + "/" + specificModel.getModel().getFullName() + ".pbm")));
			out.println("// Model #" + search.getCounter());
			out.println(specificModel);
			out.close();

			// +xml
			nts.outputFilepath = "out/" + specificModel.getModel().getFullName();

			TaskSpec.marshal(nts, resDir + "/" + nname + ".xml");
			logger.info("Model #" + search.getCounter() + (specificModel.isSuccessful() ? "" : " failed"));

			// run_job.sh
			BufferedWriter bw = new BufferedWriter(new FileWriter(
					new File(outputdir + "/" + "run_" + specificModel.getModel().getFullName() + ".sh")));
			bw.write("#!/bin/bash \n");
			bw.write("tar -xf " + nname + ".tar \n");
			bw.write("cd " + nname + "\n");
			bw.write("java -jar " + jarFileName + " res/" + nname + ".xml" + "\n");
			bw.write("cd ..\n");
			bw.write("tar czf " + nname + ".tgz" + " " + nname + "/out/\n");
			bw.close();

			// rexp.write("XRLS COMMANDS" + "job_"+search.getCounter()+".sh
			// \n");

		} while (search.hasNextModel());

		// create new runexperiments.sh file

		// rexp.close();

	}
    
    /* SEARCH AND OPTIMIZATION BLOCK */

	private void fitModel() throws JMException, RecognitionException, ClassNotFoundException, InstantiationException,
			IllegalAccessException, IOException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {

		if (incompleteModel == null)
			throw new RuntimeException(
					"Cannot perform model fitting because an incomplete model was not provided. Use the <incomplete> tag to specify one.");

		ExtendedModel incModel = new ExtendedModel(incompleteModel.copy());

		List<ExtendedModel> lmodels = fitModelParatemers(dimsToCols, endosToCols, exosToCols, outsToCols, datasets,
				incModel, ts.output, ts.settings.fitter, ts.settings.simulator, ts.settings.initialvalues);

		for (int i = 0; i < datasets.size(); i++) {
			this.out.println("// Model for dataset " + datasets.get(i).getFilepath());
			ExtendedModel eModel = lmodels.get(i);
			this.out.println(eModel);
		}
	}
	
	
	
	private void heuristicSearch() throws SecurityException, IOException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
			ClassNotFoundException, JMException, InterruptedException {
		if (this.incompleteModel == null) {
			throw new RuntimeException(
					"Cannot perform search because an incomplete model was not provided. Use the <incomplete> tag to specify one");
		}

		ExtendedModel ext = new ExtendedModel(incompleteModel);

		// create fitperformance logger
		Logger flogger = null;

		flogger = Logger.getLogger("fitPerformance");
		flogger.setUseParentHandlers(false);
		FileHandler fh = new FileHandler(ts.outputFilepath + "/" + ts.filename + "_fitPerformace" + ".log");
		fh.setFormatter(new SimpleFormatter());
		flogger.addHandler(fh);

		Algorithm algorithm = null;
		DESpec spec = (DESpec) ts.settings.fitter;

		// ensure repeatability
		MersenneTwisterFastFix msf = new MersenneTwisterFastFix();
		msf.setSeed(spec.seed);
		PseudoRandom.setRandomGenerator(msf);

		// Fixed due to regularization in the single level problem
		TrajectoryObjectiveFunction objectiveFun = new RelativeRMSEObjectiveFunctionMultiDataset(datasets, outsToCols);

		ArrayList<ExtendedModel> plateau = new ArrayList<ExtendedModel>();

		// Default is two level genetic

		boolean level2 = true;
		boolean beam = false;
		boolean random = false;
		
		if (ts.settings.search.level.contains("1"))
			level2 = false;
		if (ts.settings.search.level.contains("b"))
			beam = true;
		if (ts.settings.search.level.contains("r"))
			random = true;

		if (level2) {
			if (random) {
				
				RandomSearchProblem randomsearch = new RandomSearchProblem(ext, ts.output, datasets,
						dimsToCols, endosToCols, exosToCols, outsToCols, weightsToCols,
						(CVODESpec) ts.settings.simulator, ts.settings.fitter, ts.settings.initialvalues,
						ts.settings.search, false);
				
				randomsearch.setTempOut(outdir);
				
				randomsearch.execute();
				
				plateau = randomsearch.getPlateau();

			} else {

				if (beam) {
					// enumerate false for huge problems
					TwoLevelBeamSearchProblem beamsearch = new TwoLevelBeamSearchProblem(ext, ts.output, datasets,
							dimsToCols, endosToCols, exosToCols, outsToCols, weightsToCols,
							(CVODESpec) ts.settings.simulator, ts.settings.fitter, ts.settings.initialvalues,
							ts.settings.search, false);

					beamsearch.setTempOut(outdir);

					beamsearch.setbeamWidth(ts.settings.search.particles);

					beamsearch.execute();

					plateau = beamsearch.getPlateau();

				} else {

					TwoLevelSearchProblem problem = new TwoLevelSearchProblem(ext, ts.output, datasets, dimsToCols,
							endosToCols, exosToCols, outsToCols, weightsToCols, (CVODESpec) ts.settings.simulator,
							ts.settings.fitter, ts.settings.initialvalues, ts.settings.search, false);

					int threads = Math.max(2, ts.settings.search.threads); // 0 - use all the available cores
					IParallelEvaluator evaluator = new MultithreadedEvaluator(threads);

					algorithm = new pgGA(problem, evaluator);

					HashMap<String, Object> parameters;
					Operator crossover;
					Operator mutation;
					Operator selection;

					algorithm.setInputParameter("populationSize", ts.settings.search.particles);
					algorithm.setInputParameter("maxEvaluations", ts.settings.search.maxevaluations);

					problem.setPopulationSize(ts.settings.search.particles);

					parameters = new HashMap<String, Object>();
					parameters.put("probability", 0.9);
					crossover = CrossoverFactory.getCrossoverOperator("SinglePointCrossover", parameters);

					parameters = new HashMap<String, Object>();
					parameters.put("probability", 1.0 / problem.getNumberOfVariables());
					mutation = MutationFactory.getMutationOperator("BitFlipMutation", parameters);

					parameters = null;
					selection = SelectionFactory.getSelectionOperator("BinaryTournament", parameters);

					algorithm.addOperator("crossover", crossover);
					algorithm.addOperator("mutation", mutation);
					algorithm.addOperator("selection", selection);

					problem.setTempOut(outdir);
					
					algorithm.execute();
					plateau = problem.getPlateau();
				}
			}
		} else {
			SingleLevelSearchProblem problem = new SingleLevelSearchProblem(ext, ts.output, objectiveFun, datasets,
					dimsToCols, exosToCols, outsToCols, (CVODESpec) ts.settings.simulator, ts.settings.initialvalues,
					ts.settings.search, true);

			algorithm = new CMAES(problem);

			// takes parameters from the fitter spec

			algorithm.setInputParameter("populationSize", spec.population);

			problem.setPopulationSize(spec.population);

			Integer max_evals = spec.evaluations * (problem.getNumberOfVariables());

			algorithm.setInputParameter("maxEvaluations", max_evals);

			// Execute the optimization
			algorithm.execute();
			plateau = problem.getPlateau();
		}

		// Write output
		//	WARNING: All simulations of models in a lite plateau will be NULL!
		int counter = 1;
		for (ExtendedModel model : plateau) {
			this.sim_out = new PrintStream(
					FileUtils.openOutputStream(new File(this.simdir + "/Model" + counter + ".sim")));
			for (int i = 0; i < datasets.size(); i++) {
				out.println("// Model" + counter + " for dataset " + datasets.get(i).getFilepath());
				out.println(model);
				//this.sim_out.println("DATASET_ID:" + datasets.get(i).getId());
				try {
					this.sim_out.println(model.getSimulations().get(i));
				} catch (Exception e) {
					this.sim_out.println("SIMULATION NOT AVAILABLE");
				}
			}
			counter++;
		}
		System.out.println("Wrote " + plateau.size() + " models.");

	}
	

	private void enumerateModel(boolean fit, String... format)
			throws IOException, InterruptedException, ConfigurationException, RecognitionException, JMException,
			ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		if (this.incompleteModel == null) {
			throw new RuntimeException(
					"Cannot perform search because an incomplete model was not provided. Use the <incomplete> tag to specify one");
		}

		ExtendedModel ext = new ExtendedModel(incompleteModel);

		// create fitperformance logger
		Logger flogger = null;
		if (fit) {
			flogger = Logger.getLogger("fitPerformance");
			flogger.setUseParentHandlers(false);
			FileHandler fh = new FileHandler(ts.outputFilepath + "/" + ts.filename + "_fitPerformace" + ".log");
			fh.setFormatter(new SimpleFormatter());
			flogger.addHandler(fh);
		}

		ModelEnumerator search = new ModelEnumerator(ext);
		do {
			ExtendedModel specificModel = search.nextModel();

			List<ExtendedModel> lmodels = null;
			if (fit) {
				try {
					lmodels = fitModelParatemers(dimsToCols, endosToCols, exosToCols, outsToCols, datasets,
							specificModel, ts.output, ts.settings.fitter, ts.settings.simulator,
							ts.settings.initialvalues);
				} catch (FailedSimulationException ex) {
					specificModel.setSuccessful(false);
				}
				
				
			}

			if(lmodels == null) {
				out.println(specificModel);
			} else {
				
				this.sim_out = new PrintStream(FileUtils
						.openOutputStream(new File(this.simdir + "/Model#" + lmodels.get(0).getModelNo() + ".sim")));
				
				for (int i = 0; i < datasets.size(); i++) {
					out.println("// Model #" + search.getCounter() + " for dataset " + datasets.get(i).getFilepath());
					ExtendedModel eModel = lmodels.get(i);
					if (format.length == 1 && format[0].equals("c")) {
						out.println(SimpleWriter.serialize(eModel.getModel(), new CSerializer()));
					} else {
						out.println(eModel);
					}
					if (fit) {
	
						//this.sim_out.println("DATASET_ID:" + datasets.get(i).getId());
						try {
							this.sim_out.println(eModel.getSimulations().get(i));
						} catch (NullPointerException e) {
							this.sim_out.println("FAILED SIMULATION");
						}
	
					}
				}
			}
				if (ts.settings.evaluation != null && fit) {
					if (ts.settings.evaluation.test != null && !ts.settings.evaluation.test.isEmpty()) {
						for (int i = 0; i < lmodels.size(); i++) {
							ExtendedModel eModel = lmodels.get(i);
	
							this.eval_out = new PrintStream(FileUtils.openOutputStream(
									new File(this.evaldir + "/Model#" + eModel.getModelNo() + "_" + i + ".eval")));
	
							for (int j = 0; j < testDatasets.size(); j++) {
								this.eval_out.println("DATASET_ID:" + testDatasets.get(j).getId());
								try {
									this.eval_out.println(eModel.getEvaluations().get(j));
								} catch (NullPointerException e) {
									this.eval_out.println("FAILED SIMULATION");
								}
							}
	
						}
					}
				}
			logger.info("Model #" + search.getCounter() + (specificModel.isSuccessful() ? "" : " failed"));
		} while (search.hasNextModel());

	}

	private List<ExtendedModel> fitModelParatemers(BiMap<String, String> dimsToCols,
			BiMap<String, String> endosToCols, BiMap<String, String> exosToCols, BiMap<String, String> outsToCols,
			List<Dataset> datasets, ExtendedModel model, OutputSpec outputSpec, FitterSpec fitterSpec,
			SimulatorSpec simulatorSpec, InitialValuesSpec initialValuesSpec) throws JMException, RecognitionException,
			ClassNotFoundException, InstantiationException, IllegalAccessException, IOException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {

		List<ExtendedModel> lmodels = new LinkedList<ExtendedModel>();

		IQGraph graph = new IQGraph(model.getModel());
		Output output = new Output(outputSpec, graph);

		Algorithm algorithm = null;
		DESpec spec = (DESpec) fitterSpec;

		TrajectoryObjectiveFunction objectiveFun = setObjectiveFunc(spec);

		ObjectiveProblem problem = new ObjectiveProblem(output, objectiveFun, datasets, dimsToCols, endosToCols,
				exosToCols, outsToCols, (CVODESpec) simulatorSpec, initialValuesSpec);

		// for regualrization
		if (spec.reg)
			System.out.println("WARNING: The objective is regularized");
		problem.reg = spec.reg;

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

		MersenneTwisterFastFix msf = new MersenneTwisterFastFix();
		msf.setSeed(spec.seed);
		PseudoRandom.setRandomGenerator(msf);

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
		Variable[] variables = null;

		if (max_evals > 0) {
			population = algorithm.execute();
			variables = population.get(0).getDecisionVariables();
		}

		int totalmodels = 1;
		if (problem.totalInitialToFit > problem.initialIndexes.size()) {
			totalmodels = datasets.size();
		}

		for (int d = 0; d < totalmodels; d++) {
			ExtendedModel emodel = model.copy();
			for (int i = 0; i < problem.initialIndexes.size(); i++) {
				int indexval = problem.initialIndexes.get(i).intValue();
				String ivName = problem.initialFitted.get(indexval).id;
				Double iVal = variables[i + problem.initialIndexes.size() * d].getValue();
				emodel.getModel().allVars.get(ivName).initial = iVal;
			}

			for (int i = 0; i < problem.modelFitted.size(); i++) {
				String icName = problem.modelFitted.get(i).id;
				Double icValue = variables[problem.totalInitialToFit + i].getValue();
				emodel.getModel().allConsts.get(icName).value = icValue;
			}

			Map<String, Double> outputConsts = new LinkedHashMap<String, Double>();
			for (int i = 0; i < problem.outputFitted.size(); i++) {
				String outputName = output.fitted.getKey(i);
				Double outputValue = variables[problem.totalInitialToFit + problem.modelFitted.size() + i].getValue();

				outputConsts.put(outputName, outputValue);
			}

			emodel.setOutputConstants(outputConsts);

			if (ts.settings.evaluation != null) {
				if (ts.settings.evaluation.test != null && !ts.settings.evaluation.test.isEmpty()) {
					emodel.setEvalMeasures(evaluateModel(model, testDatasets));
				}

			}

			List<Dataset> simulations = simulateModel(emodel, datasets);
			emodel.setSimulations(simulations);

			Map<String, Double> fitnessMeasures = new HashMap<String, Double>();

			if (max_evals > 0) {
				fitnessMeasures.put(objectiveFun.getName(), population.get(0).getObjective(0));
			} else {
				Double error = 0.0;
				if (simulations == null) {
					fitnessMeasures.put(objectiveFun.getName(), Double.POSITIVE_INFINITY);
				} else {
					for (int i = 0; i < simulations.size(); i++) {
						error += objectiveFun.evaluateTrajectory(simulations.get(i), i);
					}
					fitnessMeasures.put(objectiveFun.getName(), error);
				}
			}

			emodel.setFitnessMeasures(fitnessMeasures);

			lmodels.add(emodel);
		}

		return lmodels;
	}
    
	private TrajectoryObjectiveFunction setObjectiveFunc(DESpec spec)
			throws FileNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {

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
		return objectiveFun;
	}
    
	private void count() throws IOException, ConfigurationException, InterruptedException, RecognitionException {
		if (this.incompleteModel == null) {
			throw new RuntimeException(
					"Cannot perform search because an incomplete model was not provided. Use the <incomplete> tag to specify one");
		}

		String filename = (ts.filename != null) ? ts.filename : "default";
		int counter = 1;
		while (new File(logDir + "/" + filename + "-" + counter).exists()) {
			counter++;
		}

		ExtendedModel ext = new ExtendedModel(incompleteModel);
		ModelEnumerator search = new ModelEnumerator(ext);

		int modelNum = search.count();

		out.println(modelNum + " models generated");
	}

    
    /* SERIALIZATION BLOCK */

	private void writeXML() throws JAXBException, SAXException {

		Model model = this.model;
		if (model == null) {
			model = this.incompleteModel;
		}
		IQGraph graph = new IQGraph(model);
		XMLSpec spec = new XMLSpec(ts.settings, ts.criterion, graph, ts.data, ts.mappings, new CSerializer(),
				ts.output.variables);

		XMLSpec.marshal(spec, ts.outputFilepath);

	}

	private void writeJava() {
		out.print(ModelCompiler.modelToString(model, "SerializedModel"));
	}

	private void duplicate() {
		Model model; // model to be duplicated
		if (this.model != null) {
			model = this.model;
		} else if (this.incompleteModel != null) {
			logger.info("Model not provided. Using incomplete model instead");
			model = this.incompleteModel;
		} else {
			throw new RuntimeException(
					"Cannot perform duplication because no model was provided. Use <model> tag to specify one");
		}

		Model copied = model.copy();
		logger.info("Model sucessufully duplicated");
	}    

	private void writeEQ() {
		SimpleWriter simple = new SimpleWriter(model, new CSerializer());
		simple.writeToStream(this.out);
	}

}

package task;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.reflect.*;
import java.net.URISyntaxException;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.xml.bind.*;

import jmetal.core.*;
import jmetal.metaheuristics.singleObjective.differentialEvolution.*;
import jmetal.operators.crossover.*;
import jmetal.operators.selection.*;
import jmetal.util.*;

import org.antlr.runtime.*;
import org.apache.commons.configuration.*;
import org.apache.commons.io.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.bridj.Pointer;
import org.slf4j.*;
import org.xml.sax.*;

import search.*;
import serialize.*;
import struct.inst.*;
import struct.temp.*;
import temp.*;
import traverse.*;
import util.*;
import xml.*;

import com.google.common.collect.*;
import com.google.common.io.Files;

import fit.*;
import fit.DASA_MSC.Method;
import fit.compile.*;
import fit.objective.*;

public class Task {
    public static final org.slf4j.Logger logger = LoggerFactory.getLogger(Task.class);

    TaskSpec ts;

    public Library library;
    public Model model;
    public IncompleteModel incompleteModel;

    public List<Dataset> datasets;
    public List<Dataset> trainDatasets;
    public List<Dataset> validationDatasets;
    public List<Dataset> testDatasets;

    public Integer[] trainIDset;
    public Integer[] validationIDset;
    public Integer[] testIDset;


    public String timeColumn;

    public PrintStream out = System.out;
    public PrintStream eval_out = System.out;
    public PrintStream valid_out = System.out;
    public PrintStream sim_out = System.out;
    public PrintStream best_out = System.out;
    public PrintStream ensemble_out = System.out;

    BiMap<String, String> dimsToCols = HashBiMap.create();
    BiMap<String, String> endosToCols = HashBiMap.create();
    BiMap<String, String> exosToCols = HashBiMap.create();
    BiMap<String, String> outsToCols = HashBiMap.create();

    // Mappings of uncertainty columns
    BiMap<String, String> weightsToCols = HashBiMap.create();

    //	Map<String, String> colsToVars = new LinkedHashMap<String, String>();

    BiMap<String, Integer> exoIndex = HashBiMap.create();
    BiMap<String, Integer> outIndex = HashBiMap.create();
    BiMap<String, Integer> varIndex = HashBiMap.create(); // exoIndex + outIndex
    BiMap<String, Integer> weightsIndex = HashBiMap.create(); // Uncertainties

    FitterOld fitterOld = ALG_MSC.ALG_MSC;
    SimulatorOld simulatorOld = ALG_MSC.ALG_MSC;

    //	Fitter fitter;
    //	Simulator simulator;

    String logDir = "log";

    private File outdir;
    private File evaldir;
    private File simdir;
    private File validdir;

    public Task(TaskSpec ts)
            throws IOException, RecognitionException, InstantiationException, IllegalAccessException, NoSuchFieldException, InvocationTargetException,
            NoSuchMethodException {
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

		/*this.datasets = new ArrayList<Dataset>();
        if (ts.data != null) {
			for (DatasetSpec ds : ts.data) {
				this.datasets.add(new Dataset(ds));
			}
		}*/


        this.datasets = new ArrayList<Dataset>();
        this.testDatasets = new ArrayList<Dataset>();
        this.trainDatasets = new ArrayList<Dataset>();
        this.validationDatasets = new ArrayList<Dataset>();


        if (ts.settings.evaluation != null) {

            EvalSpec tts;
            if (ts.settings.evaluation instanceof LeaveOneOutSpec) {
                tts = new LeaveOneOutSpec();
                tts = (LeaveOneOutSpec) ts.settings.evaluation;
            } else if (ts.settings.evaluation instanceof TrainTestSpec) {
                tts = new TrainTestSpec();
                tts = (TrainTestSpec) ts.settings.evaluation;
            } else if (ts.settings.evaluation instanceof CrossValidSpec) {
                tts = new CrossValidSpec();
                tts = (CrossValidSpec) ts.settings.evaluation;
            } else {//PANIC
                return;
            }

            organizeDatasets(tts);

        } else {//no eval
            if (ts.data != null) {
                for (DatasetSpec ds : ts.data) {

                    this.datasets.add(new Dataset(ds));
                    this.testDatasets = null;
                    this.validationDatasets = null;
                }
            }
        }

        //validate consistency of columns across all datasets
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
                        throw new ParsingException(String.format(Errors.ERRORS[81], d1.getFilepath(), d2.getFilepath()));
                    }

                }
            }
        }

        MDC.put("file", MDC.get("task"));
        if (ts.mappings != null) {
            // Create EXOGENOUS mappings
            for (Mapping exo : ts.mappings.exogenous) {
                if (datasets.get(0).getColIndex(exo.col) == null) {
                    throw new ParsingException(String.format(Errors.ERRORS[67], exo.col, Arrays.toString(datasets.get(0).getNames())));
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
                    throw new ParsingException(String.format(Errors.ERRORS[67], endo.col, Arrays.toString(datasets.get(0).getNames())));
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
                    throw new ParsingException(String.format(Errors.ERRORS[67], dim.col, Arrays.toString(datasets.get(0).getNames())));
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
            this.timeColumn = dimsToCols.get("time"); // FIXME: works only for one dimension = time, use map dimsToCols

            // Create OUTPUT mappings
            for (Mapping out : ts.mappings.outputs) {
                if (datasets.get(0).getColIndex(out.col) == null) {
                    throw new ParsingException(String.format(Errors.ERRORS[67], out.col, Arrays.toString(datasets.get(0).getNames())));
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
                        throw new ParsingException(String.format(Errors.ERRORS[67], out.weight, Arrays.toString(datasets.get(0).getNames())));
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
            //Weights
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
            //this.out = new PrintStream(FileUtils.openOutputStream(new File(ts.outputFilepath)));
            if (ts.settings.evaluation != null) {
                if (ts.settings.evaluation instanceof LeaveOneOutSpec) {
                    setPath(ts.outputFilepath + "/Iteration " + ts.getNumRuns());
                } else if (ts.settings.evaluation instanceof CrossValidSpec)
                    setPath(ts.outputFilepath + "/Iteration " + ts.getNumRuns() + "_TestDS_" + testDatasets.get(testDatasets.size() - 1).getId());
                else setPath(ts.outputFilepath);

            } else
                setPath(ts.outputFilepath);
        } else {
            this.out = System.out;
        }


        if (ts.settings != null) {
            if (ts.settings.fitter instanceof ALGSpec) {
                ALGSpec spec = (ALGSpec) ts.settings.fitter;

                this.fitterOld = ALG_MSC.ALG_MSC;
                ALG_MSC.Params.DEFAULT_FIT.opt_fs_restarts = spec.restarts;
                ALG_MSC.Params.DEFAULT_FIT.opt_normalize_error = (spec.normalize ? 1 : 0);
            } else if (ts.settings.fitter instanceof DASASpec) {
                DASASpec spec = (DASASpec) ts.settings.fitter;

                this.fitterOld = DASA_MSC.DASA_MSC;
                DASA_MSC.DASA_MSC.setMethod(Method.DASA);
                DASA_MSC.DASA_MSC.setRestarts(spec.restarts);
                DASA_MSC.DASA_MSC.setAnts(spec.ants);
                DASA_MSC.DASA_MSC.setEvals(spec.evaluations);
                DASA_MSC.DASA_MSC.setCauchyIncPer(spec.cauchyIncPer);
                DASA_MSC.DASA_MSC.setCauchyDecPer(spec.cauchyDecPer);
                DASA_MSC.DASA_MSC.setEvap(spec.evap);
                DASA_MSC.Params.DEFAULT_FIT.opt_normalize_error = (spec.normalize ? 1 : 0);
            } else if (ts.settings.fitter instanceof DESpecOld) {
                DESpecOld spec = (DESpecOld) ts.settings.fitter;

                this.fitterOld = DASA_MSC.DASA_MSC;
                DASA_MSC.DASA_MSC.setMethod(Method.DE);
                DASA_MSC.DASA_MSC.setRestarts(spec.restarts);
                DASA_MSC.DASA_MSC.setPopulation(spec.population);
                DASA_MSC.DASA_MSC.setEvals(spec.evaluations);
                DASA_MSC.DASA_MSC.setF(spec.F);
                DASA_MSC.DASA_MSC.setCr(spec.Cr);
                DASA_MSC.DASA_MSC.setStrategy(spec.strategy);
                DASA_MSC.Params.DEFAULT_FIT.opt_normalize_error = (spec.normalize ? 1 : 0);
            }
        }

        if (ts.command == null) {
            throw new ParsingException(String.format(Errors.ERRORS[69]));
        }
    }

    private void organizeDatasets(EvalSpec tts) {

        // Now just for Train->Validation->Test .Additionaly will be added for Learning Curve and Leave One Out

        if (tts instanceof CrossValidSpec) {
            if (ts.data != null) {

                for (DatasetSpec ds : ts.data) {

                    try {
                        this.trainDatasets.add(new Dataset(ds));
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    //this.testDatasets = null;
                    this.validationDatasets = null;
                }
            }

        } else {

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

            if (tts.validation != null && !tts.validation.isEmpty()) {

                tts.validation.trim();
                String[] validTemp = tts.validation.split(",");
                validationIDset = new Integer[validTemp.length];

                for (int i = 0; i < validTemp.length; i++) {
                    try {
                        validationIDset[i] = Integer.parseInt(validTemp[i]);
                    } catch (NumberFormatException nfe) {
                    }
                    ;
                }
            } else validationIDset = null;


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
            } else testIDset = null;

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
                        else if (validationIDset != null)
                            if (Arrays.asList(validationIDset).contains(ds.id))
                                try {
                                    this.validationDatasets.add(new Dataset(ds));
                                } catch (IOException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            else {
                                if (testIDset == null) this.testDatasets = null;
                                if (validationIDset == null) this.validationDatasets = null;
                            }
                }

            }
        }
        //FIXME For now just for TrainTest ..... LC
        if (tts instanceof TrainTestSpec)
            for (int i = 0; i < trainDatasets.size(); i++)
                this.datasets.add(trainDatasets.get(i));
        if (tts instanceof LeaveOneOutSpec) {
            for (int i = 0; i < trainDatasets.size(); i++)
                this.datasets.add(trainDatasets.get(i));
            if (ts.getNumRuns() < trainDatasets.size()) //FOR THE LAST ITTERATION AND WHT WHOLE MODEL
                datasets.remove(ts.getNumRuns());
        }
        if (tts instanceof CrossValidSpec) {
            for (int i = 0; i < trainDatasets.size(); i++)
                this.datasets.add(trainDatasets.get(i));
            if (ts.getNumRuns() < trainDatasets.size()) {
                datasets.remove(ts.getNumRuns());
                testDatasets.add(trainDatasets.get(ts.getNumRuns()));
            }
        }

    }

    public void perform()
            throws IOException, InterruptedException, ConfigurationException, JAXBException, SAXException, InstantiationException,
            IllegalAccessException, ClassNotFoundException, RecognitionException, JMException, FailedSimulationException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, URISyntaxException {

        Task.logger.info("Task started");
        switch (ts.command) {
            case VALIDATE:
                validate();
                break;
            case SIMULATE:
                simulateModel();
                break;
            case EVALUATE:
                evaluateModels();
                break;
            case FIT:
                fit();
                break;
            case WRITE_EQ:
                writeEQ();
                break;
            case DUPLICATE:
                duplicate();
                break;
            case FIT_EACH:
                fitCombinations();
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
            case PROCESS_LIST:
                enumerateProcessList();
                break;
            case COUNT:
                count();
                break;
            case DOT_ALL:
                toDot(DotFlag.ALL);
                break;
            case DOT_PROPER:
                toDot(DotFlag.PROPER);
                break;
            case DOT_NAMES:
                toDot(DotFlag.NAME);
                break;
            case MAT_TREE:
                toMatTree();
                break;
            case MSC:
                writeMSC();
                break;
            case XML:
                writeXML();
                break;
            case JAVA:
                writeJava();
                break;
            case COMPILE:
                compileModel();
                break;
            case SIMULATE_MODEL:
                simulateModel();
                break;
            case FIT_MODEL:
                fitModel();
                break;
            case CREATE_OUTPUT:
                createOutput();
                break;
            case TEST_OBJECTIVE:
                testObjective();
                break;
            case TEST_DE:
                testDE();
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


    private void evaluateModels() throws InstantiationException, IllegalAccessException, ClassNotFoundException, FailedSimulationException, RecognitionException, IOException {

        List<Dataset> outputData = simulateModel(testDatasets);
        for (int i = 0; i < testDatasets.size(); i++) {

            TrajectoryObjectiveFunction objectiveFun = new RMSEMultiDataset(testDatasets, outsToCols);
            this.eval_out.println(outputData.get(i));
            this.eval_out.println("TEST ERROR " + objectiveFun.getName() + " = " + objectiveFun.evaluateTrajectory(outputData.get(i), i));
            //error += objectiveFun.evaluateTrajectory(outputData.get(i), i);

            this.eval_out.close();
        }

    }


    private Map<String, Double> evaluateModel(ExtendedModel ExModel, List<Dataset> DSs, boolean isEvaluation)
            throws InstantiationException, IllegalAccessException,
            ClassNotFoundException, RecognitionException,
            FailedSimulationException, IOException {

        Map<String, Double> evalMeasures = new HashMap<String, Double>();
        double error = 0;

        //Model model = ExModel.getModel();


        List<Dataset> outputData = simulateModel(ExModel, DSs);
        if (isEvaluation) ExModel.setEvaluations(outputData);
        else ExModel.setValidations(outputData);


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

    private void evaluateModels(ExtendedModel ExModel, int ModelNo)
            throws InstantiationException, IllegalAccessException,
            ClassNotFoundException, RecognitionException,
            FailedSimulationException, IOException {

        PrintStream p = null;


        List<Dataset> outputData = simulateModel(ExModel, testDatasets);
        //this.eval_out.println("//Model# "+ ModelNo);
        for (int i = 0; i < testDatasets.size(); i++) {

            TrajectoryObjectiveFunction objectiveFun = new RMSEMultiDataset(
                    testDatasets, outsToCols);
            if (outputData != null) {
                //	this.eval_out.println("//Test Data "
                //			+ testDatasets.get(i).getFilepath() + "\n"
                //			+ outputData.get(i));
				/*
				 * this.eval_out.println("TEST ERROR " + objectiveFun.getName()
				 * + " = " + objectiveFun.evaluateTrajectory(outputData.get(i),
				 * i));
				 */
                //error += objectiveFun.evaluateTrajectory(outputData.get(i), i);
                //	this.eval_out.println("//" + objectiveFun.getName() + " = "
                //			+ objectiveFun.evaluateTrajectory(outputData.get(i), i)
                //			+ "\n");

            } else {
                //	this.eval_out.println("FAILED SIMULATION!!! MODEL:" + ModelNo);
            }
        }

        this.eval_out.close();

    }

    @SuppressWarnings("static-access")
    private void clusterify() throws ConfigurationException, IOException, InterruptedException, RecognitionException, JAXBException, SAXException, URISyntaxException {

        if (this.incompleteModel == null) {
            throw new RuntimeException("Cannot perform search because an incomplete model was not provided. Use the <incomplete> tag to specify one");
        }

        ExtendedModel ext = new ExtendedModel(incompleteModel);

        String outputdir = ts.outputFilepath;

        //	BufferedWriter rexp = new BufferedWriter(new FileWriter(new File(outputdir + "runexperiments.sh")));
        //	rexp.write("#!/bin/sh \n");

        ModelEnumerator search = new ModelEnumerator(ext, null, null, null, null, false, null, null, null);
        do {

            //change model name, change output done
            //create new task, change task variables, change task model
            //create new job_no.sh (hardcoded hack for dist\probmot.jar)

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


            //Copy the lib,library and jar file to the job folders
            FileUtils.copyDirectory(new File("lib"), new File(jobDir + "/lib"));
            FileUtils.copyFile(new File(ts.library), new File(resDir + "/" + LibfileName));
            FileUtils.copyFile(jarFile, new File(jobDir + "/" + jarFileName));

            //set the library and the incomplete model
            nts.library = "res/" + LibfileName;
            nts.incomplete = "res/" + IncompletefileName;

            //set the data
            File dataDir = new File(resDir + "/data");
            if (!dataDir.isDirectory()) {
                dataDir.mkdir();
            }
            for (int i = 0; i < nts.data.size(); i++) {
                String datafileName = new File(ts.data.get(i).datasetFilepath).getName();
                FileUtils.copyFile(new File(ts.data.get(i).datasetFilepath), new File(dataDir + "/" + datafileName));

                DatasetSpec dspec = nts.data.get(i);
                dspec.datasetFilepath = dspec.datasetFilepath.replace(ts.data.get(i).datasetFilepath, "res/data/" + datafileName);
            }


            // DatasetSpec dspec=ts.data.get(i);
            //   dspec.datasetFilepath="res/data/"+datafileName;
            //   nts.data.set(i, dspec);

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


            //.pbm
            specificModel.getModel().setIdS(nname);

            this.out = new PrintStream(FileUtils.openOutputStream(new File(resDir + "/" + specificModel.getModel().getFullName() + ".pbm")));
            out.println("// Model #" + search.getCounter());
            out.println(specificModel);
            out.close();

            //+xml
            nts.outputFilepath = "out/" + specificModel.getModel().getFullName();

            TaskSpec.marshal(nts, resDir + "/" + nname + ".xml");
            logger.info("Model #" + search.getCounter() + (specificModel.isSuccessful() ? "" : " failed"));


            //run_job.sh
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outputdir + "/" + "run_" + specificModel.getModel().getFullName() + ".sh")));
            bw.write("#!/bin/bash \n");
            bw.write("tar -xf " + nname + ".tar \n");
            bw.write("cd " + nname + "\n");
            bw.write("java -jar " + jarFileName + " res/" + nname + ".xml" + "\n");
            bw.write("cd ..\n");
            bw.write("tar czf " + nname + ".tgz" + " " + nname + "/out/\n");
            bw.close();

            //rexp.write("XRLS COMMANDS" + "job_"+search.getCounter()+".sh \n");

        } while (search.hasNextModel());

        //create new runexperiments.sh file

        //	rexp.close();


    }

    private void compileModel() {
        String model11Src = ModelCompiler.modelToString(model, "Model11");
        System.out.println(model11Src);
    }

    private void simulateNModel()
            throws InstantiationException, IllegalAccessException, ClassNotFoundException, RecognitionException, FailedSimulationException, IOException {
        int n = 50;
        for (int i = 0; i < n; i++) {
            simulateModel();
        }

    }

    private void validate() {
        logger.info("Task specification is validated");
    }

    private void setResultOutputPaths() {

        if (ts.outputFilepath != null) {


            setPath(ts.outputFilepath);


        } else {


            setPath("UnamedTask" + ManagementFactory.getRuntimeMXBean().getName());
        }


    }

    private void simulate()
            throws IOException, InterruptedException, RecognitionException, FailedSimulationException {
        Model model; // model to be simulated
        if (this.model != null) {
            model = this.model;
        } else if (this.incompleteModel != null) {
            logger.info("Model not provided. Using incomplete model instead");
            model = this.incompleteModel;
        } else {
            throw new RuntimeException("Cannot perform simulation because no model was provided. Use <model> tag to specify one");
        }

        simulatorOld.initWorkDir();

        IQGraph graph = new IQGraph(model);
        Output output = new Output(this.ts.output, graph);

        Simulation simulation = simulatorOld.simulate(output, this.datasets, this.timeColumn, this.varIndex, this.weightsIndex);
        simulation.writeSimulation(this.out);


    }


    private void simulateModel(ExtendedModel eModel, int ndata, int modelNo)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException, RecognitionException, IOException {
        Model model = eModel.getModel();
        //fill initial values from dataset make it to work with model

        for (String name : model.allVars.keySet()) {
            IV var = model.allVars.get(name);
            if (var.initial == null && ts.settings.initialvalues.usedatasetvalues) {
                String dsColName = endosToCols.get(name);
                if (dsColName != null) {
                    var.initial = datasets.get(ndata).getElem(0, dsColName);
                    model.allVars.put(name, var);
                }
            }
        }

        IQGraph graph = new IQGraph(model);

        Output output = new Output(ts.output, graph);


        ODEModel odeModel = new ODEModel(graph, datasets, dimsToCols, exosToCols, ndata);

        if (ts.settings != null) {
            if (ts.settings.simulator instanceof CVODESpec) {
                CVODESpec spec = (CVODESpec) ts.settings.simulator;
                CVodeSimulator simulator = new CVodeSimulator(ODESolver.BDF, NonlinearSolver.NEWTON);
                simulator.initialize(odeModel);

                simulator.setTolerances(spec.reltol, spec.abstol);
                simulator.setLinearSolver(LinearSolver.SPGMR);
                simulator.setMaxNumSteps(spec.steps);
                try {
                    Dataset simulation = simulator.simulate();

                    OutputModel outputModel = new OutputModel(output, datasets, simulation, dimsToCols, exosToCols, outsToCols, ndata);

                    Double[] op = new Double[eModel.getOutputConstants().values().size()];
                    eModel.getOutputConstants().values().toArray(op);
                    outputModel.setOutputParameters(ArrayUtils.toPrimitive(op));

                    Dataset outputData = outputModel.compute();

                    PrintStream p = new PrintStream("simulations/" + model.getFullName() + "_" + "Model" + modelNo + "_" + datasets.get(ndata).getFilepath().replace(System.getProperty("file.separator").charAt(0), '_').replace('/', '_') + ".sim");
                    p.println(outputData);
                    p.close();
                } catch (FailedSimulationException e) {
                    System.out.println("Simulation error: " + e.getMessage());
                }

            }

        }
    }

    private void simulateModel()
            throws InstantiationException, IllegalAccessException, ClassNotFoundException, RecognitionException, FailedSimulationException, IOException {
        Model model = (this.model != null) ? this.model : this.incompleteModel;
        IQGraph graph = new IQGraph(model);

        Output output = new Output(ts.output, graph);

        ODEModel odeModel = new ODEModel(graph, datasets, dimsToCols, exosToCols);

        if (ts.settings != null) {
            if (ts.settings.simulator instanceof CVODESpec) {
                CVODESpec spec = (CVODESpec) ts.settings.simulator;
                CVodeSimulator simulator = new CVodeSimulator(ODESolver.BDF, NonlinearSolver.NEWTON);
                simulator.initialize(odeModel);

                simulator.setTolerances(spec.reltol, spec.abstol);
                simulator.setLinearSolver(LinearSolver.SPGMR);
                simulator.setMaxNumSteps(spec.steps);

                Dataset simulation = simulator.simulate();


                OutputModel outputModel = new OutputModel(output, datasets, simulation, dimsToCols, exosToCols, outsToCols);

                Dataset outputData = outputModel.compute();

                this.sim_out = new PrintStream(
                        FileUtils.openOutputStream(new File(this.simdir
                                + "/Model" + model.getFullName()
                                + ".sim"))
                );
                sim_out.println(outputData);
            }
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
            case SIMULATE:
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
            case VALIDATE:
                this.outdir = new File(s);
                if (!outdir.isDirectory()) {
                    outdir.mkdir();
                }
                this.validdir = new File(outdir + "/validation");
                if (!validdir.isDirectory()) {
                    this.validdir.mkdir();
                }
                break;
            case EXHAUSTIVE_SEARCH:
                this.outdir = new File(s);
                if (!outdir.isDirectory()) {
                    outdir.mkdir();
                }
                this.evaldir = new File(outdir + "/evaluation");
                if (!evaldir.isDirectory()) {
                    this.evaldir.mkdir();
                }

                this.validdir = new File(outdir + "/validation");
                if (!validdir.isDirectory()) {
                    this.validdir.mkdir();
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


    private List<Dataset> simulateModel(List<Dataset> ODEdatasets)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException, RecognitionException, FailedSimulationException, IOException {


        Model model = (this.model != null) ? this.model : this.incompleteModel;


        //ArrayList <CVodeSimulator> simulators;
        //ArrayList<ODEModel> odeModels;
        List<Dataset> outputDatas = new ArrayList<Dataset>();

        try {

            if (ts.settings != null) {
                if (ts.settings.simulator instanceof CVODESpec) {
                    CVODESpec spec = (CVODESpec) ts.settings.simulator;
                    for (int i = 0; i < ODEdatasets.size(); i++) {

                        for (String name : model.allVars.keySet()) {
                            IV var = model.allVars.get(name);
                            if (ts.settings.initialvalues.usedatasetvalues) {
                                String dsColName = endosToCols.get(name);
                                if (dsColName != null) {
                                    var.initial = ODEdatasets.get(i).getElem(0,
                                            dsColName);
                                    model.allVars.put(name, var);
                                }
                            }
                        }

                        IQGraph graph = new IQGraph(model);
                        Output output = new Output(ts.output, graph);

                        ODEModel odeModel = new ODEModel(graph, ODEdatasets,
                                dimsToCols, exosToCols, i);
                        CVodeSimulator simulator = new CVodeSimulator(
                                ODESolver.BDF, NonlinearSolver.NEWTON);
                        simulator.initialize(odeModel);
                        simulator.setTolerances(spec.reltol, spec.abstol);
                        simulator.setLinearSolver(LinearSolver.SPGMR);
                        simulator.setMaxNumSteps(spec.steps);

                        Dataset simulation = simulator.simulate();

                        OutputModel outputModel = new OutputModel(output,
                                ODEdatasets, simulation, dimsToCols, exosToCols,
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


    private List<Dataset> simulateModel(ExtendedModel em, List<Dataset> ODEdatasets)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException, RecognitionException, FailedSimulationException, IOException {


        Model model = em.getModel();
        List<Dataset> outputDatas = new ArrayList<Dataset>();
        try {
            if (ts.settings != null) {
                if (ts.settings.simulator instanceof CVODESpec) {
                    CVODESpec spec = (CVODESpec) ts.settings.simulator;
                    for (int i = 0; i < ODEdatasets.size(); i++) {

                        for (String name : model.allVars.keySet()) {
                            IV var = model.allVars.get(name);
                            if (var.initial != null && ts.settings.initialvalues.usedatasetvalues) {
                                String dsColName = endosToCols.get(name);
                                if (dsColName != null) {
                                    var.initial = ODEdatasets.get(i).getElem(0,
                                            dsColName);
                                    model.allVars.put(name, var);
                                }
                            }
                        }

                        IQGraph graph = new IQGraph(model);
                        Output output = new Output(ts.output, graph);

                        ODEModel odeModel = new ODEModel(graph, ODEdatasets,
                                dimsToCols, exosToCols, i);
                        CVodeSimulator simulator = new CVodeSimulator(
                                ODESolver.BDF, NonlinearSolver.NEWTON);
                        simulator.initialize(odeModel);
                        simulator.setTolerances(spec.reltol, spec.abstol);
                        simulator.setLinearSolver(LinearSolver.SPGMR);
                        simulator.setMaxNumSteps(spec.steps);

                        Dataset simulation = simulator.simulate();

                        OutputModel outputModel = new OutputModel(output,
                                ODEdatasets, simulation, dimsToCols, exosToCols,
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

    private void fit()
            throws IOException, InterruptedException, ConfigurationException, RecognitionException, FailedSimulationException {
        if (this.incompleteModel == null) {
            throw new RuntimeException("Cannot perform fitting because an incomplete model was not provided. Use the <incomplete> tag to specify one");
        }
        String filename = (ts.filename != null) ? ts.filename : "default";
        int counter = 1;
        while (new File(logDir + "/" + filename + "-" + counter).exists()) {
            counter++;
        }
        String logDirpath = logDir + "/" + filename + "-" + counter;

        fitterOld.initWorkDir();

        IQGraph graph = new IQGraph(this.incompleteModel);
        Output output = new Output(this.ts.output, graph);

        ExtendedModel completedModel =
                fitterOld.fit(output, this.datasets, this.ts.mappings.dimensions.get(0).col, this.varIndex, this.weightsIndex, logDirpath);
        this.out.print(completedModel);


    }

    private void fitModel()
            throws JMException, RecognitionException, ClassNotFoundException, InstantiationException, IllegalAccessException, IOException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        ExtendedModel fittedModel = new ExtendedModel(incompleteModel.copy());

        fitSingleModel(dimsToCols, endosToCols, exosToCols, outsToCols, datasets, fittedModel, ts.output, ts.settings.fitter, ts.settings.simulator, ts.settings.initialvalues);
        this.out.print(fittedModel);
    }

    private void enumerate(boolean fit, String... format)
            throws IOException, InterruptedException, ConfigurationException, RecognitionException {
        if (this.incompleteModel == null) {
            throw new RuntimeException("Cannot perform search because an incomplete model was not provided. Use the <incomplete> tag to specify one");
        }

        String filename = (ts.filename != null) ? ts.filename : "default";
        int counter = 1;
        while (new File(logDir + "/" + filename + "-" + counter).exists()) {
            counter++;
        }
        String logDirpath = logDir + "/" + filename + "-" + counter;

        fitterOld.initWorkDir();

        ExtendedModel ext = new ExtendedModel(incompleteModel);
        String timeCol;
        if (this.ts.mappings != null) {
            timeCol = this.ts.mappings.dimensions.get(0).col;
        } else {
            timeCol = null;
        }

        ModelEnumerator search =
                new ModelEnumerator(ext, this.datasets, timeCol, this.varIndex, this.weightsIndex, fit, fitterOld, logDirpath, ts.output);
        do {
            ExtendedModel fittedModel = search.nextModel();
            out.println("// Model #" + search.getCounter());
            if (format.length == 1 && format[0].equals("c")) {
                out.println(SimpleWriter.serialize(fittedModel.getModel(), new CSerializer()));
            } else {
                out.println(fittedModel);
            }
            logger.info("Model #" + search.getCounter() + (fittedModel.isSuccessful() ? "" : " failed"));
        } while (search.hasNextModel());

        //fitter.deleteWorkDir();
    }

    private void enumerateModel(boolean fit, String... format)
            throws IOException, InterruptedException, ConfigurationException, RecognitionException, JMException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        if (this.incompleteModel == null) {
            throw new RuntimeException("Cannot perform search because an incomplete model was not provided. Use the <incomplete> tag to specify one");
        }

        ExtendedModel ext = new ExtendedModel(incompleteModel);
        String timeCol;
        if (this.ts.mappings != null) {
            timeCol = this.ts.mappings.dimensions.get(0).col;
        } else {
            timeCol = null;
        }


        //create fitperformance logger and simulations folder
        Logger flogger = null;
        if (fit) {
            flogger = Logger.getLogger("fitPerformance");
            flogger.setUseParentHandlers(false);
            FileHandler fh = new FileHandler(ts.outputFilepath
                    + "/" + ts.filename + "_fitPerformace" + ".log");
            fh.setFormatter(new SimpleFormatter());
            flogger.addHandler(fh);
			
			/*File sdir = new File("simulations");
			if (!sdir.isDirectory()) { sdir.mkdir(); }*/
        }

        ModelEnumerator search = new ModelEnumerator(ext, null, null, null, null, false, null, null, null);
        do {
            ExtendedModel specificModel = search.nextModel();

            List<ExtendedModel> lmodels = null;
            if (fit) {
                try {
                    flogger.info("Model #" + search.getCounter());
                    if (ts.settings.initialvalues.sameforalldatasets) {
                        specificModel.setModelNo(search.getCounter());
                        fitSingleModel(dimsToCols, endosToCols, exosToCols, outsToCols, datasets, specificModel, ts.output, ts.settings.fitter, ts.settings.simulator, ts.settings.initialvalues);
                        //FindBestModel(specificModel);
                    } else {
                        specificModel.setModelNo(search.getCounter());
                        lmodels = fitMultipleDatasetModel(dimsToCols, endosToCols, exosToCols, outsToCols, datasets, specificModel, ts.output, ts.settings.fitter, ts.settings.simulator, ts.settings.initialvalues);
                    }
                } catch (FailedSimulationException ex) {
                    specificModel.setSuccessful(false);
                }

            }

            if (lmodels == null) {
                out.println("// Model #" + search.getCounter());
                if (format.length == 1 && format[0].equals("c")) {
                    out.println(SimpleWriter.serialize(specificModel.getModel(), new CSerializer()));
                } else {
                    out.println(specificModel);
                    if (fit) {
                        this.sim_out = new PrintStream(
                                FileUtils.openOutputStream(new File(this.simdir
                                        + "/Model#" + specificModel.getModelNo()
                                        + ".sim"))
                        );
                        for (int i = 0; i < datasets.size(); i++) {
                            this.sim_out.println("DATASET_ID:"
                                    + datasets.get(i).getId());
                            try {
                                this.sim_out.println(specificModel
                                        .getSimulations().get(i));
                            } catch (NullPointerException e) {
                                this.sim_out.println("FAILED SIMULATION");
                            }
                        }
                    }
                }
            } else {

                this.sim_out = new PrintStream(FileUtils.openOutputStream(new File(this.simdir + "/Model#" + lmodels.get(0).getModelNo()
                        + ".sim")));
                for (int i = 0; i < datasets.size(); i++) {
                    out.println("// Model #" + search.getCounter() + " for dataset " + datasets.get(i).getFilepath());
                    ExtendedModel eModel = lmodels.get(i);
                    if (format.length == 1 && format[0].equals("c")) {
                        out.println(SimpleWriter.serialize(eModel.getModel(), new CSerializer()));
                    } else {
                        out.println(eModel);
                        if (fit) {

                            this.sim_out.println("DATASET_ID:"
                                    + datasets.get(i).getId());
                            try {
                                this.sim_out.println(eModel
                                        .getSimulations().get(i));
                            } catch (NullPointerException e) {
                                this.sim_out.println("FAILED SIMULATION");
                            }


                        }
                    }
                }
            }

            if (ts.settings.evaluation != null && fit) {

                if (lmodels == null) {

                    if (ts.settings.evaluation.validation != null
                            && !ts.settings.evaluation.validation.isEmpty()) {
                        this.valid_out = new PrintStream(
                                FileUtils.openOutputStream(new File(
                                        this.validdir + "/Model#"
                                                + specificModel.getModelNo()
                                                + ".valid"
                                ))
                        );
                        for (int i = 0; i < validationDatasets.size(); i++) {
                            this.valid_out.println("DATASET_ID:"
                                    + validationDatasets.get(i).getId());
                            try {
                                this.valid_out.println(specificModel
                                        .getValidations().get(i));
                            } catch (NullPointerException e) {
                                this.valid_out.println("FAILED SIMULATION");
                            }
                        }

                    }
                    if (ts.settings.evaluation.test != null && !ts.settings.evaluation.test.isEmpty()
                            ) {
                        this.eval_out = new PrintStream(
                                FileUtils.openOutputStream(new File(this.evaldir
                                        + "/Model#" + specificModel.getModelNo()
                                        + ".eval"))
                        );
                        for (int i = 0; i < testDatasets.size(); i++) {
                            this.eval_out.println("DATASET_ID:"
                                    + testDatasets.get(i).getId());
                            try {
                                this.eval_out.println(specificModel
                                        .getEvaluations().get(i));
                            } catch (NullPointerException e) {
                                this.eval_out.println("FAILED SIMULATION");
                            }
                        }
                    }

                } else {


                    if (ts.settings.evaluation.validation != null
                            && !ts.settings.evaluation.validation.isEmpty()) {

                        for (int i = 0; i < lmodels.size(); i++) {
                            ExtendedModel eModel = lmodels.get(i);

                            this.valid_out = new PrintStream(
                                    FileUtils.openOutputStream(new File(
                                            this.validdir + "/Model#"
                                                    + eModel.getModelNo() + "_"
                                                    + i + ".valid"
                                    ))
                            );

                            for (int j = 0; j < validationDatasets.size(); j++) {
                                this.eval_out.println("DATASET_ID:"
                                        + validationDatasets.get(j).getId());
                                try {
                                    this.valid_out.println(eModel
                                            .getValidations().get(j));
                                } catch (NullPointerException e) {
                                    this.valid_out.println("FAILED SIMULATION");
                                }
                            }

                        }


                    }

                    if (ts.settings.evaluation.test != null
                            && !ts.settings.evaluation.test.isEmpty()) {
                        for (int i = 0; i < lmodels.size(); i++) {
                            ExtendedModel eModel = lmodels.get(i);

                            this.eval_out = new PrintStream(
                                    FileUtils.openOutputStream(new File(
                                            this.evaldir + "/Model#"
                                                    + eModel.getModelNo() + "_"
                                                    + i + ".eval"
                                    ))
                            );

                            for (int j = 0; j < testDatasets.size(); j++) {
                                this.eval_out.println("DATASET_ID:"
                                        + testDatasets.get(j).getId());
                                try {
                                    this.eval_out.println(eModel
                                            .getEvaluations().get(j));
                                } catch (NullPointerException e) {
                                    this.eval_out.println("FAILED SIMULATION");
                                }
                            }

                        }
                    }


                }

            }

            logger.info("Model #" + search.getCounter() + (specificModel.isSuccessful() ? "" : " failed"));
        } while (search.hasNextModel());
//		
//		if (fit)
//		{
//		//out.println("\n//BEST MODEL\n//Model#"+ts.BestModels.get(ts.numRuns).get(0).getModelNo()+"\n"+ ts.BestModels.get(ts.numRuns).get(0));
//	
//		
//		 PrintBestModels() ;
//		}
//	

    }


    private Dataset createOutput(List<double[]> outs, ExtendedModel em, List<Dataset> dss, int num, String[] outnames) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException, RecognitionException {


        int nRow = dss.get(num).getNRows();
        int tColIndex = dss.get(num).getColIndex(
                dimsToCols.get("time"));
        double[] time = new double[nRow];
        for (int j = 0; j < nRow; j++) {
            time[j] = dss.get(num).getElem(j, tColIndex);
        }

        Double[][] outss = new Double[outs.size() + 1][time.length];


        outss[0][0] = time[0];
        for (int j = 0; j < outs.size(); j++) {
            outss[j + 1][0] = outs.get(j)[0];
        }
        for (int i = 1; i < time.length; i++) {
            outss[0][i] = time[i];

            for (int j = 0; j < outs.size(); j++) {
                outss[j + 1][i] = outs.get(j)[i];
            }
        }

        String[] headers = (String[]) ArrayUtils.add(outnames, 0, "time");
        Dataset dataset = new Dataset(outss, headers);
        return dataset;

    }


    private ExtendedModel fitSingleModel(BiMap<String, String> dimsToCols, BiMap<String, String> endosToCols, BiMap<String, String> exosToCols, BiMap<String, String> outsToCols, List<Dataset> datasets, ExtendedModel model, OutputSpec outputSpec, FitterSpec fitterSpec, SimulatorSpec simulatorSpec, InitialValuesSpec initialValuesSpec)
            throws JMException, RecognitionException, ClassNotFoundException, InstantiationException, IllegalAccessException, IOException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        IQGraph graph = new IQGraph(model.getModel());
        Output output = new Output(outputSpec, graph);


        Algorithm algorithm = null;
        DESpec spec = (DESpec) fitterSpec;


        TrajectoryObjectiveFunction objectiveFun = setObjectiveFunc(spec);
        ObjectiveProblem problem = new ObjectiveProblem(output, objectiveFun, datasets, dimsToCols, endosToCols, exosToCols, outsToCols, (CVODESpec) simulatorSpec, initialValuesSpec);
        
        //for regualrization
        if(spec.reg) System.out.println("WARNING: The objective is regularized");
        problem.reg = spec.reg;

        algorithm = new DE(problem);

        problem.setPopulationSize(spec.population);

        HashMap<String, Object> parameters;
        Operator crossover; // Crossover operator
        Operator selection; // Selection operator

        algorithm.setInputParameter("populationSize", spec.population);
        //algorithm.setInputParameter("maxEvaluations", spec.evaluations * (output.fitted.size() + graph.unknownParameters.size()));  // Maybe IQGraph should contain information about initial values that need to be fitted i.e. graph.unknownInitial and proper indexing should be done in ObjectiveProblem based on this info
        double max_evals = spec.evaluations * (problem.getNumberOfVariables());
        algorithm.setInputParameter("maxEvaluations", max_evals); // problem.getNumberOfVariables() contains the number of all variables that need: fitting initial + output + model parameters. no need to recalculate!

        //System.out.println("Max_evals:" + max_evals);
        
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

      //  RandomNumberGenerator rng = new RandomNumberGenerator();
     //   rng.setSeed(1);
     //   PseudoRandom.setRandomGenerator(rng);
        
        MersenneTwisterFastFix msf = new MersenneTwisterFastFix();
		msf.setSeed(spec.seed);
	    PseudoRandom.setRandomGenerator(msf);


	    SolutionSet population = null;
        Variable[] variables = null;
        
        if(max_evals > 0.0){
        	population = algorithm.execute();
        	variables = population.get(0).getDecisionVariables();
        }

        for (int i = 0; i < problem.initialIndexes.size(); i++) {
            int indexval = problem.initialIndexes.get(i).intValue();
            String ivName = problem.initialFitted.get(indexval).id;
            Double iVal = variables[i].getValue();
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


        if (ts.settings.evaluation != null) {

            if (ts.settings.evaluation instanceof CrossValidSpec) {

                model.setEvalMeasures(evaluateModel(model, testDatasets, true));
                if (ts.settings.evaluation.validation != null && !ts.settings.evaluation.validation.isEmpty()) {
                    model.setValidMeasures(evaluateModel(model, validationDatasets, false));
                }
            }

            if (ts.settings.evaluation.test != null && !ts.settings.evaluation.test.isEmpty()) {

                model.setEvalMeasures(evaluateModel(model, testDatasets, true));
            }
            if (ts.settings.evaluation.validation != null && !ts.settings.evaluation.validation.isEmpty()) {

                model.setValidMeasures(evaluateModel(model, validationDatasets, false));

            }
        }
        //Simulation
        List<Dataset> simulations = simulateModel(model, datasets);
        model.setSimulations(simulations);

        Map<String, Double> fitnessMeasures = new HashMap<String, Double>();
        
        if(max_evals > 0.0)
        	fitnessMeasures.put(objectiveFun.getName(), population.get(0).getObjective(0));
        else
        	fitnessMeasures.put(objectiveFun.getName(), (simulations==null)? Double.POSITIVE_INFINITY : objectiveFun.evaluateTrajectory(simulations.get(0)));

        model.setFitnessMeasures(fitnessMeasures);
        
        return model;
    }


    private List<ExtendedModel> fitMultipleDatasetModel(BiMap<String, String> dimsToCols, BiMap<String, String> endosToCols, BiMap<String, String> exosToCols, BiMap<String, String> outsToCols, List<Dataset> datasets, ExtendedModel model, OutputSpec outputSpec, FitterSpec fitterSpec, SimulatorSpec simulatorSpec, InitialValuesSpec initialValuesSpec)
            throws JMException, RecognitionException, ClassNotFoundException, InstantiationException, IllegalAccessException, IOException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {

        List<ExtendedModel> lmodels = new LinkedList<ExtendedModel>();

        IQGraph graph = new IQGraph(model.getModel());
        Output output = new Output(outputSpec, graph);

        Algorithm algorithm = null;
        DESpec spec = (DESpec) fitterSpec;

        TrajectoryObjectiveFunction objectiveFun = setObjectiveFunc(spec);

        ObjectiveProblem problem = new ObjectiveProblem(output, objectiveFun, datasets, dimsToCols, endosToCols, exosToCols, outsToCols, (CVODESpec) simulatorSpec, initialValuesSpec);
        
        //for regualrization
        if(spec.reg) System.out.println("WARNING: The objective is regularized");
        problem.reg = spec.reg;
        

        algorithm = new DE(problem);
        problem.setPopulationSize(spec.population);

        HashMap<String, Object> parameters;
        Operator crossover; // Crossover operator
        Operator selection; // Selection operator

        algorithm.setInputParameter("populationSize", spec.population);
        double max_evals = spec.evaluations * (problem.getNumberOfVariables());
        
        algorithm.setInputParameter("maxEvaluations", max_evals); // problem.getNumberOfVariables() contains the number of all variables that need: fitting initial + output + model parameters. no need to recalculate!
        
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
		

		/* Execute the Algorithm */
        //		long initTime = System.currentTimeMillis();
        
        SolutionSet population = null;
        Variable[] variables = null;
        
        if(max_evals > 0.0){
        	population = algorithm.execute();
        	variables = population.get(0).getDecisionVariables();
        }
        //		long estimatedTime = System.currentTimeMillis() - initTime;
        //		System.out.println("Total execution time: " + estimatedTime);

        
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

                if (ts.settings.evaluation instanceof CrossValidSpec) {

                    model.setEvalMeasures(evaluateModel(model, testDatasets, true));
                    if (ts.settings.evaluation.validation != null && !ts.settings.evaluation.validation.isEmpty()) {
                        model.setValidMeasures(evaluateModel(model, validationDatasets, false));
                    }
                }
                if (ts.settings.evaluation.test != null && !ts.settings.evaluation.test.isEmpty()) {

                    emodel.setEvalMeasures(evaluateModel(model, testDatasets, true));
                }
                if (ts.settings.evaluation.validation != null && !ts.settings.evaluation.validation.isEmpty()) {

                    emodel.setValidMeasures(evaluateModel(model, validationDatasets, false));

                }
            }
            
            List<Dataset> simulations = simulateModel(emodel, datasets);
            emodel.setSimulations(simulations);
            
            
            Map<String, Double> fitnessMeasures = new HashMap<String, Double>();
            
            if(max_evals >0.0){
            	fitnessMeasures.put(objectiveFun.getName(), population.get(0).getObjective(0));
            } else {
            	Double error = 0.0;
            	if(simulations == null){
            		fitnessMeasures.put(objectiveFun.getName(), Double.POSITIVE_INFINITY);	
            	} else {
	            	for(int i=0; i<simulations.size(); i++){
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


    private void writeMSC()
            throws FileNotFoundException, RecognitionException {

        IQGraph graph = new IQGraph(this.incompleteModel);
        Output output = new Output(this.ts.output, graph);

        fitterOld.generateMSCFile(output, this.datasets, this.ts.mappings.dimensions.get(0).col, this.varIndex, this.weightsIndex, ts.outputFilepath);
    }

    private void writeXML()
            throws JAXBException, SAXException {

        Model model = this.model;
        if (model == null) {
            model = this.incompleteModel;
        }
        IQGraph graph = new IQGraph(model);
        XMLSpec spec = new XMLSpec(ts.settings, ts.criterion, graph, ts.data, ts.mappings, new CSerializer(), ts.output.variables);

        XMLSpec.marshal(spec, ts.outputFilepath);

    }

    private void writeJava() {
        out.print(ModelCompiler.modelToString(model, "TestModel"));
    }

    private void duplicate() {
        Model model; // model to be duplicated
        if (this.model != null) {
            model = this.model;
        } else if (this.incompleteModel != null) {
            logger.info("Model not provided. Using incomplete model instead");
            model = this.incompleteModel;
        } else {
            throw new RuntimeException("Cannot perform duplication because no model was provided. Use <model> tag to specify one");
        }

        Model copied = model.copy();
        logger.info("Model sucessufully duplicated");
    }

    private TrajectoryObjectiveFunction setObjectiveFunc(DESpec spec) throws FileNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {

        TrajectoryObjectiveFunction objectiveFun;
        if (spec.objectives.size() == 0) {
            objectiveFun = new RMSEMultiDataset(datasets, outsToCols);
        }
        else if (spec.objectives.get(0).contains("WRMSE"))
        {
        	 objectiveFun = (TrajectoryObjectiveFunction) (Class
                     .forName("fit.objective." + spec.objectives.get(0)).getConstructor(
                             List.class, BiMap.class,BiMap.class).newInstance(datasets, outsToCols,weightsToCols));
        }
        else {
            objectiveFun = (TrajectoryObjectiveFunction) (Class
                    .forName("fit.objective." + spec.objectives.get(0)).getConstructor(
                            List.class, BiMap.class).newInstance(datasets, outsToCols));
        }
        return objectiveFun;
    }

    private void writeEQ() {
        SimpleWriter simple = new SimpleWriter(model, new CSerializer());
        simple.writeToStream(this.out);
        // logger.info("Written equations in C-format to '" + ts.outputFilepath + "'");
    }

    private void fitCombinations()
            throws IOException {
        if (this.incompleteModel == null) {
            throw new RuntimeException("Cannot perform fitting because incomplete model was not provided. Use <incomplete> tag to specify one");
        }
        IQGraph graph = new IQGraph(incompleteModel);

        String baseFilename = ALG_MSC.ALG_MSC.tempDirpath + File.separator + "params" + File.separator + "params";
        String extension = ".out";
        int counter = 0;
        String[] logFilenames = new String[graph.diffParameters.size()];

        for (String icName : graph.diffParameters.keySet()) {
            // Model copied = incompleteModel.copy();
            // IIC cons = (IIC) copied.allConsts.get(icName);
            // cons.value = null;
            //
            logFilenames[counter] = baseFilename + counter + extension;
            // Model completedModel = CVODE.CVODE.fit(copied, this.datasets, this.ts.timeColumn, varIndex, logFilenames[counter]);
            //
            counter++;
        }

        String outputLog = ALG_MSC.ALG_MSC.tempDirpath + File.separator + "params" + File.separator + "merged.out";

        ALG_MSC.mergeAllParametersLogs(outputLog, logFilenames);
    }

    // Print Best Models
    private void PrintBestModels() {

        try {
            this.best_out = new PrintStream(
                    FileUtils.openOutputStream(new File(ts.outputFilepath
                            + "/BestModels.out"))
            );
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        best_out.println("//Model#"
                + ts.BestModels.get(0).get(0).getModelNo() + "\n"
                + ts.BestModels.get(0).get(0));

        best_out.close();

    }

    private void FindBestModel(ExtendedModel emodel) {
        if (ts.BestModels.get(ts.getNumRuns()).get(0) == null) {
            ts.BestModels.get(ts.getNumRuns()).set(0, emodel);
			/*
			 * System.out.println("First MODEL" +
			 * ts.BestModels.get(ts.numRuns).get(EnsembleModel.numRuns)
			 * .getFitnessMeasures());
			 */

        } else { //if validation exists
            if (ts.settings.evaluation != null)
                if (!ts.settings.evaluation.validation.isEmpty()
                        && ts.settings.evaluation.validation != null) {

                    for (Map.Entry<String, Double> entry : emodel
                            .getValidMeasures().entrySet()) {

                        if (ts.BestModels.get(ts.getNumRuns())
                                .get(0)
                                .getValidMeasures().get(entry.getKey()) > emodel
                                .getValidMeasures().get(entry.getKey())) {
                            ts.BestModels.get(ts.getNumRuns()).set(
                                    0, emodel);
							/*
							 * System.out.println("BETTER MODEL" +
							 * ts.BestModels.get(ts.numRuns)
							 * .get(EnsembleModel.numRuns)
							 * .getFitnessMeasures());
							 */
                        }
                    }


                } else { // otherwise choose best from train data
                    for (Map.Entry<String, Double> entry : emodel
                            .getFitnessMeasures().entrySet()) {

                        if (ts.BestModels.get(ts.getNumRuns())
                                .get(0)
                                .getFitnessMeasures().get(entry.getKey()) > emodel
                                .getFitnessMeasures().get(entry.getKey())) {
                            ts.BestModels.get(ts.getNumRuns()).set(
                                    0, emodel);
							/*
							 * System.out.println("BETTER MODEL" +
							 * ts.BestModels.get(ts.numRuns)
							 * .get(EnsembleModel.numRuns)
							 * .getFitnessMeasures());
							 */
                        }
                    }
                }
        }

    }

    private void enumerateProcessList()
            throws IOException, InterruptedException, ConfigurationException, RecognitionException {
        if (this.incompleteModel == null) {
            throw new RuntimeException("Cannot perform search because an incomplete model was not provided. Use the <incomplete> tag to specify one");
        }

        String filename = (ts.filename != null) ? ts.filename : "default";
        int counter = 1;
        while (new File(logDir + "/" + filename + "-" + counter).exists()) {
            counter++;
        }
        String logDirpath = logDir + "/" + filename + "-" + counter;

        fitterOld.initWorkDir();

        ExtendedModel ext = new ExtendedModel(incompleteModel);
        String timeCol;
        if (this.ts.mappings != null) {
            timeCol = this.ts.mappings.dimensions.get(0).col;
        } else {
            timeCol = null;
        }

        ModelEnumerator search = new ModelEnumerator(ext, null, null, null, null, false, null, null, null);

        ExtendedModel fittedModel = search.nextModel();
        out.println(fittedModel.getModel().toProcessList());
        logger.info("Model #" + search.getCounter() + (fittedModel.isSuccessful() ? "" : " failed"));

        while (search.hasNextModel()) {
            fittedModel = search.nextModel();
            out.println(fittedModel.getModel().toProcessList());
            logger.info("Model #" + search.getCounter() + (fittedModel.isSuccessful() ? "" : " failed"));
        }

        //fitter.deleteWorkDir();
    }

    private void count()
            throws IOException, ConfigurationException, InterruptedException, RecognitionException {
        if (this.incompleteModel == null) {
            throw new RuntimeException("Cannot perform search because an incomplete model was not provided. Use the <incomplete> tag to specify one");
        }

        String filename = (ts.filename != null) ? ts.filename : "default";
        int counter = 1;
        while (new File(logDir + "/" + filename + "-" + counter).exists()) {
            counter++;
        }
        String logDirpath = logDir + "/" + filename + "-" + counter;

        ExtendedModel ext = new ExtendedModel(incompleteModel);
        ModelEnumerator search = new ModelEnumerator(ext, this.datasets, null, null, null, false, null, null, null);

        int modelNum = search.count();

        out.println(modelNum + " models generated");
    }

    private void toDot(DotFlag flag)
            throws FileNotFoundException {
        Visualizer.toDotFile(library, flag, false, ts.outputFilepath + "/DotFile.out");
    }

    private void toMatTree()
            throws FileNotFoundException {

        StringBuilder libTree = new StringBuilder();

        libTree.append("{");

        for (TP tp : library.tps.values()) {
            for (TP subTP : tp.subs.values()) {
                libTree.append("\"" + tp.id + "\" -> \"" + subTP.id + "\", ");
            }
        }
        libTree.delete(libTree.length() - 2, libTree.length() - 1);
        libTree.append("}");

        PrintStream out = new PrintStream(ts.outputFilepath + "/Math_tree.out");
        out.print(libTree.toString());
        out.close();

    }

    private void createOutput()
            throws RecognitionException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        Model model = (this.model != null) ? this.model : this.incompleteModel;
        IQGraph graph = new IQGraph(model);
        Output out = new Output(ts.output, graph);

        SimpleWriter simple = new SimpleWriter(graph, new CSerializer());
        simple.writeToStream(this.out);

        System.out.println(out.constants.toString());
        System.out.println(out.variables.toString());
        String odeText = ModelCompiler.iqGraphToString(graph, "Test_Differential");
        String outText = ModelCompiler.outputToString(out, "Test_Output");
        System.out.println(odeText);
        System.out.println("\n\n");
        System.out.println(outText);
    }

    private void testObjective()
            throws RecognitionException, InstantiationException, IllegalAccessException, ClassNotFoundException, FailedSimulationException, IOException {
        Model model = (this.model != null) ? this.model : this.incompleteModel;
        IQGraph graph = new IQGraph(model);
        Output output = new Output(ts.output, graph);

        ODEModel odeModel = new ODEModel(graph, datasets, dimsToCols, exosToCols);

        CVodeSimulator simulator = new CVodeSimulator(ODESolver.BDF, NonlinearSolver.NEWTON);
        simulator.initialize(odeModel);

        simulator.setTolerances(1.0e-3, 1.0e-3);
        simulator.setLinearSolver(LinearSolver.SPGMR);

        Dataset simulation = simulator.simulate();
        //		System.out.print(simulation);

        OutputModel outputModel = new OutputModel(output, datasets, simulation, dimsToCols, exosToCols, outsToCols);
        Dataset outputData = outputModel.compute();
        System.out.println(outputData);
        //
        //		TrajectoryObjectiveFunction objectiveFun = new RMSEObjectiveFunction(datasets.get(0), outsToCols);
        //		double error = objectiveFun.evaluateTrajectory(outputData);
        //		System.out.println("RMSE : " + error);

        odeModel.reset();
        //simulator.reinitialize();

        Dataset simulation2 = simulator.simulate();
        //		System.out.print(simulation2);

        double[] outputParameters = outputModel.getOutputParameters();
        outputParameters[0] = 5;

        outputModel.reset(simulation2, outputModel.getModelParameters(), outputParameters);
        Dataset outputData2 = outputModel.compute();
        System.out.println(outputData2);

    }

    private void testDE()
            throws RecognitionException, ClassNotFoundException, InstantiationException, IllegalAccessException, JMException, IOException {
        //		Model model = (this.model != null) ? this.model : this.incompleteModel;
        IQGraph graph = new IQGraph(this.incompleteModel);
        Output output = new Output(ts.output, graph);

        TrajectoryObjectiveFunction objectiveFun = new RMSEObjectiveFunction(datasets.get(0), outsToCols);

        ObjectiveProblem problem =
                new ObjectiveProblem(output, objectiveFun, datasets, dimsToCols, endosToCols, exosToCols, outsToCols, (CVODESpec) ts.settings.simulator, ts.settings.initialvalues);

        Algorithm algorithm = new DE(problem);

        HashMap<String, Object> parameters;
        Operator crossover; // Crossover operator
        Operator mutation; // Mutation operator
        Operator selection; // Selection operator

        algorithm.setInputParameter("populationSize", 30);
        algorithm.setInputParameter("maxEvaluations", 1000);

        // Crossover operator
        parameters = new HashMap<String, Object>();
        parameters.put("CR", 0.5);
        parameters.put("F", 0.1);
        parameters.put("DE_VARIANT", "rand/1/bin");

        crossover = CrossoverFactory.getCrossoverOperator("DifferentialEvolutionCrossover", parameters);

        // Add the operators to the algorithm
        parameters = null;
        selection = SelectionFactory.getSelectionOperator("DifferentialEvolutionSelection", parameters);

        algorithm.addOperator("crossover", crossover);
        algorithm.addOperator("selection", selection);

		/* Execute the Algorithm */
        long initTime = System.currentTimeMillis();
        SolutionSet population = algorithm.execute();

        long estimatedTime = System.currentTimeMillis() - initTime;
        System.out.println("Total execution time: " + estimatedTime);

        for (int i = 0; i < population.size(); i++) {
            Variable[] variables = population.get(i).getDecisionVariables();
            System.out.println(i + " : [" + variables[0].getValue() + "] - " + population.get(i).toString());
        }

    }
}

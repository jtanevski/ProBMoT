package fit;

import java.io.*;
import java.util.*;
import java.util.StringTokenizer;

import org.apache.commons.configuration.*;
import org.apache.commons.io.*;
import org.apache.commons.io.filefilter.*;
import org.apache.commons.io.output.*;
import org.apache.commons.lang3.*;
import org.apache.commons.launcher.*;
import org.slf4j.*;

import run.*;
import serialize.*;
import struct.*;
import struct.inst.*;
import temp.*;
import util.*;

import com.Ostermiller.util.*;
import com.google.common.collect.*;

public class DASA_MSC implements FitterOld {
	public static enum Method {
		DASA,
		DE,
	}

	public static class Params {
		public int opt_normalize_error;
		public int opt_init_state_fit;
		public int opt_tf_restarts;
		public int opt_fs_restarts;

		/*
		 * public static final Params DEFAULT_SIMULATION = new Params() { { opt_normalize_error = 1; opt_init_state_fit = 1; opt_tf_restarts =
		 * 0; opt_fs_restarts = 0; } };
		 */

		public static final Params DEFAULT_FIT = new Params() {
			{
				opt_normalize_error = 1;
				opt_init_state_fit = 1;
				opt_tf_restarts = 0;
				opt_fs_restarts = 0;
			}
		};
	}

	public static final Logger logger = LoggerFactory.getLogger(CLITaskMain.class);

	public static final DASA_MSC DASA_MSC = new DASA_MSC();

	/**
	 * algorithm for fitting
	 */
	private Method method = Method.DASA;

	public Method getMethod() {
		return this.method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	/**
	 * number of restarts of the algorithm
	 */
	private int restarts = 1;

	public int getRestarts() {
		return this.restarts;
	}

	public void setRestarts(int restarts) {
		this.restarts = restarts;
	}

	/**
	 * evaluations per parameter
	 */
	private int evals = 10000;

	public int getEvals() {
		return this.evals;
	}

	public void setEvals(int evals) {
		this.evals = evals;
	}

	// DASA parameters

	/**
	 * number of ants
	 */
	private int ants = 10;

	public int getAnts() {
		return this.ants;
	}

	public void setAnts(int ants) {
		this.ants = ants;
	}

	private double cauchyIncPer = 1.01;
	private double cauchyDecPer = 0.98;

	public double getCauchyIncPer() {
		return this.cauchyIncPer;
	}

	public void setCauchyIncPer(double cauchyIncPer) {
		this.cauchyIncPer = cauchyIncPer;
	}

	public double getCauchyDecPer() {
		return this.cauchyDecPer;
	}

	public void setCauchyDecPer(double cauchyDecPer) {
		this.cauchyDecPer = cauchyDecPer;
	}

	private double evap = 0.2;

	public double getEvap() {
		return this.evap;
	}
	public void setEvap(double evap) {
		this.evap = evap;

	}

	// DE parameters

	/**
	 * population size
	 */
	private int population = 200;

	public int getPopulation() {
		return this.population;
	}

	public void setPopulation(int population) {
		this.population = population;
	}

	/**
	 * DE strategy
	 *
	 *  1 - DE/best/1/exp
	 *  2 - DE/rand/1/exp
	 *  3 - DE/rand-to-best/1/exp
	 *  4 - DE/best/2/exp
	 *  5 - DE/rand/2/exp
	 *  6 - DE/best/1/bin
	 *  7 - DE/rand/1/bin
	 *  8 - DE/rand-to-best/1/bin
	 *  9 - DE/best/2/bin
	 * 10 - DE/rand/2/bin
	 */
	private int strategy;

	public int getStrategy() {
		return this.strategy;
	}

	public void setStrategy(int strategy) {
		this.strategy = strategy;
	}

	private double F = 0.5;

	public double getF() {
		return this.F;
	}

	public void setF(double F) {
		this.F = F;
	}

	private double Cr = 0.85;

	public double getCr() {
		return this.Cr;
	}

	public void setCr(double Cr) {
		this.Cr = Cr;
	}

	private Params params;
	private MSCFunctions functions = new MSCFunctionsAlt();
	private CSerializer serializer;

	private static final double MIN_INF = Double.MIN_VALUE;
	private static final double PLUS_INF = Double.MAX_VALUE;

	private String tempDirpath = "temp";
	private String workDirpath;

	private DASA_MSC() {
	}

	public void initWorkDir() throws IOException {
		int counter = 1;
		while (new File(tempDirpath + "/" + counter).exists()) {
			counter++;
		}
		this.workDirpath = tempDirpath + "/" + counter;
		new File(this.workDirpath).mkdirs();
		new File(this.workDirpath + "/params").mkdir();

		FileUtils.copyDirectory(new File("make/dasa"), new File(this.workDirpath), HiddenFileFilter.VISIBLE);

	}

	public void deleteWorkDir() throws IOException {
		FileUtils.deleteDirectory(new File(this.workDirpath));
	}

	private void deleteTemporaryFiles(String mscFilename, String outputFilename) throws IOException, InterruptedException {
		// Delete simulationFilename (ms.c) and simulationOutputFilename (simulation.out)
		deleteFile(mscFilename);
		deleteFile(outputFilename);

		// Clean temporary files used during make
		String command;

		Runtime runtime = Runtime.getRuntime();
		Process process;

		command = "make -C" + this.workDirpath + " clean";
		process = runtime.exec(command, null);
		process.waitFor();

		if (process.exitValue() != 0) {
			String errorMsg = "Error invoking MAKE : error code - " + process.exitValue() + "\n";
			errorMsg += IOUtils.toString(process.getErrorStream());
			throw new RuntimeException(errorMsg);
		}
	}

	private void deleteFile(String filename) {
		File file = new File(filename);

		// Make sure the file or directory exists and isn't write protected
		if (!file.exists())
			throw new IllegalArgumentException("Delete: no such file: " + filename);
		if (!file.canWrite())
			throw new IllegalArgumentException("Delete: write protected: " + filename);

		// Attempt to delete it
		boolean success = file.delete();

		if (!success)
			throw new IllegalArgumentException("Delete: deletion failed");
	}

	private void makeAndRun()
	// NOTE: compile and run DASA
			throws IOException, InterruptedException, FailedSimulationException {
		String command;

		Runtime runtime = Runtime.getRuntime();
		Process process;

		command = "make";
		File workDir = new File(System.getProperty("user.dir") + "/" + this.workDirpath);
		process = runtime.exec(command, null, workDir);
		process.waitFor();

		if (process.exitValue() != 0) {
			String errorMsg = "Error invoking MAKE : error code - " + process.exitValue() + "\n";
			errorMsg += IOUtils.toString(process.getErrorStream());
			throw new FailedSimulationException(errorMsg);
		}

		command = "./test";
		process = runtime.exec(command, null, workDir);

		Thread t = new StreamConnector(process.getInputStream(), new NullOutputStream());
		t.start();
		process.waitFor();
		t.join();

		if (process.exitValue() != 0) {
			String errorMsg = "Error invoking DASA : error code - " + process.exitValue() + "\n";
			errorMsg += IOUtils.toString(process.getErrorStream());
			throw new FailedSimulationException(errorMsg);
		}

	}

	public String generateMSCString(Output output, List<Dataset> datasets, String timeColumn, Map<String, Integer> varIndex, Map<String, Integer> uncIndex) {
		IQGraph graph = output.graph;

		List<IQNode> order = graph.diffOrder;

		this.serializer = new MSCSerializer();

		this.params = Params.DEFAULT_FIT;

		String MS_init_dims = functions.MS_init_dims(output, datasets, varIndex);
		String MS_fill_in = functions.MS_fill_in(output, datasets, timeColumn, varIndex);
		String MS_params = functions.MS_params(output, varIndex);
		String MS_model = functions.MS_model(output, order, varIndex);
		String sim_model_output = functions.sim_model_output(output, varIndex);

		String msc = MS_init_dims + "\n" + MS_fill_in + "\n" + MS_params + "\n" + MS_model + "\n" + sim_model_output;

		return msc;
	}

	public void generateMSCFile(Output output, List<Dataset> datasets, String timeColumn, Map<String, Integer> varIndex, Map<String, Integer> uncIndex, String filename)
			throws FileNotFoundException {
		String msc = generateMSCString(output, datasets, timeColumn, varIndex, uncIndex);

		PrintWriter out = new PrintWriter(filename);
		out.print(msc);
		out.close();
	}

	public String generateParamsString(Output output, List<Dataset> datasets) {
		IQGraph graph = output.graph;

		StringBuilder paramsText = new StringBuilder();

		double precision = 1.E-15;
		paramsText.append("3 " + (graph.unknownParameters.size() + output.fitted.size() + graph.diffDifferential.size() * datasets.size()) + "\n");

		for (int i = 0; i < graph.unknownParameters.size(); i++) {
			IC ic = graph.unknownParameters.getValue(i).cons;

			// default lower and upper bound. It can be changed bellow.
			double lower = ic.range.getLower();
			double upper = ic.range.getUpper();

			IIC iic = (IIC) ic;
			if (iic.fit_range != null) { // if fit_range is provided, use it
				lower = iic.fit_range.getLower();
				upper = iic.fit_range.getUpper();
			}

			lower = !Double.isInfinite(lower) ? lower : MIN_INF;
			upper = !Double.isInfinite(upper) ? upper : PLUS_INF;

			paramsText.append(lower + " " + upper + " " + precision + "\n");
		}

		for (int i = 0; i < output.fitted.size(); i++) {
			OutputCons cons = output.fitted.getValue(i);

			// default lower and upper bound. It can be changed bellow.
			double lower = cons.fit_range.getLower();
			double upper = cons.fit_range.getUpper();

			lower = !Double.isInfinite(lower) ? lower : MIN_INF;
			upper = !Double.isInfinite(upper) ? upper : PLUS_INF;

			paramsText.append(lower + " " + upper + " " + precision + "\n");
		}

		for (int k = 0; k < datasets.size(); k++) {
			for (int j = 0; j < graph.diffDifferential.size(); j++) {
				IV iv = graph.diffDifferential.getValue(j).var;

				double initial;
				double lower = iv.range.getLower(); // default lower and upper bound. It can be changed bellow.
				double upper = iv.range.getUpper();

				if (iv.initial != null) { // if initial value is provided, use that one
					initial = iv.initial;
				} else { // if not, initial should be fitted
					IIV iiv = (IIV) iv;
					if (iiv.fit_range != null) { // if fit_range is provided, use it
						lower = iiv.fit_range.getLower();
						upper = iiv.fit_range.getUpper();
					}
				}

				lower = !Double.isInfinite(lower) ? lower : MIN_INF;
				upper = !Double.isInfinite(upper) ? upper : PLUS_INF;

				paramsText.append(lower + " " + upper + " " + precision + "\n");
			}
		}

		return paramsText.toString();
	}

	public void generateParamsFile(Output output, List<Dataset> datasets, String filename) throws FileNotFoundException {
		String params = generateParamsString(output, datasets);

		PrintWriter out = new PrintWriter(filename);
		out.print(params);
		out.close();
	}

	public void generateOptFile(String filename) throws FileNotFoundException {
		PrintWriter output = new PrintWriter(filename);
		output.println(0);
		output.println(20);
		output.close();
	}

	public String generateINIString(Output output, List<Dataset> datasets) {
		IQGraph graph = output.graph;

		int NumOfParams = graph.unknownParameters.size() + output.fitted.size() + graph.diffDifferential.size() * datasets.size();

		switch (this.method) {
		case DASA:
//@formatter:off
			String[] dasaIniText = {
					"[Initialization]",
					"DE= 0",
					"NP= 300",
					"F= 0.5",
					"CR= 0.85",
					"Strategy= 3",
					"CASA= 0",
					"AccOfResetTrigger= 1.E-15",
					"ChosenFunction= 330",
					"NumOfParam= %d",
					"Evap= %.3f",
					"NumOfAnts= %d",
					"Time= %d",
					"LastAlgIteration= 1",
					"TraceFrequency= 1000",
					"CauchyIncPer= %.3f",
					"CauchyDecPer= %.3f",
					"Optimum= 0.0",
					"Accuracy= 15",
					"RandSeed= 0",
					"DiscreteBase= 10",
					"InitStateFit= 1",
					"NormError= 1",
					"LogFit= 0",
					"PrintFullSim= 0",
			};
			//@formatter:on
			return String.format(StringUtils.join(dasaIniText, "\n"), NumOfParams, this.evap, this.ants, NumOfParams * this.evals, this.cauchyIncPer,
					this.cauchyDecPer);
		case DE:
//@formatter:off
			String[] deIniText = {
					"[Initialization]",
					"DE= 1",
					"NP= %d",
					"F= %.3f",
					"CR= %.3f",
					"Strategy= %d",
					"CASA= 0",
					"AccOfResetTrigger= 1.E-15",
					"ChosenFunction= 330",
					"NumOfParam= %d",
					"Evap= 0.2",
					"NumOfAnts= 10",
					"Time= %d",
					"LastAlgIteration= 1",
					"TraceFrequency= 1000",
					"CauchyIncPer= 1.01",
					"CauchyDecPer= 0.999",
					"Optimum= 0.0",
					"Accuracy= 15",
					"RandSeed= 0",
					"DiscreteBase= 10",
					"InitStateFit= 1",
					"NormError= 1",
					"LogFit= 0",
					"PrintFullSim= 0",
			};
			//@formatter:on
			return String.format(StringUtils.join(deIniText, "\n"), this.population, this.F, this.Cr, this.strategy, NumOfParams, NumOfParams
					* this.evals);
		default:
			throw new IllegalStateException();
		}
	}

	public void generateINIFile(Output output, List<Dataset> datasets, String iniFilename) throws FileNotFoundException {
		String ini = generateINIString(output, datasets);

		PrintWriter out = new PrintWriter(iniFilename);
		out.print(ini);
		out.close();
	}

	public void generateFitOutputFile(Output output, List<Dataset> datasets, String timeColumn, Map<String, Integer> varIndex, Map<String, Integer> uncIndex,
			String mscFilename, String paramsFilename, String optFilename, String iniFilename, String logDirpath) throws IOException,
			InterruptedException, FailedSimulationException {
		generateMSCFile(output, datasets, timeColumn, varIndex, uncIndex, mscFilename);
		generateParamsFile(output, datasets, paramsFilename);
		generateOptFile(optFilename);
		generateINIFile(output, datasets, iniFilename);
		makeAndRun(); // runFit(fitFilename, outputFilename);
		copyFxDir(logDirpath);
	}

	public void copyFxDir(String logDirpath) throws IOException {
		if (logDirpath != null) {
			new File(logDirpath).mkdirs();
			FileUtils.copyDirectory(new File(this.workDirpath + "/Fx"), new File(logDirpath));
		}
	}

	public void generateFitOutputFile(Output output, List<Dataset> datasets, String timeColumn, Map<String, Integer> varIndex, Map<String, Integer> uncIndex,
			String logDirpath) throws IOException, InterruptedException, FailedSimulationException {

		String mscFilename = this.workDirpath + "/ms.c";
		String paramsFilename = this.workDirpath + "/params/f330.params";
		String optFilename = this.workDirpath + "/params/f330.opt";
		String iniFilename = this.workDirpath + "/DASA.ini";

		generateFitOutputFile(output, datasets, timeColumn, varIndex, uncIndex, mscFilename, paramsFilename, optFilename, iniFilename, logDirpath);
	}

	public ExtendedModel fit(Output output, List<Dataset> datasets, String timeColumn, Map<String, Integer> varIndex, Map<String, Integer> uncIndex, String logDirpath)
			throws IOException, InterruptedException, ConfigurationException {

		IQGraph graph = output.graph;
		int NumOfParams = graph.unknownParameters.size() + output.fitted.size() + graph.diffDifferential.size() * datasets.size();
		int evaluations = NumOfParams * this.evals;

		String statsFormat;
		String statsFilename;
		int normalize = ALG_MSC.Params.DEFAULT_FIT.opt_normalize_error;
		switch (this.method) {
		case DASA:
			statsFormat = "/Fx/stats/DASA-f330-ant%d-ev%.2f-s+%.3f-s-%.3f-eval%d-init1-norm" + normalize + "-log0-dim%d-1.stats";
			statsFilename = this.workDirpath
					+ String.format(statsFormat, this.ants, this.evap, this.cauchyIncPer - 1, 1 - this.cauchyDecPer, evaluations, NumOfParams);
			break;
		case DE:
			statsFormat = "/Fx/stats/DE-f330-pop%d-f%.3f-cr%.3f-str%d-eval%d-init1-norm" + normalize + "-log0-dim%d-1.stats";
			statsFilename = this.workDirpath
					+ String.format(statsFormat, this.population, this.F, this.Cr, this.strategy, evaluations, NumOfParams);
			break;
		default:
			throw new IllegalStateException();
		}

		ExtendedModel bestModel = null;
		ExtendedModel currentModel;
		int i = 0;

		do {
			try {
				generateFitOutputFile(output, datasets, timeColumn, varIndex, uncIndex, logDirpath + "/0");
				currentModel = createFittedModel(output, datasets, statsFilename, null);
				if (bestModel == null) {
					bestModel = currentModel;
				} else {
					if (currentModel.getFitnessMeasures().get("SSE") < bestModel.getFitnessMeasures().get("SSE")) {
						bestModel = currentModel;
					}
				}
			} catch (FailedSimulationException ex) {

			}

			i++;
		} while (i < this.restarts);


		FileUtils.deleteDirectory(new File(this.workDirpath + "/Fx"));

		return bestModel;
	}

	private Double[] readParameters(String statsFilename) throws ConfigurationException {
		HierarchicalINIConfiguration conf = new HierarchicalINIConfiguration(statsFilename);
		List<Double> doubles = new LinkedList<Double>();

		SubnodeConfiguration section = conf.getSection("Solution");
		Iterator<String> iter = section.getKeys();
		while (iter.hasNext()) {
			String key = iter.next();
			doubles.add(section.getDouble(key));
		}
		return (Double[]) doubles.toArray(new Double[0]);
	}

	private Map<String, Double> readFitnessMeasures(String statsFilename) throws ConfigurationException {
		Map<String, Double> fitnessMeasures = new LinkedHashMap<String, Double>();

		HierarchicalINIConfiguration conf = new HierarchicalINIConfiguration(statsFilename);
		SubnodeConfiguration section = conf.getSection("Stats");
		double error = section.getDouble("Err");
		fitnessMeasures.put("SSE", error);

		return fitnessMeasures;
	}

	private ExtendedModel createFittedModel(Output output, List<Dataset> datasets, String statsFilename, String logFilename) throws IOException,
			ConfigurationException {
		IQGraph graph = output.graph;

		// Read all parameters from the simulation output file
		Double[] parameters = readParameters(statsFilename);
		Map<String, Double> fitnessMeasures = readFitnessMeasures(statsFilename);
		int i;

		// Optionally log the fitted parameters
		if (logFilename != null) {
			logAllParameters(graph, parameters, logFilename);
		}

		// create a copy of the model
		ExtendedModel fitted = new ExtendedModel(graph.model.copy());

		// fill-in all constant parameters with 'null' values (missing values)
		for (i = 0; i < graph.unknownParameters.size(); i++) {
			String consName = graph.unknownParameters.getKey(i);
			fitted.getModel().allConsts.get(consName).value = parameters[i];
		}

		// fill-in output constants
		Map<String, Double> outputConsts = new LinkedHashMap<String, Double> ();
		for (int j = 0; j < output.fitted.size(); j++, i++) {
			String outputName = output.fitted.getKey(j);
			Double outputValue = parameters[i];
			outputConsts.put(outputName, outputValue);
		}
		fitted.setOutputConstants(outputConsts);

		// fill-in all initial values of variables with the new values
		// NOTE: here it is not clear whether ALL initial values should be changes, or only the missing ones
		// because ALG re-fits all initial values

		List<Map<String, Double>> initialsAll = new LinkedList<Map<String,Double>> ();
		Map<String, Double> initialsOne = new LinkedHashMap<String, Double> ();

		for (int j = 0; j < graph.diffDifferential.size(); j++, i++) {
			String varName = graph.diffDifferential.getValue(j).var.getFullName();
			fitted.getModel().allVars.get(varName).initial = parameters[i];
			initialsOne.put(varName, parameters[i]);
		}
		initialsAll.add(initialsOne);

		for (int k = 1; k < datasets.size(); k++) {
			initialsOne = new LinkedHashMap<String, Double> ();
			for (int j = 0; j < graph.diffDifferential.size(); j++, i++) {
				String varName = graph.diffDifferential.getValue(j).var.getFullName();
				initialsOne.put(varName, parameters[i]);
			}
			initialsAll.add(initialsOne);
		}

		fitted.setInitials(initialsAll);

		fitted.setFitnessMeasures(fitnessMeasures);

		return fitted;
	}

	private void logFittedParameters(IQGraph graph, double[] parameters, String logParametersFilename) throws IOException {
		PrintWriter writer = new PrintWriter(logParametersFilename);
		String formatString = "%-60s : %14.6f  - %14.6f  - %14.6f  : %14.6f";

		writer.println(String.format("%-60s : %14s  - %14s  - %14s  : %14s\n", "NAME", "INITIAL", "MINIMUM", "MAXIMUM", "FITTED"));
		writer.println("CONSTANT PARAMETERS\n");
		int i;
		for (i = 0; i < graph.unknownParameters.size(); i++) {
			IIC cons = (IIC) graph.unknownParameters.getValue(i).cons;
			writer.println(String.format(formatString, cons.getFullName(), cons.fit_start,
					(cons.fit_range != null) ? cons.fit_range.getLower() : cons.range.getLower(),
					(cons.fit_range != null) ? cons.fit_range.getUpper() : cons.range.getUpper(), parameters[i]));
		}

		// fill-in all initial values of variables with the new values
		// NOTE: here it is not clear whether ALL initial values should be changes, or only the missing ones because CVODEDE re-fits all
		// initial values
		writer.println("\nINITIAL VALUES\n");
		for (int j = 0; j < graph.diffDifferential.size(); j++, i++) {
			IV var = graph.diffDifferential.getValue(j).var;
			if (var instanceof IIV) {
				IIV ivar = (IIV) var;
				writer.println(String.format(formatString, ivar.getFullName(), ivar.fit_start,
						(ivar.fit_range != null) ? ivar.fit_range.getLower() : ivar.range.getLower(),
						(ivar.fit_range != null) ? ivar.fit_range.getUpper() : ivar.range.getUpper(), parameters[i]));
			} else {
				writer.println(String.format(formatString, var.getFullName(), var.initial, var.range.getLower(), var.range.getUpper(),
						parameters[i]));
			}
		}
		writer.close();
	}

	private void logAllParameters(IQGraph graph, Double[] parameters, String logParametersFilename) throws IOException {
		PrintWriter writer = new PrintWriter(logParametersFilename);
		String formatString = "%-60s : %14.6f  - %14.6f  - %14.6f  : %14.6f";

		writer.println(String.format("%-60s : %14s  - %14s  - %14s  : %14s\n", "NAME", "INITIAL", "MINIMUM", "MAXIMUM", "FITTED"));
		writer.println("FITTED CONSTANT PARAMETERS\n");
		int i;
		for (i = 0; i < graph.unknownParameters.size(); i++) {
			IIC cons = (IIC) graph.unknownParameters.getValue(i).cons;
			writer.println(String.format(formatString, cons.getFullName(), cons.fit_start,
					(cons.fit_range != null) ? cons.fit_range.getLower() : cons.range.getLower(),
					(cons.fit_range != null) ? cons.fit_range.getUpper() : cons.range.getUpper(), parameters[i]));
		}

		writer.println("\nGIVEN CONSTANT PARAMETERS\n");
		for (int k = 0; k < graph.knownParameters.size(); k++) {
			IIC cons = (IIC) graph.knownParameters.getValue(k).cons;
			writer.println(String.format(formatString, cons.getFullName(), cons.value, (cons.fit_range != null) ? cons.fit_range.getLower()
					: cons.range.getLower(), (cons.fit_range != null) ? cons.fit_range.getUpper() : cons.range.getUpper(), cons.value));
		}

		// fill-in all initial values of variables with the new values
		// NOTE: here it is not clear whether ALL initial values should be changes, or only the missing ones because CVODE re-fits all initial
		// values
		writer.println("\nINITIAL VALUES\n");
		for (int j = 0; j < graph.diffDifferential.size(); j++, i++) {
			IV var = graph.diffDifferential.getValue(j).var;
			if (var instanceof IIV) {
				IIV ivar = (IIV) var;
				writer.println(String.format(formatString, ivar.getFullName(), ivar.fit_start,
						(ivar.fit_range != null) ? ivar.fit_range.getLower() : ivar.range.getLower(),
						(ivar.fit_range != null) ? ivar.fit_range.getUpper() : ivar.range.getUpper(), parameters[i]));
			} else {
				writer.println(String.format(formatString, var.getFullName(), var.initial, var.range.getLower(), var.range.getUpper(),
						parameters[i]));
			}
		}
		writer.close();
	}

	public static void mergeFittedParametersLogs(String outputLog, String... inputLogs) throws IOException {
		String[] inputStrings = new String[inputLogs.length];
		String[] constStrings = new String[inputLogs.length];
		String[] initialStrings = new String[inputLogs.length];

		for (int i = 0; i < inputLogs.length; i++) {
			inputStrings[i] = FileUtils.readFileToString(new File(inputLogs[i]));
			String[] split = inputStrings[i].split("CONSTANT PARAMETERS|INITIAL VALUES");
			constStrings[i] = split[1].trim();
			initialStrings[i] = split[2].trim();
		}
		Map<String, Double[]> consts = new LinkedHashMap<String, Double[]>();
		Map<String, Double[]> initials = new LinkedHashMap<String, Double[]>();

		// traverse all log files
		for (int i = 0; i < inputLogs.length; i++) {
			// traverse all constants
			String[] constLines = constStrings[i].split("\n");
			for (int j = 0; j < constLines.length; j++) {
				String constName = constLines[j].substring(0, constLines[j].indexOf(' '));
				Double constValue = new Double(constLines[j].substring(constLines[j].lastIndexOf(' ')).trim());

				if (consts.containsKey(constName)) {
					consts.get(constName)[i] = constValue;
				} else {
					Double[] constValues = new Double[inputLogs.length];
					constValues[i] = constValue;
					consts.put(constName, constValues);
				}
			}

			// traverse all initial values
			String[] initialLines = initialStrings[i].split("\n");
			for (int j = 0; j < initialLines.length; j++) {
				String initialName = initialLines[j].substring(0, initialLines[j].indexOf(' '));
				Double initialValue = new Double(initialLines[j].substring(initialLines[j].lastIndexOf(' ')).trim());

				if (initials.containsKey(initialName)) {
					initials.get(initialName)[i] = initialValue;
				} else {
					Double[] initialValues = new Double[inputLogs.length];
					initialValues[i] = initialValue;
					initials.put(initialName, initialValues);
				}
			}
		}

		PrintWriter writer = new PrintWriter(outputLog);
		String formatName = "%-60s :";
		String formatValues = StringUtils.repeat("%20.6f", " : ", inputLogs.length);

		String formatTitleName = "%-60s :";
		String formatTitleValues = StringUtils.repeat("%20s", " : ", inputLogs.length) + "\n";

		writer.println(String.format(formatTitleName, "NAME") + String.format(formatTitleValues, inputLogs));
		writer.println("CONSTANT PARAMETERS\n");
		int i;
		for (String constName : consts.keySet()) {
			writer.println(String.format(formatName, constName) + String.format(formatValues, consts.get(constName)));
		}

		// fill-in all initial values of variables with the new values
		// NOTE: here it is not clear whether ALL initial values should be changes, or only the missing ones because CVODE re-fits all initial
		// values
		writer.println("\nINITIAL VALUES\n");
		for (String varName : initials.keySet()) {
			writer.println(String.format(formatName, varName) + String.format(formatValues, initials.get(varName)));
		}

		writer.close();
	}

	public static void mergeAllParametersLogs(String outputLog, String... inputLogs) throws IOException {
		// initialize string arrays for reading from inputLogs
		String[] inputStrings = new String[inputLogs.length];

		String[] fittedStrings = new String[inputLogs.length];
		String[] givenStrings = new String[inputLogs.length];
		String[] initialStrings = new String[inputLogs.length];

		// read files in strings
		for (int i = 0; i < inputLogs.length; i++) {
			inputStrings[i] = FileUtils.readFileToString(new File(inputLogs[i]));
			String[] split = inputStrings[i].split("FITTED CONSTANT PARAMETERS|GIVEN CONSTANT PARAMETERS|INITIAL VALUES");
			fittedStrings[i] = split[1].trim();
			givenStrings[i] = split[2].trim();
			initialStrings[i] = split[3].trim();
		}

		// initialize tables (maps)
		Map<String, Double[]> fitted = new LinkedHashMap<String, Double[]>();
		Map<String, Double[]> given = new LinkedHashMap<String, Double[]>();
		Map<String, Double[]> initials = new LinkedHashMap<String, Double[]>();

		// put values into tables (maps)
		// traverse all log files
		for (int i = 0; i < inputLogs.length; i++) {
			// traverse all fitted parameters
			String[] fittedLines = fittedStrings[i].split("\n");
			for (int j = 0; j < fittedLines.length; j++) {
				String fittedName = fittedLines[j].substring(0, fittedLines[j].indexOf(' '));
				Double fittedValue = new Double(fittedLines[j].substring(fittedLines[j].lastIndexOf(' ')).trim());

				if (fitted.containsKey(fittedName)) {
					fitted.get(fittedName)[i] = fittedValue;
				} else {
					Double[] fittedValues = new Double[inputLogs.length];
					fittedValues[i] = fittedValue;
					fitted.put(fittedName, fittedValues);
				}
			}

			// traverse all given parameters
			String[] givenLines = givenStrings[i].split("\n");
			for (int j = 0; j < givenLines.length; j++) {
				String givenName = givenLines[j].substring(0, givenLines[j].indexOf(' '));
				Double givenValue = new Double(givenLines[j].substring(givenLines[j].lastIndexOf(' ')).trim());

				if (given.containsKey(givenName)) {
					given.get(givenName)[i] = givenValue;
				} else {
					Double[] givenValues = new Double[inputLogs.length];
					givenValues[i] = givenValue;
					given.put(givenName, givenValues);
				}
			}

			// traverse all initial values
			String[] initialLines = initialStrings[i].split("\n");
			for (int j = 0; j < initialLines.length; j++) {
				String initialName = initialLines[j].substring(0, initialLines[j].indexOf(' '));
				Double initialValue = new Double(initialLines[j].substring(initialLines[j].lastIndexOf(' ')).trim());

				if (initials.containsKey(initialName)) {
					initials.get(initialName)[i] = initialValue;
				} else {
					Double[] initialValues = new Double[inputLogs.length];
					initialValues[i] = initialValue;
					initials.put(initialName, initialValues);
				}
			}
		}

		// print tables

		// initialize format strings
		PrintWriter writer = new PrintWriter(outputLog);
		String formatName = "%-60s";
		String formatFitted = " :  %20.6f ";
		String formatGiven = " : [%20.6f]";
		String formatValues = StringUtils.repeat(formatFitted, inputLogs.length);

		String formatTitleName = "%-60s :";
		String formatTitleValues = StringUtils.repeat("%20s", " : ", inputLogs.length) + "\n";

		// print header
		writer.println(String.format(formatTitleName, "NAME") + String.format(formatTitleValues, inputLogs));

		// print parameters
		writer.println("CONSTANT PARAMETERS\n");

		// gather all parameter names (fitter or given) in one set
		Set<String> parameters = new HashSet<String>(fitted.keySet());
		parameters.addAll(given.keySet());

		// print parameters as a table
		// for each parameter print one row
		for (String paramName : parameters) {
			writer.print(String.format(formatName, paramName));

			// for each inputLog print one value
			for (int i = 0; i < inputLogs.length; i++) {
				String formatValue;
				Double paramValue;
				if (fitted.containsKey(paramName) && fitted.get(paramName)[i] != null) {
					formatValue = formatFitted;
					paramValue = fitted.get(paramName)[i];
				} else if (given.containsKey(paramName) && given.get(paramName)[i] != null) {
					formatValue = formatGiven;
					paramValue = given.get(paramName)[i];
				} else {
					throw new IllegalStateException();
				}
				writer.print(String.format(formatValue, paramValue));
			}
			writer.println();
		}

		// fill-in all initial values of variables with the new values
		// NOTE: here it is not clear whether ALL initial values should be changes, or only the missing ones
		// because COVDE re-fits all initial values
		writer.println("\nINITIAL VALUES\n");
		for (String varName : initials.keySet()) {
			writer.println(String.format(formatName, varName) + String.format(formatValues, initials.get(varName)));
		}

		writer.close();
	}

	interface MSCFunctions {
		String MS_init_dims(Output output, List<Dataset> datasets, Map<String, Integer> varIndex);
		String MS_fill_in(Output output, List<Dataset> datasets, String timeColumn, Map<String, Integer> varIndex);
		String MS_params(Output output, Map<String, Integer> varIndex);
		String MS_model(Output output, List<IQNode> order, Map<String, Integer> varIndex);
		String sim_model_output(Output output, Map<String, Integer> varIndex);
	}

	private class MSCFunctionsAlt implements MSCFunctions {

		public String MS_init_dims(Output output, List<Dataset> datasets, Map<String, Integer> varIndex) {
			IQGraph graph = output.graph;

			int data_nsets = datasets.size();
			int data_nvars = datasets.get(0).getNCols();
			int data_length = 0;
			for (Dataset ds : datasets) {
				data_length += ds.getNRows();
			}

			int sys_var_n = graph.diffDifferential.size();

			int sys_var_nobserved = 0;
			for (int i = 0; i < graph.diffDifferential.size(); i++) {
				IV iv = graph.diffDifferential.getValue(i).var;
				Integer index = varIndex.get(iv.getFullName());
				if (index != null) {
					sys_var_nobserved++;
				}
			}

			int numSimSucc = 0;
			int numSimFail = 0;

			//@formatter:off
			String[] funText = {
					"void MS_init_dims(void) {",
					"",
//					"	sprintf(fileData, \"%s\");",
					"	data.nsets = %d;",
					"	data.nvars = %d;",
					"	data.length = %d;",
					"",
					"	sys_var.n = %d;",
					"	param.n = numOfParams;",
					"	sys_var.nobserved = %d;",
					"",
					"	opt.init_state_fit = %d;",
					"	opt.normalize_error = %d;",
					"	numSimSucc = %d;",
					"	numSimFail = %d;",
				   "}",
					""
			};

			return String.format(StringUtils.join(funText, "\n"),
//					"../../" + datasets.get(0).getFilepath(),					//FIXME: works for one data set
					data_nsets,
					data_nvars,
					data_length,
					sys_var_n,
					output.variables.size(),				// NOTE: sys_var.nobserved denotes the number of output variables!
					params.opt_init_state_fit,
					params.opt_normalize_error,
					numSimSucc,
					numSimFail
			);
			//@formatter:on
		}

		public String MS_fill_in(Output output, List<Dataset> datasets, String timeColumn, Map<String, Integer> varIndex) {
			IQGraph graph = output.graph;

			StringBuilder funText = new StringBuilder();

			funText.append("void MS_fill_in(void) {\n\n");

//			funText.append("	data.set_files[0] = strdup(fileData);\n");
//			funText.append("	data.set_tos[0] = data.length;\n");

			int curLength = 0;
			for (int i = 0; i < datasets.size(); i++) {
				funText.append("	data.set_files[" + i + "] = strdup(\"../../" + datasets.get(i).getFilepath() + "\");\n");
				curLength+=datasets.get(i).getNRows();
				funText.append("	data.set_tos[" + i + "] = " + curLength + ";\n");
			}
			funText.append("\n");

			funText.append("	data.time = data.table[" + datasets.get(0).getColIndex(timeColumn) + "];\n");
			funText.append("\n");

			for (int i = 0; i < output.variables.size(); i++) {
				OutputVar var = output.variables.get(i);
				Integer index = varIndex.get(var.name);
				funText.append("	// " + var.name + "\n");
				funText.append("	data.in[" + i + "] = data.table[" + index + "];\n");

			}


			funText.append("}\n");
			return funText.toString();
		}

		public String MS_params(Output output, Map<String, Integer> varIndex) {
			IQGraph graph = output.graph;

			StringBuilder funText = new StringBuilder();
			funText.append("void MS_params(TEVector c) {\n\n");

			funText.append("/* constant parameters */\n");
			funText.append("	int i;\n");
			funText.append("	for ( i=0; i < param.n; ++i)\n");
			funText.append("		param.vals[i] = (double) c[i];\n");

			funText.append("}\n");

			return funText.toString();
		}

		public String MS_model(Output output, List<IQNode> order, Map<String, Integer> varIndex) {
			IQGraph graph = output.graph;

			StringBuilder funText = new StringBuilder();
			funText.append("void MS_model(double t) {\n\n");

			// first serialize the equations but don't print them
			StringBuilder eqLines = new StringBuilder();
			for (int i = 0; i < order.size(); i++) {
				IQ iq = order.get(i).iq;
				String line = "double " + serializer.serialize(iq) + ";";
				eqLines.append("\t" + line + "\n");
			}
			eqLines.append("\n");

			// take values for system variables into local variables
			funText.append("/* System variables */\n");
			for (int i = 0; i < graph.diffDifferential.size(); i++) {
				IVNode ivNode = graph.diffDifferential.getValue(i);
				String line = "double " + serializer.serialize(ivNode.var) + " = sys_var.vals[" + i + "];";
				funText.append("\t" + line + "\n");
			}
			funText.append("\n");

			// take values for exogenous variables into local variables
			if (graph.diffExogenous.size() > 0) {
				funText.append("/* Exogenous variables */\n");

				funText.append("	extern int t_to_index(double t);\n");
				funText.append("	int i = t_to_index(t);\n\n");
			}

			for (int i = 0; i < graph.diffExogenous.size(); i++) {
				IVNode ivNode = graph.diffExogenous.getValue(i);
				if (!ivNode.var.id.equals("t")) { //FIXME: :)
					String line = "double " + serializer.serialize(ivNode.var) + " = data.table["
							+ varIndex.get(ivNode.var.getFullName()) + "][i];";
					funText.append("\t" + line + "\n");
				}
			}
			funText.append("\n");

			// take values from param.vals (fitted parameters) into local variables
			funText.append("/* Fitted parameters */\n");
			for (int i = 0; i < graph.unknownParameters.size(); i++) {
				IC ic = graph.unknownParameters.getValue(i).cons;
				String line = "double " + serializer.serialize(ic) + " = param.vals[" + i + "];";
				funText.append("\t" + line + "\n");
			}
			funText.append("\n");

			// take values from known parameters into local variables
			funText.append("/* Known parameters */\n");
			for (int i = 0; i < graph.knownParameters.size(); i++) {
				IC ic = graph.knownParameters.getValue(i).cons;
				String line = "double " + serializer.serialize(ic) + " = " + ic.value + ";";
				funText.append("\t" + line + "\n");
			}
			funText.append("\n");

			// write equations in local variables
			funText.append("/* Equations */\n");
			funText.append(eqLines);

			// take values from local variables to sys_var.dot_vals
			funText.append("/* System variables */\n");
			for (int i = 0; i < graph.diffDifferential.size(); i++) {
				IVNode ivNode = graph.diffDifferential.getValue(i);
				String line = "sys_var.dot_vals[" + i + "] = " + serializer.serialize(ivNode.var, true) + ";";
				funText.append("\t" + line + "\n");
			}

			funText.append("}\n");

			return funText.toString();
		}

		public String sim_model_output(Output output, Map<String, Integer> varIndex) {
			int paramStart = output.graph.unknownParameters.size();

			StringBuilder funText = new StringBuilder();
			funText.append("void sim_model_output(void) {\n\n");
			funText.append("	int i;\n");

			// Model Parameters
			funText.append("	/* Model parameters */\n");
			for (int i = 0; i < output.neededCons.size(); i++) {
				IC cons = output.neededCons.getValue(i).cons;
				String consName = serializer.serialize(cons);
				int consI = output.graph.unknownParameters.indexOf(cons.getFullName());
				funText.append("	double " + consName + " = " + "param.vals[" + consI + "];\n");
			}
			funText.append("\n");

			// Fitted Parameters
			funText.append("	/* Fitted parameters */\n");
			for (int i = 0; i < output.fitted.size(); i++) {
				OutputCons cons = output.fitted.getValue(i);
				funText.append("	double " + cons.name + " = " + "param.vals[" + (paramStart + i) + "];\n");
			}
			funText.append("\n");

			// Known Parameters
			funText.append("	/* Known parameters */\n");
			for (int i = 0; i < output.known.size(); i++) {
				OutputCons cons = output.known.getValue(i);
				funText.append("	double " + cons.name + " = " + cons.value + ";\n");
			}
			funText.append("\n");

			// For Cycle
			funText.append("	for (i = 0; i < data.length; i++) {\n");

			// Exogenous
			funText.append("		/* Exogenous */\n");
			for (int i = 0; i < output.neededExo.size(); i++) {
				IV var = output.neededExo.getValue(i).var;
				String varName = serializer.serialize(var);
				int varI = varIndex.get(var.getFullName());
				funText.append("		double " + varName + " = " + "data.table[" + varI + "][i];\n");
			}
			funText.append("\n");

			// State
			funText.append("		/* State */\n");
			for (int i = 0; i < output.neededEndo.size(); i++) {
				IV var = output.neededEndo.getValue(i).var;
				String varName = serializer.serialize(var);
				int varI = output.graph.diffDifferential.indexOf(var.getFullName() + "'");
				funText.append("		double " + varName + " = sim.buffer_2[" + varI + " * data.length + i];\n");
			}
			funText.append("\n");

			// Equations
			funText.append("		/* Equations */\n");
			for (int i = 0; i < output.variables.size(); i++) {
				OutputVar var = output.variables.get(i);
				funText.append("		double " + var.name + " = " + serializer.serialize(var.rhs) + ";\n");
			}
			funText.append("\n");

			// Output
			funText.append("		/* Output */\n");
			for (int i = 0; i < output.variables.size(); i++) {
				OutputVar var = output.variables.get(i);
				funText.append("		sim.out[" + i + " * data.length + i] = " + var.name + ";\n");
			}
			funText.append("\n");

			funText.append("	}\n");
			funText.append("}");

			return funText.toString();

		}
	}
}

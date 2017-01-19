package fit;

import java.io.*;
import java.util.*;
import java.util.StringTokenizer;

import org.apache.commons.io.*;
import org.apache.commons.io.filefilter.*;
import org.apache.commons.lang3.*;
import org.apache.commons.launcher.*;
import org.slf4j.*;

import run.*;
import serialize.*;
import struct.*;
import struct.inst.*;
import temp.*;
import util.*;
import xml.*;

import com.Ostermiller.util.*;
import com.google.common.collect.*;

public class ALG_MSC implements FitterOld, SimulatorOld {
	public static class Params {
		public int opt_normalize_error;
		public int opt_init_state_fit;
		public int opt_tf_restarts;
		public int opt_fs_restarts;

		public static final Params DEFAULT_SIMULATION = new Params() {
			{
				opt_normalize_error = 1;
				opt_init_state_fit = 1;
				opt_tf_restarts = 0;
				opt_fs_restarts = 0;
			}
		};

		public static final Params DEFAULT_FIT = new Params() {
			{
				opt_normalize_error = 1;
				opt_init_state_fit = 1;
				opt_tf_restarts = 128;
				opt_fs_restarts = 1000;
			}
		};
	}

	public static final Logger logger = LoggerFactory.getLogger(CLITaskMain.class);

	public static final ALG_MSC ALG_MSC = new ALG_MSC();

	public Params params;

	MSCFunctions functions = new MSCFunctionsAlt();

	CSerializer serializer;

	static final double MIN_INF = Double.MIN_VALUE;
	static final double PLUS_INF = Double.MAX_VALUE;

	public String tempDirpath = "temp";
	private String workDirpath;

	private ALG_MSC() {
	}

	public void initWorkDir() throws IOException {
		int counter = 1;
		while (new File(tempDirpath + "/" + counter).exists()) {
			counter++;
		}
		this.workDirpath = tempDirpath + "/" + counter;
		new File(this.workDirpath).mkdirs();

		FileUtils.copyDirectory(new File("make/alg"), new File(this.workDirpath),HiddenFileFilter.VISIBLE);

	}

	public void deleteWorkDir() throws IOException {
		FileUtils.deleteDirectory(new File(this.workDirpath));
	}

	public String generateSimulationString(Output output, List<Dataset> datasets, String timeColumn, Map<String, Integer> varIndex, Map<String, Integer> uncIndex) {
		IQGraph graph = output.graph;

		List<IQNode> order = graph.diffOrder;

		this.serializer = new CSerializer();

		this.params = Params.DEFAULT_SIMULATION;

		String MS_init_dims = functions.MS_init_dims(output, datasets, varIndex);
		String MS_fill_in = functions.MS_fill_in(output, datasets, timeColumn, varIndex, uncIndex);
		String MS_params = functions.MS_params(output, datasets, varIndex);
		String MS_model = functions.MS_model(output, order, varIndex);
		String sim_model_output = functions.sim_model_output(output, varIndex);

		String msc = MS_init_dims + "\n" + MS_fill_in + "\n" + MS_params + "\n" + MS_model + "\n" + sim_model_output;

		return msc;
	}

	public void generateSimulationFile(Output output, List<Dataset> datasets, String timeColumn, Map<String, Integer> varIndex, Map<String, Integer> uncIndex,
			String filename) throws FileNotFoundException {
		String msc = generateSimulationString(output, datasets, timeColumn, varIndex, uncIndex);

		PrintWriter out = new PrintWriter(filename);
		out.print(msc);
		out.close();
	}

	public void generateSimulationOutputFile(Output output, List<Dataset> datasets, String timeColumn, Map<String, Integer> varIndex, Map<String, Integer> uncIndex,
			String simulationFilename, String outputFilename) throws IOException, InterruptedException, FailedSimulationException {
		generateSimulationFile(output, datasets, timeColumn, varIndex, uncIndex, simulationFilename);
		makeAndRun(outputFilename); // runSimulation(simulationFilename, outputFilename);
	}

	public void generateSimulationOutputFile(Output output, List<Dataset> datasets, String timeColumn, Map<String, Integer> varIndex, Map<String, Integer> uncIndex,
			String outputFilename) throws IOException, InterruptedException, FailedSimulationException {
		String simulationFilename = this.workDirpath + "/ms.c";

		generateSimulationOutputFile(output, datasets, timeColumn, varIndex, uncIndex, simulationFilename, outputFilename);
	}

	public Simulation simulate(Output output, List<Dataset> datasets, String timeColumn, Map<String, Integer> varIndex, Map<String, Integer> uncIndex) throws IOException,
			InterruptedException, FailedSimulationException {
		String simulationFilename = this.workDirpath + "/ms.c";
		String simulationOutputFilename = this.workDirpath + "/simulation.out";



		generateSimulationOutputFile(output, datasets, timeColumn, varIndex, uncIndex, simulationFilename, simulationOutputFilename);
		Simulation simulation = createSimulationObject(output, datasets, timeColumn, varIndex, simulationOutputFilename);
		// deleteTemporaryFiles(simulationFilename, simulationOutputFilename);

		return simulation;
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

		process.getErrorStream().close();
		process.getInputStream().close();
		process.getOutputStream().close();
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

	private void makeAndRun(String resultFilename) throws IOException, InterruptedException, FailedSimulationException {
		String command;

		Runtime runtime = Runtime.getRuntime();
		Process process;


		command = "make -C" + this.workDirpath;
		process = runtime.exec(command, null);
		process.waitFor();

		if (process.exitValue() != 0) {
			String errorMsg = "Error invoking MAKE : error code - " + process.exitValue() + "\n";
			errorMsg += IOUtils.toString(process.getErrorStream());
			throw new FailedSimulationException(errorMsg);
		}

		ALG_MSC.logger.info("Simulation started");

		command = this.workDirpath + "/model";
		process = runtime.exec(command, null);

		Thread t = new StreamConnector(process.getInputStream(), new FileOutputStream(resultFilename));
		t.start();
		process.waitFor();
		t.join();

		ALG_MSC.logger.info("Simulation ended");

		process.getErrorStream().close();
		process.getInputStream().close();
		process.getOutputStream().close();

	}

	private Simulation createSimulationObject(Output output, List<Dataset> datasets, String timeColumn, Map<String, Integer> varIndex,
			String resultFile) throws IOException {
		Simulation simulation = new Simulation(output, timeColumn, resultFile);

		return simulation;
	}

	public String generateMSCString(Output output, List<Dataset> datasets, String timeColumn, Map<String, Integer> varIndex, Map<String, Integer> uncIndex) {
		IQGraph graph = output.graph;

		List<IQNode> order = graph.diffOrder;

		this.serializer = new MSCSerializer();

		this.params = Params.DEFAULT_FIT;

		String MS_init_dims = functions.MS_init_dims(output, datasets, varIndex);
		String MS_fill_in = functions.MS_fill_in(output, datasets, timeColumn, varIndex, uncIndex);
		String MS_params = functions.MS_params(output, datasets, varIndex);
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

	public void generateFitOutputFile(Output output, List<Dataset> datasets, String timeColumn, Map<String, Integer> varIndex, Map<String, Integer> uncIndex,
			String fitFilename, String outputFilename) throws IOException, InterruptedException, FailedSimulationException {
		generateMSCFile(output, datasets, timeColumn, varIndex, uncIndex, fitFilename);
		makeAndRun(outputFilename); // runFit(fitFilename, outputFilename);
	}

	public void generateFitOutputFile(Output output, List<Dataset> datasets, String timeColumn, Map<String, Integer> varIndex, Map<String, Integer> uncIndex,
			String outputFilename) throws IOException, InterruptedException, FailedSimulationException {
		String fitFilename = this.workDirpath + "/ms.c";

		generateFitOutputFile(output, datasets, timeColumn, varIndex, uncIndex, fitFilename, outputFilename);
	}

	public ExtendedModel fit(Output output, List<Dataset> datasets, String timeColumn, Map<String, Integer> varIndex, Map<String, Integer> uncIndex, String logFilename)
			throws IOException, InterruptedException, FailedSimulationException {
		String fitFilename = this.workDirpath + "/ms.c";
		String fitOutputFilename = this.workDirpath + "/fit.out";


		generateFitOutputFile(output, datasets, timeColumn, varIndex, uncIndex, fitFilename, fitOutputFilename);
		ExtendedModel fittedModel = createFittedModel(output, datasets, fitOutputFilename, logFilename);

		return fittedModel;
	}

	private double[] readParameters(String fitOutputFilename) throws IOException, FailedSimulationException {
		BufferedReader input = new BufferedReader(new FileReader(fitOutputFilename));
		String line;
		while ((line = input.readLine()) != null) {
			if (!line.startsWith("PARAMS:"))
				continue;

			String[] strings = StringUtils.remove(line, "PARAMS:").trim().split(" ");

			double[] doubles = new double[strings.length];
			for (int i = 0; i < strings.length; i++) {
				doubles[i] = new Double(strings[i]);
			}
			input.close();

			return doubles;
		}

		throw new FailedSimulationException("CVODE simulation failed");
	}

	private Map<String, Double> readFitnessMeasures(String fitOutputFilename) throws IOException {
		BufferedReader input = new BufferedReader(new FileReader(fitOutputFilename));

		Map<String, Double> fitnessMeasures = new LinkedHashMap<String, Double>();

		String line = null;
		String lastLine = null;

		while ((line = input.readLine()) != null) {
			lastLine = line;
		}
		String measuresLine = lastLine;
		String[] fitnessNames = measuresLine.split(":")[0].split(", ");
		String[] fitnessValues = measuresLine.split(":")[1].trim().split(" ");

		for (int i = 0; i < fitnessNames.length; i++) {
			Double value;
			if (fitnessValues[i].equals("nan")) {
				value = Double.NaN;
			} else if (fitnessValues[i].equals("-nan")) {
				value = Double.NaN;
			} else if (fitnessValues[i].equals("inf")) {
				value = Double.POSITIVE_INFINITY;
			} else if (fitnessValues[i].equals("-inf")) {
				value = Double.NEGATIVE_INFINITY;
			} else {
				value = new Double(fitnessValues[i]);
			}
			fitnessMeasures.put(fitnessNames[i], value);
		}

		input.close();
		return fitnessMeasures;
	}

	private ExtendedModel createFittedModel(Output output, List<Dataset> datasets, String fitOutputFilename, String logFilename) throws IOException, FailedSimulationException {
		IQGraph graph = output.graph;
		// Read all parameters from the simulation output file
		double[] parameters = readParameters(fitOutputFilename);
		Map<String, Double> fitnessMeasures = readFitnessMeasures(fitOutputFilename);
		int i;

		// Optionally log the fitted parameters
//		if (logFilename != null) {
//			logAllParameters(graph, parameters, logFilename);
//
//		}

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

	private void logFittedParameters(Output output, double[] parameters, String logParametersFilename) throws IOException {
		IQGraph graph = output.graph;

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

	private void logAllParameters(IQGraph graph, double[] parameters, String logParametersFilename) throws IOException {
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
		for (int k = 0; k < graph.unknownParameters.size(); k++) {
			IIC cons = (IIC) graph.unknownParameters.getValue(k).cons;
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
		String MS_fill_in(Output output, List<Dataset> datasets, String timeColumn, Map<String, Integer> varIndex, Map<String, Integer> uncIndex);
		String MS_params(Output output, List<Dataset> datasets, Map<String, Integer> varIndex);
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
			int sys_var_noutputs = output.variables.size();
			int param_n = graph.unknownParameters.size() + output.fitted.size() + datasets.size() * sys_var_n;

			int sys_var_nobserved = 0;
			for (int i = 0; i < graph.diffDifferential.size(); i++) {
				IV iv = graph.diffDifferential.getValue(i).var;
				Integer index = varIndex.get(iv.getFullName());
				if (index != null) {
					sys_var_nobserved++;
				}
			}
			//@formatter:off
			String[] funText = {
					"void MS_init_dims(void) {",
					"",
					"	data.nsets = %d;",
					"	data.nvars = %d;",
					"	data.length = %d;", "",
					"	sys_var.n = %d;",
					"	sys_var.noutputs = %d;",
					"	param.n = %d;",
					"",
					"	opt.normalize_error = %d;",
					"//	opt.init_state_fit = %d;",
					"//	opt.tf_restarts = %d;",
					"	opt.fs_restarts = %d;",
					"",
					"//	sys_var.nobserved = %d;",
					"}",
					""
			};
			//@formatter:on

			return String.format(StringUtils.join(funText, "\n"), data_nsets, data_nvars, data_length, sys_var_n, sys_var_noutputs, param_n,
					params.opt_normalize_error, params.opt_init_state_fit, params.opt_tf_restarts, params.opt_fs_restarts, sys_var_nobserved);
		}

		public String MS_fill_in(Output output, List<Dataset> datasets, String timeColumn, Map<String, Integer> varIndex, Map<String, Integer> uncIndex) {
			IQGraph graph = output.graph;

			StringBuilder funText = new StringBuilder();

			funText.append("void MS_fill_in(void) {\n\n");

			int curLength = 0;
			for (int i = 0; i < datasets.size(); i++) {
				funText.append("	data.set_files[" + i + "] = strdup(\"" + datasets.get(i).getFilepath() + "\");\n");
				curLength+=datasets.get(i).getNRows();
				funText.append("	data.set_tos[" + i + "] = " + curLength + ";\n");
			}
			funText.append("\n");

			funText.append("	data.time = data.table[" + datasets.get(0).getColIndex(timeColumn) + "];\n");
			funText.append("//	data.sys_var_names[0] = strdup(\"" + timeColumn + "\");\n");
			// funText.append("	data.sys_vars[0] = (double *) NULL;\n");

			// int index = 1;
			for (int i = 0; i < graph.diffDifferential.size(); i++) {
				IV iv = graph.diffDifferential.getValue(i).var;
				Integer index = varIndex.get(iv.getFullName());
				boolean observed = (index != null);
				String value = observed ? ("data.table[" + index + "]") : "(double *) NULL";
				String name = observed ? datasets.get(0).getNames()[index] : serializer.serialize(iv);
				funText.append("//	data.sys_vars[" + i + "] = " + value + ";\n");
				funText.append("	sys_var.names[" + i + "] = strdup(\"" + name + "\");\n");
			}

			for (int i = 0; i < output.variables.size(); i++) {
				OutputVar var = output.variables.get(i);
				int outI = varIndex.get(var.name);
				Integer uncI = uncIndex.get(var.name);
				funText.append("	// " + var.name + "\n");
				funText.append("	data.outputs[" + i + "] = data.table[" + outI + "];\n");
				funText.append("	data.outputs_names[" + i + "] = strdup(\"" + var.name + "\");\n");
				funText.append("	data.un[" + i + "] = " + (uncI!=null?"data.table[" + uncI + "]":"NULL") + ";\n");
			}

			funText.append("}\n");

			return funText.toString();
		}

		public String MS_params(Output output, List<Dataset> datasets, Map<String, Integer> varIndex) {
			IQGraph graph = output.graph;

			StringBuilder funText = new StringBuilder();
			funText.append("void MS_params(void) {\n\n");

			funText.append("/* constant parameters */\n");

			int i = 0;
			for (i = 0; i < graph.unknownParameters.size(); i++) {
				IC ic = graph.unknownParameters.getValue(i).cons;

				double value;
				double lower = ic.range.getLower(); // default lower and upper bound. It can be changed bellow.
				double upper = ic.range.getUpper();

				IIC iic = (IIC) ic;
				if (iic.fit_range != null) { // if fit_range is provided, use it
					lower = iic.fit_range.getLower();
					upper = iic.fit_range.getUpper();
				}

				lower = !Double.isInfinite(lower) ? lower : MIN_INF;
				upper = !Double.isInfinite(upper) ? upper : PLUS_INF;

				if (iic.fit_start != null) { // if fit_start is provided use it as starting value
					value = iic.fit_start;
				} else { // if not, use arbitrary starting in the interval <lower, upper>
					value = (lower + upper) / 2; // e.g., the middle point (this is implementation specific)
				}

				funText.append("	// " + serializer.serialize(ic) + "\n");
				funText.append("	param.vals[" + i + "] = (double) " + value + ";\n");
				funText.append("	param.bounds[" + (2 * i) + "] = (double) " + lower + ";\n");
				funText.append("	param.bounds[" + (2 * i + 1) + "] = (double) " + upper + ";\n\n");
			}

			funText.append("/* output parameters */\n");
			for (int j = 0; j < output.fitted.size(); j++, i++) {
				OutputCons cons = output.fitted.get(j);

				double lower = !Double.isInfinite(cons.fit_range.getLower()) ? cons.fit_range.getLower() : MIN_INF;
				double upper = !Double.isInfinite(cons.fit_range.getUpper()) ? cons.fit_range.getUpper() : PLUS_INF;
				double initial;
				if (cons.fit_start!=null) {
					initial = (cons.fit_start == Double.POSITIVE_INFINITY) ? PLUS_INF : (cons.fit_start == Double.NEGATIVE_INFINITY) ? MIN_INF : cons.fit_start;
				} else {
					initial = (lower + upper)/2;
				}


				funText.append("	// " + cons.name + "\n");
				funText.append("	param.vals[" + i + "] = (double) " + initial + ";\n");
				funText.append("	param.bounds[" + (2 * i) + "] = (double) " + lower + ";\n");
				funText.append("	param.bounds[" + (2 * i + 1) + "] = (double) " + upper + ";\n\n");
			}

			funText.append("/* initial values */\n");


			for (int k = 0; k < datasets.size(); k++) {
				funText.append("/* Dataset " + k + "*/\n");

				for (int j = 0; j < graph.diffDifferential.size(); j++, i++) {
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

						if (iiv.fit_start != null) { // if fit_start is provided use it as initial value
							initial = iiv.fit_start;
						} else { // if not, use arbitrary initial in the interval <lower, upper>
							initial = (lower + upper) / 2; // e.g., the middle point (this is implementation specific)
						}
					}

					initial = (initial == Double.POSITIVE_INFINITY) ? PLUS_INF : (initial == Double.NEGATIVE_INFINITY) ? MIN_INF : initial;
					lower = !Double.isInfinite(lower) ? lower : MIN_INF;
					upper = !Double.isInfinite(upper) ? upper : PLUS_INF;

					funText.append("	// " + serializer.serialize(iv) + "\n");
					Integer index = varIndex.get(iv.getFullName());
					if (index == null) {
						funText.append("	param.vals[" + i + "] = (double) " + initial + ";\n");
					}
					funText.append("	param.bounds[" + (2 * i) + "] = (double) " + lower + ";\n");
					funText.append("	param.bounds[" + (2 * i + 1) + "] = (double) " + upper + ";\n\n");
				}
			}
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
				if (!ivNode.var.id.equals("t")) {	//FIXME: :)
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
				funText.append("		double " + varName + " = sim.sysvars[" + varI + " * data.length + i];\n");
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
				funText.append("		sim.outputs[" + i + " * data.length + i] = " + var.name + ";\n");
			}
			funText.append("\n");

			funText.append("	}\n");
			funText.append("}");

			return funText.toString();

		}

	}
}

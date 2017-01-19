package run;

import java.io.*;
import java.lang.reflect.*;

import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;
import org.apache.commons.io.*;

import serialize.*;
import struct.inst.*;
import struct.temp.*;
import temp.*;
import traverse.*;

import fit.*;

public class Utilities {
	public static void mathematica()
			throws Exception {

		String measure = "SSE";

		String inputDirpath = "res/endocytosis";
		String[] inputFilenames = {

		"test-de", };
		String inputExtension = "out";

		String outputDirpath = "res/endocytosis";
		String[] outputFilenames = inputFilenames;
		String outputExtension = "list";

		for (int i = 0; i < inputFilenames.length; i++) {
			BufferedReader inputStream = new BufferedReader(new FileReader(inputDirpath + "/" + inputFilenames[i] + "." + inputExtension));
			PrintStream outputStream =
					new PrintStream(FileUtils.openOutputStream(new File(outputDirpath + "/" + outputFilenames[i] + "." + outputExtension)));

			String line;
			while ((line = inputStream.readLine()) != null) {
				if (line.startsWith("// " + measure)) {
					Double value = new Double(line.substring(line.lastIndexOf(' ')));
					outputStream.print(String.format("%.10f\n", value));
				}
			}

			inputStream.close();
			outputStream.close();
		}
	}

	public static void mergeLogs()
			throws Exception {
		String tempDirpath = "temp";
		String pre = tempDirpath + File.separatorChar;

		String outputLog = "merged.out";

		String input1 = "params1.out";
		String input2 = "params.out";

		ALG_MSC.mergeFittedParametersLogs(pre + outputLog, pre + input1, pre + input2);
	}

	public static void mergeRestarts()
			throws IOException {
		String measure = "SSE";

		String inputDirpath = "out/aquatic/ecem/bled/dasa/200";
		String inputPrefix = "Bled02Exhaustive[dasa-200]";
		String inputExtension = "txt";
		String inputPattern = "%s-%d"; //ExperimentName-Restart
		int experimentStart = 1;
		int experimentEnd = 2;
		int modelCount = 27216;

		String outputDirpath = inputDirpath;
		String outputPrefix = inputPrefix;
		String outputExtension = inputExtension;
		String outputPattern = "%s-%s";
		String outputSuffix = "best";

		PrintStream outputStream =
				new PrintStream(outputDirpath + "/" + String.format(outputPattern, outputPrefix, outputSuffix) + "." + outputExtension);
		BufferedReader[] inputStreams = new BufferedReader[experimentEnd - experimentStart + 1];

		for (int i = experimentStart; i <= experimentEnd; i++) {
			inputStreams[i - experimentStart] =
					new BufferedReader(new FileReader(inputDirpath + "/" + String.format(inputPattern, inputPrefix, i) + "." + inputExtension));
		}

		for (int modelNum = 1; modelNum <= modelCount; modelNum++) {

			double bestMeasure = Double.POSITIVE_INFINITY;
			String bestModel = "";

			for (int expNum = experimentStart; expNum <= experimentEnd; expNum++) {

				StringBuilder model = new StringBuilder();
				boolean first = true;
				Double value = Double.POSITIVE_INFINITY;
				do {
					inputStreams[expNum - experimentStart].mark(10000);
					String line = inputStreams[expNum - experimentStart].readLine();
					if (line == null)
						break;
					if (line.startsWith("// Model") && !first) {
						inputStreams[expNum - experimentStart].reset();
						break;
					}
					model.append(line + "\n");
					first = false;
					if (line.startsWith("// " + measure)) {
						value = new Double(line.substring(line.lastIndexOf(' ')));
					}

				} while (true);

				if (value < bestMeasure) {
					bestMeasure = value;
					bestModel = model.toString();
				}
			}
			outputStream.print(bestModel);
		}

		outputStream.close();

		for (int i = experimentStart; i <= experimentEnd; i++) {
			inputStreams[i - experimentStart].close();
		}

	}

	public static void translateToC()
			throws Exception {
		String libraryFilename = "res/aquatic/ecem/bled/AquaticEcosystem.pbl";
		String inputFilename = "out/aquatic/ecem/bled/dasa/200/Bled02Exhaustive[dasa-200]-best.txt";
		String outputFilename = "out/aquatic/ecem/bled/dasa/200/Bled02Exhaustive[dasa-200]-best(eq).txt";

		Traverse.addLibrary(libraryFilename);

		BufferedReader inputStream = new BufferedReader(new FileReader(inputFilename));
		PrintStream outputStream = new PrintStream(outputFilename);

		StringBuilder modelText = new StringBuilder();
		String line;
		String modelLine = null;
		String modelLineNew = null;
		String sseLine = null;
		while ((line = inputStream.readLine()) != null) {
			if (line.startsWith("// Model")) {
				modelLine=line;
				if (modelText.length() != 0) {
					modelLine = modelLineNew;
					modelLineNew = line;

					CommonTree modelTree = Traverse.readStructure(IOUtils.toInputStream(modelText));
					Model model = Traverse.loadModel(modelTree, Library.LIBRARIES);

					outputStream.println(modelLine);
					outputStream.println(SimpleWriter.serialize(model, new JavaSerializer()));
					outputStream.println(sseLine);

					modelText = new StringBuilder();
				} else {
					modelLineNew = line;
				}
			}
			if (line.startsWith("// SSE")) {
				sseLine = line;
			}
			modelText.append(line + "\n");
		}

		if (modelText.length() != 0) {
			CommonTree modelTree = Traverse.readStructure(IOUtils.toInputStream(modelText));
			Model model = Traverse.loadModel(modelTree, Library.LIBRARIES);

			outputStream.println(modelLine);
			outputStream.println(SimpleWriter.serialize(model, new JavaSerializer()));
			outputStream.println(sseLine);
		}

		inputStream.close();
		outputStream.close();
	}

	public static void main(String[] args)
			throws Exception {
		mergeRestarts();
		translateToC();
	}

}

package run;

import java.io.*;

import org.slf4j.*;

import search.*;
import serialize.*;
import struct.*;
import struct.inst.*;
import struct.temp.*;
import temp.*;
import traverse.*;
import util.*;
import cli.*;

import com.martiansoftware.jsap.*;

public class CLIMain {
	public static final Logger logger = LoggerFactory.getLogger(CLIMain.class);
	public static CLIParser cliParser;

	public static void main(String args[]) {
		try {
			cliParser = new CLIParser();
			cliParser.parse(args);

			String libraryName = cliParser.getLibrary();
			String modelName = cliParser.getModel();
			String incompleteName = cliParser.getIncomplete();

			String outputName = cliParser.getOutputFile();
			String outputFormat = cliParser.getOutputFormat();

			MDC.put("file", libraryName);
			Library library = Traverse.addLibrary(libraryName);

			// write equations for a model
			if (modelName != null) {
				MDC.put("file", modelName);
				Model model = Traverse.addModel(modelName);

				if (outputName != null) {
					if (outputFormat.equals("c")) {
						SimpleWriter simple = new SimpleWriter(model, new CSerializer());
						simple.writeToStream(new PrintStream(outputName));
   					logger.info("Written equations in C-format to '" + outputName + "'");
					} else {
						throw new IllegalStateException("Invalid output format");
					}
				}
			// enumerate specific models for an incomplete model
			} else if (incompleteName != null) {
				PrintStream out;
				if (outputName != null) {
					out = new PrintStream(outputName);
					logger.info("Writing models to '" + outputName + "'");
				} else {
					out = System.out;
				}

				MDC.put("file", incompleteName);
				Model incompleteModel = Traverse.addIncompleteModel(incompleteName);

				ExtendedModel ext = new ExtendedModel(incompleteModel);
				ModelEnumerator search = new ModelEnumerator(ext);

				int counter = 0;
				do {
					ExtendedModel completeModel = search.nextModel();
					counter++;
					String toWrite;

					if (outputFormat.equals("c")) {
						toWrite = SimpleWriter.serialize(completeModel.getModel(), new CSerializer());
					} else if (outputFormat.equals("pbm")){
 						toWrite = completeModel.toString();
					} else if (outputFormat.equals("both")) {
 						toWrite = completeModel.toString() + "\n\n" + SimpleWriter.serialize(completeModel.getModel(), new CSerializer());
					} else {
						throw new IllegalStateException("Invalid output format");
					}

					out.println("Model #" + counter);
					out.println(toWrite);
				} while (search.hasNextModel());

				if (outputName != null)
					out.close();
			}
		} catch (ParsingException ex) {
			logger.error(ex.getFilename() + ":" + ((ex.getLinenum() != -1) ? ex.getLinenum() : "<na>") + ":"
					+ ((ex.getPosnum() != -1) ? ex.getPosnum() : "<na>") + " - " + ex.getMessage(), ex);
		} catch (CLIException ex) {
			JSAPResult result = ex.getResult();
			for (java.util.Iterator errs = result.getErrorMessageIterator(); errs.hasNext();) {
				logger.error((String) errs.next());
			}
			System.err.print(cliParser.getHelp());
		} catch (Exception ex) {
			logger.error((ex.getMessage() != null) ? ex.getMessage() : ex.toString(), ex);
		}

		System.exit(0);
	}
}

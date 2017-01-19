package run;

import org.slf4j.*;

import serialize.*;
import struct.*;
import struct.inst.*;
import struct.temp.*;
import temp.*;
import traverse.*;
import util.*;
import cli.*;

import com.martiansoftware.jsap.*;

public class PartialModelTest {
	public static final Logger logger = LoggerFactory.getLogger(PartialModelTest.class);
	public static CLIParser cliParser;

	public static void main(String args[]) throws Exception {
		try {
			cliParser = new CLIParser();
			cliParser.parse(args);

			String libraryName = cliParser.getLibrary();
			String modelName = cliParser.getModel();
			String outputName = cliParser.getOutputFile();
			String outputFormat = cliParser.getOutputFormat();

			MDC.put("file", libraryName);
			Library library = Traverse.addLibrary(libraryName);
			logger.info("Library '" + libraryName + "' compiled successfully");

			if (modelName != null) {
				MDC.put("file", modelName);
				Model model = Traverse.addModel(modelName);
				logger.info("Partial Model '" + modelName + "' compiled successfully");
				
				//if (outputName != null) {
				//	if (outputFormat.equals("cvode")) {
				//		CVODE cvode = new CVODE(model, Dataset.ROSS);
				//		cvode.writeToFile(outputName);
				//		logger.info("Written CVODE simulation to '" + outputName + "'");
				//	} else if (outputFormat.equals("c")) {
				//		SimpleWriter simple = new SimpleWriter(model, new CSerializer());
				//		simple.writeToFile(outputName);
				//		logger.info("Written equations in C-format to '" + outputName + "'");
				//	} else {
				//		throw new RuntimeException("invalid state");
				//	}
				//}
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
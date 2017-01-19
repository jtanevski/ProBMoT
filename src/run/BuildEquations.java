package run;

import java.util.*;

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

public class BuildEquations {
	public static final Logger logger = LoggerFactory.getLogger(CLIMain.class);
	public static CLIParser cliParser;

	public static void main(String args[]) throws Exception {
		try {
			cliParser = new CLIParser();
			cliParser.parse(args);

			String libraryName = cliParser.getLibrary();
			String modelName = cliParser.getModel();

			MDC.put("file", libraryName);
			Library library = Traverse.addLibrary(libraryName);
			logger.info("Library '" + libraryName + "' compiled successfully");

			MDC.put("file", modelName);
			Model model = Traverse.addModel(modelName);
			logger.info("Model '" + modelName + "' compiled successfully");
			
			
			String mscName = "out/ms.c";
			//CVODE cvode = new CVODE(model, Dataset.ROSS);
			//cvode.writeToFile(mscName);
			logger.info("Written CVODE simulation to '" + mscName + "'");
				
			//IQGraph graph = new IQGraph(model);
			//List<IQ> iqs = graph.topologicalSort(true);
			//
			//CSerializer serializer = new CSerializer();
			//String eq1 = serializer.serialize(iqs.get(0));
			//String eq2 = serializer.serialize(iqs.get(1));
			//String eq3 = serializer.serialize(iqs.get(2));
				
			//System.out.println();      //for debugging
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
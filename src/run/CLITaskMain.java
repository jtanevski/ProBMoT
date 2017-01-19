package run;

import java.util.Locale;

import org.slf4j.*;

import serialize.*;
import struct.*;
import task.*;
import temp.*;
import traverse.*;
import util.*;
import xml.*;
import cli.*;

import com.martiansoftware.jsap.*;

import fit.*;

public class CLITaskMain {
	public static final Logger logger = LoggerFactory.getLogger(CLITaskMain.class);
	public static CLIParserTask cliParser;

	public static void main(String args[]) throws Exception {
		try {
			System.setProperty("user.language", "en");
			System.setProperty("user.country", "US");
			System.setProperty("user.language.display", "en");
			System.setProperty("user.country.display", "US");
			System.setProperty("user.language.format", "en");
			System.setProperty("user.country.format", "US");
			Locale.setDefault(Locale.US);
			Locale.setDefault(Locale.Category.DISPLAY, Locale.US);
			Locale.setDefault(Locale.Category.FORMAT, Locale.US);
			
			System.setProperty("java.library.path", CVodeSimulator.JVODE_LIB_PATH);

			cliParser = new CLIParserTask();
			cliParser.parse(args);

			String taskFilepath = cliParser.getTask();
			MDC.put("task", taskFilepath);

			TaskSpec taskSpec = TaskSpec.unmarshal(taskFilepath);
			Task task = new Task(taskSpec);
			
			taskSpec.taskPath=taskFilepath;
			//Task task=null;
			
			//taskSpec.BestModels=  new ArrayList<List<ExtendedModel>>();
			
			
			if (taskSpec.settings.evaluation instanceof LeaveOneOutSpec)
			{	
				taskSpec.setNumRuns(0);
				taskSpec.setEndRun(0);
				LeaveOneOutSpec loos = (LeaveOneOutSpec) taskSpec.settings.evaluation;
				//taskSpec.endRun=~~loos.train.trim().length() / 2 + 1;
				//taskSpec.endRun++;
				for (int i = 0; i < (~~loos.train.trim().length() / 2 + 1); i++) {
					task = new Task(taskSpec);
					System.out.println("Leave One Out : Iteration :" + taskSpec.getNumRuns());
					task.perform();
					taskSpec.setNumRuns(taskSpec.getNumRuns() + 1);
					
				}
			}
			else if (taskSpec.settings.evaluation instanceof CrossValidSpec)
			{	
				taskSpec.setNumRuns(0);
				taskSpec.setEndRun(0);
				CrossValidSpec cvs= (CrossValidSpec) taskSpec.settings.evaluation;
				for (DatasetSpec ds : taskSpec.data) {
					
					task = new Task(taskSpec);
					System.out.println("CrossValidation : Iteration :" + taskSpec.getNumRuns() + "-TestDS:" + ds.id);
					task.perform();
					taskSpec.setNumRuns(taskSpec.getNumRuns() + 1);
					
				}
			}
			else {
			
				task = new Task(taskSpec);
				task.perform();
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
			//} catch (Exception ex) {
			//logger.error("Exception caught"/*(ex.getMessage() != null) ? ex.getMessage() : ex.toString()*/, ex);
		}

		System.exit(0);
	}
}
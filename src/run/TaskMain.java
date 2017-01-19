package run;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.slf4j.*;

import task.*;
import util.*;
import xml.*;
import fit.*;
import temp.*;

public class TaskMain {
	public static final Logger logger = LoggerFactory.getLogger(TaskMain.class);

	@SuppressWarnings("null")
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

			System.out.println("enter location to task file relative to " + System.getProperty("user.dir") + " :");
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			String taskFilepath = in.readLine();
			//String taskFilepath = args[0];

			MDC.put("task", taskFilepath);
			
			TaskSpec taskSpec = TaskSpec.unmarshal(taskFilepath);
			
			taskSpec.taskPath=taskFilepath;
			Task task=null;
			
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
			
			//task.perform();

	

/*				taskSpec.BestModels.add(new ArrayList<ExtendedModel>());
				taskSpec.BestModels.get(0).add(null);
				task = new Task(taskSpec);
				task.perform();*/

		
		

		} catch (ParsingException ex) {
			logger.error(ex.getFilename() + ":" + ((ex.getLinenum() != -1) ? ex.getLinenum() : "<na>") + ":"
					+ ((ex.getPosnum() != -1) ? ex.getPosnum() : "<na>") + " - " + ex.getMessage(), ex);
		}

		System.exit(0);
	}
}
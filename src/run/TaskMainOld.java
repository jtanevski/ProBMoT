package run;

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

public class TaskMainOld {
	public static final Logger logger = LoggerFactory.getLogger(TaskMainOld.class);

	public static void main(String args[]) throws Exception {
		try {

			String taskFilepath = "res/task.xml";
			MDC.put("task", taskFilepath);

			TaskSpec taskSpec = TaskSpec.unmarshal(taskFilepath);
			Task task = new Task(taskSpec);

			task.perform();

		} catch (ParsingException ex) {
			logger.error(ex.getFilename() + ":" + ((ex.getLinenum() != -1) ? ex.getLinenum() : "<na>") + ":"
					+ ((ex.getPosnum() != -1) ? ex.getPosnum() : "<na>") + " - " + ex.getMessage(), ex);
		} catch (Exception ex) {
			logger.error((ex.getMessage() != null) ? ex.getMessage() : ex.toString(), ex);
		}

		System.exit(0);
	}
}
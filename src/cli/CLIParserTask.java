package cli;



import com.martiansoftware.jsap.*;
import com.martiansoftware.jsap.stringparsers.*;

/**
 * A parser for the command line arguments.
 * @author darko
 *
 */
public class CLIParserTask {
	
	protected JSAP jsap = new JSAP();
	protected JSAPResult result = null;

	public CLIParserTask() throws Exception {

			/* task */
			UnflaggedOption task = new UnflaggedOption("task")		.setStringParser(JSAP.STRING_PARSER)
																					.setGreedy(false)
			                                                		.setRequired(true)
			                                                		.setUsageName("task");
			task.setHelp("The path to the task specification");
			jsap.registerParameter(task);
			
			
	}
	
	public void parse(String[] args) {
		result = jsap.parse(args);
		if (!result.success()) {
			throw new CLIException(result);

//         for (java.util.Iterator errs = result.getErrorMessageIterator();
//                 errs.hasNext();) {
//             System.err.println("Error: " + errs.next());
//         }
//         printHelp(System.err);
//         System.exit(1);
     }
	}
	
	public String getTask() {
		return result.getString("task");
	}
	
	public String getHelp() {
		
		StringBuilder buffer = new StringBuilder();
      String jarName = CLIParserTask.class.getProtectionDomain().getCodeSource().getLocation().getPath();
      buffer.append("\n");
      buffer.append("Usage: java -jar " + jarName.substring(jarName.lastIndexOf('/') + 1) + " OPTIONS\n");
      buffer.append("\n");
      buffer.append("OPTIONS:\n");
      buffer.append(jsap.getHelp(80, "") + "\n");
	
      return buffer.toString();
	}
}

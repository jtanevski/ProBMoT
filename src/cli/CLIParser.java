package cli;



import com.martiansoftware.jsap.*;
import com.martiansoftware.jsap.stringparsers.*;

/**
 * A parser for the command line arguments.
 * @author darko
 *
 */
public class CLIParser {
	
	protected JSAP jsap = new JSAP();
	protected JSAPResult result = null;

	public CLIParser() throws Exception {

		/* library */
		FlaggedOption library = new FlaggedOption("library")	.setLongFlag("lib")
		                                                		.setShortFlag('l')
		                                                		.setStringParser(JSAP.STRING_PARSER)
		                                                		.setRequired(true)
		                                                		.setUsageName("library");
		library.setHelp("The path to the library");
		jsap.registerParameter(library);
		
		/* model */
		FlaggedOption model = new FlaggedOption("model")		.setLongFlag("model")
																				.setShortFlag('m')
																				.setStringParser(JSAP.STRING_PARSER)
																				.setRequired(false)
																				.setUsageName("model");
		model.setHelp("The path to the model");
		jsap.registerParameter(model);
		
		/* incomplete model */
		FlaggedOption incomplete = new FlaggedOption("incomplete")	.setLongFlag("incomplete")
																						.setShortFlag('i')
																						.setStringParser(JSAP.STRING_PARSER)
																						.setRequired(false)
																						.setUsageName("incomplete model");
		incomplete.setHelp("The path to the incomplete model");
		jsap.registerParameter(incomplete);

		/* output */
		FlaggedOption output = new FlaggedOption("output")		.setLongFlag("output")
																				.setShortFlag('o')
																				.setStringParser(JSAP.STRING_PARSER)
																				.setRequired(false)
																				.setUsageName("output");
		output.setHelp("The path to the output file to write the equations to");
		jsap.registerParameter(output);
		
		/* output format */
		String formats = "c;pbm;both";
		FlaggedOption outputFormat = new FlaggedOption("output_format")	.setLongFlag("format")
																								.setShortFlag('f')
																								.setStringParser(EnumeratedStringParser.getParser(formats))
																								.setDefault("c")
																								.setRequired(false);
		outputFormat.setHelp("The format of the output file - one of [" + formats + "]");
		jsap.registerParameter(outputFormat);
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
	
	public String getLibrary() {
		return result.getString("library");
	}
	
	public String getModel() {
		return result.getString("model");
	}
	
	public String getIncomplete() {
		return result.getString("incomplete");
	}
	
	public String getOutputFile() {
		return result.getString("output");
	}
	
	public String getOutputFormat() {
		return result.getString("output_format");
	}
	
	public String getHelp() {
		
		StringBuilder buffer = new StringBuilder();
      String jarName = CLIParser.class.getProtectionDomain().getCodeSource().getLocation().getPath();
      buffer.append("\n");
      buffer.append("Usage: java -jar " + jarName.substring(jarName.lastIndexOf('/') + 1) + " OPTIONS\n");
      buffer.append("\n");
      buffer.append("OPTIONS:\n");
      buffer.append(jsap.getHelp(80, "") + "\n");
	
      return buffer.toString();
	}
}

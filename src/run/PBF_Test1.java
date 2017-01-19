package run;

import org.slf4j.*;

import struct.*;
import struct.inst.*;
import struct.temp.*;
import traverse.*;
import util.*;

public class PBF_Test1 {
	public static final Logger logger = LoggerFactory.getLogger(PBF_Test1.class);
	
	public static void main(String args[]) throws Exception {
		//		for (Map.Entry<String, String> e : System.getenv().entrySet()) {
		//			System.out.println(e.getKey() + "=" + e.getValue());
		//		}
		//		for (Map.Entry<Object, Object> e : System.getProperties().entrySet()) {
		//			System.out.println(e.getKey() + "=" + e.getValue());
		//		}
		
		try {
   		String libraryName = "res\\paper\\AquaticEcosystem_paper.pbl";
   		String modelName = "res\\paper\\Example1_paper.pbm";
   		
   		MDC.put("file", libraryName);
   		Library library = Traverse.addLibrary(libraryName);
   		
   		MDC.put("file", modelName);
   		Model model = Traverse.addModel(modelName);
   
   		
   		logger.info("'" + libraryName + "' and '" + modelName + "' compiled successfully");
		} catch (ParsingException ex) {
			logger.error(ex.getFilename()+":"+((ex.getLinenum()!=-1)?ex.getLinenum():"<na>")+":"+((ex.getPosnum()!=-1)?ex.getPosnum():"<na>")+" - " + ex.getMessage(), ex);
		} catch (Exception ex) {
			logger.error((ex.getMessage()!=null)?ex.getMessage():ex.toString(), ex);
		}
		
		System.exit(0);
	}
}
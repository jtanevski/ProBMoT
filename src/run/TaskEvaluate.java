package run;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;

import javax.xml.bind.JAXBException;

import jmetal.util.JMException;

import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.io.IOUtils;
import org.xml.sax.SAXException;

import fit.CVodeSimulator;
import struct.temp.Library;
import task.Task;
import traverse.Traverse;
import util.FailedSimulationException;
import xml.TaskSpec;

public class TaskEvaluate {

	/**
	 * @param args
	 * @throws RecognitionException 
	 * @throws IOException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws NoSuchFieldException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws JAXBException 
	 * @throws JMException 
	 * @throws SAXException 
	 * @throws InterruptedException 
	 * @throws FailedSimulationException 
	 * @throws ClassNotFoundException 
	 * @throws ConfigurationException 
	 * @throws URISyntaxException 
	 * @throws SecurityException 
	 * @throws IllegalArgumentException 
	 */
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, IOException, RecognitionException, JAXBException, ConfigurationException, ClassNotFoundException, FailedSimulationException, InterruptedException, SAXException, JMException, IllegalArgumentException, SecurityException, URISyntaxException {
		
		System.setProperty("java.library.path", CVodeSimulator.JVODE_LIB_PATH);
		
		TaskSpec taskSpec = TaskSpec.unmarshal("ldlsim/task/NY-ED-test_uno_2_newlib_ld.xml");
		TaskSpec ts = TaskSpec.unmarshal("ldlsim/task/NY-ED-test_uno_2_newlib_ld.xml");
		
		BufferedReader inputStream = new BufferedReader(new FileReader(taskSpec.model));
		PrintStream outputStream = new PrintStream("ldlsim/NetworkYannisNewLD.pbm");
		ts.model = "ldlsim/NetworkYannisNewLD.pbm";
		
		StringBuilder modelText = new StringBuilder();
		String line;
		
		while ((line = inputStream.readLine()) != null) {
			if (line.startsWith("// Model")) {
				
				if (modelText.length() != 0) {
					
					outputStream.println(modelText);
					outputStream.close();
					
					Task task = new Task(ts);
					task.perform();
					
					modelText = new StringBuilder();
					outputStream = new PrintStream("ldlsim/NetworkYannisNewLD.pbm");
				} 
			}
			if (line.startsWith("// K0")) {
				ts.output.constants.get(0).value = Double.parseDouble(line.split("=")[1]);
			}
			if (line.startsWith("// K1")) {
				ts.output.constants.get(1).value = Double.parseDouble(line.split("=")[1]);
			}
			modelText.append(line + "\n");
		}

		if (modelText.length() != 0) {
			outputStream.println(modelText);
			outputStream.close();
			
			Task task = new Task(ts);
			task.perform();
			
		}

		inputStream.close();
		outputStream.close();

	}

}

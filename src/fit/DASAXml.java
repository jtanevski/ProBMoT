package fit;

import java.io.*;
import java.util.*;

import javax.xml.bind.*;

import org.xml.sax.*;

import fit.ALG_MSC.*;

import serialize.*;
import struct.*;
import struct.inst.*;
import temp.*;
import xml.*;

public class DASAXml {

	EquationSerializer serializer;

	public String tempDirpath = "temp";

	private void callDASA(String simulationFilename, String outputFilename) {
		// TODO Auto-generated method stub
	}
	
	public void generateDASAOutputFile(XMLSpec xmlSpec, String simulationFilename, String outputFilename)
			throws IOException, InterruptedException, JAXBException, SAXException {
		XMLSpec.marshal(xmlSpec, simulationFilename);
		callDASA(simulationFilename, outputFilename);
	}

	public void generateDASAOutputFile(XMLSpec xmlSpec, String outputFilename)
			throws IOException, InterruptedException, JAXBException, SAXException {
		String simulationFilename = tempDirpath + "/sim.xml";

		generateDASAOutputFile(xmlSpec, simulationFilename, outputFilename);
	}

	private Simulation createSimulationObject(XMLSpec xmlSpec, String resultFile)
			throws IOException {
		Simulation simulation = new Simulation(xmlSpec, resultFile);

		return simulation;
	}

	public Simulation simulate(XMLSpec xmlSpec)
			throws IOException, InterruptedException, JAXBException, SAXException {
		String simulationFilename = tempDirpath + "/sim.xml";
		String simulationOutputFilename = tempDirpath + "/sim.out";

		generateDASAOutputFile(xmlSpec, simulationFilename, simulationOutputFilename);
		Simulation simulation = createSimulationObject(xmlSpec, simulationOutputFilename);
		// deleteTemporaryFiles(simulationFilename, simulationOutputFilename);

		return simulation;
	}
}

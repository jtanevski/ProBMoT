package temp;

import java.io.*;
import java.util.*;

import org.apache.commons.io.*;
import org.slf4j.*;

import run.*;
import serialize.*;
import struct.*;
import struct.inst.*;
import xml.*;

import com.google.common.collect.*;

public class Simulation {
	public static final Logger logger = LoggerFactory.getLogger(CLITaskMain.class);

	Output output;
	Model model;

	String timeColumnDat;

	int timeColumnSim;
	BiMap<OutputVar, Integer> columns = HashBiMap.create(); // OutputVar from the model output <-> column# from the simulation

	Dataset values; // The simulation has the form of a dataset

	public Simulation(Output output, String timeColumn, String resultFile)
			throws IOException {
		this.output = output;
		this.model = output.graph.model;

		this.timeColumnDat = timeColumn;

		BufferedReader input = new BufferedReader(new FileReader(resultFile));

		StringBuilder text = new StringBuilder();
		String line = null;

		do {
			line = input.readLine();
		} while (line != null && !line.equals("***"));

		while ((line = input.readLine()) != null && !line.startsWith("SSE") && !line.equals("***")) { //FIXME: change to line.equals("***") when Ljupco changes ALG
			text.append(line + "\n");
		}

		readSimulationDataset(IOUtils.toInputStream(text));
		matchColumns();
	}

	public Simulation(XMLSpec xmlSpec, String resultFile) {
		// TODO Auto-generated constructor stub
	}

	private void readSimulationDataset(InputStream input)
			throws IOException {
		this.values = new Dataset(input, " ");

		//		System.out.println(this.values);  // DEBUG CODE
	}

	private void matchColumns() {
		// match time
		Integer timeCol = values.getColIndex(timeColumnDat);
		if (timeCol != null) {
			this.timeColumnSim = timeCol;
		} else {
			this.timeColumnSim = values.getColIndex("time");
		}
		//this.timeColumnSim = values.getColIndex(timeColumnDat);

		// match all output variables
		for (OutputVar var : this.output.variables.values()) {
			Integer index = this.values.getColIndex(var.name);
			this.columns.put(var, index);
		}
	}

	public void writeSimulation(OutputStream output, String separator, String[] header)
			throws IOException {
		this.values.writeDataset(output, separator, header);
	}

	public void writeSimulation(OutputStream output, String separator)
			throws IOException {
		this.values.writeDataset(output, separator);
	}

	public void writeSimulation(OutputStream output)
			throws IOException {
		this.values.writeDataset(output);
	}

	public String toString() {
		OutputStream output = new ByteArrayOutputStream();
		try {
			this.writeSimulation(output);
		} catch (IOException ex) {
			logger.error("Error with ByteArrayOutputStream", ex);
			throw new RuntimeException(ex);
		}
		return output.toString();
	}

}
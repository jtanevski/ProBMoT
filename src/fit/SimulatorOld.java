package fit;

import java.io.*;
import java.util.*;

import struct.inst.*;
import temp.*;
import util.*;

public interface SimulatorOld {
	public Simulation simulate(Output output, List<Dataset> datasets, String timeColumn, Map<String, Integer> varIndex, Map<String, Integer> uncIndex)
			throws IOException, InterruptedException, FailedSimulationException;

	public void initWorkDir() throws IOException;

	public void deleteWorkDir() throws IOException;

}

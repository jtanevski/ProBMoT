package fit;

import java.io.*;
import java.util.*;

import org.apache.commons.configuration.*;

import struct.inst.*;
import temp.*;
import util.*;

public interface FitterOld {
	public ExtendedModel fit(Output output, List<Dataset> datasets, String timeColumn, Map<String, Integer> varIndex, Map<String, Integer> uncIndex, String logFilename)
			throws IOException, InterruptedException, ConfigurationException, FailedSimulationException;

	public void initWorkDir()
			throws IOException;

	public void deleteWorkDir()
			throws IOException;

	public String generateMSCString(Output output, List<Dataset> datasets, String timeColumn, Map<String, Integer> varIndex, Map<String, Integer> uncIndex);

	public void generateMSCFile(Output output, List<Dataset> datasets, String timeColumn, Map<String, Integer> varIndex, Map<String, Integer> uncIndex, String filename)
			throws FileNotFoundException;

}

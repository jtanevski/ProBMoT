package fit.objective;

import java.util.*;

import temp.*;

import com.google.common.collect.*;

public abstract class ObjectiveFunction {
	public abstract double evaluate(Output output, List<Dataset> data, BiMap<String, String> outsToCols);
}
package fit.objective;

import java.util.*;

import temp.*;

import com.google.common.collect.*;

public abstract class MultiTrajectoryObjectiveFunction {
	List<Dataset> measured;
	BiMap<String, String> outsToCols;


	public abstract double evaluateTrajectories(List<Dataset> simulated);

}

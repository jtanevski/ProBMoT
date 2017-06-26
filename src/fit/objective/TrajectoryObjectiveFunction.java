package fit.objective;

import temp.*;

import com.google.common.collect.*;

public abstract class TrajectoryObjectiveFunction {
	protected Dataset measured;
	protected BiMap<String, String> outsToCols;

	public abstract double evaluateTrajectory(Dataset simulated);
	public double evaluateTrajectory(Dataset simulated, int ndataset){
		return evaluateTrajectory(simulated);
	}

	protected String name;

	public String getName() {
		return this.name;
	}
	public  abstract Double[] evaluateTrajectoryByPoint(Dataset simulated);
	public Double[] evaluateTrajectoryByPoint(Dataset simulated, int ndataset){
		return evaluateTrajectoryByPoint(simulated);
	}
}
package fit.objective;

public abstract class TrajectoryErrorFunction {
	protected Double[] measured;
	protected double[] weights;

	protected String name;

	public abstract double evaluate(Double[] predicted);

	public String getName() {
		return this.name;
	}

	
}

package fit.objective;

public class WSSEL
		extends TrajectoryErrorFunction {

	protected double[] weights;

	public WSSEL(Double[] measured, double[] weights) {
		this.measured = measured;
		this.weights = weights;

		this.name = "W-SSE";
	}

	@Override
	public double evaluate(Double[] predicted) {
		double sse = 0;
		for (int i = 0; i < predicted.length; i++) {
			sse += Math.log( 1 + ((weights[i]/2) * (predicted[i] - measured[i]) * (predicted[i] - measured[i])));
		}

		return sse;
	}
}

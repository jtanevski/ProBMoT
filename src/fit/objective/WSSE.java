package fit.objective;

public class WSSE
		extends TrajectoryErrorFunction {

	protected double[] weights;

	public WSSE(Double[] measured, double[] weights) {
		this.measured = measured;
		this.weights = weights;

		this.name = "W-SSE";
	}

	@Override
	public double evaluate(Double[] predicted) {
		double sse = 0;
		for (int i = 0; i < predicted.length; i++) {
			sse += weights[i] * (predicted[i] - measured[i]) * (predicted[i] - measured[i]);
		}

		return sse;
	}
}

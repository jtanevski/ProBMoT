package fit.objective;

public class SSE
		extends TrajectoryErrorFunction {


	public SSE(Double[] measured) {
		this.measured = measured;

		this.name = "SSE";
	}
	@Override
	public double evaluate(Double[] predicted) {
		double sse = 0;
		for (int i = 0; i < predicted.length; i++) {
			sse += (predicted[i] - measured[i]) * (predicted[i] - measured[i]);
		}

		return sse;
	}
}

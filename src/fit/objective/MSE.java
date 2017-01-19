package fit.objective;

public class MSE
		extends TrajectoryErrorFunction {

	private SSE sse;

	public MSE(Double[] measured) {
		this.sse = new SSE(measured);
		this.name = "MSE";
	}

	@Override
	public double evaluate(Double[] predicted) {
		return sse.evaluate(predicted)/predicted.length;
	}

}

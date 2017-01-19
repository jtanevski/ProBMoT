package fit.objective;

public class Willmot
		extends TrajectoryErrorFunction {

	private SSE sse;
	private ObservedVariance ov;

	public Willmot(Double[] measured) {
		this.measured = measured;
		this.sse = new SSE(measured);
		this.ov = new ObservedVariance(2,measured);

		this.name = "Willmot";
	}

	@Override
	public double evaluate(Double[] predicted) {
		return sse.evaluate(predicted)/ov.evaluate(predicted);
	}
}

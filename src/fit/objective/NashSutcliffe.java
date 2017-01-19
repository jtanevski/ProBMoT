package fit.objective;

public class NashSutcliffe
		extends TrajectoryErrorFunction {

	private SSE sse;
	private ObservedVariance ov;

	public NashSutcliffe(Double[] measured) {
		this.measured = measured;
		this.sse = new SSE(measured);
		this.ov = new ObservedVariance(2,measured);

		this.name = "NashSutcliffe";
	}

	@Override
	public double evaluate(Double[] predicted) {
		return sse.evaluate(predicted)/ov.evaluate(measured);
	}
}

package fit.objective;

public class GeneralNashSutcliffe
		extends TrajectoryErrorFunction {

	private ABS abs;
	private ObservedVariance ov;
	private int pow;

	public GeneralNashSutcliffe(int pow,Double[] measured) {
		this.measured = measured;
		this.abs = new ABS(measured);
		this.ov = new ObservedVariance(pow,measured);
		this.pow=pow;
		this.name = "GeneralNashSutcliffe";
	}

	@Override
	public double evaluate(Double[] predicted) {
		return Math.pow(abs.evaluate(predicted),pow)/Math.pow(ov.evaluate(measured),pow);
	}
}

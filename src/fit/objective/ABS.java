package fit.objective;

public class ABS
		extends TrajectoryErrorFunction {


	public ABS(Double[] measured) {
		this.measured = measured;

		this.name = "ABS";
	}
	@Override
	public double evaluate(Double[] predicted) {
		double abs = 0;
		for (int i = 0; i < predicted.length; i++) {
			abs += Math.abs(predicted[i] - measured[i]);
		}

		return abs;
	}
}

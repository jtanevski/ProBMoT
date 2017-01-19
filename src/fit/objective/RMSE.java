package fit.objective;

public class RMSE
		extends TrajectoryErrorFunction {

	private MSE mse;

	public RMSE(Double[] measured) {
		this.measured = measured;
		this.mse = new MSE(measured);

		this.name = "RMSE";
	}

	@Override
	public double evaluate(Double[] predicted) {
		return Math.sqrt(mse.evaluate(predicted));
	}
}

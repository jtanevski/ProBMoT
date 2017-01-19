package fit.objective;

public class WRMSE
		extends TrajectoryErrorFunction {

	private WMSE wmse;
	public WRMSE(Double[] measured, double[] weights) {
		this.measured = measured;
		this.weights = weights;
		this.wmse = new WMSE(measured, weights);
		this.name = "W-RMSE";
	}
	@Override
	public double evaluate(Double[] predicted) {
		return Math.sqrt(wmse.evaluate(predicted));
	}
}

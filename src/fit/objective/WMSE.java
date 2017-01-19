package fit.objective;

public class WMSE
		extends TrajectoryErrorFunction {

	private WSSE wsse;
	private double weightsum;
	
	public WMSE(Double[] measured, double[] weights) {
		this.wsse = new WSSE(measured, weights);
		weightsum = 0;
		for(int i=0 ; i<weights.length; i++){
			weightsum += weights[i];
		}
		this.name = "W-SSE";
	}
	@Override
	public double evaluate(Double[] predicted) {

		return wsse.evaluate(predicted)/weightsum;
		//return wsse.evaluate(predicted)/predicted.length;
	}
}

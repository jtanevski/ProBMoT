package fit.objective;

public class ObservedVariance
		extends TrajectoryErrorFunction {

private int pow;
	public ObservedVariance(int pow,Double[] measured) {
		this.measured = measured;
		this.pow=pow;
		this.name = "OV";
	}
	@Override
	public double evaluate(Double[] measured) {
		double ov = 0;
		double avgM= 0;
		
		for (int i=0 ; i<measured.length;i++)
			avgM+=measured[i];
		
		avgM=avgM/measured.length;
		
		for (int i = 0; i < measured.length; i++) {
			ov += Math.pow(Math.abs(measured[i]-avgM),pow);// * (measured[i]-avgM);
		}

		return ov;
	}
}

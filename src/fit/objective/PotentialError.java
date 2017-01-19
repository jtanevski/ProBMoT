package fit.objective;

public class PotentialError
		extends TrajectoryErrorFunction {


	public PotentialError(Double[] measured) {
		this.measured = measured;
		
		this.name = "PE";
	}
	@Override
	public double evaluate(Double[] predicted) {
		double pe = 0;
		double avgM= 0;
		
		for (int i=0 ; i<measured.length;i++)
			avgM+=measured[i];
		
		avgM=avgM/measured.length;
		
		for (int i = 0; i < predicted.length; i++) {
			pe += Math.pow(Math.abs(predicted[i]-avgM)+Math.abs(measured[i]-avgM), 2);
		}

		return pe;
	}
}

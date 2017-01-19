package fit.objective;

import java.util.Arrays;

public class RelativeErrorFunction extends TrajectoryErrorFunction {
	private TrajectoryErrorFunction error;

	//private double variance;
	private Double[] average_array;

	public RelativeErrorFunction (TrajectoryErrorFunction error, boolean weighted) {
		this.error = error;
		this.measured = error.measured;
		if (weighted){
			this.weights = error.weights;
		}

		double average = 0;
		for (int i = 0; i < measured.length; i++) {
			average += measured[i];
		}
		average /= measured.length;
		
		average_array= new Double[measured.length];
		Arrays.fill(average_array, average);

//		for (int i = 0; i < measured.length; i++) {
//			if(weighted){
//				variance += weights[i]*(measured[i] - average) * (measured[i] - average);
//			} else {
//				variance += (measured[i] - average) * (measured[i] - average);
//			}
//		}
//		if (weighted){
//			double weightsum = 0;
//			for(int i=0 ; i<weights.length; i++){
//				weightsum += weights[i];
//			}
//			variance /= weightsum;
//		} else {
//			variance /= measured.length;
//		}
//		variance /= measured.length;


		this.name = "R-" + this.error.name;
	}
	@Override
	public double evaluate(Double[] predicted) {
		return this.error.evaluate(predicted)/this.error.evaluate(this.average_array);
		//return this.error.evaluate(predicted)/Math.sqrt(variance);
	}

}

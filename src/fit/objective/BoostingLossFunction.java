package fit.objective;

import java.util.List;

public class BoostingLossFunction {
	
	protected Double[] measured;
	//protected Double[] weights;

	protected String name;
	
	public BoostingLossFunction(Double[] measured) {
		this.measured = measured;

		this.name = "BoostingLossFunction";
	}

	
	public double[] squaredDistance(Double[] predicted) {
		
		double [] Loss = new double [predicted.length];
		
		for (int i = 0; i < predicted.length; i++) {
			
			Loss[i]=Math.abs(predicted[i] - measured[i]);
		}
		
		return Loss;
	
	}
	
	public double getD(double[] squaredDistance) {

		double D = Double.NEGATIVE_INFINITY;

		for (int i = 0; i < squaredDistance.length; i++) {

			if (squaredDistance[i] > D)
				D = squaredDistance[i];

		}

		return D;

	}
	
	public double[] linearLaw(double[] squaredDistance) {
		
		double [] Loss = new double [squaredDistance.length];
		double D= this.getD(squaredDistance);
		
		for (int i = 0; i < squaredDistance.length; i++) {
			Loss[i]=squaredDistance[i]/D;
		}
		
		return this.MinMax(Loss, 1, 0);
	
	}
	
	public double[] squareLaw(double[] squaredDistance) {
		
		double [] Loss = new double [squaredDistance.length];
		double D= this.getD(squaredDistance);
		
		
		for (int i = 0; i < squaredDistance.length; i++) {
			Loss[i]=(squaredDistance[i]*squaredDistance[i])/(D*D);
		}
		
		return this.MinMax(Loss, 1, 0);
	
	}
	
	public double[] exponentialLaw(double[] squaredDistance) {
		
		double [] Loss = new double [squaredDistance.length];
		
		double D= this.getD(squaredDistance);
		
		for (int i = 0; i < squaredDistance.length; i++) {
			Loss[i]=1-Math.exp(-squaredDistance[i]/D);
		}
		
		
		
		return this.MinMax(Loss, 1, 0);
	
	}
	
	public  double[] MinMax(double[] array, double rangeMax, double rangeMin)
	{
		double tempMax= Double.NEGATIVE_INFINITY;
		double tempMin= Double.POSITIVE_INFINITY;
		double[] result = new double[array.length];
		for (int i=0; i<array.length;i++)
		{
			if (array[i] > tempMax)
				tempMax = array[i];
			else if (array[i] < tempMin)
				tempMin = array[i];

		}
		
		for (int i=0 ; i<array.length;i++)
		{
			result[i]=(array[i]-tempMin)/(tempMax-tempMin)*(rangeMax-rangeMin)+rangeMin;
		}
		
		return result;
	}
	
	public double averageLoss (double[] Loss, double[] weights)
	{
		double avgL=0;
		double sumW=0;
		
		for (int i=0;i<weights.length;i++)
			sumW+=weights[i];
		
		for (int i=0;i<Loss.length;i++)
			avgL+=Loss[i]*weights[i]/sumW;
		
		
		return avgL;
	}
	
	public double SumLoss (double[] Loss, double[] weights)
	{
		double avgL=0;
		//double sumW=0;
		
		
		
		for (int i=0;i<Loss.length;i++)
			avgL+=Loss[i]*weights[i];
		
		
		return avgL;
	}
	
	/*public double averageLossWholeDS (List<double[]> Loss, List<double[]> weights)
	{
		double avgL=0;
		double sumW=0;
		
		for (int j=0 ; j< weights.size();j++)
		for (int i=0;i<weights.get(j).length;i++)
			sumW+=weights.get(j)[i];
		
		for (int j=0 ; j< weights.size();j++)
		for (int i=0;i<Loss.get(j).length;i++)
			avgL+=Loss.get(j)[i]*weights.get(j)[i]/sumW;
		
		
		return avgL;
	}*/
	
	

}

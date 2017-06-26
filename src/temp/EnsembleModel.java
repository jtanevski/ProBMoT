package temp;

import java.util.*;

import com.google.common.collect.BiMap;

import fit.objective.Boosting;

public class EnsembleModel {

	public static int numRuns;
	public static int lastRun;
	public static List<ExtendedModel> ensembles;
	public static List<double[]> weights;
	//public static List<List<double[]>> MultiLoss;
	public static List<double[]> betas;
	public static List<Double> betaPerModel;
	public static boolean begin;
	public static double BoostingLoss;
	public static double BoostingLossTreshold;
	public static boolean BoostingIsFirstIter;
	public static int BoostingStartIteration;
	public static int BoostingEndIteration;
	public static String lossFunctionMethod;
	public static int BaggingStartIteration;
	public static int BaggingEndIteration;
	
	//dirty solution
	public static int winsize;
	public static double alpha;
	public static double sigma;
	
	
	
	
	
	
	public static String SamplingMethod;
	
	

	public EnsembleModel(int nR, List<double[]> w) {
		this.setNumRuns(nR);
		this.setWeights(w);
	}

	public List<double[]> getWeights() {
		return weights;
	}

	public void setWeights(List<double[]> weights) {
		this.weights = weights;
	}

	public static List<ExtendedModel> getEnsembles() {
		return ensembles;
	}

	public static void setEnsembles(List<ExtendedModel> ensembles) {
		EnsembleModel.ensembles = ensembles;
	}

	public int getNumRuns() {
		return numRuns;
	}

	public void setNumRuns(int numRuns) {
		this.numRuns = numRuns;
	}

	public void iterate() {
		this.numRuns++;
	}
// Uniformly chooses time points and assignests wights to them
	public static void baggingRandomWeighting(List<Dataset> datasets, long seed,int thisrun) {
		
		
	/*	System.out.println(datasets.get(0));
		System.out.println(datasets.get(0).getElem(3, ));*/
		
		
		weights = new ArrayList<double[]>();
		for (int i = 0; i < datasets.size(); i++) {
			
			weights.add(new double[datasets.get(i).getNRows()]);
			for (int j = 0; j < datasets.get(i).getNRows(); j++)
				weights.get(i)[j] = 0.0;
		}
		Random metarandom = new Random();
		metarandom.setSeed(seed);
		
		int [] metarandarray = new int[BaggingEndIteration];
		
		for (int i=0; i<metarandarray.length; i++)
			metarandarray[i]=metarandom.nextInt();
		
		Random random = new Random();
		random.setSeed(metarandarray[BaggingStartIteration+thisrun]);

		
		
		for (int i = 0; i < weights.size(); i++)
			for (int j = 0; j < weights.get(i).length; j++)
				weights.get(i)[random.nextInt(weights.get(i).length)]++;

	}
	//Uniformly chooses median (center of the gauss) and assigns weights to the neighbouring time points accoridng Gauss func
	public static void baggingWindowGaussWeighting(List<Dataset> datasets, long seed,int thisrun, double alpha, double sigmaSQR, int winsize) {
		weights = new ArrayList<double[]>();
		for (int i = 0; i < datasets.size(); i++) {
			
			weights.add(new double[datasets.get(i).getNRows()]);
			for (int j = 0; j < datasets.get(i).getNRows(); j++)
				weights.get(i)[j] = 0.0;
		}
		Random metarandom = new Random();
		metarandom.setSeed(seed);
		
		int [] metarandarray = new int[BaggingEndIteration];
		
		for (int i=0; i<metarandarray.length; i++)
			metarandarray[i]=metarandom.nextInt();
		
		Random random = new Random();
		random.setSeed(metarandarray[BaggingStartIteration+thisrun]);
		
		
		// for every dataset
		for (int i = 0; i < weights.size(); i++) {

			int points = 0;
			for (int j = 0; j < weights.get(i).length / winsize; j++) {

				// this will be the mu (center of the Gauss)
				// if window size is odd then the mu is the exact random, if it
				// is
				// even it needs to be find
				int rnd = random.nextInt(weights.get(i).length);

				if (winsize % 2 != 0) {
					// System.out.println( " rnd:"+rnd );
					// alternative pick again
					// this is with shift
					/*
					 * if (rnd - (winsize / 2) < 0) rnd=rnd + Math.abs((rnd
					 * -(winsize / 2))); else if (rnd + (winsize / 2) >
					 * weights.get(i).length-1 ) rnd=rnd - (rnd + (winsize / 2)
					 * - ( weights.get(i).length-1));
					 */

					int mu = rnd;
					for (int k = mu - (winsize / 2); k <= mu + (winsize / 2); k++) {
						if (k < 0 || k > (weights.get(i).length - 1))
							continue;
						else {
							points++;
							weights.get(i)[k] += alpha
									* Math.exp(-1
											* Math.pow(
													datasets.get(i).getElem(k,
															0)
															- datasets.get(i)
																	.getElem(
																			mu,
																			0),
													2) / (2 * sigmaSQR));
						}
					}

				} else {

					/*
					 * if (rnd - (winsize / 2) < 0) rnd=rnd + Math.abs((rnd
					 * -(winsize / 2))); else if (rnd + (winsize / 2) >
					 * weights.get(i).length-1 ) rnd=rnd - (rnd + (winsize / 2)
					 * - (weights.get(i).length-1));
					 */

					double mu = datasets.get(i).getElem(rnd, 0)
							+ ((datasets.get(i).getElem(rnd + 1, 0) - datasets
									.get(i).getElem(rnd, 0)) / 2);

					for (int k = rnd - (winsize / 2) + 1; k <= rnd
							+ (winsize / 2); k++) {
						if (k < 0 || k > (weights.get(i).length - 1))
							continue;
						else {

							weights.get(i)[k] += alpha
									* Math.exp(-1
											* Math.pow(
													datasets.get(i).getElem(k,
															0)
															- mu, 2)
											/ (2 * sigmaSQR));
						}

					}
				}
			}

			// One more time if there are left points (DIRTY SOLUTION)
			if (points < weights.get(i).length) {
				while (points < weights.get(i).length) {

					int rnd = random.nextInt(weights.get(i).length);
					int diff = weights.get(i).length - points;
					if (diff % 2 != 0) {

						int mu = rnd;
						for (int k = mu - (diff / 2); k <= mu + (diff / 2); k++) {
							// System.out.println("i:" + i + " k:"+k + " mu:"+
							// mu);
							if (k < 0 || k > (weights.get(i).length - 1))
								continue;
							else {
								points++;
								weights.get(i)[k] += alpha
										* Math.exp(-1
												* Math.pow(
														datasets.get(i)
																.getElem(k, 0)
																- datasets
																		.get(i)
																		.getElem(
																				mu,
																				0),
														2) / (2 * sigmaSQR));
							}
						}

					} else {
						double mu = datasets.get(i).getElem(rnd, 0)
								+ ((datasets.get(i).getElem(rnd + 1, 0) - datasets
										.get(i).getElem(rnd, 0)) / 2);

						for (int k = rnd - (diff / 2) + 1; k <= rnd
								+ (diff / 2); k++) {
							if (k < 0 || k > (weights.get(i).length - 1))
								continue;
							else {
								points++;
								weights.get(i)[k] += alpha
										* Math.exp(-1
												* Math.pow(datasets.get(i)
														.getElem(k, 0) - mu, 2)
												/ (2 * sigmaSQR));
							}

						}

					}

				}

			}

		}
	}
	
	//Uniformly chooses median (center of the uniform) and assigns weights to the neighbouring time points accoridng Uniform func
	public static void baggingWindowUniformWeighting(List<Dataset> datasets, long seed,int thisrun, double alpha, int winsize) {
		weights = new ArrayList<double[]>();
		for (int i = 0; i < datasets.size(); i++) {
			
			weights.add(new double[datasets.get(i).getNRows()]);
			for (int j = 0; j < datasets.get(i).getNRows(); j++)
				weights.get(i)[j] = 0.0;
		}
		Random metarandom = new Random();
		metarandom.setSeed(seed);
		
		int [] metarandarray = new int[BaggingEndIteration];
		
		for (int i=0; i<metarandarray.length; i++)
			metarandarray[i]=metarandom.nextInt();
		
		Random random = new Random();
		random.setSeed(metarandarray[BaggingStartIteration+thisrun]);
		
		for (int i = 0; i < weights.size(); i++) {
			// this will be the mu (center of the Gauss)
			// if window size is odd then the mu is the exact random, if it is
			// even it needs to be find
			int points=0;
			for (int j = 0; j < weights.get(i).length / winsize; j++) {

				int rnd = random.nextInt(weights.get(i).length );

				if (winsize % 2 != 0) {
					// int mu = rnd;
					/*if (rnd - (winsize / 2) < 0)
						rnd=rnd + (rnd -(winsize / 2));
					else if (rnd + (winsize / 2) > weights.get(i).length-1 )
						rnd=rnd - (rnd + (winsize / 2) - (weights.get(i).length-1));*/
				
					
					for (int k = rnd - (winsize / 2); k <= rnd + (winsize / 2); k++) {
						// System.out.println("i:" + i + " k:"+k + " mu:"+ mu);
						if (k < 0 || k>(weights.get(i).length-1))
							continue;
						else {
							points++;
							weights.get(i)[k] += alpha;
						}
					
					}
				}

				else {
					/*if (rnd - (winsize / 2) < 0)
						rnd=rnd + (rnd -(winsize / 2));
					else if (rnd + (winsize / 2) > weights.get(i).length-1 )
						rnd=rnd - (rnd + (winsize / 2) - (weights.get(i).length-1));*/
					
					for (int k = rnd - (winsize / 2) + 1; k <= rnd
							+ (winsize / 2); k++) {

						if (k < 0 || k>(weights.get(i).length-1))
							continue;
						else {
							points++;
							weights.get(i)[k] += alpha;
						}

					
					}
				}
			}
			
			
			if (points < weights.get(i).length) {
				while (points < weights.get(i).length) {

					int rnd = random.nextInt(weights.get(i).length);
					int diff = weights.get(i).length - points;
					if (diff % 2 != 0) {

						int mu = rnd;
						for (int k = mu - (diff / 2); k <= mu + (diff / 2); k++) {
							// System.out.println("i:" + i + " k:"+k + " mu:"+
							// mu);
							if (k < 0 || k > (weights.get(i).length - 1))
								continue;
							else {
								points++;
								weights.get(i)[k] += alpha;
							}
						}

					} else {
						double mu = datasets.get(i).getElem(rnd, 0)
								+ ((datasets.get(i).getElem(rnd + 1, 0) - datasets
										.get(i).getElem(rnd, 0)) / 2);

						for (int k = rnd - (diff / 2) + 1; k <= rnd
								+ (diff / 2); k++) {
							if (k < 0 || k > (weights.get(i).length - 1))
								continue;
							else {
								points++;
								weights.get(i)[k] += alpha;
							}

						}

					}

				}

			}	
			
		}
		
	}
	
	public static double boosting(List<Dataset> datasets,
			BiMap<String, String> outsToCols, List<Dataset> outputData ,boolean isStart,String method, int numIter) {

		double totalAVGLoss=0;
		if (isStart) {
			
			
			weights = new ArrayList <double[]>();
			betas = new ArrayList<double[]>();
			betaPerModel = new ArrayList<Double>();
			
			for (int i = 0; i < datasets.size(); i++) {

				weights.add(new double[datasets.get(i).getNRows()]);
				for (int j = 0; j < datasets.get(i).getNRows(); j++)
					weights.get(i)[j] = 1.0;///weights.get(i).length; //normalization sum up to 1
			
				BoostingIsFirstIter = false;
			}
			
			return 1;
		} else {
			double sumTotalWeights=0.0;
			double totalLoss=0.0;
			//MultiLoss = new ArrayList<List<double[]>>();
			// IF OUTPUTDATA == null failed simulation
			if (outputData != null) {
				
				//betaPerModel.add(new Double(0.0));
				double temp=0.0;
				betas.add(new double[datasets.size()]);
				
				for (int i = 0; i < datasets.size(); i++) {
				
				
					
				Boosting iteration = new Boosting(datasets.get(i), outsToCols);
				List<double[]> Loss= iteration.getLoss(outputData.get(i),method);
				//MultiLoss.add(Loss);
				
				double avgL= iteration.getTotalAvgsPerEndo(Loss, weights.get(i));
				
				totalLoss+=iteration.getTotalAvgsPerEndo(Loss, weights.get(i));
				
				double beta=avgL/(1-avgL);
			
				
				//numIter-1, because the ZeroIter is just for the beginning
				betas.get(numIter)[i]=beta;
				
				//TODO AVG LOSS by column
				//double temp=0;
				Double[] aLosses=new Double[Loss.get(0).length];
				for (int j=0;j<aLosses.length;j++)
					aLosses[j]=0.0;
				
				for (int j=0; j< Loss.get(0).length;j++)
					{
						for (int k=0;k<Loss.size();k++)
							aLosses[j]+=Loss.get(k)[j];
						
						aLosses[j]=aLosses[j]/Loss.size();
					}
						
					totalAVGLoss+=avgL;
				/*	Double[] temp= new Double [ weights.get(i).length];
					TrajectoryObjectiveFunction trajFun = new RMSEMultiDataset(
							datasets, outsToCols);
					temp = trajFun.evaluateTrajectoryByPoint(
							outputData.get(i), i);

					temp= MinMax(temp, 1, 0);*/
					
					double sumW=0.0;
					
					for (int j = 0; j < weights.get(i).length; j++) {
						weights.get(i)[j] = weights.get(i)[j]*Math.pow(beta, (1-aLosses[j]));
						sumW+=weights.get(i)[j];
						sumTotalWeights+=weights.get(i)[j];
					}
					for (int j = 0; j < weights.get(i).length; j++) { //normalization sum up to weight length
						weights.get(i)[j] =weights.get(i)[j] * weights.get(i).length/sumW; 
					}

				}
				
				
				
				betaPerModel.add((totalLoss/sumTotalWeights)/(1-(totalLoss/sumTotalWeights)));
				System.out.println("Beta "+(totalLoss/sumTotalWeights)/(1-(totalLoss/sumTotalWeights)));
				return totalAVGLoss/datasets.size();
			} else {
				for (int i = 0; i < weights.size(); i++)
					for (int j = 0; j < weights.get(i).length; j++)
						weights.get(i)[j] += 1;
			
				return 1;
			}

		}

	}
	
	public static double[] MinMax(double[] array, double rangeMax, double rangeMin)
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





}

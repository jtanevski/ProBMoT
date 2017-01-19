package fit.objective;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import com.google.common.collect.*;

import temp.*;

public class Boosting {
	
	BoostingLossFunction[] blfs;
	int[] measuredToSimulatedIndex;
	BiMap<String, String> outsToCols;
	
	public Boosting(Dataset measured, BiMap<String, String> outsToCols){
		
		blfs= new BoostingLossFunction[outsToCols.size()];
		int i=0;
		
		for (Entry<String, String>  out : outsToCols.entrySet()) {
			blfs[i] = new BoostingLossFunction(measured.getCol(measured.getColIndex(outsToCols.get(out.getValue()))));
			i++;
		}

		this.outsToCols = outsToCols;
		
	}
	
	public List<double[]> getLoss(Dataset simulated,String method) {
		
		List< double[] > Loss= new ArrayList <double []>();
		
		if (measuredToSimulatedIndex == null) {
			measuredToSimulatedIndex = new int[blfs.length];
			int i = 0;
			for (Entry<String, String>  out : outsToCols.entrySet()) {
				measuredToSimulatedIndex[i] = simulated.getColIndex(out.getValue());
				i++;
			}
		}
		for (int i = 0; i < blfs.length; i++) {
		Loss.add(blfs[i].squareLaw(blfs[i].squaredDistance(simulated.getCol(measuredToSimulatedIndex[i]))));
		}
		/*switch (method){
		
		case "linearLaw" :
			for (int i = 0; i < blfs.length; i++) {
				
				Loss.add(blfs[i].linearLaw(blfs[i].squaredDistance(simulated.getCol(measuredToSimulatedIndex[i]))));
				
			}break;
		case "squareLaw" :
			for (int i = 0; i < blfs.length; i++) {
				
				Loss.add(blfs[i].squareLaw(blfs[i].squaredDistance(simulated.getCol(measuredToSimulatedIndex[i]))));
				
			}break;
		case "exponentialLaw" :
			for (int i = 0; i < blfs.length; i++) {
				
				Loss.add(blfs[i].exponentialLaw(blfs[i].squaredDistance(simulated.getCol(measuredToSimulatedIndex[i]))));
				
			}break;
		default: for (int i = 0; i < blfs.length; i++) {
			
			Loss.add(blfs[i].linearLaw(blfs[i].squaredDistance(simulated.getCol(measuredToSimulatedIndex[i]))));
			
		}break;
		
		}*/
			
			
			
			
		
		
		
		
		return Loss;	
	}
	
	public double[] getAvgs(List<double[]> Loss, double[] weights) {
		
		
		double[] avgLs= new double[blfs.length];
		
			for (int i = 0; i < blfs.length; i++) {
								
				avgLs[i]=blfs[i].averageLoss(Loss.get(i), weights);
				
			}
			
	
		return avgLs;	
	}
	public double getTotalAvgsPerEndo(List<double[]> Loss, double[] weights) {
		
		
		double[] avgLs= new double[blfs.length];
		double avgPerendo=0;
		
			for (int i = 0; i < blfs.length; i++) {
								
				avgLs[i]=blfs[i].averageLoss(Loss.get(i), weights);
				
			}
			
			for (int i = 0; i < avgLs.length; i++) {
				
				avgPerendo+=avgLs[i];
				
			}
			
	
		return avgPerendo/avgLs.length;	
	}
	
public double getTotalSumsPerEndo(List<double[]> Loss, double[] weights) {
		
		
		double[] avgLs= new double[blfs.length];
		double avgPerendo=0;
		
			for (int i = 0; i < blfs.length; i++) {
								
				avgLs[i]=blfs[i].SumLoss(Loss.get(i), weights);
				
			}
			
			for (int i = 0; i < avgLs.length; i++) {
				
				avgPerendo+=avgLs[i];
				
			}
			
	
		return avgPerendo/avgLs.length;	
	}

}

package fit.objective;

import java.util.Map.Entry;

import temp.*;

import com.google.common.collect.*;

public class RelativeWRMSEObjectiveFunction extends TrajectoryObjectiveFunction {

	RelativeErrorFunction[] nwrmses;
	int[] measuredToSimulatedIndex;
	BiMap<String, String> outsToCols;
	BiMap<String, String> weightsToCols;

	public RelativeWRMSEObjectiveFunction(Dataset measured, BiMap<String, String> outsToCols, BiMap<String, String> weightsToCols) {
		
		
		nwrmses = new RelativeErrorFunction[outsToCols.size()];
		int i = 0;
		for (String out : outsToCols.keySet()) {
			
			double[] weights = new double [measured.getCol(measured.getColIndex(weightsToCols.get(out))).length];
			
			for (int j=0;j<weights.length;j++)
				weights[j]=measured.getCol(measured.getColIndex(weightsToCols.get(out)))[j];
			nwrmses[i] = new RelativeErrorFunction(new WRMSE(measured.getCol(measured.getColIndex(outsToCols.get(out))),weights),true);
			i++;
		}

		this.outsToCols = outsToCols;
		this.weightsToCols = weightsToCols;

		this.name = "NW-RMSE";
	}

	@Override
	public double evaluateTrajectory(Dataset simulated) {
		if (measuredToSimulatedIndex == null) {
			measuredToSimulatedIndex = new int[nwrmses.length];
			int i = 0;
			for (String out : outsToCols.keySet()) {
				measuredToSimulatedIndex[i] = simulated.getColIndex(out);
				i++;
			}
		}
		double totalNWRMSE = 0;
		for (int i = 0; i < nwrmses.length; i++) {
			totalNWRMSE += nwrmses[i].evaluate(simulated.getCol(measuredToSimulatedIndex[i]));
		}

		return totalNWRMSE;
	}
	@Override
	public Double[] evaluateTrajectoryByPoint(Dataset simulated) {
		if (measuredToSimulatedIndex == null) {
			measuredToSimulatedIndex = new int[nwrmses.length];
			int i = 0;
			for (Entry<String, String>  out : outsToCols.entrySet()) {
				measuredToSimulatedIndex[i] = simulated.getColIndex(out.getValue());
				i++;
			}
		}
		Double [] RMSESs = new Double [nwrmses.length];
		for (int i = 0; i < nwrmses.length; i++) {
			RMSESs[i]= nwrmses[i].evaluate(simulated.getCol(measuredToSimulatedIndex[i]));
		}

		return RMSESs;
	}
}

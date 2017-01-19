package fit.objective;

import java.util.Map.Entry;

import com.google.common.collect.*;

import temp.*;

public class RMSEObjectiveFunction
		extends TrajectoryObjectiveFunction {

	RMSE[] rmses;
	int[] measuredToSimulatedIndex;
	BiMap<String, String> outsToCols;


	public RMSEObjectiveFunction(Dataset measured, BiMap<String, String> outsToCols) {
		rmses = new RMSE[outsToCols.size()];
		int i = 0;
		for (Entry<String, String>  out : outsToCols.entrySet()) {
			rmses[i] = new RMSE(measured.getCol(measured.getColIndex(outsToCols.get(out.getValue()))));
			i++;
		}

		this.outsToCols = outsToCols;

		this.name = "RMSE";
	}
	@Override
	public double evaluateTrajectory(Dataset simulated) {
		if (measuredToSimulatedIndex == null) {
			measuredToSimulatedIndex = new int[rmses.length];
			int i = 0;
			for (Entry<String, String>  out : outsToCols.entrySet()) {
				measuredToSimulatedIndex[i] = simulated.getColIndex(out.getValue());
				i++;
			}
		}
		double totalRMSE = 0;
		for (int i = 0; i < rmses.length; i++) {
			totalRMSE += rmses[i].evaluate(simulated.getCol(measuredToSimulatedIndex[i]));
		}

		return totalRMSE;
	}
	@Override
	public Double[] evaluateTrajectoryByPoint(Dataset simulated) {
		if (measuredToSimulatedIndex == null) {
			measuredToSimulatedIndex = new int[rmses.length];
			int i = 0;
			for (Entry<String, String>  out : outsToCols.entrySet()) {
				measuredToSimulatedIndex[i] = simulated.getColIndex(out.getValue());
				i++;
			}
		}
		Double [] RMSESs = new Double [rmses.length];
		for (int i = 0; i < rmses.length; i++) {
			RMSESs[i]= rmses[i].evaluate(simulated.getCol(measuredToSimulatedIndex[i]));
		}

		return RMSESs;
	}
}

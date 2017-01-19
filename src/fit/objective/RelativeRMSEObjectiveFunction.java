package fit.objective;

import java.util.Map.Entry;

import com.google.common.collect.*;

import temp.*;

public class RelativeRMSEObjectiveFunction
		extends TrajectoryObjectiveFunction {

	RelativeErrorFunction[] nrmses;
	int[] measuredToSimulatedIndex;
	BiMap<String, String> outsToCols;


	public RelativeRMSEObjectiveFunction(Dataset measured, BiMap<String, String> outsToCols) {
		nrmses = new RelativeErrorFunction[outsToCols.size()];
		int i = 0;
		for (String out : outsToCols.keySet()) {
			nrmses[i] = new RelativeErrorFunction(new RMSE(measured.getCol(measured.getColIndex(outsToCols.get(out)))),false);
			i++;
		}

		this.outsToCols = outsToCols;

		this.name = "N-RMSE";
	}

	@Override
	public double evaluateTrajectory(Dataset simulated) {
		if (measuredToSimulatedIndex == null) {
			measuredToSimulatedIndex = new int[nrmses.length];
			int i = 0;
			for (String out : outsToCols.keySet()) {
				measuredToSimulatedIndex[i] = simulated.getColIndex(out);
				i++;
			}
		}
		double totalNRMSE = 0;
		for (int i = 0; i < nrmses.length; i++) {
			totalNRMSE += nrmses[i].evaluate(simulated.getCol(measuredToSimulatedIndex[i]));
		}

		return totalNRMSE;
	}

	@Override
	public Double[] evaluateTrajectoryByPoint(Dataset simulated) {
		if (measuredToSimulatedIndex == null) {
			measuredToSimulatedIndex = new int[nrmses.length];
			int i = 0;
			for (Entry<String, String>  out : outsToCols.entrySet()) {
				measuredToSimulatedIndex[i] = simulated.getColIndex(out.getValue());
				i++;
			}
		}
		Double [] RMSESs = new Double [nrmses.length];
		for (int i = 0; i < nrmses.length; i++) {
			RMSESs[i]= nrmses[i].evaluate(simulated.getCol(measuredToSimulatedIndex[i]));
		}

		return RMSESs;
	}

}

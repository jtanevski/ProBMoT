package fit.objective;

import com.google.common.collect.*;

import temp.*;

public class WSSEObjectiveFunction
		extends TrajectoryObjectiveFunction {

	WSSE[] sses;
	int[] measuredToSimulatedIndex;
	BiMap<String, String> outsToCols;


	public WSSEObjectiveFunction(Dataset measured, BiMap<String, String> outsToCols,BiMap<String, String> weightsToCols) {
		sses = new WSSE[outsToCols.size()];
		int i = 0;
		for (String out : outsToCols.keySet()) {
			double[] weights = new double [measured.getCol(measured.getColIndex(weightsToCols.get(out))).length];
			
			for (int j=0;j<weights.length;j++)
				weights[j]=measured.getCol(measured.getColIndex(weightsToCols.get(out)))[j];
			
			sses[i] = new WSSE(measured.getCol(measured.getColIndex(outsToCols.get(out))),weights);
			i++;
		}

		this.outsToCols = outsToCols;

		this.name = "WSSE";
	}
	@Override
	public double evaluateTrajectory(Dataset simulated) {
		if (measuredToSimulatedIndex == null) {
			measuredToSimulatedIndex = new int[sses.length];
			int i = 0;
			for (String  out : outsToCols.keySet()) {
				measuredToSimulatedIndex[i] = simulated.getColIndex(out);
				i++;
			}
		}
		double totalWSSE = 0;
		for (int i = 0; i < sses.length; i++) {
			totalWSSE += sses[i].evaluate(simulated.getCol(measuredToSimulatedIndex[i]));
		}

		return totalWSSE;
	}
	@Override
	public Double[] evaluateTrajectoryByPoint(Dataset simulated) {
		if (measuredToSimulatedIndex == null) {
			measuredToSimulatedIndex = new int[sses.length];
			int i = 0;
			for (String  out : outsToCols.keySet()) {
				measuredToSimulatedIndex[i] = simulated.getColIndex(out);
				i++;
			}
		}
		Double [] SSESs = new Double [sses.length];
		for (int i = 0; i < sses.length; i++) {
			SSESs[i]= sses[i].evaluate(simulated.getCol(measuredToSimulatedIndex[i]));
		}

		return SSESs;
	}

}

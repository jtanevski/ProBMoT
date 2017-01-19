package fit.objective;

import java.util.Map.Entry;

import com.google.common.collect.*;

import temp.*;

public class WSSELObjectiveFunction
		extends TrajectoryObjectiveFunction {

	WSSEL[] sses;
	int[] measuredToSimulatedIndex;
	BiMap<String, String> outsToCols;


	public WSSELObjectiveFunction(Dataset measured, BiMap<String, String> outsToCols,BiMap<String, String> weightsToCols) {
		sses = new WSSEL[outsToCols.size()];
		int i = 0;
		for (String out : outsToCols.keySet()) {
			double[] weights = new double [measured.getCol(measured.getColIndex(weightsToCols.get(out))).length];
			
			for (int j=0;j<weights.length;j++)
				weights[j]=measured.getCol(measured.getColIndex(weightsToCols.get(out)))[j];
			
			sses[i] = new WSSEL(measured.getCol(measured.getColIndex(outsToCols.get(out))),weights);
			i++;
		}

		this.outsToCols = outsToCols;

		this.name = "WSSEL";
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

package fit.objective;

import java.util.Map.Entry;

import com.google.common.collect.*;

import temp.*;

public class WillmotObjectiveFunction
		extends TrajectoryObjectiveFunction {

	Willmot[] d;
	int[] measuredToSimulatedIndex;
	BiMap<String, String> outsToCols;


	public WillmotObjectiveFunction(Dataset measured, BiMap<String, String> outsToCols) {
		d = new Willmot[outsToCols.size()];
		int i = 0;
		for (Entry<String, String>  out : outsToCols.entrySet()) {
			d[i] = new Willmot(measured.getCol(measured.getColIndex(outsToCols.get(out.getValue()))));
			i++;
		}

		this.outsToCols = outsToCols;

		this.name = "WillmotObjectiveFunction";
	}
	@Override
	public double evaluateTrajectory(Dataset simulated) {
		if (measuredToSimulatedIndex == null) {
			measuredToSimulatedIndex = new int[d.length];
			int i = 0;
			for (Entry<String, String>  out : outsToCols.entrySet()) {
				measuredToSimulatedIndex[i] = simulated.getColIndex(out.getValue());
				i++;
			}
		}
		double totalD = 0;
		for (int i = 0; i < d.length; i++) {
			totalD += d[i].evaluate(simulated.getCol(measuredToSimulatedIndex[i]));
		}

		return totalD;
	}
	@Override
	public Double[] evaluateTrajectoryByPoint(Dataset simulated) {
		if (measuredToSimulatedIndex == null) {
			measuredToSimulatedIndex = new int[d.length];
			int i = 0;
			for (Entry<String, String>  out : outsToCols.entrySet()) {
				measuredToSimulatedIndex[i] = simulated.getColIndex(out.getValue());
				i++;
			}
		}
		Double [] Ds = new Double [d.length];
		for (int i = 0; i < d.length; i++) {
			Ds[i]= d[i].evaluate(simulated.getCol(measuredToSimulatedIndex[i]));
		}

		return Ds;
	}
}

package fit.objective;

import java.util.Map.Entry;

import com.google.common.collect.*;

import temp.*;

public class GeneralNashSutcliffeObjectiveFunction
		extends TrajectoryObjectiveFunction {

	GeneralNashSutcliffe[] ns;
	int[] measuredToSimulatedIndex;
	BiMap<String, String> outsToCols;


	public GeneralNashSutcliffeObjectiveFunction(Dataset measured, BiMap<String, String> outsToCols,int pow) {
		ns = new GeneralNashSutcliffe[outsToCols.size()];
		int i = 0;
		for (Entry<String, String>  out : outsToCols.entrySet()) {
			ns[i] = new GeneralNashSutcliffe(pow,measured.getCol(measured.getColIndex(outsToCols.get(out.getValue()))));
			i++;
		}

		this.outsToCols = outsToCols;

		this.name = "GeneralNashSutcliffeObjectiveFunction";
	}
	@Override
	public double evaluateTrajectory(Dataset simulated) {
		if (measuredToSimulatedIndex == null) {
			measuredToSimulatedIndex = new int[ns.length];
			int i = 0;
			for (Entry<String, String>  out : outsToCols.entrySet()) {
				measuredToSimulatedIndex[i] = simulated.getColIndex(out.getValue());
				i++;
			}
		}
		double totalNS = 0;
		for (int i = 0; i < ns.length; i++) {
			totalNS += ns[i].evaluate(simulated.getCol(measuredToSimulatedIndex[i]));
		}

		return totalNS;
	}
	@Override
	public Double[] evaluateTrajectoryByPoint(Dataset simulated) {
		if (measuredToSimulatedIndex == null) {
			measuredToSimulatedIndex = new int[ns.length];
			int i = 0;
			for (Entry<String, String>  out : outsToCols.entrySet()) {
				measuredToSimulatedIndex[i] = simulated.getColIndex(out.getValue());
				i++;
			}
		}
		Double [] NSs = new Double [ns.length];
		for (int i = 0; i < ns.length; i++) {
			NSs[i]= ns[i].evaluate(simulated.getCol(measuredToSimulatedIndex[i]));
		}

		return NSs;
	}
}

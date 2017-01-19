package fit;

import util.*;

public class BledModel implements ODEFunction {
	public int evals;

	public long tParams;
	public long tExogenous;
	public long tState;
	public long tAlgebraic;
	public long tDifferential;
	public long tPrime;
	public long tTotal;

	public void yprime (double[] dimensions, double[] parameters, double[] exogenous, double[] state, double[] prime){
		evals++;

		long t1 = System.nanoTime();
		// Parameters
		double BledModel_daph_maxFiltrationRate = parameters[0];
		double BledModel_env_depth = parameters[1];
		double BledModel_sedimenatation_sedimentationRate = parameters[2];
		double BledModel_respirationPP813_respRate = parameters[3];
		double BledModel_phyto_maxGrowthRate = parameters[4];
		double BledModel_tempGrowthInfluence268_minTemp = parameters[5];
		double BledModel_tempGrowthInfluence268_refTemp = parameters[6];
		double BledModel_phytoLim526_halfSaturation = parameters[7];
		double BledModel_tempSedInfluence195_minTemp = parameters[8];
		double BledModel_tempSedInfluence195_refTemp = parameters[9];
		double BledModel_tempRespInfluence23_minTemp = parameters[10];
		double BledModel_tempRespInfluence23_refTemp = parameters[11];
		double BledModel_lightInfluence691_optLight = parameters[12];
		double BledModel_nutrientInfluence652_saturationRate = parameters[13];
		double BledModel_nutrientInfluence563_saturationRate = parameters[14];
		double BledModel_no_halfSaturation = parameters[15];
		double BledModel_tempGrowthInfluence557_theta = parameters[16];
		double BledModel_tempGrowthInfluence557_refTemp = parameters[17];

		long t2 = System.nanoTime();
		// Exogenous
		double BledModel_daph_conc = exogenous[0];
		double BledModel_env_temperature = exogenous[1];
		double BledModel_env_light = exogenous[2];
		double BledModel_ortp_conc = exogenous[3];
		double BledModel_silica_conc = exogenous[4];
		double BledModel_no_conc = exogenous[5];

		long t3 = System.nanoTime();
		// State
		double BledModel_phyto_conc = state[0];

		long t4 = System.nanoTime();
		// Algebraic
		double BledModel_daph_tempGrowthLim = ((BledModel_env_temperature - BledModel_tempGrowthInfluence268_minTemp) / (BledModel_tempGrowthInfluence268_refTemp - BledModel_tempGrowthInfluence268_minTemp));
		double BledModel_phyto_tempSedLim = ((BledModel_env_temperature - BledModel_tempSedInfluence195_minTemp) / (BledModel_tempSedInfluence195_refTemp - BledModel_tempSedInfluence195_minTemp));
		double BledModel_phyto_tempRespLim = ((BledModel_env_temperature - BledModel_tempRespInfluence23_minTemp) / (BledModel_tempRespInfluence23_refTemp - BledModel_tempRespInfluence23_minTemp));
		double BledModel_phyto_lightLim = ((BledModel_env_light * Math.exp((((- BledModel_env_light) / BledModel_lightInfluence691_optLight) + 1.0000))) / BledModel_lightInfluence691_optLight);
		double BledModel_phyto_nutrientLim = (((1.0000 - Math.exp(((- BledModel_nutrientInfluence652_saturationRate) * BledModel_ortp_conc))) * (1.0000 - Math.exp(((- BledModel_nutrientInfluence563_saturationRate) * BledModel_silica_conc)))) * (BledModel_no_conc / (BledModel_no_conc + BledModel_no_halfSaturation)));
		double BledModel_phyto_tempGrowthLim = Math.pow(BledModel_tempGrowthInfluence557_theta, (BledModel_env_temperature - BledModel_tempGrowthInfluence557_refTemp));
		double BledModel_daph_phytoSum = BledModel_phyto_conc;
		double BledModel_phyto_growthRate = (((BledModel_phyto_maxGrowthRate * BledModel_phyto_tempGrowthLim) * BledModel_phyto_lightLim) * BledModel_phyto_nutrientLim);
		double BledModel_daph_phytoLim = (BledModel_daph_phytoSum / (BledModel_phytoLim526_halfSaturation + BledModel_daph_phytoSum));

		long t5 = System.nanoTime();
		// Differential
		double BledModel_phyto_conc_dot = ((((BledModel_phyto_growthRate * BledModel_phyto_conc) + (((((- BledModel_daph_maxFiltrationRate) * BledModel_daph_tempGrowthLim) * BledModel_daph_conc) * BledModel_phyto_conc) * BledModel_daph_phytoLim)) + (((- (BledModel_sedimenatation_sedimentationRate / BledModel_env_depth)) * BledModel_phyto_conc) * BledModel_phyto_tempSedLim)) + (((- BledModel_respirationPP813_respRate) * BledModel_phyto_conc) * BledModel_phyto_tempRespLim));

		long t6 = System.nanoTime();
		// Prime
		prime[0] = BledModel_phyto_conc_dot;

		long t7 = System.nanoTime();
		this.tParams += t2 - t1;
		this.tExogenous += t3 - t2;
		this.tState += t4 - t3;
		this.tAlgebraic += t5 - t4;
		this.tDifferential += t6 - t5;
		this.tPrime += t7 - t6;
		this.tTotal += t7 - t1;
	}
	public long getTotalTime() {
		return this.tTotal;
	}
}
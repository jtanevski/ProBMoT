package fit;

import temp.*;
import util.*;

public class Output11 implements OutputFunction {


	public void output (double[] dimensions, double[] modelParameters, double[] exogenous, double[] state, double[] outputParameters, double[] output){
//		long t1 = System.nanoTime();
//		// Parameters
//		double KasumigauraModel_mortality_mortRate = parameters[0];
//		double KasumigauraModel_env_depth = parameters[1];
//		double KasumigauraModel_sedimenatation_sedimentationRate = parameters[2];
//		double KasumigauraModel_respirationPP347_respRate = parameters[3];
//		double KasumigauraModel_phyto_maxGrowthRate = parameters[4];
//		double KasumigauraModel_tempRespInfluence334_refTemp = parameters[5];
//		double KasumigauraModel_lightInfluence690_optLight = parameters[6];
//		double KasumigauraModel_phosphorus_halfSaturation = parameters[7];
//		double KasumigauraModel_nutrientInfluence132_saturationRate = parameters[8];
//		double KasumigauraModel_nitrogen_halfSaturation = parameters[9];
//		double KasumigauraModel_tempGrowthInfluence263_refTemp = parameters[10];
//
//		long t2 = System.nanoTime();
//		// Exogenous
//		double KasumigauraModel_env_temperature = exogenous[0];
//		double KasumigauraModel_env_light = exogenous[1];
//		double KasumigauraModel_phosphorus_conc = exogenous[2];
//		double KasumigauraModel_silica_conc = exogenous[3];
//		double KasumigauraModel_nitrogen_conc = exogenous[4];
//
//		long t3 = System.nanoTime();
//		// State
//		double KasumigauraModel_phyto_conc = state[0];
//
//		long t4 = System.nanoTime();
//		// Algebraic
//		double KasumigauraModel_phyto_tempSedLim = 1.00000;
//		double KasumigauraModel_phyto_tempRespLim = (KasumigauraModel_env_temperature / KasumigauraModel_tempRespInfluence334_refTemp);
//		double KasumigauraModel_phyto_lightLim = ((KasumigauraModel_env_light * Math.exp((((- KasumigauraModel_env_light) / KasumigauraModel_lightInfluence690_optLight) + 1.00000))) / KasumigauraModel_lightInfluence690_optLight);
//		double KasumigauraModel_phyto_nutrientLim = ((((KasumigauraModel_phosphorus_conc * KasumigauraModel_phosphorus_conc) / ((KasumigauraModel_phosphorus_conc * KasumigauraModel_phosphorus_conc) + KasumigauraModel_phosphorus_halfSaturation)) * (1.00000 - Math.exp(((- KasumigauraModel_nutrientInfluence132_saturationRate) * KasumigauraModel_silica_conc)))) * (KasumigauraModel_nitrogen_conc / (KasumigauraModel_nitrogen_conc + KasumigauraModel_nitrogen_halfSaturation)));
//		double KasumigauraModel_phyto_tempGrowthLim = (KasumigauraModel_env_temperature / KasumigauraModel_tempGrowthInfluence263_refTemp);
//		double KasumigauraModel_phyto_growthRate = (((KasumigauraModel_phyto_maxGrowthRate * KasumigauraModel_phyto_tempGrowthLim) * KasumigauraModel_phyto_lightLim) * KasumigauraModel_phyto_nutrientLim);
//
//		long t5 = System.nanoTime();
//		// Differential
//		double KasumigauraModel_phyto_conc_dot = ((((KasumigauraModel_phyto_growthRate * KasumigauraModel_phyto_conc) + (((- KasumigauraModel_mortality_mortRate) * KasumigauraModel_phyto_conc) * KasumigauraModel_phyto_conc)) + (((- (KasumigauraModel_sedimenatation_sedimentationRate / KasumigauraModel_env_depth)) * KasumigauraModel_phyto_conc) * KasumigauraModel_phyto_tempSedLim)) + ((((- KasumigauraModel_respirationPP347_respRate) * KasumigauraModel_phyto_conc) * KasumigauraModel_phyto_conc) * KasumigauraModel_phyto_tempRespLim));
//
//		long t6 = System.nanoTime();
//		// Prime
//		prime[0] = KasumigauraModel_phyto_conc_dot;
//
//		long t7 = System.nanoTime();
//		this.tParams += t2 - t1;
//		this.tExogenous += t3 - t2;
//		this.tState += t4 - t3;
//		this.tAlgebraic += t5 - t4;
//		this.tDifferential += t6 - t5;
//		this.tPrime += t7 - t6;
//		this.tTotal += t7 - t1;

	}

	public ListMap<String, Double> yprime (ListMap<String, Double> dimensions, ListMap<String, Double> parameters, ListMap<String, Double> exogenous, ListMap<String, Double> state){
		long t1 = System.nanoTime();
		// Parameters
		Double KasumigauraModel_mortality_mortRate = parameters.get("KasumigauraModel.mortality.mortRate");
		Double KasumigauraModel_env_depth = parameters.get("KasumigauraModel.env.depth");
		Double KasumigauraModel_sedimenatation_sedimentationRate = parameters.get("KasumigauraModel.sedimenatation.sedimentationRate");
		Double KasumigauraModel_respirationPP347_respRate = parameters.get("KasumigauraModel.respirationPP347.respRate");
		Double KasumigauraModel_phyto_maxGrowthRate = parameters.get("KasumigauraModel.phyto.maxGrowthRate");
		Double KasumigauraModel_tempRespInfluence334_refTemp = parameters.get("KasumigauraModel.tempRespInfluence334.refTemp");
		Double KasumigauraModel_lightInfluence690_optLight = parameters.get("KasumigauraModel.lightInfluence690.optLight");
		Double KasumigauraModel_phosphorus_halfSaturation = parameters.get("KasumigauraModel.phosphorus.halfSaturation");
		Double KasumigauraModel_nutrientInfluence132_saturationRate = parameters.get("KasumigauraModel.nutrientInfluence132.saturationRate");
		Double KasumigauraModel_nitrogen_halfSaturation = parameters.get("KasumigauraModel.nitrogen.halfSaturation");
		Double KasumigauraModel_tempGrowthInfluence263_refTemp = parameters.get("KasumigauraModel.tempGrowthInfluence263.refTemp");

		long t2 = System.nanoTime();
		// Exogenous
		Double KasumigauraModel_env_temperature = exogenous.get("KasumigauraModel.env.temperature");
		Double KasumigauraModel_env_light = exogenous.get("KasumigauraModel.env.light");
		Double KasumigauraModel_phosphorus_conc = exogenous.get("KasumigauraModel.phosphorus.conc");
		Double KasumigauraModel_silica_conc = exogenous.get("KasumigauraModel.silica.conc");
		Double KasumigauraModel_nitrogen_conc = exogenous.get("KasumigauraModel.nitrogen.conc");

		long t3 = System.nanoTime();
		// State
		Double KasumigauraModel_phyto_conc = state.get("KasumigauraModel.phyto.conc");

		long t4 = System.nanoTime();
		// Algebraic
		Double KasumigauraModel_phyto_tempSedLim = 1.00000;
		Double KasumigauraModel_phyto_tempRespLim = (KasumigauraModel_env_temperature / KasumigauraModel_tempRespInfluence334_refTemp);
		Double KasumigauraModel_phyto_lightLim = ((KasumigauraModel_env_light * Math.exp((((- KasumigauraModel_env_light) / KasumigauraModel_lightInfluence690_optLight) + 1.00000))) / KasumigauraModel_lightInfluence690_optLight);
		Double KasumigauraModel_phyto_nutrientLim = ((((KasumigauraModel_phosphorus_conc * KasumigauraModel_phosphorus_conc) / ((KasumigauraModel_phosphorus_conc * KasumigauraModel_phosphorus_conc) + KasumigauraModel_phosphorus_halfSaturation)) * (1.00000 - Math.exp(((- KasumigauraModel_nutrientInfluence132_saturationRate) * KasumigauraModel_silica_conc)))) * (KasumigauraModel_nitrogen_conc / (KasumigauraModel_nitrogen_conc + KasumigauraModel_nitrogen_halfSaturation)));
		Double KasumigauraModel_phyto_tempGrowthLim = (KasumigauraModel_env_temperature / KasumigauraModel_tempGrowthInfluence263_refTemp);
		Double KasumigauraModel_phyto_growthRate = (((KasumigauraModel_phyto_maxGrowthRate * KasumigauraModel_phyto_tempGrowthLim) * KasumigauraModel_phyto_lightLim) * KasumigauraModel_phyto_nutrientLim);

		long t5 = System.nanoTime();
		// Differential
		Double KasumigauraModel_phyto_conc_dot = ((((KasumigauraModel_phyto_growthRate * KasumigauraModel_phyto_conc) + (((- KasumigauraModel_mortality_mortRate) * KasumigauraModel_phyto_conc) * KasumigauraModel_phyto_conc)) + (((- (KasumigauraModel_sedimenatation_sedimentationRate / KasumigauraModel_env_depth)) * KasumigauraModel_phyto_conc) * KasumigauraModel_phyto_tempSedLim)) + ((((- KasumigauraModel_respirationPP347_respRate) * KasumigauraModel_phyto_conc) * KasumigauraModel_phyto_conc) * KasumigauraModel_phyto_tempRespLim));

		long t6 = System.nanoTime();
		// Prime
		ListMap<String, Double> prime = new ListMap<String, Double>();
		prime.put("KasumigauraModel.phyto.conc", KasumigauraModel_phyto_conc_dot);

		long t7 = System.nanoTime();
		return prime;
	}



}
package fit;

import temp.*;
import util.*;

public class BledOutput implements OutputFunction {
	public void output(double[] dimensions, double[] modelParameters, double[] exogenous, double[] state, double[] outputParameters, double[] output) {

		// Parameters
		double BledModel_daph_maxFiltrationRate = modelParameters[0];
		double BledModel_env_depth = modelParameters[1];
		double BledModel_sedimenatation_sedimentationRate = modelParameters[2];
		double BledModel_respirationPP813_respRate = modelParameters[3];
		double BledModel_phyto_maxGrowthRate = modelParameters[4];
		double BledModel_tempGrowthInfluence268_minTemp = modelParameters[5];
		double BledModel_tempGrowthInfluence268_refTemp = modelParameters[6];
		double BledModel_phytoLim526_halfSaturation = modelParameters[7];
		double BledModel_tempSedInfluence195_minTemp = modelParameters[8];
		double BledModel_tempSedInfluence195_refTemp = modelParameters[9];
		double BledModel_tempRespInfluence23_minTemp = modelParameters[10];
		double BledModel_tempRespInfluence23_refTemp = modelParameters[11];
		double BledModel_lightInfluence691_optLight = modelParameters[12];
		double BledModel_nutrientInfluence652_saturationRate = modelParameters[13];
		double BledModel_nutrientInfluence563_saturationRate = modelParameters[14];
		double BledModel_no_halfSaturation = modelParameters[15];
		double BledModel_tempGrowthInfluence557_theta = modelParameters[16];
		double BledModel_tempGrowthInfluence557_refTemp = modelParameters[17];

		// Exogenous
		double BledModel_daph_conc = exogenous[0];
		double BledModel_env_temperature = exogenous[1];
		double BledModel_env_light = exogenous[2];
		double BledModel_ortp_conc = exogenous[3];
		double BledModel_silica_conc = exogenous[4];
		double BledModel_no_conc = exogenous[5];

		// State
		double BledModel_phyto_conc = state[0];

		double offset1 = outputParameters[0];
		double scale1 = outputParameters[1];
		double offset2 = outputParameters[2];
		double scale2 = outputParameters[3];

		double phyto_conc = BledModel_phyto_conc * scale1 + offset1;
		double daph_conc = BledModel_daph_conc * scale2 + offset2;
		double ortp_conc = BledModel_ortp_conc;

		output[0] = phyto_conc;
		output[1] = daph_conc;
		output[2] = ortp_conc;

	}



}
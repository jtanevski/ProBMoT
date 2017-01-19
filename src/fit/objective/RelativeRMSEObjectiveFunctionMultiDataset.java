package fit.objective;

import java.util.ArrayList;
import java.util.List;

import temp.Dataset;

import com.google.common.collect.BiMap;

public class RelativeRMSEObjectiveFunctionMultiDataset extends TrajectoryObjectiveFunction {
	
	List<RelativeRMSEObjectiveFunction> nrmseobj;
	
	public RelativeRMSEObjectiveFunctionMultiDataset(List<Dataset> measured, BiMap<String, String> outsToCols) {
		// TODO Auto-generated constructor stub
		nrmseobj = new ArrayList<RelativeRMSEObjectiveFunction>();
		
		for(Dataset d: measured){
			nrmseobj.add(new RelativeRMSEObjectiveFunction(d, outsToCols));
		}
		
		this.name = "RelativeRMSEMultiDataset";
		
	}
	
	@Override
	public double evaluateTrajectory(Dataset simulated) {
		//double total = 0.0;
					
		return evaluateTrajectory(simulated,0);
	}

	@Override
	public double evaluateTrajectory(Dataset simulated, int ndataset) {
		return nrmseobj.get(ndataset).evaluateTrajectory(simulated);
	}

	@Override
	public Double[] evaluateTrajectoryByPoint(Dataset simulated) {
		return evaluateTrajectoryByPoint(simulated,0);
	}
	@Override
	public Double[] evaluateTrajectoryByPoint(Dataset simulated,int ndataset) {
		return nrmseobj.get(ndataset).evaluateTrajectoryByPoint(simulated);
	}

}

package fit.objective;

import java.util.ArrayList;
import java.util.List;

import temp.Dataset;

import com.google.common.collect.BiMap;

public class RelativeWRMSEObjectiveFunctionMultiDataset extends TrajectoryObjectiveFunction {
	
	List<RelativeWRMSEObjectiveFunction> nwrmseobj;
	
	public RelativeWRMSEObjectiveFunctionMultiDataset(List<Dataset> measured, BiMap<String, String> outsToCols, BiMap<String, String> weightsToCols) {
		// TODO Auto-generated constructor stub
		nwrmseobj = new ArrayList<RelativeWRMSEObjectiveFunction>();
		
		for(Dataset d: measured){
			nwrmseobj.add(new RelativeWRMSEObjectiveFunction(d, outsToCols, weightsToCols));
		}
		
		this.name = "RelativeWRMSEMultiDataset";
		
	}
	
	@Override
	public double evaluateTrajectory(Dataset simulated) {
		//double total = 0.0;
					
		return evaluateTrajectory(simulated,0);
	}

	@Override
	public double evaluateTrajectory(Dataset simulated, int ndataset) {
		return nwrmseobj.get(ndataset).evaluateTrajectory(simulated);
	}

	@Override
	public Double[] evaluateTrajectoryByPoint(Dataset simulated) {
		return evaluateTrajectoryByPoint(simulated,0);
	}
	@Override
	public Double[] evaluateTrajectoryByPoint(Dataset simulated,int ndataset) {
		return nwrmseobj.get(ndataset).evaluateTrajectoryByPoint(simulated);
	}

}

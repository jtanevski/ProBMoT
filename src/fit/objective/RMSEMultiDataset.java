package fit.objective;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.BiMap;

import temp.Dataset;

public class RMSEMultiDataset extends TrajectoryObjectiveFunction{

	
	List<RMSEObjectiveFunction> rmseobj;
	
	public RMSEMultiDataset(List<Dataset> measured, BiMap<String, String> outsToCols) {
		rmseobj = new ArrayList<RMSEObjectiveFunction>();
		
		for(Dataset d: measured){
			rmseobj.add(new RMSEObjectiveFunction(d, outsToCols));
		}
		
		this.name = "RMSEMultiDataset";
		
	}
	
	@Override
	public double evaluateTrajectory(Dataset simulated) {
		//double total = 0.0;
					
		return evaluateTrajectory(simulated,0);
	}

	@Override
	public double evaluateTrajectory(Dataset simulated, int ndataset) {
		return rmseobj.get(ndataset).evaluateTrajectory(simulated);
	}

	@Override
	public Double[] evaluateTrajectoryByPoint(Dataset simulated) {
		return evaluateTrajectoryByPoint(simulated,0); 
	}
	@Override
	public Double[] evaluateTrajectoryByPoint(Dataset simulated,int ndataset) {
		return rmseobj.get(ndataset).evaluateTrajectoryByPoint(simulated);
	}
	
	

}

package fit.objective;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.BiMap;

import temp.Dataset;

public class WRMSEMultiDataset extends TrajectoryObjectiveFunction{

	
	List<WRMSEObjectiveFunction> rmseobj;
	
	public WRMSEMultiDataset(List<Dataset> measured, BiMap<String, String> outsToCols,List<double []> weights) {
		rmseobj = new ArrayList<WRMSEObjectiveFunction>();
		
		//for(Dataset d: measured){
		for (int i=0;i<measured.size();i++){
			rmseobj.add(new WRMSEObjectiveFunction(measured.get(i), outsToCols,weights.get(i)));
		}
		
		this.name = "WRMSEMultiDataset";
		
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
	public Double[] evaluateTrajectoryByPoint(Dataset simulated, int ndataset) {
		return rmseobj.get(ndataset).evaluateTrajectoryByPoint(simulated);
	}
	

}

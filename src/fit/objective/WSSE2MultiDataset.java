package fit.objective;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.BiMap;

import temp.Dataset;

public class WSSE2MultiDataset extends TrajectoryObjectiveFunction{

	
	List<WSSE2ObjectiveFunction> sseobj;
	
	public WSSE2MultiDataset(List<Dataset> measured, BiMap<String, String> outsToCols,BiMap<String,String> weightsToCols) {
		sseobj = new ArrayList<WSSE2ObjectiveFunction>();
		
		//for(Dataset d: measured){
		for (int i=0;i<measured.size();i++){
			sseobj.add(new WSSE2ObjectiveFunction(measured.get(i), outsToCols,weightsToCols));
		}
		
		this.name = "WSSE2MultiDataset";
		
	}
	
	@Override
	public double evaluateTrajectory(Dataset simulated) {
		//double total = 0.0;
					
		return evaluateTrajectory(simulated,0);
	}

	@Override
	public double evaluateTrajectory(Dataset simulated, int ndataset) {
		return sseobj.get(ndataset).evaluateTrajectory(simulated);
	}

	@Override
	public Double[] evaluateTrajectoryByPoint(Dataset simulated) {
		return evaluateTrajectoryByPoint(simulated,0);
	}
	@Override
	public Double[] evaluateTrajectoryByPoint(Dataset simulated, int ndataset) {
		return sseobj.get(ndataset).evaluateTrajectoryByPoint(simulated);
	}
	

}

package search.heuristic;

import java.io.Serializable;
import java.util.Arrays;

import jmetal.core.Variable;
import jmetal.util.JMException;
import temp.ExtendedModel;

public class PlateauModel implements Comparable<PlateauModel>,  Serializable{
	Integer[] structure;
	ExtendedModel eModel;
	
	public PlateauModel(Variable[] structure, ExtendedModel eModel) {
	
		this.structure = new Integer[structure.length]; 
		for(int i=0 ; i<structure.length; i++) {
			try {
				this.structure[i] = (int)(structure[i].getValue());
			} catch (JMException e) {
				System.out.println("Cannot convert double to int structure.");
			}
					
		}
		this.eModel = eModel;
	}
	@Override
	public boolean equals(Object p) {
		return Arrays.equals(this.structure, ((PlateauModel)p).structure);
	}
	
	@Override
	public int compareTo(PlateauModel p) {
		Double e1f = this.eModel.getFitnessMeasures().values().iterator().next();
		Double e2f = p.eModel.getFitnessMeasures().values().iterator().next();
		int comp = e1f.compareTo(e2f);
        return comp != 0? comp : 1;
	}

}

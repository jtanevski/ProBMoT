package search.heuristic;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import jmetal.core.Variable;
import jmetal.util.JMException;

public class PlateauModelLite implements Comparable<PlateauModelLite>, Serializable {


	private static final long serialVersionUID = -1813030489579543703L;

	protected Integer[] structure;
	
	protected Map<String, Double> initials;
	protected Map<String, Double> params;
	protected Map<String, Double> outputConsts;
	
	double error;
	
	public PlateauModelLite(Variable[] structure, Map<String,Double> initials, Map<String,Double> params, Map<String,Double> outputConsts, double error) {
	
		this.structure = new Integer[structure.length]; 
		for(int i=0 ; i<structure.length; i++) {
			try {
				this.structure[i] = (int)(structure[i].getValue());
			} catch (JMException e) {
				System.out.println("Cannot convert double to int structure.");
			}
		}
		this.initials = initials;
		this.params = params;
		this.outputConsts = outputConsts;
		this.error = error;
		
	}
	@Override
	public boolean equals(Object p) {
		return Arrays.equals(this.structure, ((PlateauModelLite)p).structure);
	}
	
	@Override
	public int compareTo(PlateauModelLite p) {
		Double e1f = this.error;
		Double e2f = p.error;
		int comp = e1f.compareTo(e2f);
        return comp != 0? comp : 1;
	}

}

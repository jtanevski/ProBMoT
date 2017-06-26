package temp;

import java.util.*;

import struct.inst.*;

/**
 * A model together annotated with some additional info
 *  - fitness measures
 *  - output parameters
 *  - initial values for all data sets
 * 
 * @author darko
 * 
 */
public class ExtendedModel {
	private Model model;
	
	private Map<String, Double> fitnessMeasures = new LinkedHashMap<String, Double>();
	private Map<String, Double> evalMeasures = new LinkedHashMap<String, Double>();
	private Map<String, Double> validMeasures = new LinkedHashMap<String, Double>();
	private Map<String, Double> outputConsts = new LinkedHashMap<String, Double>();
	private List<Dataset> evaluations = new ArrayList<Dataset>();
	private List<Dataset> validations = new ArrayList<Dataset>();
	private List<Dataset> simulations = new ArrayList<Dataset>();
	private List<Map<String, Double>> initials = new LinkedList<Map<String,Double>>();
	private boolean successful = true;
	private int modelNo;
	
	public ExtendedModel(Model model) {
		this.model = model;
	}
	
	public ExtendedModel copy() {
		Model model = (Model) this.model.copy();
		
		ExtendedModel copied = new ExtendedModel(model);
		return copied;
	}
	
	public ExtendedModel copyNoEQ() {
		Model model = this.model.copyNoEQ();
		
		ExtendedModel copied = new ExtendedModel(model);
		return copied;
	}
	
	public Model getModel() {
		return this.model;
	}
	
	public void setModel(Model model) {
		this.model = model;
	}
	
	public Map<String, Double> getFitnessMeasures() {
		return this.fitnessMeasures;
	}
	
	public void setFitnessMeasures(Map<String, Double> fitnessMeasures) {
		this.fitnessMeasures = fitnessMeasures;
	}
	
	public Map<String, Double> getOutputConstants() {
		return this.outputConsts;
	}
	
	public void setOutputConstants(Map<String, Double> outputConstants) {
		this.outputConsts = outputConstants;
	}
	
	public List<Map<String, Double>> getInitials() {
		return this.initials;
	}
	
	public void setInitials(List<Map<String, Double>> initials) {
		this.initials = initials;
	}
	
	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append(this.model.toString());
		
		for (Map.Entry<String, Double> entry : this.fitnessMeasures.entrySet()) {
			buf.append("//Train Error :" + entry.getKey() + " = " + entry.getValue() + "\n");
		}
		for (Map.Entry<String, Double> entry : this.validMeasures.entrySet()) {
			buf.append("//Validation Error  :" + entry.getKey() + " = " + entry.getValue() + "\n");
		}
		
		for (Map.Entry<String, Double> entry : this.evalMeasures.entrySet()) {
			buf.append("//Test Error  :" + entry.getKey() + " = " + entry.getValue() + "\n");
		}
		
		for (Map.Entry<String, Double> entry : this.outputConsts.entrySet()) {
			buf.append("// " + entry.getKey() + " = " + entry.getValue() + "\n");
		}
		for (int i = 0; i < this.initials.size(); i++) {
			Map<String, Double> map = this.initials.get(i);
			buf.append("// Dataset " + i + "\n");
			for (Map.Entry<String, Double> entry : map.entrySet()) {
				buf.append("// " + entry.getKey() + " = " + entry.getValue() + "\n");
			}
		}
		
		if (!this.successful)
			buf.append("\nSimulation Failed\n");
		
		return buf.toString();
	}

	public void setSuccessful(boolean successful) {
		this.successful = successful;
	}

	public boolean isSuccessful() {
		return successful;
	}

	public Map<String, Double> getEvalMeasures() {
		return evalMeasures;
	}

	public void setEvalMeasures(Map<String, Double> evalMeasures) {
		this.evalMeasures = evalMeasures;
	}
	
	public Map<String, Double> getValidMeasures() {
		return validMeasures;
	}

	public void setValidMeasures(Map<String, Double> validMeasures) {
		this.validMeasures = validMeasures;
	}

	public int getModelNo() {
		return modelNo;
	}

	public void setModelNo(int modelNo) {
		this.modelNo = modelNo;
	}

	public List<Dataset> getEvaluations() {
		return evaluations;
	}

	public List<Dataset> getValidations() {
		return validations;
	}
	
	public void setEvaluations(List<Dataset> evaluations) {
		this.evaluations = evaluations;
	}
	
	public void setValidations(List<Dataset> validations) {
		this.validations = validations;
	}

	public List<Dataset> getSimulations() {
		return simulations;
	}

	public void setSimulations(List<Dataset> simulations) {
		this.simulations = simulations;
	}
}
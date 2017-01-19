package xml;

import javax.xml.bind.annotation.*;

public class ModelVar {
	@XmlAttribute
	public String name;
	
	@XmlValue
	public String formula;
	
	private ModelVar() {}
	
	public ModelVar(String name, String formula) {
		this.name = name;
		this.formula = formula;
	}
	
	public String toString() {
		return this.name + " = " + this.formula;
	}
}
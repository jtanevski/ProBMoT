package xml;

import javax.xml.bind.annotation.*;

public class Parameter {
	@XmlAttribute
	public String name;
	
	@XmlAttribute
	public boolean fit;

	@XmlAttribute
	public Double value;

	@XmlAttribute
	public Double lower;

	@XmlAttribute
	public Double upper;

	@XmlAttribute
	public Double initial;

	private Parameter() {}

	public Parameter(String name, boolean fit, Double value, Double lower, Double upper, Double initial) {
		this.name = name;
		this.fit = fit;
		this.value = value;
		this.lower = lower;
		this.upper = upper;
		this.initial = initial;
	}

	public String toString() {
		return "(" + this.name + ", " + this.fit + ", " + this.value + ", " + this.lower + ", " + this.upper + ", " + this.initial + ")";
	}
}

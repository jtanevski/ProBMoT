package xml;

import javax.xml.bind.annotation.XmlElement;

public class SearchSpec {

	@XmlElement(defaultValue = "2b")
	public String level;
	
	@XmlElement(defaultValue = "3")
	public int particles;
	
	@XmlElement(defaultValue = "parameters")
	public String regularization;
	
	@XmlElement(defaultValue = "0.5")
	public double lambda;
	
	@XmlElement(defaultValue = "4")
	public int threads;
	
	@XmlElement(defaultValue = "10000")
	public int maxevaluations;
	
	@XmlElement(defaultValue = "0.1")
	public double plateau;
	
}

package xml;

import javax.xml.bind.annotation.XmlElement;

import org.eclipse.persistence.oxm.annotations.XmlDiscriminatorNode;

@XmlDiscriminatorNode("@method")
public abstract class EnsembleSpec {
	
	@XmlElement(defaultValue = "2")
	public int endIteration;
	@XmlElement(defaultValue = "0")
	public int startIteration;
	@XmlElement(defaultValue = "average")
	public String output;
	//average or weighted_median

}

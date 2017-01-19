package xml;

import javax.xml.bind.annotation.XmlElement;

import org.eclipse.persistence.oxm.annotations.*;

@XmlDiscriminatorNode("@method")
public abstract class EvalSpec {
	@XmlElement(defaultValue = "")
	public String train;
	@XmlElement(defaultValue = "")
	public String validation;
	@XmlElement(defaultValue = "")
	public String test;
}
package xml;

import javax.xml.bind.annotation.*;

import org.eclipse.persistence.oxm.annotations.*;

@XmlDiscriminatorValue("CVODE")
public class CVODESpec
		extends SimulatorSpec {

	@XmlElement
	public double abstol;

	@XmlElement
	public double reltol;
	
	@XmlElement
	public int steps;

}

package xml;

import javax.xml.bind.annotation.*;

import org.eclipse.persistence.oxm.annotations.*;

@XmlDiscriminatorValue("Boosting")
public class BoostingSpec
		extends EnsembleSpec {
	
	@XmlElement(defaultValue = "0.5")
	public double lossTreshold;
	
	@XmlElement(defaultValue = "linearLaw")
	public String lossFunction;
	//linearLaw, squareLaw, exponentialLaw

	

}
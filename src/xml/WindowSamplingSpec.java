package xml;

import javax.xml.bind.annotation.*;

import org.eclipse.persistence.oxm.annotations.*;

@XmlDiscriminatorValue("Bagging")
public class WindowSamplingSpec
		extends BaggingSpec {
	
		
	@XmlElement(defaultValue = "RandomPoint")
	public String sampling;
	//WindowGauss
	//WindowUniform
	@XmlElement(defaultValue = "11")
	public double windowsize;
	@XmlElement(defaultValue = "1.0")
	public double alpha;
	@XmlElement(defaultValue = "0.5")
	public double sigma;

}
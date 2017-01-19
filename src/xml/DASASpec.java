package xml;

import javax.xml.bind.annotation.*;

import org.eclipse.persistence.oxm.annotations.*;

@XmlDiscriminatorValue("DASA")
public class DASASpec
		extends FitterSpec {

	@XmlElement
	public int restarts;

	@XmlElement
	public int evaluations;

	@XmlElement
	public int ants;

	@XmlElement
	public double cauchyIncPer;

	@XmlElement
	public double cauchyDecPer;

	@XmlElement
	public double evap;

	@XmlElement
	public boolean normalize;

}
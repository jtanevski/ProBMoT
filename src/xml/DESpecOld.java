package xml;

import javax.xml.bind.annotation.*;

import org.eclipse.persistence.oxm.annotations.*;

@XmlDiscriminatorValue("DE-old")
public class DESpecOld
		extends FitterSpec {

	@XmlElement
	public int restarts;

	@XmlElement
	public int evaluations;

	@XmlElement
	public int population;

	@XmlElement
	public int strategy;

	@XmlElement
	public double F;

	@XmlElement
	public double Cr;

	@XmlElement
	public boolean normalize;

}
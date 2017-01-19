package xml;

import javax.xml.bind.annotation.*;

import org.eclipse.persistence.oxm.annotations.*;

@XmlDiscriminatorValue("ALG")
public class ALGSpec
		extends FitterSpec {

	@XmlElement
	public int restarts;

	@XmlElement
	public boolean normalize;
}
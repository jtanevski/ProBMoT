package xml;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.*;

import org.eclipse.persistence.oxm.annotations.*;

@XmlDiscriminatorValue("DE")
public class DESpec
		extends FitterSpec {

	@XmlElement
	public int restarts;

	@XmlElement
	public int evaluations;

	@XmlElement
	public int population;

	@XmlElement
	public DEStrategy strategy;

	@XmlElement
	public double F;

	@XmlElement
	public double Cr;
	
	@XmlElement(defaultValue="1")
	public int seed;
	
	@XmlElementWrapper(name = "objectives")
	@XmlElement(name = "obj")
	public List<String> objectives = new LinkedList<String>();
	
	@XmlElement(defaultValue="false")
	public boolean reg; 

}
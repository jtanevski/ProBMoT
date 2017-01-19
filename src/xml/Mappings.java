package xml;

import java.util.*;

import javax.xml.bind.annotation.*;

public class Mappings {
	
	@XmlElementWrapper(name = "dimensions")
	@XmlElement(name = "dim")
	public List<Mapping> dimensions = new LinkedList<Mapping>();
	
	@XmlElementWrapper(name = "endogenous")
	@XmlElement(name = "endo")
	public List<Mapping> endogenous = new LinkedList<Mapping>();
	
	@XmlElementWrapper(name = "exogenous")
	@XmlElement(name = "exo")
	public List<Mapping> exogenous = new LinkedList<Mapping>();

	@XmlElementWrapper(name = "outputs")
	@XmlElement(name = "out")
	public List<Mapping> outputs = new LinkedList<Mapping>();
}
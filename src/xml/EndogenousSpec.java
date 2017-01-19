package xml;

import java.util.*;

import javax.xml.bind.annotation.*;

public class EndogenousSpec {
	@XmlElementWrapper(name = "auxiliary")
	@XmlElement(name = "var")
	public List<ModelVar> auxiliary = new LinkedList<ModelVar>();
	
	@XmlElementWrapper(name = "state")
	@XmlElement(name = "var")
	public List<ModelVar> state = new LinkedList<ModelVar>();
}

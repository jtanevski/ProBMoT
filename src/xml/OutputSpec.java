package xml;

import java.util.*;

import javax.xml.bind.annotation.*;

public class OutputSpec {
	@XmlElementWrapper(name = "constants")
	@XmlElement(name = "cons")
	public List<Parameter> constants = new LinkedList<Parameter>();
	
	@XmlElementWrapper(name = "variables")
	@XmlElement(name = "var")
	public List<ModelVar> variables = new LinkedList<ModelVar>();

}

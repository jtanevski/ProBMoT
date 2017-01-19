package xml;

import java.util.*;

import javax.xml.bind.annotation.*;

import serialize.*;
import struct.inst.*;
import task.*;
import temp.*;

public class ProblemSpec {
	@XmlElementWrapper(name = "data")
	@XmlElement(name = "d")
	public List<DatasetSpec> data;

	@XmlElement
	public Mappings mappings;

	@XmlElement
	public Parameters parameters;

	@XmlElement
	public ModelSpec model;


	private ProblemSpec() {}

	public ProblemSpec(List<DatasetSpec> data, Mappings mappings, IQGraph graph, EquationSerializer serializer, List<ModelVar> output) {
		this.data = data;
		this.mappings = mappings;
		this.parameters = new Parameters(graph, serializer);
		this.model = new ModelSpec(graph, serializer, output);

	}
}
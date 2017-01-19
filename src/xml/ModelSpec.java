package xml;

import java.util.*;

import javax.xml.bind.annotation.*;

import serialize.*;
import struct.*;
import struct.inst.*;
import temp.*;

public class ModelSpec {
	@XmlElementWrapper(name = "exogenous")
	@XmlElement(name = "var")
	public List<ModelVar> exogenous = new LinkedList<ModelVar>();

	@XmlElement
	public EndogenousSpec endogenous;


	@XmlElementWrapper(name = "output")
	@XmlElement(name = "var")
	public List<ModelVar> output = new LinkedList<ModelVar>();


	private ModelSpec() {}

	public ModelSpec(IQGraph graph, EquationSerializer serializer, List<ModelVar> output) {
		List<IQNode> order = graph.diffOrder;

		List<IQ> algebraic = new LinkedList<IQ>();
		List<IQ> differential = new LinkedList<IQ>();
		for (IQNode iqNode : order) {
			if (iqNode.type==EQType.Algebraic) {
				algebraic.add(iqNode.iq);
			} else if (iqNode.type==EQType.Differential) {
				differential.add(iqNode.iq);
			} else {
				throw new IllegalStateException("invalid state");
			}
		}

		for (String varName : graph.diffExogenous.keySet()) {
			ModelVar exogenous = new ModelVar(serializer.serialize(graph.diffExogenous.get(varName).var),null);
			this.exogenous.add(exogenous);
		}

		endogenous = new EndogenousSpec();
		for (IQ iq : algebraic) {
			ModelVar auxiliary = new ModelVar(serializer.serialize(iq.getLHS()), serializer.serialize(iq.getRHS()));
			this.endogenous.auxiliary.add(auxiliary);
		}
		for (IQ iq : differential) {
			ModelVar state = new ModelVar(serializer.serialize(iq.getLHS()), serializer.serialize(iq.getRHS()));
			this.endogenous.state.add(state);
		}

		this.output = output;
	}

}

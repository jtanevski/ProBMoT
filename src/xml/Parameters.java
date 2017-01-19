package xml;

import java.util.*;

import javax.xml.bind.annotation.*;

import serialize.*;
import struct.inst.*;
import task.*;
import temp.*;

public class Parameters {
	@XmlElementWrapper(name = "initials")
	@XmlElement(name = "i")
	public List<Parameter> initials = new LinkedList<Parameter>();

	@XmlElementWrapper(name = "constants")
	@XmlElement(name = "c")
	public List<Parameter> constants = new LinkedList<Parameter>();



	private Parameters() {}

	public Parameters(IQGraph graph, EquationSerializer serializer) {
		for (String varName : graph.diffDifferential.keySet()) {
			IV var = graph.diffDifferential.get(varName).var;

			boolean fit;
			Double value = null;
			Double initial = null;
			Double lower = null;
			Double upper = null;

			if (var.initial != null) {
				fit = false;
				value = var.initial;
			} else {
				IIV iic = (IIV) var;

				fit = true;

				if (iic.fit_range!=null) {
					lower = iic.fit_range.getLower();
					upper = iic.fit_range.getUpper();
				} else {
					lower = iic.range.getLower();
					upper = iic.range.getUpper();
				}

				initial = iic.fit_start;
			}

			Parameter parameter = new Parameter(serializer.serialize(var), fit, value, lower, upper, initial);
			this.initials.add(parameter);
		}

		for (String consName : graph.diffParameters.keySet()) {
			IC cons = graph.diffParameters.get(consName).cons;

			boolean fit;
			Double value = null;
			Double initial = null;
			Double lower = null;
			Double upper = null;

			if (cons.value != null) {
				fit = false;
				value = cons.value;
			} else {
				IIC iic = (IIC) cons;

				fit = true;

				if (iic.fit_range!=null) {
					lower = iic.fit_range.getLower();
					upper = iic.fit_range.getUpper();
				} else {
					lower = iic.range.getLower();
					upper = iic.range.getUpper();
				}

				initial = iic.fit_start;
			}

			Parameter parameter = new Parameter(serializer.serialize(cons), fit, value, lower, upper, initial);
			this.constants.add(parameter);
		}
	}
}

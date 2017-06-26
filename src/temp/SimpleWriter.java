package temp;

import java.io.*;
import java.util.*;

import equation.Number;

import serialize.*;
import struct.inst.*;
import temp.IQGraph.Type;

public class SimpleWriter {
	Model model;
	EquationSerializer serializer;

	IQGraph graph;
	List<IQNode> order;

	String text;

	public SimpleWriter(Model model, EquationSerializer serializer) {
		this.model = model;
		this.serializer = serializer;

		this.graph = new IQGraph(model);
		graph.sort(Type.DIFFERENTIAL);

		//		this.order = graph.topologicalSort(graph.neededIQNodes, true);
		//		this.order = graph.topologicalSort(graph.iqNodes, false);
		this.order = graph.diffOrder;

		StringBuilder eqLines = new StringBuilder();
		eqLines.append("//Differential\n");
		for (int i = 0; i < order.size(); i++) {
			IQ iq = order.get(i).iq;
			eqLines.append(serializer.serialize(iq) + "\n");
		}
		this.text = eqLines.toString();

		this.order = graph.algOrder;

		eqLines.append("//Algebraic\n");
		for (int i = 0; i < order.size(); i++) {
			IQ iq = order.get(i).iq;
			eqLines.append(serializer.serialize(iq) + "\n");
		}
		this.text = eqLines.toString();
	}

	public SimpleWriter(IQGraph graph, EquationSerializer serializer) {
		this.model = graph.model;
		this.serializer = serializer;
		this.graph = graph;

		this.order = graph.diffOrder;

		StringBuilder eqLines = new StringBuilder();
		eqLines.append("//Differential\n");
		for (int i = 0; i < order.size(); i++) {
			IQ iq = order.get(i).iq;
			eqLines.append(serializer.serialize(iq) + "\n");
		}
		this.text = eqLines.toString();

		this.order = graph.algOrder;

		eqLines.append("//Algebraic\n");
		for (int i = 0; i < order.size(); i++) {
			IQ iq = order.get(i).iq;
			eqLines.append(serializer.serialize(iq) + "\n");
		}
		this.text = eqLines.toString();
	}

	public void writeToStream(PrintStream output) {
		output.print(text);
	}

	public static String serialize(Model model, EquationSerializer serializer) {
		IQGraph graph = new IQGraph(model);
		graph.sort(Type.ALL);


		StringBuilder paramLines = new StringBuilder();
		for (int i = 0; i < graph.diffParameters.size(); i++) {
			IC cons = graph.diffParameters.get(i).cons;
			paramLines.append(serializer.serialize(cons) + " = " + serializer.serialize(new Number(cons.value)) + "\n");
		}

		List<IQNode> diffOrder = graph.diffOrder;

		StringBuilder diffLines = new StringBuilder();
		for (int i = 0; i < diffOrder.size(); i++) {
			IQ iq = diffOrder.get(i).iq;
			diffLines.append(serializer.serialize(iq) + "\n");
		}

		List<IQNode> algOrder = graph.algOrder;

		StringBuilder algLines = new StringBuilder();
		for (int i = 0; i < algOrder.size(); i++) {
			IQ iq = algOrder.get(i).iq;
			algLines.append(serializer.serialize(iq) + "\n");
		}
		return paramLines.toString() + "\n" + diffLines.toString() + "\n" + algLines.toString();

	}
}

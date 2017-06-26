package temp;

import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;

import parser.*;
import struct.*;
import util.*;
import xml.*;

/**
 * This class represents the output of a Process-Based Model. It contains a number of output parameters which can be fitted if required. It also
 * contains a number of output variables with associated equations.
 *
 * @author Darko
 *
 */
public class Output {
	public ListMap<String, OutputVar> variables = new ListMap<String, OutputVar>();
	public ListMap<String, OutputCons> constants = new ListMap<String, OutputCons>();

	public ListMap<String, OutputCons> known = new ListMap<String, OutputCons>();
	public ListMap<String, OutputCons> fitted = new ListMap<String, OutputCons>();

	public ListMap<String, ICNode> neededCons = new ListMap<String, ICNode>();
	public ListMap<String, IVNode> neededVars = new ListMap<String, IVNode>();
	public ListMap<String, IVNode> neededExo = new ListMap<String, IVNode>();
	public ListMap<String, IVNode> neededEndo = new ListMap<String, IVNode>();

	public IQGraph graph;

	public Output(OutputSpec spec, IQGraph graph)
			throws RecognitionException {
		this.graph = graph;

		for (Parameter par : spec.constants) {
			OutputCons cons = new OutputCons();
			cons.name = par.name;
			if (!par.fit) {
				cons.value = par.value;
				known.put(cons.name, cons);
			} else {
				cons.value = null;
				cons.fit_start = par.initial;
				cons.fit_range = new Interval(par.lower, par.upper);
				fitted.put(cons.name, cons);
			}
			constants.put(cons.name, cons);
		}

		for (ModelVar var : spec.variables) {
			ANTLRStringStream inputStream;
			PBFLexer lexer;
			CommonTokenStream tokenStream;
			PBFParser parser;
			PBFParser.expression_return parserReturn;
			CommonTree parserTree;

			inputStream = new ANTLRStringStream(var.formula);
			lexer = new PBFLexer(inputStream);
			tokenStream = new CommonTokenStream();
			tokenStream.setTokenSource(lexer);

			parser = new PBFParser(tokenStream);
			parserReturn = parser.expression();

			parserTree = (CommonTree) parserReturn.getTree();

			//			System.out.println(parserTree.toStringTree());

			OutputVar out = new OutputVar(var.name, parserTree, graph, constants);
			variables.put(out.name, out);

			for (String consName : out.ieConsts.keySet()) {
				neededCons.put(consName, this.graph.allParameters.get(consName));
			}
			for (String consName : out.ipConsts.keySet()) {
				neededCons.put(consName, this.graph.allParameters.get(consName));
			}
			//			neededCons.putAll(out.ieConsts);
			//			neededCons.putAll(out.ipConsts);

			for (String varName : out.ieVarsExo.keySet()) {
				neededExo.put(varName, this.graph.allVariables.get(varName));
				neededVars.put(varName, this.graph.allVariables.get(varName));
			}
			for (String varName : out.ieVarsEndo.keySet()) {
				neededEndo.put(varName, this.graph.allVariables.get(varName));
				neededVars.put(varName, this.graph.allVariables.get(varName));
			}

			//			neededExo.putAll(out.ieVarsExo);
			//			neededEndo.putAll(out.ieVarsEndo);

			//			System.out.println(out);
		}

		graph.sort(this);
	}
}

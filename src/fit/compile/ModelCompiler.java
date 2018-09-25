package fit.compile;

import java.io.IOException;
import java.util.*;

import javax.tools.*;

import org.apache.commons.lang3.*;

import fit.*;

import serialize.*;
import struct.*;
import struct.inst.*;
import temp.*;
import temp.IQGraph.Type;

public class ModelCompiler {
	//	public static Model model;
	//	public static IQGraph graph;
	//	public static ODEFunction odeFunction;
	//	public static ODEModel odeModel;
	//	public static Dataset dataset;
	public static EquationSerializer serializer = new JavaSerializer();

	/**
	 * Generates Java source code for the given model
	 *
	 * @param model
	 * @return
	 */
	public static String modelToString(Model model, String className) {
		IQGraph graph = new IQGraph(model);
		graph.sort(Type.DIFFERENTIAL);
		return ModelCompiler.iqGraphToString(graph, className);
	}

	/**
	 * Generates Java source code for the given equation graph
	 *
	 * @param model
	 * @return
	 */
	public static String iqGraphToString(IQGraph graph, String className) {

		StringBuilder classSrc = new StringBuilder();
		StringBuilder yprimeSrc = new StringBuilder();
		classSrc.append("import fit.*;\n");
		classSrc.append("import util.*;\n\n");
		classSrc.append("public class " + className + " implements ODEFunction {\n");

		yprimeSrc.append("\tpublic void yprime (double[] dimensions, double[] parameters, double[] exogenous, double[] state, double[] prime){\n");

		yprimeSrc.append("\t\t// Parameters\n");
		int i = 0;
		for (ICNode icNode : graph.reachParameters.valueList()) {

			String icNameJava = serializer.serialize(icNode.cons);
			yprimeSrc.append("\t\tdouble " + icNameJava + " = parameters[" + i + "];\n");

			i++;
		}
		yprimeSrc.append("\n");

		yprimeSrc.append("double time=dimensions[0];");
		yprimeSrc.append("\t\t// Exogenous\n");
		i = 0;
		for (IVNode ivNode : graph.reachExogenous.valueList()) {
			String ivNameJava = serializer.serialize(ivNode.var);
			
			//quickfix
			if(!ivNameJava.equals("time"))
			yprimeSrc.append("\t\tdouble " + ivNameJava + " = exogenous[" + i + "];\n");

			i++;
		}
		yprimeSrc.append("\n");

		yprimeSrc.append("\t\t// State\n");
		i = 0;
		for (IVNode ivNode : graph.diffDifferential.valueList()) {
			String ivNameJava = serializer.serialize(ivNode.var);
			yprimeSrc.append("\t\tdouble " + ivNameJava + " = state[" + i + "];\n");

			i++;
		}
		yprimeSrc.append("\n");

		List<IQNode> order = graph.diffOrder;
		List<IQ> algebraic = new LinkedList<IQ>();
		List<IQ> differential = new LinkedList<IQ>();
		for (IQNode iqNode : order) {
			if (iqNode.type == EQType.Algebraic) {
				algebraic.add(iqNode.iq);
			} else if (iqNode.type == EQType.Differential) {
				differential.add(iqNode.iq);
			} else {
				throw new IllegalStateException("Invalid state");
			}
		}

		yprimeSrc.append("\t\t// Algebraic\n");
		for (IQ iq : algebraic) {
			yprimeSrc.append("\t\tdouble " + serializer.serialize(iq) + ";\n");
		}
		yprimeSrc.append("\n");

		yprimeSrc.append("\t\t// Differential\n");
		for (IQ iq : differential) {
			yprimeSrc.append("\t\tdouble " + serializer.serialize(iq) + ";\n");
		}
		yprimeSrc.append("\n");

		yprimeSrc.append("\t\t// Prime\n");
		i = 0;
		for (IVNode ivNode : graph.diffDifferential.valueList()) {
			String ivNameJava = serializer.serialize(ivNode.var, true);
			yprimeSrc.append("\t\tprime[" + i + "] = " + ivNameJava + ";\n");

			i++;
		}
		yprimeSrc.append("\n");

		yprimeSrc.append("\t}\n");

		classSrc.append(yprimeSrc);

		classSrc.append("public String toString() {\n");
		classSrc.append("	return \"" + StringEscapeUtils.escapeJava(yprimeSrc.toString()) + "\";\n");
		classSrc.append("}\n");

		classSrc.append("}\n");

		return classSrc.toString();

	}

	/**
	 * Generates an ODEFunction subclass and instance from the given source code
	 *
	 * @param src
	 * @param className
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws IOException 
	 */
	public static <T> T stringToFunction(String src, String className)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException {

		// We get an instance of JavaCompiler. Then we create a file manager (our custom implementation of it)
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		if (compiler == null) {
			throw new UnsupportedOperationException("No Java compiler in the current JRE");
		}

		JavaFileManager fileManager = new ClassFileManager(compiler.getStandardFileManager(null, null, null));

		// Dynamic compiling requires specifying a list of "files" to compile. In our case
		// this is a list containing one "file" which is in our case our own implementation (see details below)
		List<JavaFileObject> jfiles = new ArrayList<JavaFileObject>();
		jfiles.add(new CharSequenceJavaFileObject(className, src));

		// We specify a task to the compiler. Compiler should use our file manager and our list of "files".
		// Then we run the compilation with call()
		compiler.getTask(null, fileManager, null, null, null, jfiles).call();

		// Creating an instance of our compiled class and running its toString() method
		T instance;

		instance = (T) fileManager.getClassLoader(null).loadClass(className).newInstance();
		
		fileManager.close();

		return instance;
	}

	/**
	 * Generates ODEFunction class and instantiates it from the given equation graph
	 *
	 * @param model
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws IOException 
	 */
	public static ODEFunction iqGraphToODEFunction(IQGraph graph)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException {
		String className = generateClassName(ODEFunction.class);

		String src = ModelCompiler.iqGraphToString(graph, className);
		ODEFunction odeFunction = ModelCompiler.stringToFunction(src, className);

		return odeFunction;
	}

	/**
	 * Generates ODEFunction class and instantiates it from the given model
	 *
	 * @param model
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws IOException 
	 */
	public static ODEFunction modelToODEFunction(Model model)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException {
		IQGraph graph = new IQGraph(model);
		graph.sort(Type.DIFFERENTIAL);


		return ModelCompiler.iqGraphToODEFunction(graph);
	}

	/**
	 * Generates a new class name of the form ODEFunction_<uuid> where <uuid> is a UUID generated with java.util.UUID.randomUUID()
	 *
	 * @return
	 */
	public static String generateClassName(Class clazz) {
		String className = clazz.getSimpleName() + "_" + UUID.randomUUID().toString().replace("-", "_");
		return className;
	}


	public static String outputToString(Output output, String className) {
		IQGraph graph = output.graph;

		StringBuilder classSrc = new StringBuilder();
		StringBuilder outputSrc = new StringBuilder();

		classSrc.append("import fit.*;\n");
		classSrc.append("import util.*;\n\n");
		classSrc.append("public class " + className + " implements OutputFunction {\n");

		outputSrc.append("\tpublic void output(double[] dimensions, double[] modelParameters, double[] exogenous, double[] state, double[] outputParameters, double[] output) {\n");

		
		outputSrc.append("\t\t// Model Parameters\n");
		for (int i = 0; i < graph.reachParameters.size(); i++) {
			IC cons = graph.reachParameters.get(i).cons;
			String consName = serializer.serialize(cons);
			outputSrc.append("\t\tdouble " + consName + " = modelParameters[" + i + "];\n");
		}
		outputSrc.append("\n");

		
		outputSrc.append("double time=dimensions[0];");
		
		outputSrc.append("\t\t// Exogenous\n");
		for (int i = 0; i < graph.reachExogenous.size(); i++) {
			IV exo = graph.reachExogenous.get(i).var;
			String exoName = serializer.serialize(exo);
			
			if(!exoName.equals("time"))
			outputSrc.append("\t\tdouble " + exoName + " = exogenous[" + i + "];\n");
		}
		outputSrc.append("\n");

		outputSrc.append("\t\t// State\n");
		for (int i = 0; i < graph.diffDifferential.size(); i++) {
			IV endo = graph.diffDifferential.get(i).var;
			String endoName = serializer.serialize(endo);
			outputSrc.append("\t\tdouble " + endoName + " = state[" + i + "];\n");
		}
		outputSrc.append("\n");

		outputSrc.append("\t\t// Fitted Parameters\n");
		for (int i = 0; i < output.fitted.size(); i++) {
			OutputCons cons = output.fitted.get(i);
			outputSrc.append("\t\tdouble " + cons.name + " = outputParameters[" + i + "];\n");
		}
		outputSrc.append("\n");

		outputSrc.append("\t\t// Known Parameters\n");
		for (int i = 0; i < output.known.size(); i++) {
			OutputCons cons = output.known.get(i);
			outputSrc.append("\t\tdouble " + cons.name + " = " + cons.value + ";\n");
		}
		outputSrc.append("\n");

		outputSrc.append("\t\t// Algebraic equations\n");
		for (IQNode iqNode : graph.algOrder) {
			outputSrc.append("\t\tdouble " + serializer.serialize(iqNode.iq) + ";\n");
		}
		outputSrc.append("\n");

		outputSrc.append("\t\t// Output equations\n");
		for (OutputVar var : output.variables.valueList()) {
			outputSrc.append("\t\tdouble " + var.name + " = " + serializer.serialize(var.rhs) + ";\n");
		}
		outputSrc.append("\n");

		outputSrc.append("\t\t// Output\n");
		for (int i = 0; i < output.variables.size(); i++) {
			OutputVar var = output.variables.get(i);
			outputSrc.append("\t\toutput[" + i + "] = " + var.name + ";\n");
		}
		outputSrc.append("\n");

		outputSrc.append("\t}\n");

		classSrc.append(outputSrc);

		classSrc.append("\tpublic String toString() {\n");
		classSrc.append("\t\treturn \"" + StringEscapeUtils.escapeJava(outputSrc.toString()) + "\";\n");
		classSrc.append("}\n");

		classSrc.append("}\n");

		return classSrc.toString();
	}

	public static OutputFunction outputToFunction(Output output)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException {
		String className = generateClassName(OutputFunction.class);

		String src = ModelCompiler.outputToString(output, className);
		OutputFunction outputFunction = ModelCompiler.stringToFunction(src, className);

		return outputFunction;
	}
}
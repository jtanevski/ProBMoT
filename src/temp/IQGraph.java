package temp;

import java.util.*;

import struct.*;
import struct.inst.*;
import util.*;

import static struct.Role.*;

public class IQGraph {
	public static enum Type {
		ALL,
		OUTPUT,
		DIFFERENTIAL,
	}

	public Model model;
	public Type type;

	// All Nodes in the Model regardless of whether they are used or not
	/**
	 * All Variable Nodes in the Model
	 */
	public ListMap<String, IVNode> allVariables = new ListMap<String, IVNode>();
	/**
	 * All Exogenous Nodes in the Model
	 */
	public ListMap<String, IVNode> allExogenous = new ListMap<String, IVNode>();
	/**
	 * All Algebraic Nodes in the Model
	 */
	public ListMap<String, IVNode> allAlgebraic = new ListMap<String, IVNode>();
	/**
	 * All Differential Nodes in the Model
	 */
	public ListMap<String, IVNode> allDifferential = new ListMap<String, IVNode>();
	/**
	 * All Parameter Nodes in the Model
	 */
	public ListMap<String, ICNode> allParameters = new ListMap<String, ICNode>();
	/**
	 * All Equation Nodes in the Model
	 */
	public Map<Integer, IQNode> allEquations = new LinkedHashMap<Integer, IQNode>();
	/**
	 * Equation Nodes used in the model (Omitting equations for exogenous nodes)
	 */
	public Map<Integer, IQNode> usedEquations = new LinkedHashMap<Integer, IQNode>();

	// Nodes in the Model that are reachable from the starting set (usually output variables)
	/**
	 * Variable Nodes reachable from the start set
	 */
	public ListMap<String, IVNode> reachVariables = new ListMap<String, IVNode>();
	/**
	 * Exogenous Nodes reachable from the start set
	 */
	public ListMap<String, IVNode> reachExogenous = new ListMap<String, IVNode>();
	/**
	 * Algebraic Nodes reachable from the start set
	 */
	public ListMap<String, IVNode> reachAlgebraic = new ListMap<String, IVNode>();
	/**
	 * Differential Nodes reachable from the start set
	 */
	public ListMap<String, IVNode> reachDifferential = new ListMap<String, IVNode>();
	/**
	 * Parameter Nodes reachable from the start set
	 */
	public ListMap<String, ICNode> reachParameters = new ListMap<String, ICNode>();
	/**
	 * Equation Nodes reachable from the start set
	 */
	public Map<Integer, IQNode> reachEquations = new LinkedHashMap<Integer, IQNode>();

	// Nodes needed for the Differential Simulation
	/**
	 * Variable Nodes needed for Differential Simulation
	 */
	public ListMap<String, IVNode> diffVariables = new ListMap<String, IVNode>();
	/**
	 * Exogenous Nodes needed for Differential Simulation
	 */
	public ListMap<String, IVNode> diffExogenous = new ListMap<String, IVNode>();
	/**
	 * Algebraic Nodes needed for Differential Simulation
	 */
	public ListMap<String, IVNode> diffAlgebraic = new ListMap<String, IVNode>();
	/**
	 * Differential Nodes needed for Differential Simulation
	 */
	public ListMap<String, IVNode> diffDifferential = new ListMap<String, IVNode>();
	/**
	 * Parameters Nodes needed for Differential Simulation
	 */
	public ListMap<String, ICNode> diffParameters = new ListMap<String, ICNode>();
	/**
	 * Equation Nodes needed for the Differential Simulation
	 */
	public Map<Integer, IQNode> diffEquations = new LinkedHashMap<Integer, IQNode>();
	/**
	 * Equation Nodes needed for Differential Simulation (Topologically Sorted)
	 */
	public List<IQNode> diffOrder = new LinkedList<IQNode>();
	/**
	 * Starting set of Differential Nodes for the Topological Sort
	 */
	public ListMap<String, IVNode> diffStart = new ListMap<String, IVNode>();

	// Nodes needed for the Algebraic Simulation
	/**
	 * Variable Nodes needed for Algebraic Simulation
	 */
	public ListMap<String, IVNode> algVariables = new ListMap<String, IVNode>();
	/**
	 * Exogenous Nodes needed for Algebraic Simulation
	 */
	public ListMap<String, IVNode> algExogenous = new ListMap<String, IVNode>();
	/**
	 * Algebraic Nodes needed for Algebraic Simulation
	 */
	public ListMap<String, IVNode> algAlgebraic = new ListMap<String, IVNode>();
	/**
	 * Differential Nodes needed for Algebraic Simulation
	 */
	public ListMap<String, IVNode> algDifferential = new ListMap<String, IVNode>();
	/**
	 * Parameters Nodes needed for Algebraic Simulation
	 */
	public ListMap<String, ICNode> algParameters = new ListMap<String, ICNode>();
	/**
	 * Equation Nodes needed for the Algebraic Simulation
	 */
	public Map<Integer, IQNode> algEquations = new LinkedHashMap<Integer, IQNode>();
	/**
	 * Equation Nodes needed for Algebraic Simulation (Topologically Sorted)
	 */
	public List<IQNode> algOrder = new LinkedList<IQNode>();
	/**
	 * Starting set of Algebraic Nodes for the Topological Sort
	 */
	public ListMap<String, IVNode> algStart = new ListMap<String, IVNode>();

	// Needed Parameters for both phases of Simulation (Differential and Algebraic)
	/**
	 * Needed Parameter Nodes
	 */
	// Not needed because this information is kept in 'reachParameters'
//	public ListMap<String, ICNode> neededParameters = new ListMap<String, ICNode>();
	/**
	 * Known Parameter Nodes (from the Needed Parameters)
	 */
	public ListMap<String, ICNode> knownParameters = new ListMap<String, ICNode>();
	/**
	 * Unknown Parameter Nodes (from the Needed Parameters)
	 */
	public ListMap<String, ICNode> unknownParameters = new ListMap<String, ICNode>();

	// Starting Nodes for both phases of Simulation
	/**
	 * Starting Variable Nodes for the Simulations
	 */
	public ListMap<String, IVNode> startVariables = new ListMap<String, IVNode>();
	/**
	 * Starting Parameter Nodes for the Simulations
	 */
	public ListMap<String, ICNode> startParameters = new ListMap<String, ICNode>();

	public IQGraph(Model model) {
		this.model = model;

		buildAllGraph();
	}

	public void sort(Output output) {
		this.type = Type.OUTPUT;
		initializeStartOutput(output);
		topologicalSort();
		determineNeededParameters();

	}

	public void sort(Type type) {
		this.type = type;

		switch (type) {
			case DIFFERENTIAL:
				initializeStartDifferential();
				break;
			case ALL:
				initializeStartAll();
				break;
			case OUTPUT:
				throw new RuntimeException("Output not specified");
			default:
				throw new IllegalStateException("Invalid state");
		}

		topologicalSort();
		determineNeededParameters();
	}

	private void buildAllGraph() {
		buildIVtoIQGraph();
		buildAggregateEquations();
		buildIQtoIQGraph();
	}

	private void buildIVtoIQGraph() {
		for (IE ie : model.iesRec.values()) { // for each entity in the model
			for (IV iv : ie.vars.values()) { // for each variable in the entity
				IVNode ivNode = new IVNode(iv);
				allVariables.put(ivNode.id, ivNode);
				if (iv.role == EXOGENOUS)
					allExogenous.put(ivNode.id, ivNode);
			}

			for (IC ic : ie.consts.values()) { // for each constant in the entity
				ICNode icNode = new ICNode(ic);
				allParameters.put(icNode.id, icNode);
			}
		}

		for (IP ip : model.ipsRec.values()) { // for each process in the model
			for (IC ic : ip.consts.values()) { // for each constant in the process
				ICNode icNode = new ICNode(ic);
				allParameters.put(icNode.id, icNode);
			}

			for (IQ iq : ip.equations) { // for each equation in the process
				IQNode iqNode = new IQNode(iq);
				allEquations.put(iqNode.id, iqNode);
				if (iq.getLHS().role != EXOGENOUS)
					usedEquations.put(iqNode.id, iqNode);

				// Connect he LHS IV with the IQ
				String lhsName = iq.getLHS().getFullName();
				IVNode lhsNode = allVariables.get(lhsName);
				lhsNode.inputIQs.put(iqNode.id, iqNode);
				iqNode.outputVar = lhsNode;

				switch (iq.type) {
					case Algebraic:
						allAlgebraic.put(lhsName, lhsNode);
						break;
					case Differential:
						allDifferential.put(lhsName, lhsNode);
						break;
					default:
						throw new IllegalStateException("illeagal state");
				}

				// Connect the RHS IVs with the IQ
				for (String ivName : iq.ieVars.keySet()) { // for each variable on the RHS
					IVNode ivNode = allVariables.get(ivName);
					ivNode.outputIQs.put(iqNode.id, iqNode);
					iqNode.inputVariables.put(ivNode.id, ivNode);
				}
				// Connect the RHS ICs with the IQ
				for (String icName : iq.ieConsts.keySet()) { // for each entity constant on the RHS
					ICNode icNode = allParameters.get(icName);
					icNode.outputIQs.put(iqNode.id, iqNode);
					iqNode.inputParameters.put(icNode.id, icNode);
				}
				// Connect the RHS process constants with the IQ
				for (String icName : iq.ipConsts.keySet()) { // for each process constant on the RHS
					ICNode icNode = allParameters.get(icName);
					icNode.outputIQs.put(iqNode.id, iqNode);
					iqNode.inputParameters.put(icNode.id, icNode);
				}

			}
		}
	}

	private void buildAggregateEquations() {
		for (IVNode ivNode : allVariables.values()) { // for each variable
			if (ivNode.inputIQs.size() == 0) { // No equations
				ivNode.inputIQ = null;
			} else if (ivNode.inputIQs.size() == 1) { // One equation, no aggregation
				ivNode.inputIQ = ivNode.inputIQs.values().iterator().next();
			} else { // More than one equation. Aggregate them.
				IQNode aqNode = new IQNode();
				allEquations.put(aqNode.id, aqNode);
				if (ivNode.var.role != EXOGENOUS)
					usedEquations.put(aqNode.id, aqNode);

				Collection<IQ> iqs = new LinkedList<IQ>(); // collect all IQs in a list

				Set<String> algProcesses = new HashSet<String>();
				Set<String> diffProcesses = new HashSet<String>();

				for (IQNode iqNode : ivNode.inputIQs.values()) {
					switch (iqNode.type) {
						case Algebraic:
							algProcesses.add(iqNode.iq.cont.getFullName());
							break;
						case Differential:
							diffProcesses.add(iqNode.iq.cont.getFullName());
							break;
						default:
							throw new IllegalStateException("illeagal state");
					}

					iqs.add(iqNode.iq);
					allEquations.remove(iqNode.id);
					if (ivNode.var.role != EXOGENOUS)
						usedEquations.remove(iqNode.id);

					for (IVNode ivNode1 : iqNode.inputVariables.values()) { // for each Variable Node in the equation
						ivNode1.outputIQs.remove(iqNode.id);

						ivNode1.outputIQs.put(aqNode.id, aqNode);
						aqNode.inputVariables.put(ivNode1.id, ivNode1);
					}
					for (ICNode icNode1 : iqNode.inputParameters.values()) { // for each Parameter Node in the equation
						icNode1.outputIQs.remove(iqNode.id);

						icNode1.outputIQs.put(aqNode.id, aqNode);
						aqNode.inputParameters.put(icNode1.id, icNode1);
					}

				}

				if (algProcesses.size() > 0 && diffProcesses.size() > 0) {
					throw new ParsingException(String.format(Errors.ERRORS[63], ivNode.var.getFullName(), diffProcesses.toString(),
							algProcesses.toString()));
				}

				AQ aq = new AQ(iqs); // create a new AQ with the gathered list
				aqNode.setIQ(aq);

				ivNode.inputIQ = aqNode;
				aqNode.outputVar = ivNode;

			}
		}
	}

	private void buildIQtoIQGraph() {
		for (IVNode ivNode : this.allVariables.values()) {
			IQNode input = ivNode.inputIQ;
			if (input != null) {
				for (IQNode output : ivNode.outputIQs.values()) {
					if (input != output || input.type != EQType.Differential) {
						output.allInputIQs.put(input.id, input);
						input.allOutputIQs.put(output.id, output);

						if (input.type != EQType.Differential) {
							output.allInputIQsA.put(input.id, input);
							input.allOutputIQsA.put(output.id, output);
						}
					}
				}
				if (ivNode.var.role != EXOGENOUS) {
					for (IQNode output : ivNode.outputIQs.values()) {
						if (input != output || input.type != EQType.Differential) {
							output.usedInputIQs.put(input.id, input);
							input.usedOutputIQs.put(output.id, output);

							if (input.type != EQType.Differential) {
								output.usedInputIQsA.put(input.id, input);
								input.usedOutputIQsA.put(output.id, output);
							}

						}
					}
				}
			}
		}
	}

	private void initializeStartOutput(Output output) {
		this.startVariables = output.neededVars;
		this.startParameters = output.neededCons;
	}

	private void initializeStartAll() {
		this.startVariables = allVariables;
		this.startParameters = allParameters;
	}

	private void initializeStartDifferential() {
		this.startVariables = allDifferential;
		//this.startParameters is empty
	}

	/**
	 * Starting from a set of variables and parameters that are needed, determine the nodes needed for Algebraic and Differential simulation and
	 * compute the topological sort of all needed Differential and Algebraic equations
	 *
	 * @param variables
	 *           - variables that are of interest to us in the end
	 */

	private void computeReachabilityGraph() {
		Set<IQNode> traversed = new LinkedHashSet<IQNode>();
		Queue<IQNode> queue = new LinkedList<IQNode>();

		reachParameters.putAll(startParameters);
		reachVariables.putAll(startVariables);

		for (IVNode ivNode : startVariables.values()) { // initialize the queue with the equations for the starting variables
			if (ivNode.var.role == EXOGENOUS) {
				reachExogenous.put(ivNode.id, ivNode);
			} else {
				IQNode iqNode = ivNode.inputIQ;
				if (iqNode == null)
					throw new ParsingException(String.format(Errors.ERRORS[64], ivNode.var.getFullName()));

				switch (iqNode.type) {
					case Algebraic:
						reachAlgebraic.put(ivNode.id, ivNode);
						break;
					case Differential:
						reachDifferential.put(ivNode.id, ivNode);
						break;
					default:
						throw new IllegalStateException("illeagal state");
				}
				queue.add(iqNode);
			}
		}

		while (!queue.isEmpty()) {
			IQNode iqNode = queue.poll();
			if (!traversed.contains(iqNode)) {
				traversed.add(iqNode);
				reachEquations.put(iqNode.id, iqNode);

				reachParameters.putAll(iqNode.inputParameters);
				reachVariables.putAll(iqNode.inputVariables);

				for (IVNode ivNode1 : iqNode.inputVariables.values()) {
					if (ivNode1.var.role == EXOGENOUS) {
						reachExogenous.put(ivNode1.id, ivNode1);
					} else {
						IQNode iqNode1 = ivNode1.inputIQ;
						if (iqNode1 == null)
							throw new ParsingException(String.format(Errors.ERRORS[64], ivNode1.var.getFullName()));

						switch (iqNode1.type) {
							case Algebraic:
								reachAlgebraic.put(ivNode1.id, ivNode1);
								break;
							case Differential:
								reachDifferential.put(ivNode1.id, ivNode1);
								break;
							default:
								throw new IllegalStateException("illeagal state");
						}
						if (iqNode != iqNode1 || iqNode.type != EQType.Differential) {

							iqNode.reachInputIQs.put(iqNode1.id, iqNode1);
							iqNode1.reachOutputIQs.put(iqNode.id, iqNode);

							if (iqNode1.type != EQType.Differential) {
								iqNode.reachInputIQsA.put(iqNode1.id, iqNode1);
								iqNode1.reachOutputIQsA.put(iqNode.id, iqNode);
							}
						}
						queue.add(iqNode1);
					}
				}
			}
		}
	}

	private void computeDifferentialReachabilityGraph() {
		Set<IQNode> traversed = new LinkedHashSet<IQNode>();
		Queue<IQNode> queue = new LinkedList<IQNode>();

		for (IQNode iqNode : reachEquations.values()) { // find all equations with zero output-degree (not counting Diff-Diff links)
			if (iqNode.reachOutputIQsA.isEmpty())
				queue.add(iqNode);
		}

		while (!queue.isEmpty()) {
			IQNode iqNode = queue.poll();
			if (!traversed.contains(iqNode)) {
				traversed.add(iqNode);
				switch (iqNode.type) {
					case Differential:
						diffStart.put(iqNode.outputVar.id, iqNode.outputVar);
						break;
					case Algebraic:
						queue.addAll(iqNode.reachInputIQs.values());
						break;
				}
			}
		}

		traversed.clear();
		queue.clear();

		for (IVNode ivNode : diffStart.values()) {
			queue.add(ivNode.inputIQ);
		}

		diffVariables.putAll(diffStart);
		diffDifferential.putAll(diffStart);

		//		for (IVNode ivNode : diffStart.values()) { // initialize the queue with the equations for the starting differential variables
		//			if (ivNode.var.role == EXOGENOUS) {
		//				diffExogenous.put(ivNode.id, ivNode);
		//			} else {
		//				IQNode iqNode = ivNode.inputIQ;
		//				if (iqNode == null)
		//					throw new ParsingException(String.format(Errors.ERRORS[64], ivNode.var.getFullName()));
		//
		//				switch (iqNode.type) {
		//					case Algebraic:
		//						diffAlgebraic.put(ivNode.id, ivNode);
		//						break;
		//					case Differential:
		//						diffDifferential.put(ivNode.id, ivNode);
		//						break;
		//					default:
		//						throw new IllegalStateException("illeagal state");
		//				}
		//				queue.add(iqNode);
		//			}
		//		}

		while (!queue.isEmpty()) {
			IQNode iqNode = queue.poll();
			if (!traversed.contains(iqNode)) {
				traversed.add(iqNode);
				diffEquations.put(iqNode.id, iqNode);

				diffParameters.putAll(iqNode.inputParameters);
				diffVariables.putAll(iqNode.inputVariables);

				for (IVNode ivNode1 : iqNode.inputVariables.values()) {
					if (ivNode1.var.role == EXOGENOUS) {
						diffExogenous.put(ivNode1.id, ivNode1);
					} else {
						IQNode iqNode1 = ivNode1.inputIQ;
						if (iqNode1 == null)
							throw new ParsingException(String.format(Errors.ERRORS[64], ivNode1.var.getFullName()));

						switch (iqNode1.type) {
							case Algebraic:
								diffAlgebraic.put(ivNode1.id, ivNode1);
								break;
							case Differential:
								diffDifferential.put(ivNode1.id, ivNode1);
								break;
							default:
								throw new IllegalStateException("illeagal state");
						}

						if (iqNode != iqNode1 || iqNode.type != EQType.Differential) {

							iqNode.diffInputIQs.put(iqNode1.id, iqNode1);
							iqNode1.diffOutputIQs.put(iqNode.id, iqNode);

							if (iqNode1.type != EQType.Differential) {
								iqNode.diffInputIQsA.put(iqNode1.id, iqNode1);
								iqNode1.diffOutputIQsA.put(iqNode.id, iqNode);
							}
						}

						queue.add(iqNode1);
					}
				}
			}
		}
	}

	private void computeAlgebraicReachabilityGraph() {
		Set<IQNode> traversed = new LinkedHashSet<IQNode>();
		Queue<IQNode> queue = new LinkedList<IQNode>();

		for (IVNode ivNode : startVariables.values()) {
			if (ivNode.var.role != EXOGENOUS && ivNode.inputIQ.type == EQType.Algebraic) {
				algStart.put(ivNode.id, ivNode);
				queue.add(ivNode.inputIQ);
			}
		}
		algVariables.putAll(algStart);
		algAlgebraic.putAll(algStart);

		//		for (IVNode ivNode : algStart.values()) {		// initialize the queue with the equations for the starting algebraic variables
		//			if (ivNode.var.role == EXOGENOUS) {
		//				algExogenous.put(ivNode.id, ivNode);
		//			} else {
		//				IQNode iqNode = ivNode.inputIQ;
		//				if (iqNode == null)
		//					throw new ParsingException(String.format(Errors.ERRORS[64], ivNode.var.getFullName()));
		//
		//				switch (iqNode.type) {
		//					case Algebraic:
		//						algAlgebraic.put(ivNode.id, ivNode);
		//						queue.add(iqNode);
		//						break;
		//					case Differential:
		//						algDifferential.put(ivNode.id, ivNode);
		//						break;
		//					default:
		//						throw new IllegalStateException("illeagal state");
		//				}
		//
		//			}
		//		}

		while (!queue.isEmpty()) {
			IQNode iqNode = queue.poll();
			if (!traversed.contains(iqNode)) {
				traversed.add(iqNode);
				algEquations.put(iqNode.id, iqNode);

				algParameters.putAll(iqNode.inputParameters);
				algVariables.putAll(iqNode.inputVariables);

				for (IVNode ivNode1 : iqNode.inputVariables.values()) {
					if (ivNode1.var.role == EXOGENOUS) {
						algExogenous.put(ivNode1.id, ivNode1);
					} else {
						IQNode iqNode1 = ivNode1.inputIQ;
						if (iqNode1 == null)
							throw new ParsingException(String.format(Errors.ERRORS[64], ivNode1.var.getFullName()));

						switch (iqNode1.type) {
							case Algebraic:
								algAlgebraic.put(ivNode1.id, ivNode1);
								iqNode.algInputIQs.put(iqNode1.id, iqNode1);
								iqNode1.algOutputIQs.put(iqNode.id, iqNode);

								queue.add(iqNode1);
								break;
							case Differential:
								algDifferential.put(ivNode1.id, ivNode1);
								break;
							default:
								throw new IllegalStateException("illeagal state");
						}

					}
				}
			}
		}
	}

	private void topologicalSort() {
		computeReachabilityGraph();

		// Topological sort for Differential simulation
		computeDifferentialReachabilityGraph();

		Queue<IQNode> zeroDegree = new LinkedList<IQNode>();
		Map<Integer, Map<Integer, IQNode>> passedMap = new LinkedHashMap<Integer, Map<Integer, IQNode>>();
		Set<IQNode> passedSet = new LinkedHashSet<IQNode>();

		for (IQNode iqNode : diffEquations.values()) {
			if (iqNode.diffInputIQsA.isEmpty()) {
				zeroDegree.add(iqNode);
			}
		}
		while (!zeroDegree.isEmpty()) {
			IQNode iqNode = zeroDegree.poll();
			diffOrder.add(iqNode);
			passedSet.add(iqNode);
			for (IQNode iqNode1 : iqNode.diffOutputIQsA.values()) {
				Map<Integer, IQNode> passedNodes = passedMap.get(iqNode1.id);
				if (passedNodes == null) {
					passedNodes = new LinkedHashMap<Integer, IQNode>();
					passedMap.put(iqNode1.id, passedNodes);
				}
				passedNodes.put(iqNode.id, iqNode);

				if (passedNodes.size() == iqNode1.diffInputIQsA.size()) { // all input IQs are passed
					zeroDegree.add(iqNode1);
				}
			}
		}

		if (passedSet.size() != diffEquations.size()) {
			Set<IQNode> unpassed = new HashSet<IQNode> (diffEquations.values());
			unpassed.removeAll(passedSet);
			throw new ParsingException(String.format(Errors.ERRORS[61], unpassed));
		}

		// Topological sort for Algebraic simulation
		computeAlgebraicReachabilityGraph();

		zeroDegree = new LinkedList<IQNode>();
		passedMap = new LinkedHashMap<Integer, Map<Integer, IQNode>>();
		passedSet = new LinkedHashSet<IQNode>();

		for (IQNode iqNode : algEquations.values()) {
			if (iqNode.algInputIQs.isEmpty()) {
				zeroDegree.add(iqNode);
			}
		}
		while (!zeroDegree.isEmpty()) {
			IQNode iqNode = zeroDegree.poll();
			algOrder.add(iqNode);
			passedSet.add(iqNode);
			for (IQNode iqNode1 : iqNode.algOutputIQs.values()) {
				Map<Integer, IQNode> passedNodes = passedMap.get(iqNode1.id);
				if (passedNodes == null) {
					passedNodes = new LinkedHashMap<Integer, IQNode>();
					passedMap.put(iqNode1.id, passedNodes);
				}
				passedNodes.put(iqNode.id, iqNode);
				if (passedNodes.size() == iqNode1.algInputIQs.size()) { // all input IQs are passed
					zeroDegree.add(iqNode1);
				}
			}
		}

		if (passedSet.size() != algEquations.size()) {
			Set<IQNode> unpassed = new HashSet<IQNode> (algEquations.values());
			unpassed.removeAll(passedSet);
			throw new ParsingException(String.format(Errors.ERRORS[61], unpassed));
		}

	}

	private void determineNeededParameters() {
		for (ICNode icNode : this.reachParameters.values()) {
			if (icNode.cons.value == null) {
				this.unknownParameters.put(icNode.id, icNode);
			} else {
				this.knownParameters.put(icNode.id, icNode);
			}
		}
	}

}

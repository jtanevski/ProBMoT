package temp;

import java.util.*;

import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;
import org.apache.commons.lang3.*;

import parser.*;
import struct.inst.*;
import util.*;
import equation.*;
import equation.Number;

/**
 * This class represents an output variable. Output variables are variables that are built on top of the model. They are equations that can use all quantities of
 * the model: variables and constants (possibly dimensions in the future). In essence they have a structure just like IQs, but they are built without TQs
 * @author Darko
 *
 */
public class OutputVar {
	public Model model;
	CommonTree tree;
	ListMap<String, OutputCons> parameters;

	public String name;
	public Node rhs;

	public Map<String, IV> ieVarsExo = new LinkedHashMap<String, IV>();
	public Map<String, IV> ieVarsEndo = new LinkedHashMap<String, IV>();
	public Map<String, IC> ieConsts = new LinkedHashMap<String, IC>();
	public Map<String, IC> ipConsts = new LinkedHashMap<String, IC>();

	public Map<String, OutputCons> outConsts = new LinkedHashMap<String, OutputCons>();

	public OutputVar(String name, CommonTree tree, IQGraph graph, ListMap<String, OutputCons> parameters) {
		this.name = name;

		this.tree = tree;
		this.model = graph.model;
		this.parameters = parameters;

		build();
	}

	public void build() {
		rhs = buildExpr(tree);
	}

	public static int[] operatorTokens = { PBFFilter.PLUS, PBFFilter.MINUS, PBFFilter.STAR, PBFFilter.SLASH };
	public static int[] functionTokens = { PBFFilter.CALL };
	public static int[] referenceTokens = { PBFFilter.ID, PBFFilter.DOT };
	public static int[] numberTokens = { PBFFilter.INT_LITERAL, PBFFilter.DOUBLE_LITERAL };

	Node buildExpr(CommonTree tree) {
		Token tok = tree.getToken();

		if (ArrayUtils.contains(operatorTokens, tok.getType())) {
			return buildOperator(tree);
		} else if (ArrayUtils.contains(functionTokens, tok.getType())) {
			return buildFunction(tree);
		} else if (ArrayUtils.contains(referenceTokens, tok.getType())) {
			return buildReference(tree);
		} else if (ArrayUtils.contains(numberTokens, tok.getType())) {
			return buildNumber(tree);
		} else {
			throw new ParsingException(String.format(Errors.ERRORS[46], PBFFilter.tokenNames[tok.getType()]));
		}
	}

	private Node buildNumber(CommonTree tree) {
		return new Number(new Double(tree.token.getText()));
	}

	private Node buildReference(CommonTree referenceTree) {
		Token tok = referenceTree.token;
		int tokType = tok.getType();
		switch (tokType) {
			case PBFFilter.ID:
				return buildConstReference(referenceTree);
			case PBFFilter.DOT:
				return buildModelReference(referenceTree);
			default:
				throw new ParsingException(String.format(Errors.ERRORS[46], PBFFilter.tokenNames[tokType],
						Arrays.toString(new String[] { PBFFilter.tokenNames[PBFFilter.ID], PBFFilter.tokenNames[PBFFilter.DOT] })));
		}
	}

	private Node buildConstReference(CommonTree referenceTree) {
		String constName = referenceTree.token.getText();
		OutputCons cons = parameters.get(constName);
		if (cons == null) {
			throw new ParsingException(String.format(Errors.ERRORS[55], constName));
		} else {
			if (outConsts.containsKey(constName)) {

			} else {
				outConsts.put(constName, cons);
			}
			OutConsRef ref = new OutConsRef(cons);
			return ref;
		}
	}

	private Node buildModelReference(CommonTree referenceTree) {
		Node ref;											// to return

		List<String> ids = refTreeToList(referenceTree);

		AM container;
		// first ID is model ID
		if (!ids.get(0).equals(model.id)) {
			throw new ParsingException(String.format(Errors.ERRORS[72], ids.get(0), model.id));
		}
		container = this.model;

		// from second to third-to-last ID are compartment IDs
		for (int i = 1; i <= ids.size() - 3; i++) {
			IK containerNew = container.iks.get(ids.get(i));
			if (containerNew == null) {
				throw new ParsingException(String.format(Errors.ERRORS[73], ids.get(0), container.iks.values().toString()));
			}
			container=containerNew;
		}

		// Before-last is the entity/process ID [parentID]
		// Last is the variable/constant ID [id]
		String parentID = ids.get(ids.size() - 2);
		IE parentIE;
		IP parentIP;
		if ((parentIE = container.ies.get(parentID)) != null) {												// if it is within an entity
			String id = ids.get(ids.size() - 1);
			IV var;
			IC cons;
			if ((var = parentIE.vars.get(id)) != null) {														// if it is a variable
				ref = new AEVarRef(var);
				switch (var.role) {
					case ENDOGENOUS:
						this.ieVarsEndo.put(var.getFullName(), var);
						break;
					case EXOGENOUS:
						this.ieVarsExo.put(var.getFullName(), var);
						break;
					default:
						throw new RuntimeException("invalid state");
				}
			} else if ((cons = parentIE.consts.get(id)) != null) {											// if it is a constant
				ref = new AEConstRef(cons);
				this.ieConsts.put(cons.getFullName(), cons);
			} else {																										// if it doesn't exist
				throw new ParsingException(String.format(Errors.ERRORS[75], parentIE.id, id));
			}
		} else if ((parentIP = container.ips.get(parentID)) != null) {									// if it is within a process
			String id = ids.get(ids.size() - 1);
			IC cons = parentIP.consts.get(id);
			if (cons == null) {																						// if it doesn't exist
				throw new ParsingException(String.format(Errors.ERRORS[76], parentIP.id, id));
			}
			ref = new ICRef(cons);
			this.ipConsts.put(cons.getFullName(), cons);
		} else {																											// if it doesn't exist
			throw new ParsingException(String.format(Errors.ERRORS[74], container.getId(), parentID));
		}

		return ref;
	}

	/**
	 * Extracts from the tree that corresponds to a reference with many DOTs, the list of identifiers: a.b.c -> {a,b,c}
	 * @param refTree
	 * @return
	 */
	private List<String> refTreeToList(CommonTree refTree) {
		List<String> ids = new LinkedList<String>();

		CommonTree curTree = refTree;

		while (curTree != null) {
			Token tok = curTree.token;
			if (tok.getType() == PBFFilter.ID) {
				ids.add(0, tok.getText());

				curTree = null;
			} else if (tok.getType() == PBFFilter.DOT) {
				Token rToken = ((CommonTree)curTree.getChild(1)).token;

				if (rToken.getType() != PBFFilter.ID) {
					throw new ParsingException(String.format(Errors.ERRORS[46], PBFFilter.tokenNames[rToken.getType()], PBFFilter.tokenNames[PBFFilter.ID]));
				}

				ids.add(0,rToken.getText());
				curTree = (CommonTree)curTree.getChild(0);
			} else {
				throw new ParsingException(String.format(Errors.ERRORS[46], PBFFilter.tokenNames[tok.getType()], Arrays.toString(new String[] { PBFFilter.tokenNames[PBFFilter.ID], PBFFilter.tokenNames[PBFFilter.ITER] })));
			}
		}

		return ids;
	}

	private Node buildFunction(CommonTree functionTree) {
		Token funToken = ((CommonTree) functionTree.getChild(0)).token;
		String funName = funToken.getText();
		Node[] args = new Node[functionTree.getChildCount() - 1];
		for (int i = 1; i < functionTree.getChildCount(); i++) {
			args[i - 1] = buildExpr((CommonTree) functionTree.getChild(i));
		}
		int arity;
		if (funName.equals("sin")) {
			arity = 1;
			if (args.length != arity) {
				throw new ParsingException(String.format(Errors.ERRORS[54], funName, arity, args.length));
			}
			return new Sinus(args[0]);
		} else if (funName.equals("exp")) {
			arity = 1;
			if (args.length != arity) {
				throw new ParsingException(String.format(Errors.ERRORS[54], funName, arity, args.length));
			}
			return new Exponential(args[0]);

		} else if (funName.equals("pow")) {
			arity = 2;
			if (args.length != arity) {
				throw new ParsingException(String.format(Errors.ERRORS[54], funName, arity, args.length));
			}
			return new Power(args[0], args[1]);

		} else if (funName.equals("min")) {
			arity = 2;
			if (args.length != arity) {
				throw new ParsingException(String.format(Errors.ERRORS[54], funName, arity, args.length));
			}
			return new Minimum(args[0], args[1]);
		} else if (funName.equals("max")) {
			arity = 2;
			if (args.length != arity) {
				throw new ParsingException(String.format(Errors.ERRORS[54], funName, arity, args.length));
			}
			return new Maximum(args[0], args[1]);
		} else if (funName.equals("datamax")) {
			arity = 1;
			if (args.length != arity) {
				throw new ParsingException(String.format(Errors.ERRORS[54], funName, arity, args.length));
			}
			return new DataMax(args[0]);
		} else if (funName.equals("datamin")) {
			arity = 1;
			if (args.length != arity) {
				throw new ParsingException(String.format(Errors.ERRORS[54], funName, arity, args.length));
			}
			return new DataMin(args[0]);
		} else if (funName.equals("log")) {
			arity = 1;
			if (args.length != arity) {
				throw new ParsingException(String.format(Errors.ERRORS[54], funName, arity, args.length));
			}
			return new Logarithm(args[0]);

		} else if (funName.equals("sign")) {
			arity = 1;
			if (args.length != arity) {
				throw new ParsingException(String.format(Errors.ERRORS[54], funName, arity, args.length));
			}
			return new Sign(args[0]);

		} else if (funName.equals("log10")) {
			arity = 1;
			if (args.length != arity) {
				throw new ParsingException(String.format(Errors.ERRORS[54], funName, arity, args.length));
			}
			return new Logarithm10(args[0]);

		} else {
			throw new ParsingException(String.format(Errors.ERRORS[52], funName, Arrays.toString(new String[] {
					"sin", "exp", "pow", "log", "log10", "min", "max" })));
		}
	}

	private Node buildOperator(CommonTree operatorTree) {

		if (operatorTree.getChildCount() != 2) {
			if (operatorTree.token.getType() != PBFFilter.MINUS || operatorTree.getChildCount() != 1) {
				throw new ParsingException(String.format(Errors.ERRORS[51], operatorTree.token
						.getText(), 2, operatorTree.getChildCount()));
			}
		}
		CommonTree lTree = (CommonTree) operatorTree.getChild(0);
		CommonTree rTree = (CommonTree) operatorTree.getChild(1);

		int tokType = operatorTree.token.getType();

		switch (tokType) {
			case PBFFilter.PLUS:
				return new Plus(buildExpr(lTree), buildExpr(rTree));
			case PBFFilter.MINUS:
				if (operatorTree.getChildCount() == 1) {
					return new UMinus(buildExpr(lTree));
				} else {
					return new Minus(buildExpr(lTree), buildExpr(rTree));
				}
			case PBFFilter.STAR:
				return new Times(buildExpr(lTree), buildExpr(rTree));
			case PBFFilter.SLASH:
				return new Slash(buildExpr(lTree), buildExpr(rTree));
			default:
				throw new ParsingException(String.format(Errors.ERRORS[46],
						PBFFilter.tokenNames[operatorTree.token.getType()], Arrays.toString(new String[] {
								PBFFilter.tokenNames[PBFFilter.PLUS], PBFFilter.tokenNames[PBFFilter.MINUS],
								PBFFilter.tokenNames[PBFFilter.STAR], PBFFilter.tokenNames[PBFFilter.SLASH] })));

		}

	}


	public String toString() {
		return name + " = " + rhs.toString();
	}

}

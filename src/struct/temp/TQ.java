package struct.temp;

import java.util.*;

import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;
import org.apache.commons.lang3.*;

import parser.*;
import struct.*;
import util.*;
import equation.*;
import equation.Number;

/** Template Equation **/
public class TQ
		extends Template {

	/* ID */
	public String id;

	/* Container */
	public TP cont;

	/* Content */
	public EQType type;

	public PEVar lhs;
	public Node rhs;

	/* Reference */

	/* Back-reference */

	/* Other */
	public PE iterated; // The real PE which is iterated - ns in <n:ns>
	public PE iterator; // The artificial PE for iteration - n in <n:ns>
	public boolean isIter; // is iterated?

	public Map<String, PEVar> peVars = new LinkedHashMap<String, PEVar>();
	public Map<String, PEConst> peConsts = new LinkedHashMap<String, PEConst>();
	public Map<String, TC> tcs = new LinkedHashMap<String, TC>();

	CommonTree tree;

	public TQ(CommonTree tree, Structure container) {
		this.tree = tree;
		this.cont = (TP) container;

	}

	public void build() {

		if (tree.token.getType() != PBFFilter.EQ) {
			throw new ParsingException(tree.token, String.format(Errors.ERRORS[45], PBFFilter.tokenNames[tree.token
					.getType()], PBFFilter.tokenNames[PBFFilter.EQ]));
		}
		CommonTree lTree = (CommonTree) tree.getChild(0);
		CommonTree rTree = (CommonTree) tree.getChild(1);

		// Left-hand side
		buildLHS(lTree);

		// Right-hand side
		buildRHS(rTree);

	}

	void buildLHS(CommonTree lTree) {
		CommonTree dotTree;
		if (lTree.token.getType() == PBFFilter.CALL) { // Differential

			if (lTree.getChildCount() > 2) {
				throw new ParsingException(lTree.token, String.format(Errors.ERRORS[47]));
			}
			CommonTree llTree = (CommonTree) lTree.getChild(0);
			CommonTree lrTree = (CommonTree) lTree.getChild(1);

			if (llTree.token.getType() != PBFFilter.ID || !llTree.token.getText().toLowerCase().equals("td")) {
				throw new ParsingException(llTree.token, String.format(Errors.ERRORS[48], "td", llTree.token.getText()));
			}

			if (lrTree.token.getType() != PBFFilter.DOT) {
				throw new ParsingException(lrTree.token, String.format(Errors.ERRORS[45], PBFFilter.tokenNames[lrTree.token
						.getType()], "DOT"));
			}

			type = EQType.Differential;
			dotTree = lrTree;

		} else if (lTree.token.getType() == PBFFilter.DOT) { // Algebraic
			type = EQType.Algebraic;
			dotTree = lTree;
		} else {
			throw new ParsingException(lTree.token, String.format(Errors.ERRORS[46], PBFFilter.tokenNames[lTree.token
					.getType()], Arrays.toString(new String[] { PBFFilter.tokenNames[PBFFilter.CALL],
					PBFFilter.tokenNames[PBFFilter.DOT] })));
		}
		lhs = ((PEVarRef)buildPERef(dotTree, true)).getPEVar();

	}

	void buildRHS(CommonTree rTree) {
		rhs = buildExpr(rTree);
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
			throw new ParsingException(tok, String.format(Errors.ERRORS[46], PBFFilter.tokenNames[tok.getType()]));
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
				return buildConstRef(referenceTree);
			case PBFFilter.DOT:
				return buildPERef(referenceTree, false);
			default:
				throw new ParsingException(tok, String.format(Errors.ERRORS[46], PBFFilter.tokenNames[tokType], Arrays
						.toString(new String[] { PBFFilter.tokenNames[PBFFilter.ID], PBFFilter.tokenNames[PBFFilter.DOT] })));
		}

	}

	private Node buildPERef(CommonTree dotTree, boolean isLHS) {
		CommonTree lTree = (CommonTree) dotTree.getChild(0);
		CommonTree rTree = (CommonTree) dotTree.getChild(1);
		
		// left side of the dot
		
		PE pe;
		if (lTree.token.getType() == PBFFilter.ID) { 				// Normal variable
			String peName = lTree.token.getText();
			pe = cont.parameters.get(peName);
			if (pe == null) {
				if (isIter) {
					if (iterator.id.equals(peName)) {
						pe = iterator;
					}
				}
			}
			if (pe == null) {
				throw new ParsingException(lTree.token, String.format(Errors.ERRORS[49], peName));
			}

		} else if (lTree.token.getType() == PBFFilter.ITER) { 	// Iterated variable
			CommonTree llTree = (CommonTree) lTree.getChild(0);
			CommonTree lrTree = (CommonTree) lTree.getChild(1);

			String iteratorName = llTree.token.getText();
			String iteratedName = lrTree.token.getText();

			pe = cont.parameters.get(iteratedName);
			if (pe == null) {
				throw new ParsingException(lrTree.token, String.format(Errors.ERRORS[49], iteratedName));
			}
			iterated = pe;
			isIter = true;

			iterator = new PE();
			iterator.id = iteratorName;
			iterator.card = new Interval(1);
			iterator.te = iterated.te;
			iterator.tp = iterated.tp;
			iterator.idI = iterated.idI;

			pe = iterator;

		} else {
			throw new ParsingException(lTree.token, String.format(Errors.ERRORS[46], PBFFilter.tokenNames[lTree.token
					.getType()], Arrays.toString(new String[] { "ID", "ITER" })));
		}
		
		// right side of the dot
		
		if (rTree.token.getType() != PBFFilter.ID) {
			throw new ParsingException(rTree.token, String.format(Errors.ERRORS[45], PBFFilter.tokenNames[rTree.token
					.getType()], "ID"));
		}
		
		String varOrConstName = rTree.token.getText();

		TV var = pe.te.vars.get(varOrConstName);
		if (var != null) {													// if the ID refers to a variable
			String peVarName = pe.id + "." + var.id;
			PEVar peVar = peVars.get(peVarName);
			if (peVar == null) {
				peVar = new PEVar(pe, var);
				peVars.put(peVar.toString(), peVar);
			}
			PEVarRef varRef = new PEVarRef(peVar);
			return varRef;
		} else {																	// if the ID refers to a constant
			if (isLHS) {														// if this is the LHS expression, then it must be a variable
				throw new ParsingException(rTree.token, String.format(Errors.ERRORS[50], varOrConstName, pe.id));
			}
			
			TC cons = pe.te.consts.get(varOrConstName);
			if (cons != null) {
				String constName = pe.id + "." + cons.id;
				PEConst peConst = peConsts.get(constName);
				if (peConst == null) {
					peConst = new PEConst(pe, cons);
					peConsts.put(peConst.toString(), peConst);
				}
				PEConstRef constRef = new PEConstRef(peConst);
				return constRef;
			} else {
				throw new ParsingException(rTree.token, String.format(Errors.ERRORS[56], varOrConstName, pe.id));
			}
		}
	}

	private Node buildConstRef(CommonTree referenceTree) {
		String constName = referenceTree.token.getText();
		TC tc = cont.consts.get(constName);
		if (tc == null) {
			throw new ParsingException(referenceTree.token, String.format(Errors.ERRORS[55], constName));
		} else {
			if (tcs.containsKey(tc.id)) {
				
			} else {
				tcs.put(tc.id, tc);
			}
			TCRef tcRef = new TCRef(tc);
			return tcRef;
		}

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
				throw new ParsingException(funToken, String.format(Errors.ERRORS[54], funName, arity, args.length));
			}
			return new Sinus(args[0]);
		} else if (funName.equals("exp")) {
			arity = 1;
			if (args.length != arity) {
				throw new ParsingException(funToken, String.format(Errors.ERRORS[54], funName, arity, args.length));
			}
			return new Exponential(args[0]);

		} else if (funName.equals("pow")) {
			arity = 2;
			if (args.length != arity) {
				throw new ParsingException(funToken, String.format(Errors.ERRORS[54], funName, arity, args.length));
			}
			return new Power(args[0], args[1]);

		} else if (funName.equals("min")) {
			arity = 2;
			if (args.length != arity) {
				throw new ParsingException(funToken, String.format(Errors.ERRORS[54], funName, arity, args.length));
			}
			return new Minimum(args[0], args[1]);
		} else if (funName.equals("max")) {
			arity = 2;
			if (args.length != arity) {
				throw new ParsingException(funToken, String.format(Errors.ERRORS[54], funName, arity, args.length));
			}
			return new Maximum(args[0], args[1]);
		} else if (funName.equals("datamax")) {
			arity = 1;
			if (args.length != arity) {
				throw new ParsingException(funToken, String.format(Errors.ERRORS[54], funName, arity, args.length));
			}
			return new DataMax(args[0]);
		} else if (funName.equals("datamin")) {
			arity = 1;
			if (args.length != arity) {
				throw new ParsingException(funToken, String.format(Errors.ERRORS[54], funName, arity, args.length));
			}
			return new DataMin(args[0]);
		} else if (funName.equals("log")) {
			arity = 1;
			if (args.length != arity) {
				throw new ParsingException(funToken, String.format(Errors.ERRORS[54], funName, arity, args.length));
			}
			return new Logarithm(args[0]);

		} else if (funName.equals("sign")) {
			arity = 1;
			if (args.length != arity) {
				throw new ParsingException(funToken, String.format(Errors.ERRORS[54], funName, arity, args.length));
			}
			return new Sign(args[0]);

		} else if (funName.equals("log10")) {
			arity = 1;
			if (args.length != arity) {
				throw new ParsingException(funToken, String.format(Errors.ERRORS[54], funName, arity, args.length));
			}
			return new Logarithm10(args[0]);

		} else {
			throw new ParsingException(funToken, String.format(Errors.ERRORS[52], funName, Arrays.toString(new String[] {
					"sin", "exp", "pow", "log", "log10", "min", "max" })));
		}
	}

	private Node buildOperator(CommonTree operatorTree) {

		if (operatorTree.getChildCount() != 2) {
			if (operatorTree.token.getType() != PBFFilter.MINUS || operatorTree.getChildCount() != 1) {
				throw new ParsingException(operatorTree.token, String.format(Errors.ERRORS[51], operatorTree.token
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
				throw new ParsingException(operatorTree.token, String.format(Errors.ERRORS[46],
						PBFFilter.tokenNames[operatorTree.token.getType()], Arrays.toString(new String[] {
								PBFFilter.tokenNames[PBFFilter.PLUS], PBFFilter.tokenNames[PBFFilter.MINUS],
								PBFFilter.tokenNames[PBFFilter.STAR], PBFFilter.tokenNames[PBFFilter.SLASH] })));

		}

	}

/*
	void buildRVarRef(CommonTree dotTree) {
		CommonTree lTree = (CommonTree) dotTree.getChild(0);
		CommonTree rTree = (CommonTree) dotTree.getChild(1);
		PE pe;
		if (lTree.token.getType() == PBFFilter.ID) { // Normal variable
			String peName = lTree.token.getText();
			pe = cont.parameters.map.get(peName);
			if (pe == null) {
				throw new ParsingException(lTree.token, String.format(Errors.ERRORS[49], peName));
			}

		} else if (lTree.token.getType() == PBFFilter.ITER) { // Iterated variable
			CommonTree llTree = (CommonTree) lTree.getChild(0);
			CommonTree lrTree = (CommonTree) lTree.getChild(1);

			String iteratorName = llTree.token.getText();
			String iteratedName = lrTree.token.getText();

			pe = cont.parameters.map.get(iteratedName);
			if (pe == null) {
				throw new ParsingException(lrTree.token, String.format(Errors.ERRORS[49], iteratedName));
			}
			iterated = pe;
			isIter = true;

			iterator = new PE();
			iterator.id = iteratorName;
			iterator.card = new Interval(1);
			iterator.te = iterated.te;
			iterator.tp = iterated.tp;
			iterator.idI = iterated.idI;


			pe = iterator;

		} else {
			throw new ParsingException(lTree.token, String.format(Errors.ERRORS[46], PBFFilter.tokenNames[lTree.token
					.getType()], Arrays.toString(new String[] { "ID", "ITER" })));
		}

		if (rTree.token.getType() != PBFFilter.ID) {
			throw new ParsingException(rTree.token, String.format(Errors.ERRORS[45], PBFFilter.tokenNames[rTree.token
					.getType()], "ID"));
		}
		String varName = rTree.token.getText();
		TV var = pe.te.vars.get(varName);
		if (var == null) {
			throw new ParsingException(rTree.token, String.format(Errors.ERRORS[50], varName, pe.id));
		}

		lhs = new PEVar(pe, var);
	}
	
	*/

	@Override
	public TP getContainer() {
		return this.cont;
	}

	@Override
	public String getIdS() {
		return this.id;
	}

	@Override
	public void setContainer(Structure container) {
		this.cont = (TP) container;
	}

	@Override
	public void setIdS(String id) {
		this.id = (String) id;
	}

	public String toString() {
		String lhsString = (type == EQType.Differential) ? "td(" + lhs.toString() + ")" : lhs.toString();
		return lhsString + " = " + rhs.toString();
	}

}

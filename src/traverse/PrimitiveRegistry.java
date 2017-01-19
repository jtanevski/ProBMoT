package traverse;

import java.util.*;
import java.util.regex.*;

import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;

import parser.*;
import struct.*;
import struct.temp.*;
import util.*;

public class PrimitiveRegistry {
	public static final PrimitiveRegistry REGISTRY = new PrimitiveRegistry();

	Map<String, Primitive> primitives = new LinkedHashMap<String, Primitive>();

	public PrimitiveRegistry() {
		primitives.put("INTEGER", new PrimitiveInteger());
		primitives.put("SIGNED_INTEGER", new PrimitiveSignedInteger());
		primitives.put("DOUBLE", new PrimitiveDouble());
		primitives.put("SIGNED_DOUBLE", new PrimitiveSignedDouble());
		primitives.put("STRING", new PrimitiveString());
		primitives.put("INTER", new PrimitiveInterval());
		primitives.put("ROLE", new PrimitiveRole());
		primitives.put("AGGREGATION", new PrimitiveAggregation());
		primitives.put("TQ", new PrimitiveEquation());
	}
}

interface Primitive<T> {
	public T create(CommonTree tree, Structure container);
}

class PrimitiveInteger implements Primitive<Integer> {

	@Override
	public Integer create(CommonTree tree, Structure container) {
		if (tree.token.getType() == PBFFilter.NULL) {
			return null;
		}

		if (tree.token.getType() != PBFFilter.INT_LITERAL) {
			throw new ParsingException(tree.token, String.format(Errors.ERRORS[33], "integer",
					PBFFilter.tokenNames[tree.token.getType()]));
		}
		return new Integer(tree.token.getText());
	}
}

class PrimitiveDouble implements Primitive<Double> {

	@Override
	public Double create(CommonTree tree, Structure container) {
		if (tree.token.getType() == PBFFilter.NULL) {
			return null;
		}

		int type = tree.token.getType();
		if (type == PBFFilter.DOUBLE_LITERAL || type == PBFFilter.INT_LITERAL) {
			return new Double(tree.token.getText());
		} else if (type == PBFFilter.INF) {
			return Double.POSITIVE_INFINITY;
		} else {
			throw new ParsingException(tree.token, String.format(Errors.ERRORS[33], "dobule",
					PBFFilter.tokenNames[tree.token.getType()]));
		}
	}
}

class PrimitiveString implements Primitive<String> {

	@Override
	public String create(CommonTree tree, Structure container) {
		if (tree.token.getType() == PBFFilter.NULL) {
			return null;
		}

		if (tree.token.getType() != PBFFilter.STRING_LITERAL) {
			throw new ParsingException(tree.token, String.format(Errors.ERRORS[33], "string",
					PBFFilter.tokenNames[tree.token.getType()]));
		}
		String quoted = tree.token.getText();
		Matcher mat = Pattern.compile("\"([^\"]*)\"").matcher(quoted);
		if (!mat.matches()) {
			throw new ParsingException(tree.token, String.format(Errors.ERRORS[34], quoted));
		}
		return mat.group(1);
	}
}

class PrimitiveSignedInteger implements Primitive<Integer> {

	@Override
	public Integer create(CommonTree tree, Structure container) {
		if (tree.token.getType() == PBFFilter.NULL) {
			return null;
		}

		int tokType = tree.token.getType();
		switch (tokType) {
		case PBFFilter.PLUS:
		case PBFFilter.MINUS:
			if (tree.getChildCount() > 1) {
				throw new ParsingException(tree.token, String.format(Errors.ERRORS[35], "integer", tree.getChildCount()));
			}
			Token tok = ((CommonTree) tree.getChild(0)).token;
			if (tok.getType() != PBFFilter.INT_LITERAL) {
				throw new ParsingException(tok.getLine(), tok.getCharPositionInLine(), String.format(Errors.ERRORS[33],
						"integer", PBFFilter.tokenNames[tok.getType()]));
			}
			return new Integer(((tokType == PBFFilter.MINUS) ? "-" : "+") + tok.getText());
		case PBFFilter.INT_LITERAL:
			return new Integer(tree.token.getText());
		default:
			throw new ParsingException(tree.token, String.format(Errors.ERRORS[33], "signed integer",
					PBFFilter.tokenNames[tokType]));
		}
	}
}

class PrimitiveSignedDouble implements Primitive<Double> {

	@Override
	public Double create(CommonTree tree, Structure container) {
		if (tree.token.getType() == PBFFilter.NULL) {
			return null;
		}

		int tokType = tree.token.getType();
		switch (tokType) {
		case PBFFilter.PLUS:
		case PBFFilter.MINUS:
			if (tree.getChildCount() > 1) {
				throw new ParsingException(tree.token, String.format(Errors.ERRORS[35], "double", tree.getChildCount()));
			}
			Token tok = ((CommonTree) tree.getChild(0)).token;
			double val;
			if (tok.getType() == PBFFilter.DOUBLE_LITERAL || tok.getType() == PBFFilter.INT_LITERAL) {
				val = new Double(tok.getText());
			} else if (tok.getType() == PBFFilter.INF) {
				val = Double.POSITIVE_INFINITY;
			} else {
				throw new ParsingException(tok, String.format(Errors.ERRORS[33], "double",
						PBFFilter.tokenNames[tok.getType()]));
			}
			return new Double((tokType == PBFFilter.MINUS) ? (-val) : (val));
		case PBFFilter.INT_LITERAL:
		case PBFFilter.DOUBLE_LITERAL:
			return new Double(tree.token.getText());
		case PBFFilter.INF:
			return Double.POSITIVE_INFINITY;
		default:
			throw new ParsingException(tree.token, String.format(Errors.ERRORS[33], "signed double",
					PBFFilter.tokenNames[tokType]));
		}
	}
}


/**
 * A class for representing data for fitting a constant. Can be a single number,
 * in which case, it is the value of the constant, or it can be a whole
 * structure with information such as starting value for the fit and interval in
 * which the value should be fitted. It can also be a null value, in which case
 * it represents a missing value, which should be fitted in some default way.
 * 
 * @author darko
 * 
 */
/*
class PrimitiveSignedDoubleFit implements Primitive<Double> {

	@Override
	public Double create(CommonTree tree, Structure container) {

		int tokType = tree.token.getType();
		if (tokType == PBFFilter.NULL) { // null value
			return null;
		} else if (tokType == PBFFilter.STRUCT) { // structure with fit data
			return null;
		} else {
			switch (tokType) {
			case PBFFilter.PLUS:
			case PBFFilter.MINUS:
				if (tree.getChildCount() > 1) {
					throw new ParsingException(tree.token, String.format(Errors.ERRORS[35], "double", tree.getChildCount()));
				}
				Token tok = ((CommonTree) tree.getChild(0)).token;
				double val;
				if (tok.getType() == PBFFilter.DOUBLE_LITERAL || tok.getType() == PBFFilter.INT_LITERAL) {
					val = new Double(tok.getText());
				} else if (tok.getType() == PBFFilter.INF) {
					val = Double.POSITIVE_INFINITY;
				} else {
					throw new ParsingException(tok, String.format(Errors.ERRORS[33], "double",
							PBFFilter.tokenNames[tok.getType()]));
				}
				return new Double((tokType == PBFFilter.MINUS) ? (-val) : (val));
			case PBFFilter.INT_LITERAL:
			case PBFFilter.DOUBLE_LITERAL:
				return new Double(tree.token.getText());
			case PBFFilter.INF:
				return Double.POSITIVE_INFINITY;
			default:
				throw new ParsingException(tree.token, String.format(Errors.ERRORS[33], "signed double",
						PBFFilter.tokenNames[tokType]));
			}
		}
	}
}
*/

class PrimitiveInterval implements Primitive<Interval> {

	@Override
	public Interval create(CommonTree tree, Structure container) {
		if (tree.token.getType() == PBFFilter.NULL) {
			return null;
		}

		if (tree.token.getType() != PBFFilter.INTER) {
			throw new ParsingException(tree.token, String.format(Errors.ERRORS[33], "interval",
					PBFParser.tokenNames[tree.token.getType()]));
		}
		if (tree.getChildCount() < 1 || tree.getChildCount() > 2) {
			throw new ParsingException(tree.token, String.format(Errors.ERRORS[36], tree.getChildCount()));
		}
		CommonTree first = (CommonTree) tree.getChild(0);
		Double left = getValue(first);

		if (tree.getChildCount() == 2) {
			CommonTree second = (CommonTree) tree.getChild(1);
			Double right = getValue(second);

			return new Interval(left, right);
		} else {
			return new Interval(left);
		}
	}

	Double getValue(CommonTree tree) {
		int tokType = tree.token.getType();
		switch (tokType) {
		case PBFFilter.PLUS:
		case PBFFilter.MINUS:
			if (tree.getChildCount() > 1) {
				throw new ParsingException(tree.token, String.format(Errors.ERRORS[35], "double", tree.getChildCount()));
			}
			Token tok = ((CommonTree) tree.getChild(0)).token;
			double val;
			if (tok.getType() == PBFFilter.DOUBLE_LITERAL || tok.getType() == PBFFilter.INT_LITERAL) {
				val = new Double(tok.getText());
			} else if (tok.getType() == PBFFilter.INF) {
				val = Double.POSITIVE_INFINITY;
			} else {
				throw new ParsingException(tok, String.format(Errors.ERRORS[33], "double",
						PBFFilter.tokenNames[tok.getType()]));
			}
			return new Double((tokType == PBFFilter.MINUS) ? (-val) : (val));
		case PBFFilter.INT_LITERAL:
		case PBFFilter.DOUBLE_LITERAL:
			return new Double(tree.token.getText());
		case PBFFilter.INF:
			return Double.POSITIVE_INFINITY;
		default:
			throw new ParsingException(tree.token, String.format(Errors.ERRORS[33], "signed double",
					PBFFilter.tokenNames[tokType]));
		}
	}
}

class PrimitiveRole implements Primitive<Role> {

	@Override
	public Role create(CommonTree tree, Structure container) {
		if (tree.token.getType() == PBFFilter.NULL) {
			return null;
		}

		if (tree.token.getType() != PBFFilter.ID) {
			throw new ParsingException(tree.token, String.format(Errors.ERRORS[38], "role",
					PBFFilter.tokenNames[tree.token.getType()], Arrays.toString(Role.values())));
		}
		String text = tree.token.getText();
		if (text.equals("endogenous")) {
			return Role.ENDOGENOUS;
		} else if (text.equals("exogenous")) {
			return Role.EXOGENOUS;
		} else {
			throw new ParsingException(tree.token, String.format(Errors.ERRORS[37], "role", text,
					Arrays.toString(Role.values())));
		}
	}
}

class PrimitiveAggregation implements Primitive<Aggregation> {

	@Override
	public Aggregation create(CommonTree tree, Structure container) {
		if (tree.token.getType() == PBFFilter.NULL) {
			return null;
		}

		if (tree.token.getType() != PBFFilter.ID) {
			throw new ParsingException(tree.token, String.format(Errors.ERRORS[38], "aggregation",
					PBFFilter.tokenNames[tree.token.getType()], Arrays.toString(Aggregation.values())));
		}
		String text = tree.token.getText();
		if (text.equals("sum")) {
			return Aggregation.SUM;
		} else if (text.equals("product")) {
			return Aggregation.PRODUCT;
		} else if (text.equals("average")) {
			return Aggregation.AVERAGE;
		} else if (text.equals("minimum")) {
			return Aggregation.MININUM;
		} else if (text.equals("maximum")) {
			return Aggregation.MAXIMUM;
		} else {
			throw new ParsingException(tree.token, String.format(Errors.ERRORS[37], "aggregation", text,
					Arrays.toString(Aggregation.values())));
		}
	}
}

class PrimitiveEquation implements Primitive<TQ> {

	@Override
	public TQ create(CommonTree tree, Structure container) {
		// return new Expression(tree);

		if (tree.token.getType() == PBFFilter.NULL) {
			return null;
		}

		return new TQ(tree, container);
	}
}

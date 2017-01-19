/**
 * 
 */
package serialize.node;

import equation.*;

public class BOperatorSerializer<T extends Operator>
		extends FormatSerializer<T> {
	String symbol;

	public BOperatorSerializer(String operatorSymbol, String lParen, String rParen) {
		super(lParen + "%s " + operatorSymbol + " %s" + rParen);
		this.symbol = operatorSymbol;

	}
}
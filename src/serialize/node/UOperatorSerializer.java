/**
 * 
 */
package serialize.node;

import equation.*;

public class UOperatorSerializer<T extends UOperator>
		extends FormatSerializer<T> {
	String symbol;

	public UOperatorSerializer(String operatorSymbol, String lParen, String rParen) {
		super(lParen + operatorSymbol + " %s" + rParen);
		this.symbol = operatorSymbol;

	}
}
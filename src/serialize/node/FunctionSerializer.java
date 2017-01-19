/**
 * 
 */
package serialize.node;

import equation.*;

public class FunctionSerializer<T extends Function>
		extends FormatSerializer<T> {
	String name;

	public FunctionSerializer(String functionName, int arity, String lParen, String rParen, String delimiter) {
		this.name = functionName;
		StringBuilder arguments = new StringBuilder();
		arguments.append(lParen);
		if (arity > 0) {
			arguments.append("%s");
		}
		for (int i = 2; i <= arity; i++) {
			arguments.append(delimiter + " %s");
		}
		arguments.append(rParen);

		super.format = functionName + arguments.toString();
	}
}
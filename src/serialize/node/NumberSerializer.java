/**
 * 
 */
package serialize.node;

import org.apache.commons.lang.*;

import equation.*;
import equation.Number;

public class NumberSerializer
		extends NodeSerializer<Number> {
	String format;

	public NumberSerializer() {
		this(null);
	}

	public NumberSerializer(String format) {
		this.format = format;
	}


	@Override
	public String serialize(Number node, String... children) {
		double value = node.value.doubleValue();
		if (format == null) {
			return node.value.toString();
		} else {
			return String.format(this.format, node.value); 
		}
	}

}
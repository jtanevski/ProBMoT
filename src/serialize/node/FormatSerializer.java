/**
 * 
 */
package serialize.node;

import equation.*;

public class FormatSerializer<T extends Node>
		extends NodeSerializer<T> {
	String format;

	protected FormatSerializer() {
	}

	public FormatSerializer(String format) {
		this.format = format;
	}

	@Override
	public String serialize(T node, String... children) {
		return String.format(this.format, (Object[]) children);
	}

}
/**
 * 
 */
package serialize.node;

import equation.*;

public abstract class NodeSerializer<T extends Node> {
	public abstract String serialize(T node, String... children);
}
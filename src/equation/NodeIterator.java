/**
 * 
 */
package equation;

import java.util.*;

public class NodeIterator
		implements Iterator<Node> {
	Node node;
	int toGo;

	public NodeIterator(Node node) {
		this.node = node;
		this.toGo = node.getChildCount();
	}

	public boolean hasNext() {
		return (toGo > 0);
	}

	public Node next() {
		if (toGo > 0) {
			return node.children[node.children.length - toGo--];
		} else {
			throw new NoSuchElementException();
		}
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}
}
/**
 * 
 */
package equation;

import java.io.*;
import java.util.*;


public abstract class Node
		implements Serializable, Iterable<Node> {

	protected Node parent;
	protected int index;

	protected Node[] children;

	public Node getParent() {
		return this.parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public Node getChild(int index) {
		return this.children[index];
	}

	public void setChild(int index, Node child) {
		this.children[index] = child;
		child.parent = this;
		child.index = index;
	}

	public int getIndex() {
		return this.index;
	}

	public int getChildCount() {
		return this.children.length;
	}

	public Iterator<Node> iterator() {
		return new NodeIterator(this);
	}

}
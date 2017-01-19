/**
 * 
 */
package equation;


public abstract class BOperator
		extends Operator {

	public BOperator(Node left, Node right) {
		this.children = new Node[2];
		this.children[0] = left;
		this.children[1] = right;

		left.parent = this;
		left.index = 0;
		right.parent = this;
		right.index = 1;
	}

	public String toString() {
		return "(" + children[0].toString() + sign + children[1].toString() + ")";
	}

}
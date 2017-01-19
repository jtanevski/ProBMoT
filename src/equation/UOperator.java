/**
 * 
 */
package equation;

public abstract class UOperator
		extends Operator {

	public UOperator(Node arg) {
		this.children = new Node[1];
		this.children[0] = arg;

		arg.parent = this;
		arg.index = 0;
	}

	public String toString() {
		return "(" + sign + children[0].toString() + ")";
	}
}
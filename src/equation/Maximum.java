/**
 * 
 */
package equation;

public class Maximum
		extends Function {

	public Maximum(Node arg1, Node arg2) {

		this.children = new Node[] { arg1, arg2 };

		arg1.parent = this;
		arg1.index = 0;
		arg2.parent = this;
		arg2.index = 1;

	}

	public String getName() {
		return "max";
	}

	public int getArity() {
		return 2;
	}

	public Node getArg1() {
		return children[0];
	}

	public Node getArg2() {
		return children[1];
	}
}
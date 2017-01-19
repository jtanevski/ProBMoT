/**
 * 
 */
package equation;

public class Logarithm
		extends Function {

	public Logarithm(Node arg) {
		this.children = new Node[] { arg };

		arg.parent = this;
		arg.index = 0;
	}

	public String getName() {
		return "log";
	}

	public int getArity() {
		return 1;
	}

	public Node getArg() {
		return children[0];
	}
}
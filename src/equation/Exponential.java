/**
 * 
 */
package equation;

public class Exponential
		extends Function {

	public Exponential(Node arg) {
		this.children = new Node[] { arg };

		arg.parent = this;
		arg.index = 0;
	}

	public String getName() {
		return "exp";
	}

	public int getArity() {
		return 1;
	}

	public Node getArg() {
		return children[0];
	}
}
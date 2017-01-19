/**
 * 
 */
package equation;

public class Logarithm10
		extends Function {

	public Logarithm10(Node arg) {
		this.children = new Node[] { arg };

		arg.parent = this;
		arg.index = 0;
	}

	public String getName() {
		return "log10";
	}

	public int getArity() {
		return 1;
	}

	public Node getArg() {
		return children[0];
	}
}
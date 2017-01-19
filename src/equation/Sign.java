/**
 * 
 */
package equation;

public class Sign
		extends Function {

	public Sign(Node arg) {
		this.children = new Node[] { arg };

		arg.parent = this;
		arg.index = 0;
	}

	public String getName() {
		return "sign";
	}

	public int getArity() {
		return 1;
	}

	public Node getArg() {
		return children[0];
	}
}
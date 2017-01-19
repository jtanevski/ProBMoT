/**
 * 
 */
package equation;

public class Sinus
		extends Function {

	public Sinus(Node arg) {
		this.children = new Node[] { arg };

		arg.parent = this;
		arg.index = 0;
	}

	public String getName() {
		return "sin";
	}

	public int getArity() {
		return 1;
	}

	public Node getArg() {
		return children[0];
	}
}
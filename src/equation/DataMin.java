/**
 * 
 */
package equation;

public class DataMin
		extends Function {

	public DataMin(Node arg) {
		this.children = new Node[] { arg };

		arg.parent = this;
		arg.index = 0;
	}

	public String getName() {
		return "datamin";
	}

	public int getArity() {
		return 1;
	}

	public Node getArg() {
		return children[0];
	}
}
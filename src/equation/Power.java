/**
 * 
 */
package equation;

public class Power
		extends Function {

	public Power(Node base, Node exp) {

		this.children = new Node[] { base, exp };

		base.parent = this;
		base.index = 0;
		exp.parent = this;
		exp.index = 1;

	}

	public String getName() {
		return "pow";
	}

	public int getArity() {
		return 2;
	}

	public Node getBase() {
		return children[0];
	}

	public Node getExp() {
		return children[1];
	}
}
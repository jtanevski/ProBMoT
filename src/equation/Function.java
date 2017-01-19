/**
 * 
 */
package equation;


public abstract class Function
		extends Inner {

	public abstract String getName();

	public abstract int getArity();

	public String toString() {
		StringBuilder buf = new StringBuilder();
		if (getArity() >= 1) {
			buf.append(children[0].toString());
			for (int i = 1; i < children.length; i++) {
				buf.append("," + children[i].toString());
			}
		}
		return getName() + "(" + buf.toString() + ")";

	}

}
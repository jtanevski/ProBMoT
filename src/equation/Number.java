/**
 *
 */
package equation;

public class Number
		extends Leaf {
	public Double value;

	public Number(Double value) {
		this.value = value;
	}

	public Number(Integer value) {
		this.value = new Double(value);
	}

	public String toString() {
		if (value != null) {
			return value.toString();
		} else {
			return "null";
		}
	}
}
package temp;

import struct.*;

public class OutputCons {

	public String name;

	public Double value;

	public Double fit_start; //= null;
	public Interval fit_range; //= null;

	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append(this.name + " = " + (this.value!=null?this.value:this.fit_range));
		
		return buf.toString();
	}

}

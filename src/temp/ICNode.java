package temp;

import java.util.*;

import struct.inst.*;

public class ICNode {
	public String id;
	public IC cons;

	Map<Integer, IQNode> outputIQs = new LinkedHashMap<Integer, IQNode>();

	public ICNode(IC ic) {
		this.cons = ic;
		this.id = ic.getFullName();
	}

	public String toString() {
		return this.cons.toString();
	}

}
package temp;

import java.util.*;

import struct.inst.*;

public class IVNode {
	public String id;
	public IV var;

	IQNode inputIQ; // after aggregating all input equations

	Map<Integer, IQNode> inputIQs = new LinkedHashMap<Integer, IQNode>();
	Map<Integer, IQNode> outputIQs = new LinkedHashMap<Integer, IQNode>();

	public IVNode(IV iv) {
		this.var = iv;
		this.id = iv.getFullName();
	}

	public String toString() {
		return this.var.toString();
	}

}
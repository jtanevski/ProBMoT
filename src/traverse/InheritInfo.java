package traverse;

import java.util.*;

public class InheritInfo {
	String name;
	String children;
	
	List<FieldInfo> fields = new LinkedList<FieldInfo> (); 

	public InheritInfo(String name, String children, List<FieldInfo> fields) {
		this.name = name;
		this.children = children;
		this.fields = fields;
	}
}


class FieldInfo {
	String id;
	IDType type;
	
	public FieldInfo() {}
	
	public FieldInfo(String id, IDType type) {
		this.id = id;
		this.type = type;
	}
}
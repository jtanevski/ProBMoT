package traverse;

import java.util.*;

public class InheritInfoRegistry {
	public static final InheritInfoRegistry REGISTRY = new InheritInfoRegistry();
	
	
	Map<String, InheritInfo> infos = new LinkedHashMap<String, InheritInfo> ();
	
	public InheritInfoRegistry() {
		List<FieldInfo> fields = null;
		InheritInfo info = null;
		
		// TE
		fields = new LinkedList<FieldInfo> ();

		fields.add(new FieldInfo("vars", IDType.String));
		fields.add(new FieldInfo("consts", IDType.String));
		
		info = new InheritInfo("TE", "subs", fields);
		infos.put("TE", info);
		
		// TP
		fields = new LinkedList<FieldInfo> ();

		fields.add(new FieldInfo("consts", IDType.String));
		fields.add(new FieldInfo("parameters", IDType.Both));
		fields.add(new FieldInfo("processes", IDType.Integer));
		fields.add(new FieldInfo("equations", IDType.Integer));
		
		info = new InheritInfo("TP", "subs", fields);
		infos.put("TP", info);
	}
}

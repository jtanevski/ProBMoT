package traverse;


import java.util.*;

public class ComposeInfoRegistry {
	public static final ComposeInfoRegistry REGISTRY = new ComposeInfoRegistry();
	
	
	Map<String, ComposeInfo> infos = new LinkedHashMap<String, ComposeInfo>();
	
	public ComposeInfoRegistry() {
		List<CopyInfo> copy = null;
		List<RecurseInfo> recurse = null;
		List<ProprietaryInfo> prop = null;
		Validator validator = null;
		
		ComposeInfo info = null;
		
		// Model
		copy = new LinkedList<CopyInfo>();
		recurse = new LinkedList<RecurseInfo>();
		prop = new LinkedList<ProprietaryInfo>();
		validator = null;
		
		copy.add(new CopyInfo("tks", "tks"));
		copy.add(new CopyInfo("tes", "tes"));
		copy.add(new CopyInfo("tps", "tps"));
		
		recurse.add(new RecurseInfo("IK", ComposeType.Explicit, "tks", "iks"));
		recurse.add(new RecurseInfo("IE", ComposeType.Explicit, "tes", "ies"));
		recurse.add(new RecurseInfo("IP", ComposeType.Explicit, "tps", "ips"));
		
		info = new ComposeInfo("MODEL", copy, recurse, prop, validator);
		infos.put("MODEL", info);
		
		// IK
		copy = new LinkedList<CopyInfo>();
		recurse = new LinkedList<RecurseInfo>();
		prop = new LinkedList<ProprietaryInfo>();
		validator = null;
		
		copy.add(new CopyInfo("tes", "tes"));
		copy.add(new CopyInfo("tps", "tps"));
		
		recurse.add(new RecurseInfo("IK", ComposeType.Explicit, "tks", "iks"));
		recurse.add(new RecurseInfo("IE", ComposeType.Explicit, "tes", "ies"));
		recurse.add(new RecurseInfo("IP", ComposeType.Explicit, "tps", "ips"));
		
		info = new ComposeInfo("IK", copy, recurse, prop, validator);
		infos.put("IK", info);
		
		// IE
		copy = new LinkedList<CopyInfo>();
		recurse = new LinkedList<RecurseInfo>();
		prop = new LinkedList<ProprietaryInfo>();
		validator = null;
		
		recurse.add(new RecurseInfo("IV", ComposeType.Name, "vars", "vars"));
		recurse.add(new RecurseInfo("IC", ComposeType.Name, "consts", "consts"));
		
		info = new ComposeInfo("IE", copy, recurse, prop, validator);
		infos.put("IE", info);
		
		// IP
		copy = new LinkedList<CopyInfo>();
		recurse = new LinkedList<RecurseInfo>();
		prop = new LinkedList<ProprietaryInfo>();
		validator = null;
		
		recurse.add(new RecurseInfo("IC", ComposeType.Name, "consts", "consts"));
		recurse.add(new RecurseInfo("AE", ComposeType.Position, "parameters", "arguments"));
		recurse.add(new RecurseInfo("AP", ComposeType.Position, "processes", "processes"));
		
		prop.add(new ExpressionInfo("equations", "equations"));
		
		info = new ComposeInfo("IP", copy, recurse, prop, validator);
		infos.put("IP", info);
		
		// IV
		copy = new LinkedList<CopyInfo>();
		recurse = new LinkedList<RecurseInfo>();
		prop = new LinkedList<ProprietaryInfo>();
		validator = null;
		
		copy.add(new CopyInfo("range", "range"));
		copy.add(new CopyInfo("unit", "unit"));
		copy.add(new CopyInfo("agg", "agg"));
		
		info = new ComposeInfo("IV", copy, recurse, prop, validator);
		infos.put("IV", info);
		
		// IC
		copy = new LinkedList<CopyInfo>();
		recurse = new LinkedList<RecurseInfo>();
		prop = new LinkedList<ProprietaryInfo>();
		validator = null;
		
		copy.add(new CopyInfo("range", "range"));
		copy.add(new CopyInfo("unit", "unit"));
		
		info = new ComposeInfo("IC", copy, recurse, prop, validator);
		infos.put("IC", info);
		
		// AE
		copy = new LinkedList<CopyInfo>();
		recurse = new LinkedList<RecurseInfo>();
		prop = new LinkedList<ProprietaryInfo>();
		validator = null;
		
		copy.add(new CopyInfo("te", "te"));
		copy.add(new CopyInfo("card", "card"));
		
		validator = new AEValidator();
		
		info = new ComposeInfo("AE", copy, recurse, prop, validator);
		infos.put("AE", info);
		
		// AP
		copy = new LinkedList<CopyInfo>();
		recurse = new LinkedList<RecurseInfo>();
		prop = new LinkedList<ProprietaryInfo>();
		validator = null;
		
		copy.add(new CopyInfo("tp", "tp"));
		copy.add(new CopyInfo("parameters", "parameters"));
		
		validator = new APValidator();
		
		info = new ComposeInfo("AP", copy, recurse, prop, validator);
		infos.put("AP", info);
	}
}

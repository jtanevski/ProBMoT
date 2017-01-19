package traverse;

import java.util.*;

import struct.*;
import struct.inst.*;
import struct.temp.*;

public class StructInfoRegistry {
//	public static final StructInfoRegistry REGISTRY = new StructInfoRegistry(); 
	
	public static final StructInfoRegistry LIBRARY_REGISTRY = new StructInfoRegistry(); 
	public static final StructInfoRegistry MODEL_REGISTRY = new StructInfoRegistry(); 
	public static final StructInfoRegistry INCOMPLETE_MODEL_REGISTRY = new StructInfoRegistry(); 

	
	/* Library Definitions */
	static {
		Map<String, StructInfo> infos = null;
		
		
		Validator<? extends Structure> val = null;
		
		Map<String, ContentInfo> cont = null; 
		Map<String, ReferenceInfo> ref = null; 
		Map<String, ReferenceInfo> refPost = null; 
		
		StructInfo info = null;
		
		
		/* Library Definitions */
		infos = LIBRARY_REGISTRY.infos;
		
		
		// LIBRARY
		cont = new LinkedHashMap<String, ContentInfo>();
		ref = new LinkedHashMap<String, ReferenceInfo> ();
		refPost = new LinkedHashMap<String, ReferenceInfo> ();
		
		cont.put("tes", new ContentInfo("tes", "TE", 0, Double.POSITIVE_INFINITY, "tes"));
		cont.put("tps", new ContentInfo("tps", "TP", 0, Double.POSITIVE_INFINITY, "tps"));
		cont.put("tks", new ContentInfo("tks", "TK", 0, Double.POSITIVE_INFINITY, "tks"));
		
		info = new StructInfo("LIBRARY", Library.class, IDType.String, cont, ref, refPost);
		infos.put("LIBRARY", info);
		
		
		// TK
		cont = new LinkedHashMap<String, ContentInfo>();
		ref = new LinkedHashMap<String, ReferenceInfo> ();
		refPost = new LinkedHashMap<String, ReferenceInfo> ();

		ref.put("entities", new ReferenceInfo("entities", 0, Double.POSITIVE_INFINITY, "tes", "lib.tes"));
		ref.put("processes", new ReferenceInfo("processes", 0, Double.POSITIVE_INFINITY, "tps", "lib.tps"));
		ref.put("compartments", new ReferenceInfo("compartments", 0, Double.POSITIVE_INFINITY, "tks", "lib.tks"));
		
		info = new StructInfo("TK", TK.class, IDType.String, cont, ref, refPost);
		infos.put("TK", info);
				
		
		// TE
		cont = new LinkedHashMap<String, ContentInfo>();
		ref = new LinkedHashMap<String, ReferenceInfo> ();
		refPost = new LinkedHashMap<String, ReferenceInfo> ();
		
		cont.put("vars", new ContentInfo("vars", "TV", 0, Double.POSITIVE_INFINITY, "vars"));
		cont.put("consts", new ContentInfo("consts", "TC", 0, Double.POSITIVE_INFINITY, "consts"));
		
		ref.put("super", new ReferenceInfo("super", 1, 1, "zuper", "lib.tes"));
		
		val = new TEValidator();
		
		info = new StructInfo("TE", TE.class, IDType.String, cont, ref, refPost, val, "zuper.subs");
		infos.put("TE", info);
		
		
		// TP
		cont = new LinkedHashMap<String, ContentInfo>();
		ref = new LinkedHashMap<String, ReferenceInfo> ();
		refPost = new LinkedHashMap<String, ReferenceInfo> ();
		
		cont.put("parameters", new ContentInfo("parameters", "PE", 0, Double.POSITIVE_INFINITY, "parameters"));
		cont.put("consts", new ContentInfo("consts", "TC", 0, Double.POSITIVE_INFINITY, "consts"));
		cont.put("equations", new ContentInfo("equations", "TQ", 0, Double.POSITIVE_INFINITY, "equations"));
		cont.put("processes", new ContentInfo("processes", "PP", 0, Double.POSITIVE_INFINITY, "processes"));

		ref.put("super", new ReferenceInfo("super", 1, 1, "zuper", "lib.tps"));
		
		info = new StructInfo("TP", TP.class, IDType.String, cont, ref, refPost, "zuper.subs");
		infos.put("TP", info);

		
		// TV
		cont = new LinkedHashMap<String, ContentInfo>();
		ref = new LinkedHashMap<String, ReferenceInfo> ();
		refPost = new LinkedHashMap<String, ReferenceInfo> ();
		
		cont.put("range", new ContentInfo("range", "INTER", 0, 1, "range", new Interval(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY)));
		cont.put("unit", new ContentInfo("unit", "STRING", 0, 1, "unit", ""));
		cont.put("aggregation", new ContentInfo("aggregation", "AGGREGATION", 0, 1, "agg", Aggregation.SUM));

		info = new StructInfo("TV", TV.class, IDType.String, cont, ref, refPost);
		infos.put("TV", info);

		
		// TC
		cont = new LinkedHashMap<String, ContentInfo>();
		ref = new LinkedHashMap<String, ReferenceInfo> ();
		refPost = new LinkedHashMap<String, ReferenceInfo> ();
		
		cont.put("range", new ContentInfo("range", "INTER", 0, 1, "range", new Interval(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY)));
		cont.put("unit", new ContentInfo("unit", "STRING", 0, 1, "unit", ""));
		
		info = new StructInfo("TC", TC.class, IDType.String, cont, ref, refPost);
		infos.put("TC", info);

		
		// PE
		cont = new LinkedHashMap<String, ContentInfo>();
		ref = new LinkedHashMap<String, ReferenceInfo> ();
		refPost = new LinkedHashMap<String, ReferenceInfo> ();
		
		cont.put("card", new ContentInfo("card", "INTER", 0, 1, "card", new Interval(1)));

		ref.put("template", new ReferenceInfo("template", 1, 1, "te", "tp.lib.tes"));

		info = new StructInfo("PE", PE.class, IDType.Both, cont, ref, refPost);
		infos.put("PE", info);
	
		
		// PP
		cont = new LinkedHashMap<String, ContentInfo>();
		ref = new LinkedHashMap<String, ReferenceInfo> ();
		refPost = new LinkedHashMap<String, ReferenceInfo> ();

		ref.put("template", new ReferenceInfo("template", 1, 1, "tp", "cont.lib.tps"));

		refPost.put("parameters", new ReferenceInfo("parameters", 0, Double.POSITIVE_INFINITY, "parameters", "cont.parameters"));

		val = new PPValidator();
		
		info = new StructInfo("PP", PP.class, IDType.Integer, cont, ref, refPost, val);
		infos.put("PP", info);
	}
	
	/* Model Definitions */
	static {
		
		Map<String, StructInfo> infos = null;
		
		
		Validator<? extends Structure> val = null;
		
		Map<String, ContentInfo> cont = null; 
		Map<String, ReferenceInfo> ref = null; 
		Map<String, ReferenceInfo> refPost = null; 
		
		StructInfo info = null;

		/* Model Definitions */
		infos = MODEL_REGISTRY.infos;
		
		
		// MODEL
		cont = new LinkedHashMap<String, ContentInfo>();
		ref = new LinkedHashMap<String, ReferenceInfo> ();
		refPost = new LinkedHashMap<String, ReferenceInfo> ();
		
		cont.put("ies", new ContentInfo("ies", "IE", 0, Double.POSITIVE_INFINITY, "ies"));
		cont.put("ips", new ContentInfo("ips", "IP", 0, Double.POSITIVE_INFINITY, "ips"));
		cont.put("iks", new ContentInfo("iks", "IK", 0, Double.POSITIVE_INFINITY, "iks"));
		
		ref.put("template", new ReferenceInfo("template", 1, 1, "template", "libraryLookup"));
		
		info = new StructInfo("MODEL", Model.class, IDType.String, cont, ref, refPost);
		infos.put("MODEL", info);

		
		// IK
		cont = new LinkedHashMap<String, ContentInfo>();
		ref = new LinkedHashMap<String, ReferenceInfo> ();
		refPost = new LinkedHashMap<String, ReferenceInfo> ();
		
		cont.put("ies", new ContentInfo("ies", "IE", 0, Double.POSITIVE_INFINITY, "ies"));
		cont.put("ips", new ContentInfo("ips", "IP", 0, Double.POSITIVE_INFINITY, "ips"));
		cont.put("iks", new ContentInfo("iks", "IK", 0, Double.POSITIVE_INFINITY, "iks"));
		
		ref.put("template", new ReferenceInfo("template", 1, 1, "template", "cont.template.tks"));
		
		info = new StructInfo("IK", IK.class, IDType.String, cont, ref, refPost);
		infos.put("IK", info);

		
		// IE
		cont = new LinkedHashMap<String, ContentInfo>();
		ref = new LinkedHashMap<String, ReferenceInfo> ();
		refPost = new LinkedHashMap<String, ReferenceInfo> ();
		
		cont.put("vars", new ContentInfo("vars", "IV", 0, Double.POSITIVE_INFINITY, "vars"));
		cont.put("consts", new ContentInfo("consts", "IC", 0, Double.POSITIVE_INFINITY, "consts"));
		
		ref.put("template", new ReferenceInfo("template", 1, 1, "template", "cont.template.tes"));

		info = new StructInfo("IE", IE.class, IDType.String, cont, ref, refPost);
		infos.put("IE", info);

		
		// IP
		cont = new LinkedHashMap<String, ContentInfo>();
		ref = new LinkedHashMap<String, ReferenceInfo> ();
		refPost = new LinkedHashMap<String, ReferenceInfo> ();
		
		cont.put("consts", new ContentInfo("consts", "IC", 0, Double.POSITIVE_INFINITY, "consts"));
		cont.put("arguments", new ContentInfo("arguments", "AE", 0, Double.POSITIVE_INFINITY, "arguments"));
		cont.put("processes", new ContentInfo("processes", "AP", 0, Double.POSITIVE_INFINITY, "processes"));
		
		ref.put("template", new ReferenceInfo("template", 1, 1, "template", "cont.template.tps"));

		info = new StructInfo("IP", IP.class, IDType.String, cont, ref, refPost);
		infos.put("IP", info);

		
		// IV
		cont = new LinkedHashMap<String, ContentInfo>();
		ref = new LinkedHashMap<String, ReferenceInfo> ();
		refPost = new LinkedHashMap<String, ReferenceInfo> ();
		
		cont.put("role", new ContentInfo("role", "ROLE", 0, 1, "role", Role.EXOGENOUS));
		cont.put("initial", new ContentInfo("initial", "SIGNED_DOUBLE", 0, 1, "initial", 0.0));
		
		info = new StructInfo("IV", IV.class, IDType.String, cont, ref, refPost);
		infos.put("IV", info);

		
		// IC
		cont = new LinkedHashMap<String, ContentInfo>();
		ref = new LinkedHashMap<String, ReferenceInfo> ();
		refPost = new LinkedHashMap<String, ReferenceInfo> ();
		
		cont.put("value", new ContentInfo("value", "SIGNED_DOUBLE", 1, 1, "value"));
		
		info = new StructInfo("IC", IC.class, IDType.String, cont, ref, refPost);
		infos.put("IC", info);

		
		// AE
		cont = new LinkedHashMap<String, ContentInfo>();
		ref = new LinkedHashMap<String, ReferenceInfo> ();
		refPost = new LinkedHashMap<String, ReferenceInfo> ();
		
		ref.put("arg", new ReferenceInfo("arg", 0, Double.POSITIVE_INFINITY, "args", "ip.cont.iesRec"));

		val = new AEValidator();

		info = new StructInfo("AE", AE.class, IDType.Integer, cont, ref, refPost, val);
		infos.put("AE", info);

		
		// AP
		cont = new LinkedHashMap<String, ContentInfo>();
		ref = new LinkedHashMap<String, ReferenceInfo> ();
		refPost = new LinkedHashMap<String, ReferenceInfo> ();
		
		ref.put("arg", new ReferenceInfo("arg", 1, 1, "arg", "ip.cont.ips"));

		val = new APValidator();

		info = new StructInfo("AP", AP.class, IDType.Integer, cont, ref, refPost, val);
		infos.put("AP", info);
	}

	/* Incomplete Model Definitions */
	static {
		
		Map<String, StructInfo> infos = null;
		
		
		Validator<? extends Structure> val = null;
		
		Map<String, ContentInfo> cont = null; 
		Map<String, ReferenceInfo> ref = null; 
		Map<String, ReferenceInfo> refPost = null; 
		
		StructInfo info = null;

		/* Incomplete Model Definitions */
		infos = INCOMPLETE_MODEL_REGISTRY.infos;
		
		
		// INCOMPLETE_MODEL
		cont = new LinkedHashMap<String, ContentInfo>();
		ref = new LinkedHashMap<String, ReferenceInfo> ();
		refPost = new LinkedHashMap<String, ReferenceInfo> ();
		
		cont.put("ies", new ContentInfo("ies", "IE", 0, Double.POSITIVE_INFINITY, "ies"));
		cont.put("ips", new ContentInfo("ips", "IP", 0, Double.POSITIVE_INFINITY, "ips"));
		cont.put("iks", new ContentInfo("iks", "IK", 0, Double.POSITIVE_INFINITY, "iks"));
		
		ref.put("template", new ReferenceInfo("template", 1, 1, "template", "libraryLookup"));
		
		info = new StructInfo("INCOMPLETE_MODEL", IncompleteModel.class, IDType.String, cont, ref, refPost);
		infos.put("INCOMPLETE_MODEL", info);

		
		// IK
		cont = new LinkedHashMap<String, ContentInfo>();
		ref = new LinkedHashMap<String, ReferenceInfo> ();
		refPost = new LinkedHashMap<String, ReferenceInfo> ();
		
		cont.put("ies", new ContentInfo("ies", "IE", 0, Double.POSITIVE_INFINITY, "ies"));
		cont.put("ips", new ContentInfo("ips", "IP", 0, Double.POSITIVE_INFINITY, "ips"));
		cont.put("iks", new ContentInfo("iks", "IK", 0, Double.POSITIVE_INFINITY, "iks"));
		
		ref.put("template", new ReferenceInfo("template", 1, 1, "template", "cont.template.tks"));
		
		info = new StructInfo("IK", IK.class, IDType.String, cont, ref, refPost);
		infos.put("IK", info);

		
		// IE
		cont = new LinkedHashMap<String, ContentInfo>();
		ref = new LinkedHashMap<String, ReferenceInfo> ();
		refPost = new LinkedHashMap<String, ReferenceInfo> ();
		
		cont.put("vars", new ContentInfo("vars", "IV", 0, Double.POSITIVE_INFINITY, "vars"));
		cont.put("consts", new ContentInfo("consts", "IC", 0, Double.POSITIVE_INFINITY, "consts"));
		
		ref.put("template", new ReferenceInfo("template", 1, 1, "template", "cont.template.tes"));

		info = new StructInfo("IE", IE.class, IDType.String, cont, ref, refPost);
		infos.put("IE", info);

		
		// IP
		cont = new LinkedHashMap<String, ContentInfo>();
		ref = new LinkedHashMap<String, ReferenceInfo> ();
		refPost = new LinkedHashMap<String, ReferenceInfo> ();
		
		cont.put("consts", new ContentInfo("consts", "IC", 0, Double.POSITIVE_INFINITY, "consts"));
		cont.put("arguments", new ContentInfo("arguments", "AE", 0, Double.POSITIVE_INFINITY, "arguments"));
		cont.put("processes", new ContentInfo("processes", "AP", 0, Double.POSITIVE_INFINITY, "processes"));
		
		ref.put("template", new ReferenceInfo("template", 1, 1, "template", "cont.template.tps"));

		info = new StructInfo("IP", IP.class, IDType.String, cont, ref, refPost);
		infos.put("IP", info);

		
		// IIV
		cont = new LinkedHashMap<String, ContentInfo>();
		ref = new LinkedHashMap<String, ReferenceInfo> ();
		refPost = new LinkedHashMap<String, ReferenceInfo> ();
		
		cont.put("role", new ContentInfo("role", "ROLE", 0, 1, "role", Role.EXOGENOUS));
		cont.put("initial", new ContentInfo("initial", "SIGNED_DOUBLE", 0, 1, "initial"));
		cont.put("fit_start", new ContentInfo("fit_start", "SIGNED_DOUBLE", 0, 1, "fit_start"));
		cont.put("fit_range", new ContentInfo("fit_range", "INTER", 0, 1, "fit_range"));
		
		
		info = new StructInfo("IV", IIV.class, IDType.String, cont, ref, refPost);
		infos.put("IV", info);

		
		// IIC
		cont = new LinkedHashMap<String, ContentInfo>();
		ref = new LinkedHashMap<String, ReferenceInfo> ();
		refPost = new LinkedHashMap<String, ReferenceInfo> ();
		
		cont.put("value", new ContentInfo("value", "SIGNED_DOUBLE", 0, 1, "value"));
		cont.put("fit_start", new ContentInfo("fit_start", "SIGNED_DOUBLE", 0, 1, "fit_start"));
		cont.put("fit_range", new ContentInfo("fit_range", "INTER", 0, 1, "fit_range"));
		
		info = new StructInfo("IC", IIC.class, IDType.String, cont, ref, refPost);
		infos.put("IC", info);

		
		// IAE
		cont = new LinkedHashMap<String, ContentInfo>();
		ref = new LinkedHashMap<String, ReferenceInfo> ();
		refPost = new LinkedHashMap<String, ReferenceInfo> ();
		
		ref.put("arg", new ReferenceInfo("arg", 0, Double.POSITIVE_INFINITY, "args", "ip.cont.iesRec"));
		ref.put("lowerArg", new ReferenceInfo("lowerArg", 0, Double.POSITIVE_INFINITY, "lowerArgs", "ip.cont.iesRec"));
		ref.put("upperArg", new ReferenceInfo("upperArg", 0, Double.POSITIVE_INFINITY, "upperArgs", "ip.cont.iesRec"));

		val = new IAEValidator();

		info = new StructInfo("AE", IAE.class, IDType.Integer, cont, ref, refPost, val);
		infos.put("AE", info);

		
		// IAP
		cont = new LinkedHashMap<String, ContentInfo>();
		ref = new LinkedHashMap<String, ReferenceInfo> ();
		refPost = new LinkedHashMap<String, ReferenceInfo> ();
		
		ref.put("arg", new ReferenceInfo("arg", 1, 1, "arg", "ip.cont.ips"));

		val = new APValidator();

		info = new StructInfo("AP", IAP.class, IDType.Integer, cont, ref, refPost, val);
		infos.put("AP", info);
	}
	
	
	Map<String, StructInfo> infos = new LinkedHashMap<String, StructInfo> ();
	
	public StructInfoRegistry() {}
}

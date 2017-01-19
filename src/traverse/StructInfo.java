package traverse;

import java.util.*;

public class StructInfo {
	String name;
	Class clazz;
	
	IDType idtype;

	Map<String, ContentInfo> contProps; // Content
	Map<String, ReferenceInfo> refPropsPre; // Reference Pre-inherit
	Map<String, ReferenceInfo> refPropsPost; // Reference Post-inherit
	
	String[] backrefs;
	
	Validator validator;

	public StructInfo(String name, Class clazz, IDType idtype, Map<String, ContentInfo> contProps, Map<String, ReferenceInfo> refPropsPre, Map<String, ReferenceInfo> refPropsPost, String... backrefs) {
		this(name, clazz, idtype, contProps, refPropsPre, refPropsPost, null, backrefs);
	}
	

	public StructInfo(String name, Class clazz, IDType idtype, Map<String, ContentInfo> contProps, Map<String, ReferenceInfo> refPropsPre, Map<String, ReferenceInfo> refPropsPost, Validator validator, String... backrefs) {
		this.name = name;
		this.clazz = clazz;
		this.idtype = idtype;
		this.contProps = contProps;
		this.refPropsPre = refPropsPre;
		this.refPropsPost = refPropsPost;
		this.validator = validator;
		this.backrefs = backrefs;
	}

	
	public String[][] backrefs () {
		String[][] toReturn = new String[backrefs.length][];
		
		for (int i = 0; i < backrefs.length; i++) {
			toReturn[i] =  backrefs[i].split("\\.");
		}
		
		return toReturn;
	}
}

enum IDType {
	Integer,
	String,
	Both,
}
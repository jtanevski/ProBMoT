package traverse;


import java.util.*;


public class ComposeInfo {
	String name;
	List<CopyInfo> copy;
	List<RecurseInfo> recurse;
	List<ProprietaryInfo> prop;
	Validator validator;

	public ComposeInfo(String name, List<CopyInfo> copy, List<RecurseInfo> recurse, List<ProprietaryInfo> prop, Validator validator) {
		this.name = name;
		this.copy = copy;
		this.recurse = recurse;
		this.prop = prop;
		this.validator = validator;
	}
}

abstract class ProprietaryInfo {
	String templateField;
	String instanceField;

	public abstract void compose();
	public abstract void validate();
}

class ExpressionInfo
		extends ProprietaryInfo {
	public ExpressionInfo(String templateField, String instanceField) {
		this.templateField = templateField;
		this.instanceField = instanceField;
	}

	public void compose() {
		//	System.out.println("Compose expression");
	}
	
	public void validate() {
	}
}

class RecurseInfo {
	String name;
	ComposeType type;
	String templateField;
	String instanceField;

	public RecurseInfo(String name, ComposeType type, String templateField, String instanceField) {
		this.name = name;
		this.type = type;
		this.templateField = templateField;
		this.instanceField = instanceField;
	}
}

class CopyInfo {
	String templateField;
	String instanceField;

	public CopyInfo(String templateField, String instanceField) {
		this.templateField = templateField;
		this.instanceField = instanceField;
	}
}

enum ComposeType {
	Explicit,
	Position,
	Name,
}


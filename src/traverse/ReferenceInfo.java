package traverse;


public class ReferenceInfo {
	String propName;
//	String propType;
	
	double minCard;
	double maxCard;
	
	String varName;
	
	String lookup;
	//	String[] backref;
	
	public ReferenceInfo() {}

	public ReferenceInfo(String propName, /*String propType,*/ double minCard, double maxCard, String varName, String lookup) {
		this.propName = propName;
		//	this.propType = propType;
		this.minCard = minCard;
		this.maxCard = maxCard;
		this.varName = varName;
		
		this.lookup = lookup;
	}
	
	
	
	public String[] lookup() {
		return lookup.split("\\.");
	}
}

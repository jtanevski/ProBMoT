package traverse;



public class ContentInfo {
	/** Name of the property in the AST */
	String propName;
	
	/** Name of the class of the property */
	String propType;

	/** Minimum cardinality of the property */
	double minCard;
	
	/** Maximum cardinality of the property */
	double maxCard;
	
	/** Name of the field in the class */ 
	String varName;
	
	/** Optional default value for the property */
	Object defavlt;
	
	public ContentInfo() {}

	public ContentInfo(String propName, String propType, double minCard, double maxCard, String varName, Object... defavlt) {
		this.propName = propName;
		this.propType = propType;
		this.minCard = minCard;
		this.maxCard = maxCard;
		this.varName = varName;
		
		if (defavlt != null && defavlt.length > 0) {
			if (defavlt.length != 1) {
				throw new RuntimeException("One default value should be specified");
			}
			this.defavlt = defavlt[0];
		}
	}
}

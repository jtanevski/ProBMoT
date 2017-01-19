package xml;

import javax.xml.bind.annotation.XmlElement;

public class InitialValuesSpec {
	
	@XmlElement(defaultValue = "true")
	public boolean sameforalldatasets;
	
	@XmlElement(defaultValue = "false")
	public boolean usedatasetvalues;
	
	@XmlElement(defaultValue = "false")
	public boolean evaluatewithdatasetvalues;
	


}

package struct.inst;

import java.util.*;





/** Abstract Model **/
public abstract class AM
		extends Instance {

	/* Content */
	public Map<String, IE> ies = new LinkedHashMap<String, IE>();
	public Map<String, IP> ips = new LinkedHashMap<String, IP>();
	public Map<String, IK> iks = new LinkedHashMap<String, IK>();
	
	/* Recursive content */
	public Map<String, IE> iesRec = new LinkedHashMap<String, IE>();
	public Map<String, IP> ipsRec = new LinkedHashMap<String, IP>();
	public Map<String, IK> iksRec = new LinkedHashMap<String, IK>();

	
	
	public abstract AM getContainer();
	
	
	/**
	 * if this is a model return this, if it's a compartment return the model that contains it.
	 * @return
	 */
	public Model getModel() {
		AM toReturn = this;

		while (!(toReturn instanceof Model)) {
			toReturn = toReturn.getContainer();
		}
		
		return (Model)toReturn;

	}
	
	
}

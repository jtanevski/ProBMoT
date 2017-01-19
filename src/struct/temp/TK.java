package struct.temp;


import java.util.*;

import struct.*;
import struct.inst.*;


/** Template Compartment **/
public class TK
		extends Template {
	/* ID */
	public String id;
	
	/* Container */
	public Library lib;
	
	/* Contain */
	
	/* Reference */
	public Map<String, TE> tes = new LinkedHashMap<String, TE> ();
	public Map<String, TP> tps = new LinkedHashMap<String, TP> ();
	public Map<String, TK> tks = new LinkedHashMap<String, TK> ();
	
	
	/* Back-reference */
	public Map<String, IK> instances = new LinkedHashMap<String, IK> ();
	
	/* Other */
	
	
	
	@Override
	public Library getContainer() {
		return this.lib;
	}

	@Override
	public String getIdS() {
		return this.id;
	}

	@Override
	public void setContainer(Structure container) {
		this.lib = (Library) container;
	}

	@Override
	public void setIdS(String id) {
		this.id = (String) id;
	}

}

package struct.temp;

import java.util.*;

import org.apache.commons.lang3.*;

import struct.*;

public class Library
		extends Template {

	/* ID */
	private String id;

	/* Container */
	
	/* Content */
	public Map<String, TE> tes = new LinkedHashMap<String, TE> ();
	public Map<String, TP> tps = new LinkedHashMap<String, TP> ();
	public Map<String, TK> tks = new LinkedHashMap<String, TK> ();
	
	/* Reference */
	
	/* Back-reference */
	
	/* Other */
	public final TE ENTITY = new TE("Entity", this);
	public final TP PROCESS = new TP("Process", this);

	private Map<String, TP> topLevelTPs = new LinkedHashMap<String, TP>(); 
	
	public static final Map<String, Library> LIBRARIES = new LinkedHashMap<String, Library> ();
	
	public Library() {
		this.tes.put("Entity", ENTITY);
		this.tps.put("Process", PROCESS);
	}
	
	@Override
	public String getIdS() {
		return this.id;
	}

	@Override
	public void setIdS(String id) {
		this.id = (String) id;
	}

	@Override
	public Structure getContainer() {
		return null;
	}

	@Override
	public void setContainer(Structure container) {
		if (container != null) 
			throw new UnsupportedOperationException("Cannot set a container for library");
	}
	
	public void determineTopLevelTPs() {
		this.topLevelTPs = new LinkedHashMap<String, TP>(this.tps);
		for (TP tp : this.tps.values()) {
			for (PP pp : tp.processes) {
					removeTPandSubs(pp.tp);
				
			}
		}
	}
	
	private void removeTPandSubs(TP tp) {
		this.topLevelTPs.remove(tp.id);
		for (TP sub : tp.subs.values()) {
			removeTPandSubs(sub);
		}
	}
	
	public boolean isTopLevel(String tpName) {
		return this.topLevelTPs.containsKey(tpName);
	}

	public boolean isTopLevel(TP tp) {
		return this.topLevelTPs.containsKey(tp.id);
	}

	public String toString() {
		String format = "library %s;\n\n//Entities\n\n%s\n//Processes\n\n%s"; 
		
		Set<TE> tesWithoutRoot = new LinkedHashSet<TE>(this.tes.values());
		tesWithoutRoot.remove(this.ENTITY);
		String entitiesString = StringUtils.join(tesWithoutRoot, "\n");

		Set<TP> tpsWithoutRoot = new LinkedHashSet<TP>(this.tps.values());
		tpsWithoutRoot.remove(this.PROCESS);
		String processesString = StringUtils.join(tpsWithoutRoot, "\n");
		
		String str = String.format(format, this.id, entitiesString, processesString);
		
		return str;
	}
}

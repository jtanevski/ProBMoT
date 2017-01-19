package struct.inst;


import java.io.*;
import java.util.*;

import org.apache.commons.lang3.*;

import struct.*;
import struct.temp.*;


/** Instance Entity **/
public class IE
		extends Instance {
	
	/* ID */
	public String id;

	/* Container */
	public AM cont;

	/* Content */
	public Map<String, IV> vars = new LinkedHashMap<String, IV> ();
	public Map<String, IC> consts = new LinkedHashMap<String, IC> ();

	/* Reference */
	
	/* Back-reference */
	
	/* Template */
	public transient TE template;
	
	/* Copy */
	
	/* Other */


	@Override
	public String getIdS() {
		return this.id;
	}
	

	@Override
	public void setIdS(String id) {
		this.id = (String) id;
	}
	

	@Override
	public TE getTemplate() {
		return this.template;
	}

	@Override
	public void setTemplate(Template template) {
		this.template = (TE) template;
	}

	@Override
	public AM getContainer() {
		return this.cont;
	}

	@Override
	public void setContainer(Structure container) {
		this.cont = (AM) container;
	}

	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
		stream.defaultReadObject();

		// set template
		this.templateID = stream.readObject();

	}
	
	private void writeObject(ObjectOutputStream stream) throws IOException {
		stream.defaultWriteObject();
		
		//NOTE: at this moment it's not clear whether
		//full-name or ID (string or integer) should be used, could be revised later on
		Object id;
		try {
			id = getTemplate().getIdS();
		} catch (UnsupportedOperationException ex) {
			id = getTemplate().getIdI();
		}
		stream.writeObject(id);
	}

	
	public void restoreTemplate() {
		String templateName = (String) this.templateID;
		TE template = getContainer().getModel().getTemplate().tes.get(templateName);
		setTemplate(template);
		
		
		for (IV iv : this.vars.values()) {
			iv.restoreTemplate();
		}
		for (IC ic : this.consts.values()) {
			ic.restoreTemplate();
		}

	}

	public String toString() {
		String format = "entity %s : %s {\n%s%s}\n"; 

		StringBuilder varsString = new StringBuilder();
		if (this.vars.size() > 0) {
			varsString.append("\tvars:\n\t\t");
			varsString.append(StringUtils.join(this.vars.values(), ",\n\t\t"));
			varsString.append(";\n");
		}
	
		StringBuilder constsString = new StringBuilder();
		if (this.consts.size() > 0) {
			constsString.append("\tconsts:\n\t\t");
			constsString.append(StringUtils.join(this.consts.values(), ",\n\t\t"));
			constsString.append(";\n");
		}
		
		String str = String.format(format, this.id, this.getTemplate().id, varsString, constsString);
		
		return str;
	}

}

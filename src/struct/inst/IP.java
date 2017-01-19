package struct.inst;


import java.io.*;
import java.util.*;

import org.apache.commons.lang3.*;

import struct.*;
import struct.temp.*;


/** Instance Process **/
public class IP
		extends Instance {
	
	/* ID */
	public String id;

	/* Container */
	public AM cont;
	
	/* Content */
	public Map<String, IC> consts = new LinkedHashMap<String, IC> ();
	public List<AE> arguments = new LinkedList<AE> ();
	public List<AP> processes = new LinkedList<AP> ();

	/* Reference */
	
	/* Back-reference */
	
	/* Template */
	public transient TP template;

	/* Copy */
	public transient List<IQ> equations = new LinkedList<IQ> ();

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
	public TP getTemplate() {
		return this.template;
	}

	@Override
	public void setTemplate(Template template) {
		this.template = (TP) template;
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
		this.templateID = (String) stream.readObject();
		
		// set copy fields
		this.equations = new LinkedList<IQ> ();
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
		TP template = getContainer().getModel().getTemplate().tps.get(templateName);
		setTemplate(template);
		
		
		for (IC ic : this.consts.values()) {
			ic.restoreTemplate();
		}
		for (AE ae : this.arguments) {
			ae.restoreTemplate();
		}
		for (AP ap : this.processes) {
			ap.restoreTemplate();
		}
	}
	
	public boolean isTerminal() {
		return this.template.isTerminal();
	}
	
	public boolean isTopLevel() {
		return this.template.isTopLevel();
	}
	
	public Model getModel() {
		return this.getContainer().getModel();
	}
	
	
	public String toString() {
		String format = "process %s%s : %s {\n%s%s}\n";
		
		StringBuilder argumentsString = new StringBuilder();
		if (this.arguments.size() > 0) {
			argumentsString.append("(");
			argumentsString.append(StringUtils.join(this.arguments, ", "));
			argumentsString.append(")");
		}

		StringBuilder constsString = new StringBuilder();
		if (this.consts.size() > 0) {
			constsString.append("\tconsts:\n\t\t");
			constsString.append(StringUtils.join(this.consts.values(), ",\n\t\t"));
			constsString.append(";\n");
		}
		
		StringBuilder processesString = new StringBuilder();
		if (this.processes.size() > 0) {
			processesString.append("\tprocesses:\n\t\t");
			processesString.append(StringUtils.join(this.processes, ", "));
			processesString.append(";\n");
		}
		
		String str = String.format(format, this.id, argumentsString, this.getTemplate().id, constsString, processesString);
		
		return str;
	}
}

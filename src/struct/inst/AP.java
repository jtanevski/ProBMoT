package struct.inst;



import java.io.*;

import struct.*;
import struct.temp.*;
import util.*;

/** Argument Process **/
public class AP
		extends Instance {

	/* ID */
	public Integer id;

	/* Container */
	public IP ip;

	/* Contains */
	
	/* Reference */
	public IP arg;

	/* Back-reference */
	
	/* Template */
	public transient PP template;
	
	/* Copy */
	public transient TP tp;
	public transient ListMap<String, PE> parameters = new ListMap<String, PE> ();
	
	/* Other */
	/**
	 * is an index, if it's PP is iterated, otherwise meaningless
	 */
	public int index;
	
	@Override
	public Integer getIdI() {
		return this.id;
	}

	@Override
	public void setIdI(Integer id) {
		this.id = (Integer) id;
	}

	@Override
	public IP getContainer() {
		return this.ip;
	}

	@Override
	public void setContainer(Structure container) {
		this.ip = (IP) container;
	}

	@Override
	public PP getTemplate() {
		return this.template;
	}

	@Override
	public void setTemplate(Template template) {
		this.template = (PP) template;
	}
	
	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
		stream.defaultReadObject();

		// set template
		this.templateID = stream.readObject();
		
		// set copy fields
		this.parameters = new ListMap<String, PE> ();
	
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
		Integer templateID = (Integer) this.templateID;
		PP template = getContainer().getTemplate().processes.get(templateID);
		setTemplate(template);
	}
	
	public String toString() {
		return (this.arg!=null)?this.arg.id:"null";
	}
}

package struct.inst;

import java.io.*;

import struct.*;
import struct.temp.*;

/** Instance Variable **/
public class IV
		extends Instance {

	/* ID */
	public String id;

	/* Container */
	public IE ie;

	/* Content */
	public Role role; // = Role.EXOGENOUS;
	public Double initial; // = 0.0;

	/* Reference */

	/* Back-reference */

	/* Template */
	public transient TV template;

	/* Copy */
	public transient Interval range;
	public transient String unit;
	public transient Aggregation agg;

	/* Other */
	double value;
//	private String fullName;

	@Override
	public String getIdS() {
		return this.id;
	}

	@Override
	public void setIdS(String id) {
		this.id = (String) id;
	}

	@Override
	public IE getContainer() {
		return this.ie;
	}

	@Override
	public void setContainer(Structure container) {
		this.ie = (IE) container;
	}

	@Override
	public TV getTemplate() {
		return this.template;
	}

	@Override
	public void setTemplate(Template template) {
		this.template = (TV) template;
	}

//	public String getFullName() {
//		if (fullName == null) {
//			this.fullName = this.ie.id + "." + this.id;
//		}
//		return fullName;
//	}
	
	
	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
		stream.defaultReadObject();

		// set template
		this.templateID = (String) stream.readObject();
		
		// set copy fields
	
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
		TV template = getContainer().getTemplate().vars.get(templateName);
		setTemplate(template);
	}
	
	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append(this.id + " {" );
		buf.append(" role: " + this.role + ";");
		if (this.role == Role.ENDOGENOUS)
			buf.append(" initial: " + this.initial + ";");
		buf.append("}");
		
		return buf.toString();

	}
	
}

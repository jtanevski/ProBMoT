package struct.inst;

import java.io.*;

import struct.*;
import struct.temp.*;

/** Instance Constant **/
public class IC extends Instance {

	/* ID */
	public String id;

	/* Container */
	public Instance cont;

	/* Content */
	public Double value;

	/* Reference */

	/* Back-reference */

	/* Template */
	public transient TC template;

	/* Copy */
	public transient Interval range;
	public transient String unit;

	/* Other */

	// private String fullName;

	@Override
	public String getIdS() {
		return this.id;
	}

	@Override
	public void setIdS(String id) {
		this.id = (String) id;
	}

	@Override
	public Instance getContainer() {
		return this.cont;
	}

	@Override
	public void setContainer(Structure container) {
		this.cont = (Instance) container;
	}

	@Override
	public TC getTemplate() {
		return this.template;
	}

	@Override
	public void setTemplate(Template template) {
		this.template = (TC) template;
	}

	// public String getFullName() {
	// if (this.fullName == null) {
	// this.fullName = cont.getIdS() + "." + this.id;
	// }
	// return this.fullName;
	// }

	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
		stream.defaultReadObject();

		// set template
		this.templateID = stream.readObject();

		// set copy fields

	}

	private void writeObject(ObjectOutputStream stream) throws IOException {
		stream.defaultWriteObject();

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
		TC template;

		Template temp = getContainer().getTemplate();
		// constants can be part of entities or part of processes
		if (temp instanceof TE) {
			template = ((TE) temp).consts.get(templateName);
		} else if (temp instanceof TP) {
			template = ((TP) temp).consts.get(templateName);
		} else {
			throw new IllegalStateException();
		}
		setTemplate(template);

	}
	
	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append(this.id + " = " + ((this.value == null) ? 0 : this.value));
		
		return buf.toString();
	}


}

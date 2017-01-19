package struct.inst;

import java.io.*;

import struct.*;
import struct.temp.*;
import util.*;

/** Argument Entity **/
public class AE
		extends Instance {

	/* ID */
	public Integer id;

	/* Container */
	public IP ip;

	/* Contains */

	/* Reference */
	public ListMap<String, IE> args = new ListMap<String, IE>();

	/* Back-reference */

	/* Template */
	public transient PE template;

	/* Copy */
	public transient TE te;
	public transient Interval card;

	/* Other */
	String name;

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
	public PE getTemplate() {
		return this.template;
	}

	@Override
	public void setTemplate(Template template) {
		this.template = (PE) template;
	}

	private void readObject(ObjectInputStream stream)
			throws IOException, ClassNotFoundException {
		stream.defaultReadObject();

		// set template
		this.templateID = stream.readObject();

		// set copy fields

	}

	private void writeObject(ObjectOutputStream stream)
			throws IOException {
		stream.defaultWriteObject();

		// NOTE: at this moment it's not clear whether
		// full-name or ID (string or integer) should be used, could be revised later on
		Object id;
		try {
			id = getTemplate().getIdS();
		} catch (UnsupportedOperationException ex) {
			id = getTemplate().getIdI();
		}
		stream.writeObject(id);
	}

	public void restoreTemplate() {
		String templateName = (String) templateID;
		PE template = getContainer().getTemplate().parameters.get(templateName);
		setTemplate(template);
	}

	public String toString() {
		StringBuilder buf = new StringBuilder();

		if (this.args.size() == 0) {
			buf.append("[]"); // NOTE: this doesn't appear anywhere yet :)
		} else if (this.args.size() == 1) {
			buf.append(this.args.getValue(0).id);
		} else { // args.size() > 1
			buf.append("[" + this.args.getKey(0));
			for (int i = 1; i < this.args.size(); i++) {
				buf.append(", " + this.args.getKey(i));
			}
			buf.append("]");

		}
		return buf.toString();
	}
}

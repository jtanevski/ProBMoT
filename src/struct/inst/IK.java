package struct.inst;


import java.io.*;
import java.util.*;

import org.apache.commons.lang3.*;

import struct.*;
import struct.temp.*;


/** Instance Compartment **/
public class IK
		extends AM {

	/* ID */
	public String id;

	/* Container */
	public AM cont;

	/* Reference */

	/* Back-reference */

	/* Template */
	public transient TK template;

	/* Copy */
	public transient Map<String, TE> tes = new LinkedHashMap<String, TE> ();
	public transient Map<String, TP> tps = new LinkedHashMap<String, TP> ();

	/* Other */



	@Override
	public String getIdS() {
		return id;
	}

	@Override
	public void setIdS(String id) {
		this.id = (String) id;
	}

	@Override
	public AM getContainer() {
		return cont;
	}

	@Override
	public void setContainer(Structure container) {
		this.cont = (AM) container;
	}

	@Override
	public TK getTemplate() {
		return this.template;
	}

	@Override
	public void setTemplate(Template template) {
		this.template = (TK) template;
	}


	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
		stream.defaultReadObject();

		// set template
		this.templateID = (String) stream.readObject();


		// set copy fields
		this.tes = new LinkedHashMap<String, TE> ();
		this.tps = new LinkedHashMap<String, TP> ();

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

		// get to the model that contains this Compartment
		AM cont = getContainer();
		while (!(cont instanceof Model)) {
			cont = cont.getContainer();
		}
		Model model = (Model) cont;
		setTemplate(model.getTemplate().tks.get(templateName));


		// recurse
		for (IE ie : this.ies.values()) {
			ie.restoreTemplate();
		}
		for (IP ip : this.ips.values()) {
			ip.restoreTemplate();
		}
		for (IK ik : this.iks.values()) {
			ik.restoreTemplate();
		}


	}

	public String toString() {
		String format = "compartment %s : %s {\n\n//Entities\n\n%s\n//Processes\n\n%s//Compartments\n\n%s}\n";

		String entitiesString =  StringUtils.join(this.ies.values(), "\n");
		String processesString = StringUtils.join(this.ips.values(), "\n");
		String compartmentsString = StringUtils.join(this.iks.values(), "\n");

		String str = String.format(format, this.id, this.getTemplate().getIdS(), entitiesString, processesString, compartmentsString);

		return str;
	}

}

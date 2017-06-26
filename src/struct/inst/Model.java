package struct.inst;

import java.io.*;
import java.util.*;

import org.apache.commons.lang3.*;

import struct.*;
import struct.temp.*;
import traverse.*;

public class Model extends AM {
	public static final Map<String, Model> MODELS = new LinkedHashMap<String, Model>();

	/* ID */
	public String id;

	/* Container */

	/* Reference */

	/* Back-reference */

	/* Template */
	public transient Library template;

	/* Copy */
	public transient Map<String, TE> tes = new LinkedHashMap<String, TE>();
	public transient Map<String, TP> tps = new LinkedHashMap<String, TP>();
	public transient Map<String, TK> tks = new LinkedHashMap<String, TK>();

	/* Other */
	public transient Map<String, Library> libraryLookup;

	public Map<String, IV> allVars = new LinkedHashMap<String, IV>();
	public Map<String, IC> allConsts = new LinkedHashMap<String, IC>();

	/**
	 * lookup, for each template entity gives all instance entities that are of
	 * that type gives direct instances and instances of sub-types
	 */
	public Map<String, List<IE>> TEtoIEs = new LinkedHashMap<String, List<IE>>();

	/**
	 * lookup, for each template process gives all instance processes that are of
	 * that type gives direct instances and instances of sub-types
	 */
	public Map<String, List<IP>> TPtoIPs = new LinkedHashMap<String, List<IP>>();

	/**
	 * A list containing all top level instance processes. They are the starting
	 * point of the search
	 */
	public Map<String, IP> topLevelIPs = new LinkedHashMap<String, IP>();

	@Override
	public String getIdS() {
		return this.id;
	}

	@Override
	public void setIdS(String id) {
		this.id = (String) id;
	}

	@Override
	public AM getContainer() {
		return null;
	}

	@Override
	public void setContainer(Structure container) {
		if (container != null)
			throw new UnsupportedOperationException("Cannot set a container for model");
	}

	@Override
	public Library getTemplate() {
		return this.template;
	}

	@Override
	public void setTemplate(Template template) {
		this.template = (Library) template;
	}

	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
		stream.defaultReadObject();

		// set template
		this.templateID = (String) stream.readObject();

		// set copy fields
		this.tes = new LinkedHashMap<String, TE>();
		this.tps = new LinkedHashMap<String, TP>();
		this.tks = new LinkedHashMap<String, TK>();

	}

	private void writeObject(ObjectOutputStream stream) throws IOException {
		stream.defaultWriteObject();

		// NOTE: at this moment it's not clear whether
		// full-name or ID (string or integer) should be used, could be revised
		// later on
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
		this.libraryLookup = Library.LIBRARIES;
		setTemplate(libraryLookup.get(templateName));

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

	private boolean hasCounted = false;

	public void upkeep() {
		count();
		buildTEtoIEs();
		buildTPtoIPs();
		determineTopLevelIPs();
	}

	public void count() {

		this.allVars = new LinkedHashMap<String, IV>();
		this.allConsts = new LinkedHashMap<String, IC>();

		for (IE ie : this.iesRec.values()) {
			for (IC ic : ie.consts.values()) {
				this.allConsts.put(ic.getFullName(), ic);
			}
			for (IV iv : ie.vars.values()) {
				this.allVars.put(iv.getFullName(), iv);
			}
		}

		// counting of process constants is redone because processes can be dynamically added and removed during search
		for (IP ip : this.ipsRec.values()) {
			for (IC ic : ip.consts.values()) {
				this.allConsts.put(ic.getFullName(), ic);
			}
		}

	}

	/**
	 * this would not work with compartments, there each AM should have its own
	 * loookup structures
	 */
	void buildTEtoIEs() {
		TEtoIEs = new LinkedHashMap<String, List<IE>>();
		for (IE ie : this.iesRec.values()) {
			TE te = ie.getTemplate();
			do {

				if (TEtoIEs.containsKey(te.getFullName())) {
					TEtoIEs.get(te.getFullName()).add(ie);
				} else {
					List<IE> ies = new ArrayList<IE>();
					ies.add(ie);
					TEtoIEs.put(te.getFullName(), ies);
				}

				te = te.zuper;
			} while (te != null);

		}
	}

	/**
	 * this would not work with compartments, there each AM should have its own
	 * loookup structures
	 */
	void buildTPtoIPs() {
		TPtoIPs = new LinkedHashMap<String, List<IP>>();
		for (IP ip : this.ipsRec.values()) {
			TP tp = ip.getTemplate();
			do {

				if (TPtoIPs.containsKey(tp.getFullName())) {
					TPtoIPs.get(tp.getFullName()).add(ip);
				} else {
					List<IP> ips = new ArrayList<IP>();
					ips.add(ip);
					TPtoIPs.put(tp.getFullName(), ips);
				}

				tp = tp.zuper;
			} while (tp != null);

		}
	}

	private void determineTopLevelIPs() {
		this.topLevelIPs = new LinkedHashMap<String, IP>(this.ipsRec);

		for (IP ip : this.ipsRec.values()) {
			if (!ip.isTopLevel()) {
				this.topLevelIPs.remove(ip.getFullName());
			}
		}

	}

	public Model copy() {
		Model copied;
		try {
			copied = (Model) SerializationUtils.clone(this);
			copied.restoreTemplate();
			Traverse.composeModel(copied);
			Traverse.composeIQs(copied);
			copied.count();

			return copied;

		} catch (IllegalAccessException ex) {
			throw new RuntimeException(ex);
		} catch (SecurityException ex) {
			throw new RuntimeException(ex);
		} catch (IllegalArgumentException ex) {
			throw new RuntimeException(ex);
		} catch (NoSuchFieldException ex) {
			throw new RuntimeException(ex);
		}
	}


	public Model copyNoEQ() {
		Model copied;
		try {
			copied = (Model) SerializationUtils.clone(this);
			copied.restoreTemplate();
			Traverse.composeModel(copied);
			copied.count();

			return copied;

		} catch (IllegalAccessException ex) {
			throw new RuntimeException(ex);
		} catch (SecurityException ex) {
			throw new RuntimeException(ex);
		} catch (IllegalArgumentException ex) {
			throw new RuntimeException(ex);
		} catch (NoSuchFieldException ex) {
			throw new RuntimeException(ex);
		}
	}

	public String toString() {
		String format = "model %s : %s;\n\n//Entities\n\n%s\n//Processes\n\n%s//Compartments\n\n%s";

		String entitiesString =  StringUtils.join(this.ies.values(), "\n");
		String processesString = StringUtils.join(this.ips.values(), "\n");
		String compartmentsString = StringUtils.join(this.iks.values(), "\n");

		String str = String.format(format, this.id, this.getTemplate().getIdS(), entitiesString, processesString, compartmentsString);

		return str;
	}

	public String toProcessList() {
		List<String> processStrings = new LinkedList<String>();

	/*
		for (IP ip : this.ips.values()) {
			StringBuilder processString = new StringBuilder();
			processString.append(ip.getTemplate().getIdS());
			for (AE ae : ip.arguments) {
				String argString = StringUtils.join(ae.args.keyList(), "_");
				processString.append("__" + argString);
			}
			processStrings.add(processString.toString());
		}
	*/
		for (IP ip : this.ips.values()) {
			StringBuilder processString = new StringBuilder();
			processString.append(ip.getTemplate().getIdS());

			if (ip.arguments.size() > 0) {
				processString.append("(");
				processString.append(StringUtils.join(ip.arguments, ", "));
				processString.append(")");
			}

			processStrings.add(processString.toString().replaceAll(" ", ""));

		}

		return StringUtils.join(processStrings, " ");

	}

}

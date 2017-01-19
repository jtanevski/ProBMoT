package struct.temp;


import java.util.*;

import org.apache.commons.lang3.*;

import struct.*;
import struct.inst.*;


/** Template Entity **/
public class TE
		extends Template {

	/* ID */
	public String id;

	/* Container */
	public Library lib;

	/* Content */
	public Map<String, TV> vars = new LinkedHashMap<String, TV>();
	public Map<String, TC> consts = new LinkedHashMap<String, TC>();

	/* Reference */
	public TE zuper;

	/* Back reference */
	public Map<String, TE> subs = new LinkedHashMap<String, TE>();
	public Map<String, IE> instances = new LinkedHashMap<String, IE>();

	/* Other */

	public TE() {
		super();
	}

	public TE(String id, Library lib) {
		this.id = id;
		this.lib = lib;
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
	public Library getContainer() {
		return this.lib;
	}

	@Override
	public void setContainer(Structure container) {
		this.lib = (Library) container;
	}

	public boolean isEqualOrSuccessorOf(TE te) {
		TE current = this;
		while (current != null) {
			if (current == te) {
				return true;
			}
			current = current.zuper;
		}
		return false;
	}

	public boolean isEqualOrAncestorOf(TE te) {
		return te.isEqualOrSuccessorOf(this);
	}

	public boolean isSuccessorOf(TE te) {
		TE current = this.zuper;
		while (current != null) {
			if (current == te) {
				return true;
			}
			current = current.zuper;
		}
		return false;
	}

	public boolean isAncestorOF(TE te) {
		return te.isSuccessorOf(this);
	}
	
	public String toString() {
		String format = "template entity %s%s {\n%s%s}\n"; 

		String superString;
		if (this.zuper == null) {
			superString = " : null";
		} else if (this.zuper==this.getContainer().ENTITY) {
			superString = "";
		} else {
			superString = " : " + this.zuper.id;
		}

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
		
		String str = String.format(format, this.id, superString, varsString, constsString);
		
		return str;
	}

}

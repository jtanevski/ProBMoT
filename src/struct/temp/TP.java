package struct.temp;


import java.util.*;

import org.apache.commons.lang3.*;

import struct.*;
import struct.inst.*;
import util.*;

/** Template Process **/
public class TP
		extends Template {
	
	/* ID */
	public String id;

	/* Container */
	public Library lib;

	/* Contain */
	public Map<String, TC> consts = new LinkedHashMap<String, TC> ();
	public ListMap<String, PE> parameters = new ListMap<String, PE> ();
	public List<PP> processes = new LinkedList<PP> ();
	public List<TQ> equations = new LinkedList<TQ> ();

	/* Reference */
	public TP zuper;
	
	/* Back-reference */
	public Map<String, TP> subs = new LinkedHashMap<String, TP> ();
	public Map<String, IP> instances = new LinkedHashMap<String, IP> ();

	/* Other */

	public TP() {
		super();
	}
	
	public TP(String id, Library lib) {
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
	public void setContainer(Structure container) {
		this.lib = (Library) container;
	}
	
	@Override
	public Library getContainer() {
		return this.lib;
	}

	public boolean isEqualOrSuccessorOf(TP tp) {
		TP current = this;
		while (current != null) {
			if (current == tp) {
				return true;
			}
			current = current.zuper;
		}
		return false;
	}

	public boolean isEqualOrAncestorOf(TP tp) {
		return tp.isEqualOrSuccessorOf(this);
	}
	
	public boolean isSuccessorOf(TP tp) {
		TP current = this.zuper;
		while (current != null) {
			if (current == tp) {
				return true;
			}
			current = current.zuper;
		}
		return false;
		
	}
	
	public boolean isAncestorOf(TP tp) {
		return tp.isSuccessorOf(this);
	}
	
	/**
	 * whether is at the level of the model or a component process of another process
	 * @return
	 */
	public boolean isTopLevel() {
		return this.lib.isTopLevel(this);
	}

	public boolean isTerminal() {
		return this.subs.isEmpty();
	}
	
	public String toString() {
		String format = "template process %s%s%s {\n%s%s%s}\n";
		
		String superString;
		if (this.zuper == null) {
			superString = " : null";
		} else if (this.zuper==this.getContainer().PROCESS) {
			superString = "";
		} else {
			superString = " : " + this.zuper.id;
		}

		StringBuilder parametersString = new StringBuilder();
		if (this.parameters.size() > 0) {
			parametersString.append("(");
			parametersString.append(StringUtils.join(this.parameters.values(), ", "));
			parametersString.append(")");
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
		
		StringBuilder equationsString = new StringBuilder();
		if (this.equations.size() > 0) {
			equationsString.append("\tequations:\n\t\t");
			equationsString.append(StringUtils.join(this.equations, ",\n\t\t"));
			equationsString.append(";\n");
		}
		String str = String.format(format, this.id, parametersString, superString, constsString, processesString, equationsString);
		
		return str;
	}

}

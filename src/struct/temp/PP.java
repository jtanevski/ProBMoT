package struct.temp;


import struct.*;
import util.*;

/** Parameter Process **/
public class PP
		extends Template {

	/* ID */
	public Integer id;

	/* Container */
	public TP cont;

	/* Content */
	
	/* Reference */
	public TP tp;
	public ListMap<String, PE> parameters = new ListMap<String, PE> ();

	/* Back-reference */
	
	/* Other */
	String name; /* artificial */

	public PE iterated;					// The real PE which is iterated -  ns in <n:ns>
	public PE iterator;					// The artificial PE for iteration - n in <n:ns>
	public boolean isIter;				// is iterated?
	
	@Override
	public TP getContainer() {
		return this.cont;
	}

	@Override
	public Integer getIdI() {
		return this.id;
	}

	@Override
	public void setContainer(Structure container) {
		this.cont = (TP) container;
	}

	@Override
	public void setIdI(Integer id) {
		this.id = (Integer) id;
	}
	
	public String toString() {
		String format = "%s%s";
		
		StringBuilder parametersString = new StringBuilder();
		if (!this.parameters.isEmpty()) {
			parametersString.append("(");
			PE param = parameters.getValue(0);
			parametersString.append((param==iterator)?("<"+param.id+":"+iterated.id+">"):param.id);
			for (int i = 1; i < parameters.size(); i++) {
				param = parameters.getValue(i);
				parametersString.append(", " + ((param==iterator)?("<"+param.id+":"+iterated.id+">"):param.id));
			}
			parametersString.append(")");
		}
		String str = String.format(format, this.tp.id, parametersString);
		
		return str;
	}

}

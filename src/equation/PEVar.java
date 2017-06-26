/**
 * 
 */
package equation;

import struct.temp.*;

public class PEVar
		extends PEAttr {

	public PE pe;
	public TV var;

	public PEVar(PE pe, TV var) {
		this.pe = pe;
		this.var = var;

		this.id = pe.id + "." + var.id;
	}
}
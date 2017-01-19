/**
 * 
 */
package equation;

import struct.*;
import struct.temp.*;

public class PEConst
		extends PEAttr {

	public PE pe;
	public TC cons;

	public PEConst(PE pe, TC cons) {
		this.pe = pe;
		this.cons = cons;

		this.id = pe.id + "." + cons.id;
	}
}
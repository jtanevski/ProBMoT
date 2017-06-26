/**
 * 
 */
package equation;

import struct.temp.*;

public class TCRef
		extends PEAttrRef {
	@SuppressWarnings("ucd")
	transient TC cons;

	public TCRef(TC cons) {
		this.cons = cons;
		this.id = cons.id;
	}

}
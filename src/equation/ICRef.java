/**
 * 
 */
package equation;

import struct.inst.*;

public class ICRef
		extends AEAttrRef {
	@SuppressWarnings("ucd")
	transient IC cons;

	public ICRef(IC cons) {
		this.cons = cons;
		this.id = cons.getFullName();
	}
}
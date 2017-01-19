/**
 * 
 */
package equation;

import struct.*;
import struct.inst.*;

public class ICRef
		extends AEAttrRef {
	transient IC cons;

	public ICRef(IC cons) {
		this.cons = cons;
		this.id = cons.getFullName();
	}
}
/**
 * 
 */
package equation;

import struct.*;
import struct.temp.*;

public class TCRef
		extends PEAttrRef {
	transient TC cons;

	public TCRef(TC cons) {
		this.cons = cons;
		this.id = cons.id;
	}

}
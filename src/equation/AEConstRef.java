/**
 * 
 */
package equation;

import struct.inst.*;

public class AEConstRef
		extends AEAttrRef {

//	transient AEConst aeConst;
//	public AEConstRef(AEConst aeConst) {
//		this.aeConst = aeConst;
//		this.id = aeConst.id;
//	}
	@SuppressWarnings("ucd")
	transient IC ic;
	public AEConstRef(IC ic) {
		this.ic = ic;
		this.id = ic.getFullName();
	}

}
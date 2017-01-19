/**
 * 
 */
package equation;

import struct.*;
import struct.inst.*;

public class AEVarRef
		extends AEAttrRef {

//	transient AEVar aeVar;
//	public AEVarRef(AEVar aeVar) {
//		this.aeVar = aeVar;
//		this.id = aeVar.id;
//	}
	
	transient IV iv;
	public AEVarRef(IV iv) {
		this.iv = iv;
		this.id = iv.getFullName();
	}

	public IV getIV() {
		return this.iv;
	}
}
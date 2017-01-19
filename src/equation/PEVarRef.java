/**
 * 
 */
package equation;

public class PEVarRef
		extends PEAttrRef {
	transient PEVar peVar;

	public PEVarRef(PEVar peVar) {
		this.peVar = peVar;
		this.id = peVar.id;
	}
	
	public PEVar getPEVar() {
		return this.peVar;
	}

}
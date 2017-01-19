/**
 * 
 */
package equation;

public class PEConstRef
		extends PEAttrRef {
	transient PEConst peConst;

	public PEConstRef(PEConst peConst) {
		this.peConst = peConst;
		this.id = peConst.id;
	}

}
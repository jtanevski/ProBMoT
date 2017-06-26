/**
 * 
 */
package equation;

public class PEConstRef
		extends PEAttrRef {
	@SuppressWarnings("ucd")
	transient PEConst peConst;

	public PEConstRef(PEConst peConst) {
		this.peConst = peConst;
		this.id = peConst.id;
	}

}
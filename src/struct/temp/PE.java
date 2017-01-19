package struct.temp;

import struct.*;


/** Parameter Entity **/
public class PE
		extends Template {
	
	/* ID */
	public Integer idI;
	public String id;

	/* Container */
	public TP tp;

	/* Content */
	public Interval card; // = new Interval(1);

	/* Reference */
	public TE te;
	
	/* Back-reference */

	/* Other */
	Integer pos;
	String name;

	@Override
	public TP getContainer() {
		return this.tp;
	}

	@Override
	public void setContainer(Structure container) {
		this.tp = (TP) container;
	}

	@Override
	public String getIdS() {
		return this.id;
	}

	@Override
	public void setIdS(String id) {
		this.id = (String) id;
	}

	public Integer getIdI() {
		return this.idI;
	}
	
	public void setIdI(Integer id) {
		this.idI = id;
	}
	
	public String toString() {
		String format = "%s : %s%s";
		String str = String.format(format, this.id, this.te.id, this.card);
		
		return str;
	}
}

package struct.temp;

import struct.*;

/** Template Variable **/
public class TV
		extends Template {
	
	/* ID */
	public String id;

	/* Container */
	public TE te;

	/* Content */
	public Interval range; // = new Interval(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
	public String unit; // = "";
	public Aggregation agg; // =  ;
	
	/* Reference */
	
	/* Back-reference */
	
	/* Other */

	@Override
	public TE getContainer() {
		return this.te;
	}

	@Override
	public String getIdS() {
		return this.id;
	}

	@Override
	public void setContainer(Structure container) {
		this.te = (TE) container;
	}

	@Override
	public void setIdS(String id) {
		this.id = (String) id;
	}
	
	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append(this.id + " {" );
		buf.append(" range: " + this.range + ";");
		buf.append(" unit: \"" + this.unit + "\";");
		buf.append(" aggregation: " + this.agg + ";");
		buf.append("}");
		
		return buf.toString();
	}

}

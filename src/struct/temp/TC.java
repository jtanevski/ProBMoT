package struct.temp;

import struct.*;




/** Template Constant **/
public class TC extends Template{
	
	/* ID */
	public String id;
	
	/* Container */
	public Template cont;
	
	/* Content */
	public Interval range; // = new Interval(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
	public String unit; // = "";
	
	/* Reference */
	
	/* Back-reference */
	
	/* Other */
	
	
	
	
	@Override
	public Template getContainer() {
		return this.cont;
	}
	
	@Override
	public String getIdS() {
		return this.id;
	}
	
	@Override
	public void setContainer(Structure container) {
		this.cont = (Template) container;
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
		buf.append("}");
		
		return buf.toString();
	}

}

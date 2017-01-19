package struct;

import java.io.*;


public class Interval implements Serializable {
	private double lower;
	private double upper;
	
	
	public Interval(double lower, double upper) {
		this.lower = lower;
		this.upper = upper;
	}
	
	public Interval(double single) {
		this.lower = single;
		this.upper = single;
	}
	
	
	public double getLower() {
		return this.lower;
	}
	public void setLower(double lower) {
		this.lower = lower;
	}
	
	public double getUpper() {
		return this.upper;
	}
	public void setUpper(double upper) {
		this.upper = upper;
	}
	
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append("<");
		
		int lowerInt = (int) this.lower;
		int upperInt = (int) this.upper;
		
		if (lowerInt == this.lower) {
			buffer.append(lowerInt);
		} else if (Double.isInfinite(this.lower)){
			buffer.append(((this.lower<0)?"-":"") + "inf");
		} else {
			buffer.append(this.lower);
		}
		buffer.append(",");
		if (upperInt == this.upper) {
			buffer.append(upperInt);
		} else if (Double.isInfinite(this.upper)){
			buffer.append(((this.upper<0)?"-":"") + "inf");
		} else {
			buffer.append(this.upper);
		}
		
		buffer.append(">");
		return buffer.toString();
	}

	public boolean contains(Interval card) {
		if (this.lower < card.lower)
			return false;
		if (this.upper > card.upper)
			return false;
		
		return true;
	}
}

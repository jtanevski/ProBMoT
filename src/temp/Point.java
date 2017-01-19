package temp;

public class Point implements Comparable<Point>{
	
	private Double value;
	private double confidence;
	
	public Point (double val, double conf)
	{
		this.setValue(val);
		this.setConfidence(conf);
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public double getConfidence() {
		return confidence;
	}

	public void setConfidence(double confidence) {
		this.confidence = confidence;
	}

	@Override
	public int compareTo(Point o) {
	
		return value.compareTo(o.getValue());
	}
	
	

}

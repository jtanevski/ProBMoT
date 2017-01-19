package struct;

public enum Aggregation {
	SUM('+'),
	PRODUCT('*'),
	AVERAGE('='),
	MININUM('m'),
	MAXIMUM('M');
	
	private Character charac;
	
	private Aggregation(Character charac) {
		this.charac = charac;
	}

	public String toString() {
		return super.toString().toLowerCase();
	}

	public Character toChar() {
		return this.charac;
	}
}

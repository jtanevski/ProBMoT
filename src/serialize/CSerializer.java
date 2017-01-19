package serialize;

import equation.*;

public class CSerializer
		extends StandardSerializer {

	public CSerializer() {
		// standard operator symbols
		this.bOperatorSigns.put(Plus.class, "+");
		this.bOperatorSigns.put(Minus.class, "-");
		this.bOperatorSigns.put(Times.class, "*");
		this.bOperatorSigns.put(Slash.class, "/");
		this.uOperatorSigns.put(UMinus.class, "-");

		// standard function names

		this.uFunctionNames.put(Sinus.class, "sin");
		this.uFunctionNames.put(Exponential.class, "exp");
		this.uFunctionNames.put(Logarithm.class, "log");
		this.uFunctionNames.put(Logarithm10.class, "log10");	
		this.uFunctionNames.put(Sign.class, "sign");
		this.bFunctionNames.put(Power.class, "pow");
		this.bFunctionNames.put(Minimum.class, "min");
		this.bFunctionNames.put(Maximum.class, "max");
		
		//	this.uFunctionNames.put(DataMax.class, "datamax");
		//	this.uFunctionNames.put(DataMin.class, "datamin");
		
		

		// other standard elements
		this.eqString = "=";

		this.lParen = "(";
		this.rParen = ")";

		this.lFunct = "(";
		this.rFunct = ")";

		this.dotString = "_";
		this.delimString = ",";

		this.numberString = "%.5g";
		
		buildNodeSerializers();     // NOTE:must be the last line of every constructor that inherits Standard Serializer!!
	}
}

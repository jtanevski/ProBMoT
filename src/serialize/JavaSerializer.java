package serialize;

import equation.*;

public class JavaSerializer
		extends StandardSerializer {

	public JavaSerializer() {
		// standard operator symbols
		this.bOperatorSigns.put(Plus.class, "+");
		this.bOperatorSigns.put(Minus.class, "-");
		this.bOperatorSigns.put(Times.class, "*");
		this.bOperatorSigns.put(Slash.class, "/");
		this.uOperatorSigns.put(UMinus.class, "-");

		// standard function names

		this.uFunctionNames.put(Sinus.class, "Math.sin");
		this.uFunctionNames.put(Exponential.class, "Math.exp");
		this.uFunctionNames.put(Logarithm.class, "Math.log");
		this.uFunctionNames.put(Logarithm10.class, "Math.log10");	
		this.uFunctionNames.put(Sign.class, "Math.signum");
		this.bFunctionNames.put(Power.class, "Math.pow");
		this.bFunctionNames.put(Minimum.class, "Math.min");
		this.bFunctionNames.put(Maximum.class, "Math.max");
		
		
		

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

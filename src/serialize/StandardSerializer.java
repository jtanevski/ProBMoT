package serialize;

import java.util.*;

import serialize.node.*;
import struct.*;
import struct.inst.*;
import equation.*;
import equation.Number;

public abstract class StandardSerializer extends BaseSerializer {
	public Map<Class<? extends BOperator>, String> bOperatorSigns = new LinkedHashMap<Class<? extends BOperator>, String>();
	public Map<Class<? extends UOperator>, String> uOperatorSigns = new LinkedHashMap<Class<? extends UOperator>, String>();
	public Map<Class<? extends Function>, String> uFunctionNames = new LinkedHashMap<Class<? extends Function>, String>();
	public Map<Class<? extends Function>, String> bFunctionNames = new LinkedHashMap<Class<? extends Function>, String>();

	public String lParen;
	public String rParen;

	public String lFunct;
	public String rFunct;

	public String eqString;

	public String dotString;
	public String delimString;

	public String numberString;

	@Override
	protected String serializeEQ() {
		return this.eqString;
	}

	@Override
	protected String serializeLHS(IV lhs, EQType type) {

		return lhs.getFullName().replace(".", dotString) + ((type == EQType.Differential) ? (dotString + "dot") : "");
	}

	protected void buildNodeSerializers() {
		for (Map.Entry<Class<? extends BOperator>, String> entry : this.bOperatorSigns.entrySet()) {
			this.serializers.put(entry.getKey(), new BOperatorSerializer(entry.getValue(), this.lParen, this.rParen));
		}
		for (Map.Entry<Class<? extends UOperator>, String> entry : this.uOperatorSigns.entrySet()) {
			this.serializers.put(entry.getKey(), new UOperatorSerializer(entry.getValue(), this.lParen, this.rParen));
		}
		for (Map.Entry<Class<? extends Function>, String> entry : this.uFunctionNames.entrySet()) {
			this.serializers.put(entry.getKey(), new FunctionSerializer(entry.getValue(), 1, this.lFunct, this.rFunct, this.delimString));
		}
		for (Map.Entry<Class<? extends Function>, String> entry : this.bFunctionNames.entrySet()) {
			this.serializers.put(entry.getKey(), new FunctionSerializer(entry.getValue(), 2, this.lFunct, this.rFunct, this.delimString));
		}
		this.serializers.put(AEVarRef.class, new RefSerializer(this.dotString));
		this.serializers.put(AEConstRef.class, new RefSerializer(this.dotString));
		this.serializers.put(ICRef.class, new RefSerializer(this.dotString));

		this.serializers.put(Number.class, new NumberSerializer(this.numberString));
	}

	public String serialize(IV iv) {
		return serialize(iv, false);
	}

	public String serialize(IV iv, boolean isDeriv) {
		if (iv.id.equals("t")) {
			return "time";								// FIXME: :)
		} else {
			return this.serializers.get(AEVarRef.class).serialize(new AEVarRef(iv)) + (isDeriv ? (dotString + "dot") : "");
		}
	}

	public String serialize(IC ic) {
		return this.serializers.get(ICRef.class).serialize(new ICRef(ic));
	}
}

package serialize;

import java.util.*;

import org.apache.commons.lang.*;

import serialize.node.*;
import struct.*;
import struct.inst.*;
import equation.*;

public interface EquationSerializer {
	public abstract String serialize(IQ iq);
	public abstract String serialize(IV iv);
	public abstract String serialize(IV iv, boolean isDeriv);
	public abstract String serialize(IC ic);
	public abstract String serialize(Node node);
}

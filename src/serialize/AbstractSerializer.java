package serialize;

import struct.*;
import struct.inst.*;
import equation.*;

public abstract class AbstractSerializer
		implements EquationSerializer {
	public String serialize(IQ iq) {
		StringBuilder buffer = new StringBuilder();

		buffer.append(serializeLHS(iq.getLHS(), iq.type));
		buffer.append(" " + serializeEQ() + " ");
		buffer.append(serialize(iq.getRHS()));

		return buffer.toString();
	}

	public String serialize(Node node) {
		String[] childStrings = new String[node.getChildCount()];
		for (int i = 0; i < node.getChildCount(); i++) {
			childStrings[i] = serialize(node.getChild(i));
		}
		return serializeNode(node, childStrings);

		// return String.format(this.formats.get(node.getClass()), childStrings);
	}

	protected abstract String serializeLHS(IV lhs, EQType type);

	protected abstract String serializeEQ();

	protected abstract String serializeNode(Node node, String... children);

}

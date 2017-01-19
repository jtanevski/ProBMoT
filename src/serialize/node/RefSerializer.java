/**
 * 
 */
package serialize.node;

import org.apache.commons.lang3.*;

import equation.*;

public class RefSerializer<T extends Ref> extends NodeSerializer<T> {
	String dotString;
	String prepend;

	public RefSerializer(String dotString, String prepend) {
		this.dotString = dotString;
		this.prepend = prepend;
	}

	public RefSerializer(String dotString) {
		this(dotString, null);
	}

	@Override
	public String serialize(T node, String... children) {
		if (node instanceof AEVarRef && ((AEVarRef)node).getIV().id.equals("t")) {		//FIXME: :)
			return "t";
		} else {
			String nodeName = node.toString();
			String newName = StringUtils.defaultString(prepend) + nodeName.replace(".", dotString);
			return newName;
		}
	}

}
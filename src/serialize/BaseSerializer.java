package serialize;

import java.util.*;

import org.slf4j.*;

import serialize.node.*;
import equation.*;

public abstract class BaseSerializer
		extends AbstractSerializer {
	public static final Logger logger = LoggerFactory.getLogger(BaseSerializer.class);

	public Map<Class, NodeSerializer> serializers = new LinkedHashMap<Class, NodeSerializer>();

	protected String serializeNode(Node node, String... children) {
		try {
			return this.serializers.get(node.getClass()).serialize(node, children);
		} catch (NullPointerException ex) {
			//logger.warn("Serializer for type '{}' is not available - toString() is used", new Object[] { node.getClass() });
			return node.toString();
		}
	}
}

package xml;

import org.eclipse.persistence.oxm.annotations.*;

@XmlDiscriminatorNode("@method")
public abstract class FitterSpec {
}
package xml;

import javax.xml.bind.annotation.XmlElement;

import org.eclipse.persistence.oxm.annotations.*;

@XmlDiscriminatorValue("LeaveOneOut")
public  class LeaveOneOutSpec extends EvalSpec{

}
package xml;

import javax.xml.bind.annotation.XmlElement;

import org.eclipse.persistence.oxm.annotations.*;

@XmlDiscriminatorValue("TrainTest")
public  class TrainTestSpec extends EvalSpec{

}
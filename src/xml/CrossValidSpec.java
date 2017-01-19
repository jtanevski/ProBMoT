package xml;

import javax.xml.bind.annotation.XmlElement;

import org.eclipse.persistence.oxm.annotations.*;

@XmlDiscriminatorValue("CrossValidation")
public  class CrossValidSpec extends EvalSpec{

}
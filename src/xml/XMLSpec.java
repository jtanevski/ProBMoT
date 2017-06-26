package xml;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

import javax.xml.bind.*;
import javax.xml.bind.annotation.*;


import org.antlr.runtime.*;
import org.slf4j.*;
import org.xml.sax.*;

import serialize.*;
import struct.inst.*;
import struct.temp.*;
import temp.*;
import traverse.*;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "root")
public class XMLSpec {
	public static final Logger logger = LoggerFactory.getLogger(XMLSpec.class);

	public static final Class[] classes = {XMLSpec.class, FitterSpec.class, DESpec.class};

	@XmlElement
	Settings settings;

	@XmlElement
	Object criterion;

	@XmlElement
	ProblemSpec problem;

	private XMLSpec() {
	}

	public XMLSpec(Settings settings, Object criterion, IQGraph graph, List<DatasetSpec> data, Mappings mappings,
			EquationSerializer serializer, List<ModelVar> output) {
		this.settings = settings;
		this.criterion = criterion;

		this.problem = new ProblemSpec(data, mappings, graph, serializer, output);

	}

	public static XMLSpec unmarshal(String filepath)
			throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(classes);

		// String schemaLang = "http://www.w3.org/2001/XMLSchema";
		// SchemaFactory factory = SchemaFactory.newInstance(schemaLang);
		// Schema schema = factory.newSchema(new StreamSource("conf/schema1.xsd"));

		Unmarshaller u = jaxbContext.createUnmarshaller();
		// u.setSchema(schema);

		XMLSpec spec = (XMLSpec) u.unmarshal(new File(filepath));

		logger.info("Spec '" + filepath + "' read successfully");

		return spec;
	}

	public static void marshal(XMLSpec xmlSpec, String filepath)
			throws JAXBException, SAXException {
		JAXBContext jaxbContext = JAXBContext.newInstance(classes);

		// String schemaLang = "http://www.w3.org/2001/XMLSchema";
		// SchemaFactory factory = SchemaFactory.newInstance(schemaLang);
		// Schema schema = factory.newSchema(new StreamSource("conf/schema1.xsd"));

		Marshaller m = jaxbContext.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		// m.setProperty(Marshaller.JAXB_NO_NAMESPACE_SCHEMA_LOCATION, "conf/schema1.xsd");
		// m.setSchema(schema);

		m.marshal(xmlSpec, new File(filepath));
	}

	public static void main(String[] args)
			throws JAXBException, SAXException, IOException, RecognitionException, InstantiationException, IllegalAccessException,
			NoSuchFieldException, InvocationTargetException, NoSuchMethodException {

		TaskSpec ts = TaskSpec.unmarshal("res/endocytosis/task/EndocytosisMSCModel.xml");
		System.out.println();

		Library lib = Traverse.addLibrary(ts.library);
		IncompleteModel model = Traverse.addIncompleteModel(ts.incomplete);
		IQGraph graph = new IQGraph(model);

		XMLSpec spec = new XMLSpec(ts.settings, ts.criterion, graph, ts.data, ts.mappings, new CSerializer(), ts.output.variables);

		XMLSpec.marshal(spec, "res/endocytosis/task/EndocytosisMSCModel[fit].xml");

	}
}

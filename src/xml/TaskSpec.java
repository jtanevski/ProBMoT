package xml;

import java.io.*;
import java.util.*;

import javax.xml.bind.*;
import javax.xml.bind.annotation.*;
import javax.xml.transform.stream.*;
import javax.xml.validation.*;

import org.apache.commons.lang.*;
import org.slf4j.*;
import org.w3c.dom.Element;
import org.xml.sax.*;

import temp.ExtendedModel;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "task")
public class TaskSpec {
	
	
	
	
	//the whole XML path
	public  String taskPath;
	private Integer numRuns;
	private Integer endRun;
	
	public List <List <ExtendedModel>> BestModels;
	
	
	public static final Logger logger = LoggerFactory.getLogger(TaskSpec.class);

	public static final Class[] classes = {TaskSpec.class, FitterSpec.class, ALGSpec.class, DASASpec.class, DESpecOld.class, DESpec.class, EvalSpec.class,
		TrainTestSpec.class,SimulatorSpec.class, CVODESpec.class, LeaveOneOutSpec.class,CrossValidSpec.class};

	@XmlTransient
	public String filename;

	@XmlElement
	public String library;

	@XmlElement
	public String model;

	@XmlElement
	public String incomplete;

	@XmlElementWrapper(name = "data")
	@XmlElement(name = "d")
	public List<DatasetSpec> data;

	@XmlElement
	public Mappings mappings;

	@XmlElement
	public OutputSpec output;

	@XmlElement(name = "writeDir")
	public String outputFilepath;

	@XmlElement
	public Command command;

	@XmlElement
	public Settings settings;

	@XmlElement
	public Object criterion;

	public TaskSpec() {}

	public static TaskSpec unmarshal(String filepath) throws JAXBException  {
		JAXBContext jaxbContext = JAXBContext.newInstance(classes);

//		String schemaLang = "http://www.w3.org/2001/XMLSchema";
//		SchemaFactory factory = SchemaFactory.newInstance(schemaLang);
//		Schema schema = factory.newSchema(new StreamSource("conf/schema1.xsd"));

		Unmarshaller u = jaxbContext.createUnmarshaller();
//		u.setSchema(schema);

		TaskSpec spec = (TaskSpec) u.unmarshal(new File(filepath));
		spec.filename = new File(filepath).getName();

		logger.info("Task '" + filepath + "' read successfully");

		return spec;
	}

	public static void marshal(TaskSpec taskSpec, String filepath) throws JAXBException, SAXException {

		JAXBContext jaxbContext = JAXBContext.newInstance(classes);

//		String schemaLang = "http://www.w3.org/2001/XMLSchema";
//		SchemaFactory factory = SchemaFactory.newInstance(schemaLang);
//		Schema schema = factory.newSchema(new StreamSource("conf/schema1.xsd"));

		Marshaller m = jaxbContext.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//		m.setProperty(Marshaller.JAXB_NO_NAMESPACE_SCHEMA_LOCATION, "conf/schema1.xsd");
//		m.setSchema(schema);

		m.marshal(taskSpec, new File(filepath));
	}

	public static void main(String[] args) throws JAXBException, SAXException {

		TaskSpec ts = TaskSpec.unmarshal("res/aquatic/ecem/Zurich/task/zur.xml");
		System.out.println();

		TaskSpec.marshal(ts, "res/aquatic/ecem/Zurich/task/zzz2.xml");
		logger.info(" marshaled successfully");

	}

	public int getNumRuns() {
		return numRuns;
	}

	public void setNumRuns(int numRuns) {
		this.numRuns = numRuns;
	}

	public int getEndRun() {
		return endRun;
	}

	public void setEndRun(int endRun) {
		this.endRun = endRun;
	}
}



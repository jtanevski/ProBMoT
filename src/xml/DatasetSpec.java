package xml;

import javax.xml.bind.annotation.*;

public class DatasetSpec {
	@XmlValue
	public String datasetFilepath;

	@XmlAttribute
	public boolean header = true;

	@XmlAttribute
	public String separator = ",";
	
	@XmlAttribute
	public int id = 0;

	private DatasetSpec() {}

	public DatasetSpec(String datasetFilepath, boolean hasHeader, String separator,Integer idd) {
		this.datasetFilepath = datasetFilepath;

		this.header = hasHeader;
		this.separator = separator;
		this.id=idd;
	}
	
	public DatasetSpec(String datasetFilepath) {
		this(datasetFilepath, true, ",",null);
	}

	public DatasetSpec(String datasetFilepath, String separator) {
		this(datasetFilepath, true, separator,null);
	}
	
}
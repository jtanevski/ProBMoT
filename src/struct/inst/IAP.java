package struct.inst;

import java.io.*;

import struct.temp.*;

public class IAP extends AP {
	/* Reference */
	public transient TP argTemp;					// used in case where a TP is specified in the 'processes' property instead of an IP 
	public String argTempID;						// needed in serialization because argTemp is transient
	
	
	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
		stream.defaultReadObject();

//		// set template
//		this.templateID = stream.readObject();
//		
//		// set copy fields
//		this.parameters = new MapList<String, PE> ();
	

		this.argTempID = (String) stream.readObject();
	
	}

	private void writeObject(ObjectOutputStream stream) throws IOException {
		stream.defaultWriteObject();
		
//		//NOTE: at this moment it's not clear whether
//		//full-name or ID (string or integer) should be used, could be revised later on
//		Object id;
//		try {
//			id = getTemplate().getIdS();
//		} catch (UnsupportedOperationException ex) {
//			id = getTemplate().getIdI();
//		}

		
		
		stream.writeObject((argTemp!=null)?argTemp.id:null);
	}

	public void restoreTemplate() {
		Integer templateID = (Integer) this.templateID;
		PP template = getContainer().getTemplate().processes.get(templateID);
		setTemplate(template);
		
		
		TP argTemp = getContainer().template.getContainer().tps.get(this.argTempID); // argTemp can be null
		this.argTemp = argTemp;
	}
}

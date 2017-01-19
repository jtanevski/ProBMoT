package struct.inst;

import java.io.*;

import struct.*;
import struct.temp.*;


public abstract class Instance
		extends AbstractStructure 
		implements Serializable {
	
	
	/* ID */
	
	/* Container */
	
	/* Content */
	
	/* Reference */
	
	/* Back-reference */
	
	/* Template */
	public Object templateID;					// this is needed for serialization because the template field is transient

	/* Copy */
	
	/* Other */
	
	
	
	
	public abstract Template getTemplate();
	public abstract void setTemplate(Template template);
	
	public abstract Instance getContainer();
	
	//	private void writeObject(ObjectOutputStream stream) throws IOException {
	//		stream.defaultWriteObject();
	//		
	//		//NOTE: at this moment it's not clear whether
	//		//full-name or ID (string or integer) should be used, could be revised later on
	//		Object id;
	//		try {
	//			id = getTemplate().getIdS();
	//		} catch (UnsupportedOperationException ex) {
	//			id = getTemplate().getIdI();
	//		}
	//		stream.writeObject(id);
	//		System.out.println("write: " + this.getFullName() + " - temp: " + id);
	//	}
	
	
	public abstract void restoreTemplate();
	/*
	 * This cannot be implemented here, it has to be different for each instance type: different location of lookup
	 * for templates
	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
		stream.defaultReadObject();
		String templateName = (String) stream.readObject();
		//getContainer().getTemplate().<getSuitableStructureWithTemplates>.get(templateName)
	}
	*/
}

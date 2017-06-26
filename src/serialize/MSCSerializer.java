package serialize;

import java.util.*;

import serialize.node.*;
import equation.*;

public class MSCSerializer
		extends CSerializer {
	public Map<String, String> datamaxVars = new LinkedHashMap<String, String> ();
	public Map<String, String> dataminVars = new LinkedHashMap<String, String> ();
	
	
	public MSCSerializer() {
		this.serializers.put(DataMax.class, new DataMaxMinSerializer());
		this.serializers.put(DataMin.class, new DataMaxMinSerializer());
	}
	
	
	String dataminPrefix = "datamin";
	String datamaxPrefix = "datamax";
	
	
	class DataMaxMinSerializer extends NodeSerializer {

		@Override
		public String serialize(Node node, String... children) {
			Class clazz =  node.getClass();
			if (!clazz.equals(DataMax.class) && !clazz.equals(DataMin.class)) {
				throw new RuntimeException();
			}
			
			AEVarRef var = (AEVarRef) node.getChild(0);
			String varName = MSCSerializer.this.serialize(var.getIV());
			String resultName;
			if (clazz.equals(DataMax.class)) {
				resultName = datamaxPrefix + dotString + varName;
				datamaxVars.put(var.getIV().getFullName(), resultName);
			} else if (clazz.equals(DataMin.class)) {
				resultName = dataminPrefix + dotString + varName;
				dataminVars.put(var.getIV().getFullName(), resultName);
			} else {
				throw new  IllegalStateException("invalid state");
			}
			return resultName;
			
			
			
		}
		
	}

}



package struct.inst;

import java.util.*;

import util.*;

public class IncompleteModel extends Model {

	public void upkeep() {
		super.upkeep();
		
		for (IP ip : ipsRec.values()) {
			for (AE ae : ip.arguments) {
				IAE iae = (IAE) ae;
				if (iae.isUpperAll) {
					iae.upperArgs = new ListMap<String, IE>();
					List<IE> ies = this.TEtoIEs.get(iae.te.getFullName());
					for (IE ie : ies) {
						iae.upperArgs.put(ie.id, ie);
					}
				}
			}
		}
	}
}

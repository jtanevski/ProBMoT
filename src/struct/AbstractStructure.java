package struct;

public abstract class AbstractStructure
		implements Structure {

	@Override
	public Integer getIdI() {
		throw new UnsupportedOperationException("this method is illegal");
	}

	@Override
	public String getIdS() {
		throw new UnsupportedOperationException("this method is illegal");
	}

	@Override
	public void setIdI(Integer id) {
		throw new UnsupportedOperationException("this method is illegal");
	}

	@Override
	public void setIdS(String id) {
		throw new UnsupportedOperationException("this method is illegal");
	}
	
	@Override
	public Object getId() {
		try {
			return this.getIdS();
		} catch (UnsupportedOperationException ex) {
			return this.getIdI();
		}
	}

	public String toString() {
		StringBuilder buffer = new StringBuilder();

		buffer.append(this.getId());
		buffer.append(" {\n");

		// to be specified

		buffer.append("}\n");

		return buffer.toString();
	}
	
	public String getFullName() {
		String s;

		Structure cont = getContainer();
		if (cont!= null) {
			s = cont.getFullName() + ".";
		} else {
			s = "";
		}
		
		s += this.getId();
		return s;
	}
}

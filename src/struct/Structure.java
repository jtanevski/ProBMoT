package struct;

public interface Structure {

	public abstract String getIdS();

	public abstract void setIdS(String id);

	public abstract Integer getIdI();
	
	public abstract void setIdI(Integer id);
	
	public abstract Object getId();
	
	public abstract Structure getContainer();

	public abstract void setContainer(Structure container);

	public abstract String getFullName();
}
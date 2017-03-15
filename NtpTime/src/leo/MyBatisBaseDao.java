package leo;

public interface MyBatisBaseDao {
	//Illegal modifier for the interface method getNameById; only public & abstract are permitted
	//synchronized String getNameById(Integer id);

	String getById(Integer id);
	public String getNameById(Integer id);
	abstract String getNameById1(Integer id);
	public abstract String getNameById2(Integer id);
	abstract public String getNameById3(Integer id);
}

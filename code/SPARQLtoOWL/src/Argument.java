package neu.proposal.map;

public class Argument {
	
	String name;
	String classType;
	Relation domain;
	
	public String getClassType() {
		return classType;
	}
	public void setClassType(String classType) {
		this.classType = classType;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Relation getDomain() {
		return domain;
	}
	public void setDomain(Relation domain) {
		this.domain = domain;
	}
	
	

}

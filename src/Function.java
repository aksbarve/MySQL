import java.util.ArrayList;

public class Function{
	private Integer id;
	private String name = "";
	private Integer salary = 0;
	ArrayList<Integer> employees = new ArrayList<Integer>();

	public Function(Integer id) {
		super();
		this.id = id;
	}

	public Function(Integer id, String name, Integer salary) {
		super();
		this.id = id;
		this.name = name;
		this.salary = salary;
	}

	public String toString() {
		return "Person [id=" + id + ", name=" + name + ", salary=" + salary
				+ ", employees=" + employees + "]";
	}

	public int getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSalary() {
		return salary;
	}

	public void setSalary(Integer salary) {
		this.salary = salary;
	}

	public ArrayList<Integer> getEmployees() {
		return employees;
	}
	
	public void addEmployee(Integer employeeID) {
		employees.add(employeeID);
	}
}
import java.util.ArrayList;

public class Switch {
	private int code;
	private Function person;
	public Switch(String line) throws Exception {
		line = line.replaceAll("\\s+", " ");
		String[] tokens = line.split(" ");
		
		code = Integer.parseInt(tokens[0]);
		if (code < 1 || code > 6) {
			throw new Exception();
		}
		
		switch(code){
		case 1: 
		case 4: 
		case 5:
			person = new Function (new Integer(tokens[1]));
			break;
		
		case 2:
			person = new Function (new Integer(tokens[1]), tokens[2], new Integer(tokens[3]));

			for(int i = 4; i < tokens.length; i++){
				person.addEmployee(new Integer(tokens[i]));
			}
			break;
		
		default:
			break;
		}
	}

	public void execute(Data data1)
	{
		ArrayList<Integer> ids = null;
		
		switch(code) {
		case 1:
			data1.deletePerson(person);
			break;
		case 2:
			data1.insertPerson(person);
			break;
		case 3:
			System.out.println(data1.averageSalary().toString());
			break;
		case 4:
			
			ids = data1.empIdEmp(person.getId());
			ArrayList<String> str1;
			str1 = data1.empNameID(ids);
			if (str1.isEmpty()== false)
			{
			System.out.println(str1);
			}
			else 
			{
				System.out.println("error");	
			}
			break;
		case 5:
			ids = data1.empIdEmp(person.getId());
			System.out.println(data1.avgSalEmpID(ids));
			break;
		case 6:
			ArrayList<String> people = data1.indMulMangName();
			if (people.size() > 0) {
				System.out.println(people);				
			}
			else {
				System.out.println("no employees with more than one manager");
			}
			break;
		default:
			break;
		}
		
	}
	
	public String toString() {
		return "Command [code=" + code + ", person=" + person + "]";
	}
}

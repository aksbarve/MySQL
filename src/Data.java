import java.sql.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.io.InputStream;
import java.util.Properties;

public class Data {
	static String connection = "jdbc:mysql://localhost:3306/";
	static String username = "xyz";
	static String password = "123";
	static String dbname = "mydbname";
	static final String driver = "com.mysql.jdbc.Driver";
	Connection conn = null;
	private Statement statement = null;
	
	public Data() {
		setupProperties();
		setupConnection();
		getConnection();
	}

	private void getConnection() {
		try {
			conn = DriverManager.getConnection(connection, username, password);
			statement = conn.createStatement();
		}
		catch(Exception ex) {
			ex.printStackTrace();
			System.exit(3);
		}
	}

	private void setupConnection() {
		try {
			Class.forName(driver).newInstance();
		}
		catch(ClassNotFoundException ex) {
			ex.printStackTrace();
			System.exit(1);
		}
		catch(IllegalAccessException ex) {
			ex.printStackTrace();
			System.exit(2);
		}
		catch(InstantiationException ex) {
			ex.printStackTrace();
			System.exit(3);
		}
	}

	private void setupProperties() {
		Properties prop = new Properties();
		InputStream input = null;
		
		try {
	 
			input = new FileInputStream("config.properties");
			prop.load(input);
			if(prop.containsKey("dbusername")){
				username = prop.getProperty("dbusername");
			}
			
			if (prop.containsKey("dbpassword")){
				password = prop.getProperty("dbpassword");
			}
			if (prop.contains("dbname")){
				dbname = prop.getProperty("dbname");
			}
			
			if (prop.contains("dbserver") && prop.contains("dbport")){
				connection = "jdbc:mysql://" + prop.getProperty("dbserver") + ":" + prop.getProperty("dbport") + "/"+ prop.getProperty("dbname");
			} 
			else if (prop.contains("dbserver")){
				connection = "jdbc:mysql://" + prop.getProperty("dbserver") + "/";
			}
	 	} 
		catch (IOException ex) {
			ex.printStackTrace();
		} 
		finally {
			if (input != null) {
				try {
					input.close();
				} 
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	protected void pulldownDatabase()
	{
		try {
			statement.executeUpdate("DROP DATABASE " + dbname);
		}
		catch (Exception ex) {
			System.out.println(ex.toString());
		}

		try {
			conn.close();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void setupDatabase()
	{
		try {
			statement.executeUpdate("CREATE DATABASE " + dbname);
			
			statement.executeUpdate("USE " + dbname);
			
			statement.executeUpdate("CREATE TABLE employee " +
					"(eid INT PRIMARY KEY, " +
					"name VARCHAR(20), " +
					"salary INT)");
			
			statement.executeUpdate("CREATE TABLE worksfor " +
					"(eid INT NOT NULL, " +
					"mid INT NOT NULL, " +
					"PRIMARY KEY (eid, mid), " +
					"FOREIGN KEY (eid) REFERENCES employee(eid) ON DELETE CASCADE ON UPDATE CASCADE, " +
					"FOREIGN KEY (mid) REFERENCES employee(eid) ON DELETE CASCADE ON UPDATE CASCADE)");
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void insertPerson(Function person)
	{
		
		ArrayList<Integer> employees = person.getEmployees();
		String sql = "INSERT INTO employee " +
				"(eid, name, salary) VALUES " +
				"(" + person.getId() + ",'" + person.getName() + "', " + person.getSalary() + ")";
		
		try
		{
			statement.executeUpdate(sql);
			for (int i = 0; i < employees.size(); i++){
				insertEmployee(person.getId(), employees.get(i));
			}
			System.out.println("done");
		}
		catch (Exception ex)
		{
			System.out.println("error");
		}
	}
	
	private void insertEmployee(Integer employee, Integer manager) throws Exception
	{
		String sql = "INSERT INTO worksfor " +
				"(eid, mid) VALUES " +
				"(" + employee + ", " + manager +")";

		statement.executeUpdate(sql);
	}

	public void deletePerson(Function person) {
		String sql = "DELETE FROM employee " +
				"WHERE eid = " + person.getId();

		try {
			int numRows = statement.executeUpdate(sql);
			if (numRows == 0) {
				System.out.println("error");
			}
			else {
				System.out.println(numRows);	
			}			
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public Integer averageSalary() {
		String sql = "SELECT AVG(salary) " +
				"FROM employee ";
		
		ResultSet rs = null;
		try {
			rs = statement.executeQuery(sql);
			if(rs.next()){
				return rs.getInt(1);
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("error");
		}
		return -1;
	}

	public ArrayList<String> nameofMulMang() {
		String sql = "SELECT e1.name " +
				"FROM employee AS e1 NATURAL JOIN (" +
					"SELECT wf1.eid " +
					"FROM worksfor AS wf1 " +
					"GROUP BY wf1.eid " +
					"HAVING count(*) > 1) AS w1";

		ArrayList<String> results = new ArrayList<String>();
		ResultSet rs = null;
		
		try {
			rs = statement.executeQuery(sql);
			while (rs.next()){
				results.add(rs.getString(1));
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("error");
		}
		return results;
	}
	
	public ArrayList<String> indMulMangName() {
		String sql = "SELECT DISTINCT (name) " +
				"FROM (SELECT e1.name AS name " +
				"FROM employee AS e1 " +
				"NATURAL JOIN (SELECT wf1.eid " +
				"FROM worksfor AS wf1 " +
				"GROUP BY wf1.eid " +
				"HAVING count(*) > 1) AS w1 " + 
				"UNION " +
				"SELECT employee.name " +
				"FROM employee " +
				"NATURAL JOIN (SELECT wf2.eid " +
				"FROM worksfor AS wf2 " +
				"JOIN worksfor AS wf3 ON wf2.mid = wf3.eid) AS w3) AS q1";

		ArrayList<String> results = new ArrayList<String>();
		ResultSet rs = null;
		try {
			rs = statement.executeQuery(sql);
			while (rs.next()){
				results.add(rs.getString(1));
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("error");
		}
		return results;
	}
	
	public Integer avgSalEmpID(ArrayList<Integer> ids) {
		String idList = "";
		for (Integer id : ids) {
			idList = idList.concat(id.toString() + ", ");
		}
		if (idList.length() > 0){
			idList = idList.substring(0, idList.length() - 2);
		}
		else {
			return 0;
		}
		String sql = "SELECT AVG(salary) " +
				"FROM employee " +
				"WHERE eid IN (" + idList + ")";
	
		
		ResultSet rs = null;
		try {
			rs = statement.executeQuery(sql);

			if(rs.next()){
				return rs.getInt(1);
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	
		return 0;
	}

	public ArrayList<Integer> empIdEmp(Integer id) {
		String sql = "SELECT eid " +
				"FROM worksfor " +
				"WHERE mid = " + id;
		
		ArrayList<Integer> employees = new ArrayList<Integer>();
		ResultSet rs = null;
		try {
			rs = statement.executeQuery(sql);
            
			while(rs.next()){
				employees.add(rs.getInt(1));
				
			}
			
			ArrayList<Integer> employeesEmployees = new ArrayList<Integer>();
			for (Integer x : employees){
				ArrayList<Integer> temp = new ArrayList<Integer>();
				temp = this.empIdEmp(x);
				
				
				for (Integer y : temp) {
					if (!employeesEmployees.contains(y)){
						employeesEmployees.add(y);
						
					}
				}
			}
			employees.addAll(employeesEmployees);
			
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}	

		return employees;
	}
	
	
	public ArrayList<String> empNameID(ArrayList<Integer> ids) {
		String idList = "";
		ResultSet rs = null;
		ArrayList<String> names = new ArrayList<String>();
		
		for (Integer id : ids) {
			idList = idList.concat(id.toString() + ", ");
		}
		if (idList.length() > 0){
			idList = idList.substring(0, idList.length() - 2);
		}
		else {
			return names;
		}
		String sql = "SELECT name " +
				"FROM employee " +
				"WHERE eid IN (" + idList + ")";
	    
		try {
			rs = statement.executeQuery(sql);
	
			while(rs.next()){
				
				names.add(rs.getString(1));
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	
		return names;
	}
}
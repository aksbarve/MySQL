import java.io.IOException;

public class Transfile {
	

	static private Data data1 = null;
	
	public static void main(String[] args) {
		data1 = new Data();
		data1.setupDatabase();

		File fp = new File ("transfile", data1);
		try {
			fp.open_file();
		}
		catch (IOException e) {
			System.out.println(e);
		}
	
		data1.pulldownDatabase();
	}
}

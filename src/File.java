import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;

public class File {
	private String path;
	
	
	Data data1 = null;

	public File(String path, Data data1) {
		this.path = path;
		this.data1 = data1;
	}
	
	public void open_file() throws IOException {
		FileReader fr = new FileReader(path);
		BufferedReader textReader = new BufferedReader(fr);
		
		String line;
		
		while ((line = textReader.readLine()) != null) {
		    process(line);
		}
		
		textReader.close();
	}

	private void process(String line)
	{
			if (line.charAt(0) == '*') return;
		
		try {
			Switch command = new Switch(line);
			command.execute(data1);
		}
		catch (Exception ex){
			ex.printStackTrace();
		}
	}
}

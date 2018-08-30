import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class BufferedWriterDemo {
	
	public static void main(String args[]) {
		try {
			BufferedWriter bw = new BufferedWriter(
					// search on how to make accessing directories shorter
					new FileWriter("C:\\Users\\Chust\\Documents\\Eclipse\\FacePamphlet Prototype\\src\\BufferedWriterDemo.txt"));
		
			bw.write("str");
			
			bw.close();
		}
		catch(IOException ex) { System.out.print("error on: " + ex); }
	}

}

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class has one function that will read a file (based on the given FileName) and will return an ArrayList of String objects
 * where each string represents one line from the file. 
 * @author Kendra Walther
 * email: kwalther@usc.edu
 * ITP 265, 
 * Assignment ##
 */
public class FileReader {
	
	public static void main(String[] args) {
		ArrayList<String> list =  FileReader.readFile("bi/books.txt");
		System.out.println("got the list: " + list);
	}
	
	public static ArrayList<String> readFile(String fileName)  {

		ArrayList<String> fileLines = new ArrayList<>();

		try (FileInputStream fis = new FileInputStream(fileName);
				Scanner scan = new Scanner(fis)){
			while(scan.hasNextLine()) {
				String line = scan.nextLine();
				fileLines.add(line);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} //auto-close
		return fileLines;
	}
}

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class DefFileTest 
{
	public static void main(String args[]) throws FileNotFoundException
	{
		File file = new File("src/file.txt");
		Scanner scan = new Scanner(file);
		System.out.println(scan.next());
		
		scan.close();
	}
}

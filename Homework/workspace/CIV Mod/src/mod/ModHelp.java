package mod;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class ModHelp 
{
	public static void main(String[] args) throws FileNotFoundException{
		Scanner scan = new Scanner(System.in);
		System.out.println("Set:");
		String first = scan.nextLine();
		scan.close();
		
		File file = new File("src/mod/code.txt");
		
		Scanner out = new Scanner(file);
		
		while (out.hasNextLine())
		{
			String cur = out.nextLine();
			if (!cur.equals("<Row>") && !cur.equals("</Row>") && cur.substring(0, 6).equals("<Type>"))
			{
				String type = cur.substring(6,cur.length()-6);
				System.out.println("<Update>\n\t"+first+"\n\t<Where Type=\""+type+"\"/>\n</Update>");
			}
		}
		out.close();
	}	
}

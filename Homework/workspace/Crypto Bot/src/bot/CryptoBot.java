package bot;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class CryptoBot
{
	public static void main(String[] args) throws FileNotFoundException
	{
		File file = new File("src/bot/code.txt");
		
		Scanner out = new Scanner(file);
		int count = 0;
		int coin = 0;
		
		while (out.hasNextLine())
		{
			String cur = out.nextLine();
			
			/**
			 * if (count<5)
			{
				if (count==0){System.out.println("echo \"<table>\";");}
				count++;
			}
			
			System.out.println("echo \"<td>\";\n"
					+ "echo '<fieldset style=\"width:125px\"><legend>"+cur+"</legend>';\n"
					+ "echo '<p id=\"coin"+coin+"a\">test0</p>';\n"
					+ "echo '<p id=\"coin"+coin+"b\">test0</p>';\n"
					+ "echo '<p id=\"coin"+coin+"c\">test0</p>';\n"
					+ "echo '<p id=\"coin"+coin+"d\">test0</p>';\n"
					+ "echo '<p id=\"coin"+coin+"f\">test0</p>';\n"
					+ "echo '</fieldset>';\n"
					+ "echo \"</td>\";");
			
			
			if (count>=5) {System.out.println("echo \"</table>\";");count=0;}
			
			coin++;
			 */
			
			System.out.println("array(\""+cur+"\",0,0,0,0,0,),");
			
		}
		out.close();
	}	
}

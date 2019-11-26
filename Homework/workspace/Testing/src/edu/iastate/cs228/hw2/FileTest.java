package edu.iastate.cs228.hw2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileTest 
{
	public static void main(String[] args) throws FileNotFoundException
	{
		File file = new File("src/a/b/chars.txt");
		Scanner scan = new Scanner(file);
		char[] chars;
		int size = 0;
		String list = "";
		
		while(scan.hasNext()) {
			size++;
			list += scan.nextLine();
		}
		
		chars = new char[size];
		
		for (int i = 0; i < chars.length; i++) {
			chars[i] = list.charAt(i);
		}
		
		scan.close();
		
		for (char c : chars) {
			System.out.println(c);
		}
		
		file = new File("src/edu.iastate.cs228.hw2/file.txt");
		scan = new Scanner(file);
		System.out.println(scan.next());
	}
}

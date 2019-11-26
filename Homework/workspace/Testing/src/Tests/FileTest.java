package Tests;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class FileTest 
{
	public static void main(String[] args) throws FileNotFoundException
	{
		Scanner scan = new Scanner(new File("TestFolder/file.txt"));
	
		while(scan.hasNextLine()) {
			System.out.println(scan.nextLine());
		}
		
		scan.close();
	}
	
	/*private char[] characterOrdering;
	
	public static void main(String[] args)
	{
		FileTest test = new FileTest();
		
		System.out.println(test.getCharacterOrdering('c'));
	}
	
	public FileTest() 
	{
		//this.characterOrdering = new char[7];
		this.characterOrdering = new char[]{'a', 'b', 'c', 'd', 'e', 'f'};
	}
	
	public void printChars()
	{
		System.out.println(Arrays.toString(this.characterOrdering));
	}
	
	public int getCharacterOrdering(char key) {
		//TODO: write it and return something meaningful. 
		//Binary search
		int first = 0; int last = this.characterOrdering.length-1; int mid = (first + last) / 2;
		
		while (last >= first) {
			if (this.characterOrdering[mid] > key) {
				last = mid - 1;
			}
			else if (this.characterOrdering[mid] < key) {
				first = mid + 1;
			}
			else {
				return mid;
			}
			mid = (first + last) / 2;
		}
		return -1;
	}*/
}

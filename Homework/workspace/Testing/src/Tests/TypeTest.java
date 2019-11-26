package Tests;

import java.util.ArrayList;

public class TypeTest 
{
	public static void main(String[] args)
	{
		Animal lion = new Cat();
		System.out.println(lion.getClass());
		System.out.println(lion.getSound());
		
		char[] chars = {'a', 'b', 'c'};
		System.out.println("" + chars[0] + chars[1] + chars[2]);
		System.out.println(7/3);
		System.out.println(Character.isAlphabetic('$'));
		
		ArrayList<Character> arrayChars = new ArrayList<Character>();
		arrayChars.add('a'); arrayChars.add('b'); arrayChars.add('c');
		System.out.println(arrayChars.contains('d'));
	}
}

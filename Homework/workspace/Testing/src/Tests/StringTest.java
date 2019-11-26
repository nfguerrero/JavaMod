package Tests;

public class StringTest 
{
	public static void main(String[] args) 
	{
		String s1 = "cat";
		String s2 = "cat2";
		System.out.println(s2.compareTo(s1));
		
		String[] s = new String[2];
		System.out.println(s.length);
		
		 String whatever0 = "firstname\t\tlastname";
		 String whatever1 = "firstname"+"\t"+"\t"+"\t"+"lastname";
		 System.out.println(whatever0);
		 System.out.println(whatever1);
	}
}

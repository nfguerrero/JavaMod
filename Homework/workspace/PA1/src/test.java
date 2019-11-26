
public class test 
{
	public static void main(String args[])
	{
		PriorityQ q = new PriorityQ();
		q.add("one", 1);
		q.add("two", 2);
		q.add("three", 3);
		q.add("four", 4);
		q.add("five", 5);
		q.add("six", 6);
		q.add("seven", 7);
		q.add("eight", 8);
		
		System.out.println(q);
		
		System.out.println(q.extractMax());
		
		System.out.println(q);
	}
}

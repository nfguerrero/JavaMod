package Tests;

public class ArrayTest
{
	static Node[] heap;
	
	public static void main(String[] args)
	{
		heap = new Node[4];
		Node node1 = new Node("one", 1); heap[1] = node1;
		Node node2 = new Node("two", 2); heap[2] = node2;
		Node node3 = new Node("three", 3); heap[3] = node3;
		
		for (int i = 1; i < 4; i++)
		{
			System.out.print(heap[i].value());
			System.out.print(" ");
		}
		
		System.out.println("");
		change();
		
		for (int i = 1; i < 4; i++)
		{
			System.out.print(heap[i].value());
			System.out.print(" ");
		}
		System.out.println("");
		
		System.out.println((int) (Math.log(8)/Math.log(2)) + 1);
		System.out.println(11/2);
	}
	public static void change()
	{
		Node[] newHeap = new Node[4];
		Node node1 = new Node("three", 1); newHeap[1] = node1;
		Node node2 = new Node("two", 2); newHeap[2] = node2;
		Node node3 = new Node("one", 3); newHeap[3] = node3;
		
		heap = newHeap;
	}
}

class Node
{
	private String v;
	private int p;
	
	public Node(String v, int p)
	{
		this.v = v;
		this.p = p;
	}
	public int key()
	{
		return this.p;
	}
	public String value()
	{
		return this.v;
	}
}

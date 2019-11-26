public class PriorityQ 
{
	private Node[] heap;
	
	public PriorityQ()
	{
		this.heap = new Node[1];
		this.heap[0] = new Node("Height", 0);
	}
	public void add(String s, int p)
	{
		Node[] newHeap = new Node[this.heap.length+1];
		for (int i = 0; i < this.heap.length; i++) {
			newHeap[i] = this.heap[i];
		}
		newHeap[this.heap.length] = new Node(s, p);
		newHeap[0] = new Node("Height", (int) (Math.log(newHeap.length-1)/Math.log(2)) + 1);
		this.heap = newHeap;
		this.heapify();
	}
	public String returnMax()
	{
		return this.heap[1].value();
	}
	public String extractMax()
	{
		String max = this.heap[1].value();
		Node[] newHeap = new Node[this.heap.length-1];
		newHeap[1] = this.heap[this.heap.length-1];
		newHeap[0] = new Node("Height", (int) (Math.log(newHeap.length-1)/Math.log(2)) + 1);
		for (int i = 2; i < newHeap.length; i++) {
			newHeap[i] = this.heap[i];
		}
		int n = 1;
		for (int i = 0; i < newHeap[0].key(); i++) {
			if (newHeap.length - 1 >= 2*n + 1) {
				if ((newHeap[n].key() < newHeap[2*n].key()) && (newHeap[2*n].key() > newHeap[2*n + 1].key())) {
					Node temp = newHeap[n];
					newHeap[n] = newHeap[2*n];
					newHeap[2*n] = temp;
					n = 2*n;
				}
				else if ((newHeap[n].key() < newHeap[2*n + 1].key()) && (newHeap[2*n + 1].key() > newHeap[2*n].key())) {
					Node temp = newHeap[n];
					newHeap[n] = newHeap[2*n + 1];
					newHeap[2*n + 1] = temp;
					n = 2*n + 1;
				}
			}
			else if (newHeap.length - 1 >= 2*n) {
				Node temp = newHeap[n];
				newHeap[n] = newHeap[2*n];
				newHeap[2*n] = temp;
				n = 2*n;
			}
			else {break;}
		}
		this.heap = newHeap;
		return max;
	}
	public void remove(int i)
	{
		Node[] newHeap = new Node[this.heap.length-1];
		newHeap[0] = new Node("Height", (int) (Math.log(newHeap.length-1)/Math.log(2)) + 1);
		for (int j = 1; j < i; j++) {
			newHeap[j] = this.heap[j];
		}
		for (int j = i+1; j < this.heap.length; j++) {
			newHeap[j-1] = this.heap[j];
		}
		this.heap = newHeap;
		this.heapify();
	}
	public void decrementPriority(int i, int k)
	{
		this.heap[i].setKey(this.heap[i].key() - k);
		this.heapify();
	}
	public int[] priorityArray()
	{
		int[] keys = new int[this.heap.length-1];
		for (int i = 1; i < keys.length; i++) {
			keys[i-1] = this.heap[i].key();
		}
		return keys;
	}
	public int getKey(int i)
	{
		return heap[i].key();
	}
	public String getValue(int i)
	{
		return heap[i].value();
	}
	public boolean isEmpty()
	{
		if (this.heap.length <= 1) {
			return true;
		}
		return false;
	}
	
	private void heapify()
	{
		int start = (this.heap.length-1)/2;
		int cur = start;
		while (start > 0) {
			if (this.heap.length-1 >= 2*cur + 1) {
				if ((this.heap[2*cur].key() > this.heap[2*cur+1].key()) && (this.heap[2*cur].key() > this.heap[cur].key())) {
					Node temp = this.heap[cur];
					this.heap[cur] = this.heap[2*cur];
					this.heap[2*cur] = temp;
					cur = 2*cur;
				}
				else if ((this.heap[2*cur+1].key() > this.heap[2*cur].key()) && (this.heap[2*cur+1].key() > this.heap[cur].key())) {
					Node temp = this.heap[cur];
					this.heap[cur] = this.heap[2*cur+1];
					this.heap[2*cur+1] = temp;
					cur = 2*cur + 1;
				}
				else {
					start--;
					cur = start;
				}
			}
			else if ((this.heap.length-1 >= 2*cur) && (this.heap[2*cur].key() > this.heap[cur].key())) {
				Node temp = this.heap[cur];
				this.heap[cur] = this.heap[2*cur];
				this.heap[2*cur] = temp;
				cur = 2*cur;
			}
			else {
				start--;
				cur = start;
			}
		}
	}
	public String toString()
	{
		String info = "";
		for (int i = 1; i < this.heap.length; i++) {
			info += "<" + this.heap[i].value() + ", " + this.heap[i].key() + "> ";
		}
		return info;
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
	public void setKey(int p)
	{
		this.p = p;
	}
	public void setValue(String v)
	{
		this.v = v;
	}
}
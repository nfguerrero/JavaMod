import java.util.Arrays;
import java.util.ArrayList;

/**
 * Team members:
 * @author Nicolas Guerrero
 * 
 * A wrapper class for RBTree
 */
public class Intervals {
	private int idCount; 
	private RBTree rbt;
	private ArrayList<Interval> intervals;
	
	/**
	 * Constructor with no parameters.
	 */
	public Intervals() {
		this.idCount = 0;
		this.rbt = new RBTree();
		this.intervals = new ArrayList<Interval>();
	}
	
	/**
	 * 
	 * Adds the interval with left endpoint a and right endpoint b 
	 * to the collection of intervals. Each newly inserted interval 
	 * must be assigned an ID. The IDs should be consecutive; that is, 
	 * the ID of the interval inserted on the ith call of this method should be i.
	 * For example if intervalInsert is called successively to insert intervals 
	 * [5,7],[4,9],[1,8], then the IDs of these intervals should be 1,2,3, respectively.These IDs are permanent
	 *  for the respective intervals. Keep track of the IDs, as multiple intervals that have the same endpoints
	 *   on both sides can be added. intervalInsertshould run in O(logn)time
	 * @param a
	 * @param b
	 */
	void intervalInsert(int a, int b) {
		this.intervals.add(new Interval(a, b, this.idCount));
		this.idCount++;
		this.rbt.insert(a, 1);
		this.rbt.insert(b, -1);
	}
	
	/**
	 * To delete an interval from delete.
	 * 
	 * 
	 * Deletes the interval whose ID (gener-ated byintervalInsert) isintervalID. Returnstrueif 
	 * deletion was successful. Thismethod should run inO(logn)time.Note.TheintervalDeletemethod 
	 * isoptional; that is, you are not requiredto implement it. However, your codemustprovide 
	 * anintervalDeletemethodeven if you choose not to implement interval deletion. If you do not
	 *  implementdeletion, theintervalDeletemethod should consist of just one line that returnsfalse.
	 * @param intervalID
	 * @return
	 */
	boolean intervalDelete(int intervalID) {
		//TODO: Complete it as needed (This is optional so you can leave it as it is)
		return false;
	}
	
	/**
	 * Finds the endpoint that has maximum overlap and returns its value. Thismethod should run in constant time.
	 * @return
	 */
	int findPOM() {
		return this.rbt.getRoot().getEmax().getValue();
	}
	
	/**
	 * Returns the red-black tree used, which is an object of typeRBTree.
	 * @return
	 */
	RBTree getRBTree() {
		return this.rbt;
	}
	
	
	//Add more functions as  you see fit.
	
	
	/**
	 * This is a suggested way on how to add intervals and call POM()
	 * 
	 * @param args
	 */
	public static void main(String [] args) {
		int points[][] = {{0, 4}, {1, 6}, {3, 9}, {7, 11}};
		Intervals intv = new Intervals();
		
		for(int i=0; i<points.length; i++) {
			System.out.println("Inserting: "+ Arrays.toString(points[i]));
			intv.intervalInsert(points[i][0], points[i][1]);
		}
		System.out.println("POM is: "+ intv.findPOM()); //Should return 3.
		
		//Testing:
		//System.out.println(intv.rbt);
	}
	
	private class Interval
	{
		private int l;
		private int r;
		private int id;
		
		Interval(int a, int b, int c){
			this.l = a;
			this.r= b;
			this.id = c;
		}
		
		int getLeft(){
			return this.l;
		}
		
		int getRight(){
			return this.r;
		}
		
		int getID(){
			return this.id;
		}
	}
}


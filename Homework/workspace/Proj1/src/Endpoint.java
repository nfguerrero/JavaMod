/**
 * Team members:
 * @author Nicolas Guerrero
 * 
 * Endpoint class for Node.
 */
public class Endpoint {
	//Private Variables:
	private int val;
	private int pe; //+1 if left, -1 if right, 0 if nil node
	
	
	/**
	 * Constructor of an endpoint
	 * @param v		int value of endpoint
	 * @param l		boolean if endpoint is left or not
	 */
	public Endpoint(int v, int p){
		this.val = v;
		this.pe = p;
	}
	
	/**
	 * returns the endpoint value.  For example if the
	 * End point object represents the left end point of the 
	 * interval [1,3], this would return 1.
	 * @return val		int value of endpoint
	 */
	public int getValue() {
		return this.val;
	}
	
	/**
	 * returns if this endpoint is a left or right endpoint
	 * @return left 		boolean if left is true
	 */
	public int getP(){
		return this.pe;
	}
}

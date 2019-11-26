/**
 * Team members:
 * @author Nicolas Guerrero
 * 
 * Node class for RBTree.
 */
public class Node {
	//Private variables:
	Node parent;
	Node left;
	Node right;
	int black; // black = 1, red = 0
	int val;
	int maxVal;
	Endpoint emax;
	Endpoint e;
	int size;
	int height;
	
	/**
	 * Constructor for Node
	 */
	public Node(Node p, Node l, Node r, int c, Endpoint end){
		this.parent = p;
		this.left = l;
		this.right = r;
		this.black = c;
		this.e = end;
		this.emax = end;
		if (end.getP()==0){
			this.size = 0;
			this.height = 0;}
		else{
			this.size = 1;
			this.height = 1;}
	}
	
	/**
	 * Returns the parent of this node.
	 * @return
	 */
	public Node getParent() {
		return this.parent;
	}
	
	public void setParent(Node n){
		this.parent = n;
	}
	
	/**
	 * Returns the left child.
	 * @return
	 */
	public Node getLeft() {
		return this.left;
	}
	
	public void setLeft(Node n){
		this.left = n;
	}
	
	/**
	 * Returns the right child.
	 * @return
	 */
	public Node getRight() {
		return this.right;
	}
	
	public void setRight(Node n){
		this.right = n;
	}
	
	/**
	 * Returns the endpoint value, which is an integer.
	 * @return
	 */
	public int getKey() {
		return this.e.getValue();
	}
	
	/**
	 * Returns the value of the functionpbased on this endpoint.
	 * @return
	 */
	public int getP() {
		return this.e.getP();
	}
	
	/**
	 * Returns the val of the node as described in this assignment.
	 * @return
	 */
	public int getVal() {
		return this.val;
	}
	
	public void setVal(int v){
		this.val = v;
	}
	
	/**
	 * Returns themaxvalof the node as described in this assignment.
	 * @return
	 */
	public int getMaxVal() {
		return this.maxVal;
	}
	
	public void setMaxVal(int mv){
		this.maxVal = mv;
	}
	
	/**
	 * Returns theEndpointobject that this node represents.
	 * @return
	 */
	public Endpoint getEndpoint() {
		return this.e;
	}
	
	/**
	 * Returns anEndpointobject that represents emax. 
	 * Calling this method on the root node will give the End point object whose getValue() 
	 * provides a point of maximum overlap.
	 * @return
	 */
	public Endpoint getEmax() {
		return this.emax;
	}
	
	public void setEmax(Endpoint em){
		this.emax = em;
	}
	
	/**
	 * Returns 0 if red. Returns 1 if black.
	 * @return
	 */
	public int getColor() {
		return this.black;
	}
	
	public void setColor(int c){
		this.black = c;
	}
	
	//Add more functions as  you see fit.
	
	public int getSize(){
		return this.size;
	}
	
	public void setSize(int s){
		this.size = s;
	}
	
	public int getHeight(){
		return this.height;
	}
	
	public void setHeight(int h){
		this.height	= h;
	}
}

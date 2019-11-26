import java.util.ArrayList;

/**
 * Team members:
 * @author Nicolas Guerrero
 * 
 * RBTree class, maintains operations on RBTree.
 */
public class RBTree {
	//Private Variables:
	Node root;	
	Node nil;
	
	/**
	 * RB Tree constructor. It initializes nil node as well.
	 */
	public RBTree() {
		this.nil = new Node(null,null, null, 1, new Endpoint(-1, 0));
		this.root = this.nil;
	}
	
	/**
	 * Returns the root of teh tree.
	 * @return
	 */
	public Node getRoot() {
		return this.root;
	}
	
	/**
	 * Returns reference for the nil node, for the rbTree.
	 * @return
	 */
	public Node getNILNode() {
		return this.nil;
	}
	
	/**
	 * Returns the number of internal nodes in the tree.
	 * @return
	 */
	public int getSize() {
		return this.root.getSize();
	}
	
	
	/**
	 * Returns the height of the tree.
	 * @return
	 */
	public int getHeight() {
		return this.root.getHeight();
	}
	
	//Add more functions as  you see fit.
	
	/**
	 * Insert new endpoint to be turned into a node
	 * @param v
	 * @param p
	 */
	public void insert(int v, int p){
		Endpoint e = new Endpoint(v, p);
		Node n = new Node(null, null, null, 0, e);
		this.insertNode(n);
	}
	
	/**
	 * Insert new node into tree
	 * @param z
	 */
	public void insertNode(Node z){		
		Node y = this.nil;
		Node x = this.root;
		while (x != this.nil){
			y = x;
			if ((z.getKey() < x.getKey()) || ((z.getKey() == x.getKey())&&(z.getEndpoint().getP()<x.getEndpoint().getP())))
				x = x.getLeft();
			else
				x = x.getRight();
		}
		z.setParent(y);
		if (y == this.nil)
			this.root = z;
		else if (z.getKey() < y.getKey() || ((z.getKey() == x.getKey())&&(z.getEndpoint().getP()<x.getEndpoint().getP()))){
			y.setLeft(z);
			this.update(y);}
		else{
			y.setRight(z);
			this.update(y);}
		z.setLeft(this.nil);
		z.setRight(this.nil);
		z.setColor(0);
		this.update(z);
		if ( (this.root != z)&&(this.root != z.getParent()) )
			insertFix(z);
		//System.out.println(this);
	}
	
	/**
	 * Fixes the tree after inserting a node
	 */
	public void insertFix(Node z){
		Node y;
		while (z.getParent().getColor() == 0){
			if (z.getParent() == z.getParent().getParent().getLeft()){
				y = z.getParent().getParent().getRight();
				if (y.getColor() == 0){
					z.getParent().setColor(1);
					y.setColor(1);
					z.getParent().getParent().setColor(0);
					z = z.getParent().getParent();
				}
				else {
					if (z == z.getParent().getRight()){
						z = z.getParent();
						this.leftRotate(z);
					}
					z.getParent().setColor(1);
					z.getParent().getParent().setColor(0);
					this.rightRotate(z.getParent().getParent());
				}
			}//end left
			else {
				y = z.getParent().getParent().getLeft();
				if (y.getColor() == 0){
					z.getParent().setColor(1);
					y.setColor(1);
					z.getParent().getParent().setColor(0);
					z = z.getParent().getParent();
				}
				else {
					if (z == z.getParent().getLeft()){
						z = z.getParent();
						this.rightRotate(z);
					}
					z.getParent().setColor(1);
					z.getParent().getParent().setColor(0);
					this.leftRotate(z.getParent().getParent());
				}
			}//end right
			this.root.setColor(1);
		}
	}
	
	/**
	 * Does a left rotation around a node x
	 */
	public void leftRotate(Node x){
		Node y = x.getRight();
		x.setRight(y.getLeft());
		this.update(x);
		if (y.getLeft() != this.nil){
			y.getLeft().setParent(x);
			this.update(y.getParent());}
		y.setParent(x.getParent());
		this.update(y.getParent());
		if (x.getParent() == this.nil)
			this.root = y;
		else if (x == x.getParent().getLeft()){
			x.getParent().setLeft(y);
			this.update(x.getParent());}
		else{
			x.getParent().setRight(y);
			this.update(x.getParent());}
		y.setLeft(x);
		this.update(y);
		x.setParent(y);
		this.update(x.getParent());
	}
	
	/**
	 * Does a right rotation around a node x
	 */
	public void rightRotate(Node x){
		Node y = x.getLeft();
		x.setLeft(y.getRight());
		this.update(x);
		if (y.getRight() != this.nil){
			y.getRight().setParent(x);
			this.update(y.getParent());}
		y.setParent(x.getParent());
		this.update(y.getParent());
		if (x.getParent() == this.nil)
			this.root = y;
		else if (x == x.getParent().getRight()){
			x.getParent().setRight(y);
			this.update(x.getParent());}
		else{
			x.getParent().setLeft(y);
			this.update(x.getParent());}
		y.setRight(x);
		this.update(y);
		x.setParent(y);
		this.update(x.getParent());
	}
	
	/**
	 * Control to change valuables during node insertion dynamically
	 * @param n
	 */
	public void update(Node n){
		if (n == this.nil)
			return;
		this.updateVal(n);
		this.updateMaxVal(n);
		this.updateSize(n);
		this.updateHeight(n);
	}
	
	/**
	 * Change val variable dynamically of a node during insertion
	 * @param n
	 */
	public void updateVal(Node n){
		if (n == this.nil)
			n.setVal(0);
		else
			n.setVal(n.getLeft().getVal() + n.getP() + n.getRight().getVal());
	}
	
	/**
	 * Change maxVal variable dynamically of a node during insertion
	 * @param n
	 */
	public void updateMaxVal(Node n){
		int a = n.getLeft().getMaxVal();
		int b = n.getLeft().getVal()+n.getP();
		int c = n.getLeft().getVal()+n.getP()+n.getRight().getMaxVal();
		if ( (a>=b) && (a>=c) ){
			n.setMaxVal(a);
			this.updateEMax(n, n.getLeft().getEmax());}
		else if ( (b>=a) && (b>=c) ){
			n.setMaxVal(b);
			this.updateEMax(n, n.getEndpoint());}
		else{
			n.setMaxVal(c);
			this.updateEMax(n, n.getRight().getEmax());}
	}
	
	/**
	 * Change emax variable dynamically of a node during insertion
	 * @param n
	 */
	public void updateEMax(Node n, Endpoint e){
		n.setEmax(e);
	}
	
	/**
	 * Change size variable dynamically of a node during insertion
	 * @param n
	 */
	public void updateSize(Node n){
		n.setSize(n.getLeft().getSize()+n.getRight().getSize()+1);
	}
	
	/**
	 * Change height variable dynamically of a node during insertion
	 * @param n
	 */
	public void updateHeight(Node n){
		n.setHeight(Math.max(n.getLeft().getHeight()+1, n.getRight().getHeight()+1));
	}
	
	/**
	 * toString method
	 */
	public String toString(){
		String output = "";
		ArrayList<Node> back = new ArrayList<Node>();
		Node cur = this.root;
		int count = 0;
		while ((cur != this.nil || back.size() > 0) && count++ < 10){
			output += "Key: " + cur.getEndpoint().getValue() + ", Left: " + cur.getLeft().getEndpoint().getValue() + ", Right: " + cur.getRight().getEndpoint().getValue() + ", emax: " + cur.getEmax().getValue() + "\n";
			back.remove(cur);
			if (cur.getRight() != this.nil && cur.getLeft() != this.nil){
				back.add(cur.getRight());
				cur = cur.getLeft();
			}
			else if (cur.getLeft() != this.nil && cur.getRight() == this.nil){
				cur = cur.getLeft();
			}
			else if (cur.getRight() != this.nil && cur.getLeft() == this.nil){
				cur = cur.getRight();
			}
			else{
				if (back.size() > 0)
					cur = back.get(0);
				else
					cur = this.nil;
			}
		}
		return output;
	}
}

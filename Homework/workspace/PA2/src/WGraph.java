import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class WGraph 
{
	public int connected;
	private int edge;
	public Vertex[] vertices;
	public Edge[] edges;
	
	public WGraph(String FName) throws FileNotFoundException
	{
		File file = new File(FName);
		Scanner scan = new Scanner(file);
		
		this.connected = 0;
		this.edge = 0;
		this.vertices = new Vertex[scan.nextInt()];
		this.edges = new Edge[scan.nextInt()];
		while(scan.hasNextLine()){
			Vertex u = new Vertex(scan.nextInt(), scan.nextInt());
			Vertex v = new Vertex(scan.nextInt(), scan.nextInt());
			boolean uIn = false; boolean vIn = false;
			int ui = 0; int vi = 0;
			for (int i = 0; i < this.connected; i++){
				if ( (this.vertices[i].getX() == u.getX())&&(this.vertices[i].getY() == u.getY()) ){
					uIn = true;
					ui = i;
				}
				else if( (this.vertices[i].getX() == v.getX())&&(this.vertices[i].getY() == v.getY()) ){
					vIn = true;
					vi = i;
				}
				if (uIn && vIn){
					i = this.connected;
				}
			}
			if (!uIn){
				this.vertices[this.connected] = u; ui = this.connected; this.connected++;
			}
			if (!vIn){
				this.vertices[this.connected] = v; vi = this.connected; this.connected++;
			}
			this.edges[edge] = new Edge(ui, vi, scan.nextInt()); this.edge++;
		}
		
		scan.close();
	}
	
	// pre: ux, uy, vx, vy are valid coordinates of vertices u and v
	// in the graph
	// post: return arraylist contains even number of integers,
	// for any even i,
	// i-th and i+1-th integers in the array represent
	// the x-coordinate and y-coordinate of the i/2-th vertex
	// in the returned path (path is an ordered sequence of vertices)
	public ArrayList<Integer> V2V(int ux, int uy, int vx, int vy)
	{
		boolean[] temp = new boolean[this.connected];
		int[] prev  = new int[this.connected];
		int[] path = new int[this.connected];
		int cur = -1;
		int start = -1;
		for (int i = 0; i < temp.length; i++){
			prev[i] = -1;
			if (this.vertices[i].equals(ux, uy)){
				cur = i;
				start = i;
				temp[i] = false;
				path[i] = 0;
			}
			else {
				temp[i] = true;
				path[i] = Integer.MAX_VALUE;
			}
		}
		for (int a = 0; a < this.connected; a++){
			int shortV = -1;
			int shortP = Integer.MAX_VALUE;
			for (int i = 0; i < this.edges.length; i++){
				int u = this.edges[i].getU(); 
				int v = this.edges[i].getV();
				if ( (!temp[u])&&(temp[v]) ){
					if (path[cur]+edges[i].getW()<path[v]){
						prev[v] = cur;
						path[v] = path[cur]+edges[i].getW();
					}				
					if (path[v] <= shortP){
						shortP = path[v];
						shortV = v;
					}
				}
			}
			if (shortV > -1){
				temp[shortV] = false;
				cur = shortV;
			}
			
			if (this.vertices[cur].equals(vx, vy)){
				a = this.connected;
			}
		}
		Vertex[] route = new Vertex[this.connected];
		int r = 1;
		route[0] = this.vertices[cur];
		while (cur != start){
			route[r] = this.vertices[prev[cur]]; r++;
			cur = prev[cur];
		}
		ArrayList<Integer> shortR = new ArrayList<Integer>();
		for (int i = r-1; i >= 0; i--){
			shortR.add(route[i].getX());
			shortR.add(route[i].getY());
		}
		return shortR;
	}

	// pre: ux, uy are valid coordinates of vertex u from the graph
	// S represents a set of vertices.
	// The S arraylist contains even number of intergers
	// for any even i,
	// i-th and i+1-th integers in the array represent
	// the x-coordinate and y-coordinate of the i/2-th vertex
	// in the set S.
	// post: same structure as the last method’s post.
	ArrayList<Integer> V2S(int ux, int uy, ArrayList<Integer> S)
	{
		boolean[] temp = new boolean[this.connected];
		int[] prev  = new int[this.connected];
		int[] path = new int[this.connected];
		int cur = -1;
		int start = -1;
		for (int i = 0; i < temp.length; i++){
			prev[i] = -1;
			if (this.vertices[i].equals(ux, uy)){
				cur = i;
				start = i;
				temp[i] = false;
				path[i] = 0;
			}
			else {
				temp[i] = true;
				path[i] = Integer.MAX_VALUE;
			}
		}
		for (int a = 0; a < this.connected; a++){
			int shortV = -1;
			int shortP = Integer.MAX_VALUE;
			for (int i = 0; i < this.edges.length; i++){
				int u = this.edges[i].getU(); 
				int v = this.edges[i].getV();
				if ( (!temp[u])&&(temp[v]) ){
					if (path[cur]+edges[i].getW()<path[v]){
						prev[v] = cur;
						path[v] = path[cur]+edges[i].getW();
					}				
					if (path[v] <= shortP){
						shortP = path[v];
						shortV = v;
					}
				}
			}
			if (shortV > -1){
				temp[shortV] = false;
				cur = shortV;
			}
		}
		
		int shortVS = -1;
		int shortPS = Integer.MAX_VALUE;		
		boolean[] inS = new boolean[this.connected];
		for (int s = 0; s < this.connected; s++){
			inS[s] = false;
		}
		for (int s = 0; s < S.size(); s+=2){
			for (int v = 0; v < this.connected; v++){
				if ( (S.get(s)==this.vertices[v].getX())&&(S.get(s+1)==this.vertices[v].getY()) ){
					inS[v] = true;
					if (path[v] < shortPS){
						shortPS = path[v];
						shortVS = v;
					}
				}
			}
		}
		
		Vertex[] route = new Vertex[this.connected];
		int r = 1;
		route[0] = this.vertices[shortVS];
		while (shortVS != start){
			route[r] = this.vertices[prev[shortVS]]; r++;
			shortVS = prev[shortVS];
		}
		ArrayList<Integer> shortR = new ArrayList<Integer>();
		for (int i = r-1; i >= 0; i--){
			shortR.add(route[i].getX());
			shortR.add(route[i].getY());
		}
		return shortR;
	}
	
	// pre: S1 and S2 represent sets of vertices (see above for
	// the representation of a set of vertices as arrayList)
	// post: same structure as the last method’s post.
	ArrayList<Integer> S2S(ArrayList<Integer> S1, ArrayList<Integer> S2)
	{
		int[] s1 = new int[S1.size()/2];
		int si = 0;
		for (int i = 0; i < s1.length; i++){
			for (int v = 0; v < this.connected; v++){
				if (this.vertices[v].equals(S1.get(i),S1.get(i+1))){
					s1[si] = v; si++;
					break;
				}
			}
		}
		boolean[] temp = new boolean[this.connected];
		int[] prev  = new int[this.connected];
		int[] path = new int[this.connected];
		for (int i = 0; i < temp.length; i++){
			prev[i] = -1;
			path[i] = Integer.MAX_VALUE;
			boolean outS1 = true;
			for (int j = 0; j < s1.length; j++){
				if (s1[j] == i){
					outS1 = false;
					path[i] = 0;
				}
			}
			temp[i] = outS1;
		}
		
		for (int a = 0; a < this.connected-s1.length; a++){
			int shortV = -1;
			int shortP = Integer.MAX_VALUE;
			for (int i = 0; i < this.edges.length; i++){
				int u = this.edges[i].getU(); 
				int v = this.edges[i].getV();
				if ( (!temp[u])&&(temp[v]) ){
					if (path[u]+edges[i].getW()<path[v]){
						prev[v] = u;
						path[v] = path[u]+edges[i].getW();
					}				
					if (path[v] <= shortP){
						shortP = path[v];
						shortV = v;
					}
				}
			}
			if (shortV > -1){
				temp[shortV] = false;
			}
		}
		
		int shortVS = -1;
		int shortPS = Integer.MAX_VALUE;		
		boolean[] inS = new boolean[this.connected];
		for (int s = 0; s < this.connected; s++){
			inS[s] = false;
		}
		for (int s = 0; s < S2.size(); s+=2){
			for (int v = 0; v < this.connected; v++){
				if ( (S2.get(s)==this.vertices[v].getX())&&(S2.get(s+1)==this.vertices[v].getY()) ){
					inS[v] = true;
					if (path[v] < shortPS){
						shortPS = path[v];
						shortVS = v;
					}
				}
			}
		}
		
		Vertex[] route = new Vertex[this.connected];
		int r = 1;
		route[0] = this.vertices[shortVS];
		boolean key = true;
		while (key){
			route[r] = this.vertices[prev[shortVS]]; r++;
			shortVS = prev[shortVS];
			for (int i = 0; i < s1.length; i++){
				if (shortVS == s1[i]){
					key = false;
				}
			}
			if (shortVS == -1){
				key = false;
			}
		}
		ArrayList<Integer> shortR = new ArrayList<Integer>();
		for (int i = r-1; i >= 0; i--){
			shortR.add(route[i].getX());
			shortR.add(route[i].getY());
		}
		return shortR;
	}
}

class Vertex
{
	private int x;
	private int y;
	
	public Vertex(int vx, int vy)
	{
		this.x = vx;
		this.y = vy;
	}
	
	public int getX()
	{
		return this.x;
	}
	
	public int getY()
	{
		return this.y;
	}
	
	public boolean equals(Vertex u)
	{
		return (this.x == u.getX())&&(this.y == u.getY());
	}
	
	public boolean equals(int ux, int uy)
	{
		return (this.x == ux)&&(this.y == uy);
	}
	
	public String toString()
	{
		return "<" + this.x + "," + this.y + ">";
	}
}

class Edge
{
	private int u;
	private int v;
	private int w;
	
	public Edge(int a, int b, int weight)
	{
		this.u = a;
		this.v = b;
		this.w = weight;
	}
	
	public int getU()
	{
		return this.u;
	}
	
	public int getV()
	{
		return this.v;
	}
	
	public int getW()
	{
		return this.w;
	}
	
	public String toString()
	{
		return "(" + this.u + "->" + this.v + "," + this.w + ")";
	}
}

/**
 * @author Nicolas Guerrero
 */

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

public class CommunicationsMonitor 
{
	boolean graphCreated;
	List<Triple> triples;
	HashMap<Integer,List<ComputerNode>> map;
	
	public CommunicationsMonitor()
	{
		this.graphCreated = false;
		this.triples = new ArrayList<Triple>();
		this.map = new HashMap<Integer, List<ComputerNode>>();
	}
	
	public void addCommunication(int c1, int c2, int timestamp)
	{
		if (this.graphCreated)
			return;
		
		this.triples.add(new Triple(c1, c2, timestamp));
	}
	
	public void createGraph()
	{
		this.graphCreated = true;
		this.triples = this.Sort(this.triples);
		this.Map();
	}
	
	public List<ComputerNode> queryInfection(int c1, int c2, int x, int y)
	{
		List<ComputerNode> n1 = this.map.get(c1); //List of computer nodes with ID = c1
		List<ComputerNode> n2 = this.map.get(c2); //List of computer nodes with ID = c2
		if (n1 == null || n2 == null) //return null if no list of computer nodes with ID c1 or c2 exists
			return null;
		ComputerNode f = null; //node with ID c2 && Timestamp <= y
		for (ComputerNode cn: n2)
			if (cn.getTimestamp() <= y)
				f = cn;
		
		if (f == null) //return null if no computer node with ID c2 && Timestamp <= y exists
			return null;
		
		ComputerNode s = null; //node with ID c1 && Timestamp >= x
		for (ComputerNode cn : n1){
			if (cn.getTimestamp() >= x){
				s = cn;
				break;
			}
		}
		if (s == null) //return null if no computer node with ID c1 && Timestamp >= x exists
			return null;
		
		ComputerNode end = DFS(s, c2, y);
		
		if (end != null){
			ArrayList<ComputerNode> path = new ArrayList<ComputerNode>();
			path.add(f);
			ComputerNode p = f.getPred();
			while (p != null && p != s){
				path.add(0, p);
				p = p.getPred();
			}
			path.add(0, p);
			return path;
		}
		
		return null; 
	}
	
	public HashMap<Integer, List<ComputerNode>> getComputerMapping()
	{
		return this.map;
	}
	
	public List<ComputerNode> getComputerMapping(int c)
	{
		return this.map.get(c);
	}
	
	private ComputerNode DFS(ComputerNode s, int f, int t)
	{
		for (Integer i : this.map.keySet()){
			for (ComputerNode c : this.map.get(i)){
				c.setStatus(0);
				c.setPred(null);
			}
		}		
		return DFS_Visit(s, f, t);
	}
	
	private ComputerNode DFS_Visit(ComputerNode u, int v, int t)
	{
		ComputerNode path = null;
		u.setStatus(1);		
		for (ComputerNode c : u.getOutNeighbors()){
			if (c.getStatus() == 0){
				c.setPred(u);
				if (c.getID() == v && c.getTimestamp() <= t)
					return c;
				if (path == null)
					path = DFS_Visit(c, v, t);
			}
		}
		return path;
	}
	
	private void Map()
	{		
		for (int i = 0; i < this.triples.size(); i++){
			Triple tri = this.triples.get(i);
			int c1 = tri.getComp1();
			int c2 = tri.getComp2();
			int t = tri.getTime(); 
			List<ComputerNode> n1 = this.map.get(c1); //List of computer nodes already associated with c1
			List<ComputerNode> n2 = this.map.get(c2); //List of computer nodes already associated with c2
			ComputerNode cn1 = null;
			ComputerNode cn2 = null;
			//n1 check
			if (n1 == null){ //n1 empty so far
				this.map.put(c1, new ArrayList<ComputerNode>());
				cn1 = new ComputerNode(c1, t);
				this.map.get(c1).add(cn1); //add c1 to map
			}
			else{ //n1 is not empty
				for (int j = 0; j < n1.size(); j++){ //sets c1 to existing node if it already exists
					if (n1.get(j).getTimestamp() == t){
						cn1 = n1.get(j);
						break;
					}
				}
				if (cn1 == null){ //c1 not in list of nodes
					cn1 = new ComputerNode(c1, t);
					n1.add(cn1); //add c1 to map
					ComputerNode cn1p = n1.get(n1.size()-2); //node of c1 with earlier time
					cn1p.addOutNeighbor(cn1); //add c1 as out neighbor of earlier c1 node		
				}				
			}
			//n2 check
			if (n2 == null){ //n2 empty so far
				this.map.put(c2, new ArrayList<ComputerNode>());
				cn2 = new ComputerNode(c2, t);
				this.map.get(c2).add(cn2); //add c2 to map
			}
			else{ //n2 is not empty
				for (int j = 0; j < n2.size(); j++){ //sets c2 to existing node if it already exists
					if (n2.get(j).getTimestamp() == t){
						cn2 = n2.get(j);
						break;
					}
				}
				if (cn2 == null){ //c2 not in list of nodes
					cn2 = new ComputerNode(c2, t);
					n2.add(cn2); //add c2 to map
					ComputerNode cn2p = n2.get(n2.size()-2); //node of c2 with earlier time
					cn2p.addOutNeighbor(cn2); //add c2 as out neighbor of earlier c2 node		
				}				
			}
			cn2.addOutNeighbor(cn1); //add edge between c2 and c1
			cn1.addOutNeighbor(cn2); //add edge between c1 and c2
		}
	}
	
	private List<Triple> Sort(List<Triple> a)
	{
		int n = a.size();
		if (n == 1){
			return a;
		}
		ArrayList<Triple> left = new ArrayList<Triple>(a.subList(0,n/2));
		ArrayList<Triple> right = new ArrayList<Triple>(a.subList(n/2, n));
		return this.Merge(this.Sort(left), this.Sort(right));
	}
	
	private List<Triple> Merge(List<Triple> b, List<Triple> c)
	{
		int p = b.size(); int q = c.size();
		ArrayList<Triple> d = new ArrayList<Triple>(p+q);
		int i = 0; int j = 0;
		while (i<p && j<q){
			if (b.get(i).getTime() <= c.get(j).getTime()){
				d.add(b.get(i));
				i++;
			}
			else{
				d.add(c.get(j));
				j++;
			}
		}
		if (i >= p){
			for (int k = j; k < q; k++){
				d.add(c.get(k));
			}
		}
		else{
			for (int k = i; k < p; k++){
				d.add(b.get(k));
			}
		}
		
		return d;
	}
	
	private class Triple
	{
		int comp1;
		int comp2;
		int time;
		
		private Triple(int c1, int c2, int t)
		{
			this.comp1 = c1;
			this.comp2 = c2;
			this.time = t;
		}
		
		private int getComp1()
		{
			return this.comp1;
		}
		
		private int getComp2()
		{
			return this.comp2;
		}			
		
		private int getTime()
		{
			return this.time;
		}
		
		public String toString()
		{
			return "("+this.comp1+", "+this.comp2+", "+this.time+")";
		}
	}
}

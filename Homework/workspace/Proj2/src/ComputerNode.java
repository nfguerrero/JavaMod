/**
 * @author Nicolas Guerrero
 */

import java.util.List;
import java.util.ArrayList;

public class ComputerNode 
{
	List<ComputerNode> out;
	int comp;
	int time;
	int status;
	ComputerNode pred;
	
	public ComputerNode(int c, int t)
	{
		this.comp = c;
		this.time = t;
		this.out = new ArrayList<ComputerNode>();
	}
	
	public int getID()
	{
		return this.comp;
	}
	
	public int getTimestamp()
	{
		return this.time;
	}
	
	public List<ComputerNode> getOutNeighbors()
	{
		return this.out;
	}
	
	public void addOutNeighbor(ComputerNode cn)
	{
		this.out.add(cn);
	}
	
	public int getStatus()
	{
		return this.status;
	}
	
	public void setStatus(int s)
	{
		this.status = s;
	}
	
	public ComputerNode getPred()
	{
		return this.pred;
	}
	
	public void setPred(ComputerNode p)
	{
		this.pred = p;
	}
	
	public String DFS_String()
	{
		return "(C"+this.comp+","+this.time+": "+this.status+")";
	}
	
	public String toString()
	{
		String outEdges = "[";
		for (ComputerNode cn : this.out){
			outEdges += "(C"+cn.getID()+", "+cn.getTimestamp()+")"; 
		}
		outEdges += "]";
		return "(C"+this.comp+", "+this.time+" -> "+outEdges+")";
	}
}

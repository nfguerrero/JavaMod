package edu.iastate.cs228.hw5.shared;
/**
 * @author Nic Fox G
 */

import java.util.ArrayList;

/**
 * @author Nic Fox G
 */

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class PathFinder {

    /**
     * This member always holds the cost of the path (if any)
     * found by the most recently finished solving operation. 
     * MIN_VALUE is used to signal that the value is not yet valid. 
     */
    public static int lastCost = Integer.MIN_VALUE;
	/**
	 * First, computes a shortest path from a source vertex to a destination
	 * vertex in a graph by using Dijkstra's algorithm. Second, visits and saves
	 * (in a stack) each vertex in the path, in reverse order starting from the
	 * destination vertex, by using the map object pred. Third, uses a
	 * List and Stack to generate the return Integer List by first pushing 
	 * each vertex into the stack, and then poping vertices 
	 * from the stack and adding the index of each to the 
	 * return list. The vertex indices in the return object are now in the
	 * right order. Note that the getIndex() method is called from a
	 * BareV object (vertex) to get its original integer name.
	 *
	 * @param G
	 *            - The graph in which a shortest path is to be computed
	 * @param source
	 *            - The first vertex of the shortest path
	 * @param dest
	 *            - The last vertex of the shortest path
	 * @return A List of Integers corresponding the the vertices on the path
	 *         in order from source to dest. 
	 *
	 *         The contents of an example String object: Path Length: 5 Path
	 *         Cost: 4 Path: 0 4 2 5 7 9
	 *
	 * @throws NullPointerException
	 *             - If any arugment is null
	 *
	 * @throws RuntimeException
	 *             - If the given source or dest vertex is not in the graph
	 *
	 */

    public static List<Integer> findPath(BareG g, BareV source, BareV dest) {
          lastCost = Integer.MIN_VALUE;
          
          if (g == null || source == null || dest == null) {
        	  throw new NullPointerException("One of the arguments are null.");
          }
          if (!g.checkVertex(source) || !g.checkVertex(dest)) {
        	  throw new RuntimeException("Either the source or destination vertex is not in the graph.");
          }
          
          //TODO: implement dijkstra's shortest path algorithm. Use
          // the supplied heap, and stack. 
          // you may also use HashMap and HashSet from JCF. 
          // the following is only here so that the app will run (but not
          // product correct results when first unpacked from the templates.
          
          Heap<Vpair<BareV, Integer>> priQ = new Heap<Vpair<BareV, Integer>>();
          ArrayList<Info<BareV, Integer>> table = new ArrayList<Info<BareV, Integer>>();
          ArrayList<BareV> visited = new ArrayList<BareV>();
          
          for (int i = 0; i < Math.max(source.getIndex(), dest.getIndex())+1; i++) {
        	  Info<BareV, Integer> toAdd = new Info<BareV, Integer>(g.getVertex(i), Integer.MAX_VALUE, null);
        	  table.add(toAdd);
          }
          
          priQ.add(new Vpair<BareV, Integer>(source, 0));
          updateTable(table, source, 0, null);
          while (!priQ.isEmpty()) {
        	  Vpair<BareV, Integer> curr = priQ.removeMin();
        	  Iterable<BareE> edges = curr.getVertex().getBareEdges();
        	  for (BareE edge : edges) {
        		  BareV v = edge.getToVertex();
        		  if (!visited.contains(v)) {
        			  int cost = curr.getCost() + edge.getWeight();
        			  priQ.add(new Vpair<BareV, Integer>(v, cost));
        			  updateTable(table, v, cost, curr.getVertex());
        		  }
        	  }
        	  visited.add(curr.getVertex());
          }
          
          Info<BareV, Integer> last = findInfo(table, dest);
          
          LinkedStack<Integer> stack = new LinkedStack<Integer>();
          if (last != null) {lastCost = last.getPath();}
          while(last != null) {
        	  stack.push(last.getVertex().getIndex());
        	  last = findInfo(table, last.getPred());
          }
          
          ArrayList<Integer> path = new ArrayList<Integer>();
          int size = stack.size();
          for (int i = 0; i < size; i++) {
        	  path.add(stack.pop());
          }
          System.out.format("findPath was called with start=%d, dest=%d%n"
                  + "Now you need to give it a real implementation.%n",
                  source.getIndex(), dest.getIndex());
          
          return path; 
    }  
    
    /**
     * Helper Method to update table of values
     * @param table	Table to update
     * @param v	Vertex to potentially update.
     * @param c	Cost to potentially update.
     * @param pred Predecessor vertex to update.
     */
    private static void updateTable(ArrayList<Info<BareV, Integer>> table, BareV v, Integer c, BareV pred) {
    	for (Info<BareV, Integer> info : table) {
    		if (info.getVertex().equals(v)) {
    			if (info.getPath().intValue() > c.intValue()) {
    				info.setPath(c);
    				info.setPred(pred);
    			}
    			return;
    		}
    	}
    	table.add(new Info<BareV, Integer>(v, c, pred));
    	return;
    }
    
    /**
     * Helper method that returns the info of a certain vertex in a table.
     * @param table	Table to check where the info of a certain vertex is.
     * @param v	Vertex to find info for in the given Table.
     * @return	Info of the given Vertex in the given Table.
     */
    private static Info<BareV, Integer> findInfo(ArrayList<Info<BareV, Integer>> table, BareV v) {
    	for (Info<BareV, Integer> info : table) {
    		if (info.getVertex().equals(v)) {
    			return info;
    		}
    	}
    	return null;
    }
    
    /**
     * A pair class with two components of types V and C, where V is a vertex
     * type and C is a cost type.
     */

    private static class Vpair<V, C extends Comparable<? super C>> implements
            Comparable<Vpair<V, C>> {
        private V node;
        private C cost;

        Vpair(V n, C c) {
            node = n;
            cost = c;
        }

        public V getVertex() {
            return node;
        }

        public C getCost() {
            return cost;
        }

        public int compareTo(Vpair<V, C> other) {
            return cost.compareTo(other.getCost());
        }

        public String toString() {
            return "<" + node.toString() + ", " + cost.toString() + ">";
        }

        public int hashCode() {
            return node.hashCode();
        }

        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if ((obj == null) || (obj.getClass() != this.getClass()))
                return false;
            // object must be Vpair at this point
            Vpair<?, ?> test = (Vpair<?, ?>) obj;
            return (node == test.node || (node != null && node
                    .equals(test.node)));
        }
    }
    
    /**
     * Helper class to store table data for vertices, shortest paths, and previous vertices.
     * @author Nic Fox G
     *
     * @param <V>	Vertex	
     * @param <N>	Path Length
     */
    private static class Info<V, N> {
    	private V vertex;
    	private N path;
    	private V pred;
    	
    	/**
    	 * Default Constructor
    	 * 
    	 * @param v1	Vertex	
    	 * @param n		Shortest Path to this Vertex from start
    	 * @param v2	Previous Vertex
    	 */
    	Info(V v1, N n, V v2) {
    		this.vertex = v1;
    		this.path = n;
    		this.pred = v2;
    	}
    	
    	/**
    	 * Accessor method that returns the Vertex
    	 * 
    	 * @return	Vertex of the info
    	 */
    	public V getVertex() {
    		return this.vertex;
    	}
    	
    	/**
    	 * Accessor method that returns the shortest path to this vertex from start
    	 * 
    	 * @return	Integer of shortest path
    	 */
    	public N getPath() {
    		return this.path;
    	}
    	
    	/**
    	 * Accessor method to return the predecessor vertex
    	 * @return
    	 */
    	public V getPred() {
    		return this.pred;
    	}
    	
    	/**
    	 * Modifier method that changes the current shortest path to this vertex
    	 * @param n	Shortest path to replace with the current one.
    	 */
    	public void setPath(N n) {
    		this.path = n;
    	}
    	
    	/**
    	 * Modifier method that changes the current predecessor to the vertex given
    	 * @param v	Vertext to change predecessor to.
    	 */
    	public void setPred(V v) {
    		this.pred = v;
    	}
    	
    	/**
    	 * toString method of class
    	 * @return Appropriate String value representing the data
    	 */
    	@Override
    	public String toString() {
    		return "(V: " + this.vertex.toString() + " ; C: " + this.path.toString() + " ; P: " + this.pred + ")";
    	}
    }

}

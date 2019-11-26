package edu.iastate.cs228.hw5;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.junit.Test;

import edu.iastate.cs228.hw5.mapStructures.Coordinate;
import edu.iastate.cs228.hw5.mapStructures.TerrainType;
import edu.iastate.cs228.hw5.rendering.HexGraph;
import edu.iastate.cs228.hw5.rendering.TerrainCell;
import edu.iastate.cs228.hw5.rendering.TerrainGraph;
import edu.iastate.cs228.hw5.shared.Graph.Edge;
import edu.iastate.cs228.hw5.shared.Graph.Vertex;

public class TestGraph {

    @Test
    public void test() {
       Coordinate.setGeometry(4, 4);
       TerrainGraph g = new TerrainGraph(4, 4);
       Vertex<TerrainCell> v = g.getVertex(0,2);
       assertNotNull(v);
       assertNotNull(v.getEdges());
       assertEquals(8, v.getIndex());
       System.out.println(v); 
       System.out.println();
       System.out.println(g);
       
       List<Edge> buffer = new ArrayList<Edge>();
       for (Edge x: v.getEdges()){
           buffer.add(x);
       }
       System.out.println(buffer);
       buffer.sort(new Comparator<Edge>() {

        @Override
        public int compare(Edge o1, Edge o2) {
            return o1.getTo() - o2.getTo();
        }       
           
       });
       System.out.println(buffer);

       assertEquals("[(8, 4: 165) , (8, 5: 165) , (8, 9: 165) , (8, 12: 165) ]", buffer.toString());
//       assertTrue(g.toString().contains("10: (10, 11: 165) (10, 5: 165) (10, 14: 165) (10, 6: 165) (10, 9: 165) (10, 7: 165) "));
       
    
    }
    
}

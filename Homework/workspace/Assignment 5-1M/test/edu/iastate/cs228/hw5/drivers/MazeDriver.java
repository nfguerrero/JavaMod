package edu.iastate.cs228.hw5.drivers;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.List;

import org.junit.Test;

import edu.iastate.cs228.hw5.shared.BareG;
import edu.iastate.cs228.hw5.shared.BareV;
import edu.iastate.cs228.hw5.shared.EE;
import edu.iastate.cs228.hw5.shared.Graph;
import edu.iastate.cs228.hw5.shared.Maze;
import edu.iastate.cs228.hw5.shared.MazeBuilder;
import edu.iastate.cs228.hw5.shared.PathFinder;

/**
 * This driver (it doesn't evaluate results) is handy for launching tests
 * (especially when you want to work in debug mode) of the shortest path
 * algorithm. 
 * 
 * NOTE: see MazeTest for an automated test covering all these files. 
 * 
 * @author Robert Ward
 *
 */
public class MazeDriver {
    private static String[] maze = { 
            "mazes/maze.txt",    
            "mazes/maze.10.10.50.txt",
            "mazes/maze.20.10.90.txt",
            "mazes/maze.30.100.10.txt",
            "mazes/maze.30.50.75.txt"
    };

    // edit this line to run with a different one of the above input files. 
    //     //
    int selected = 0; 
    @Test
    public void test() throws FileNotFoundException {
        MazeBuilder builder = new MazeBuilder();
        Graph<EE> g = builder.buildGraph(maze[selected]);
        System.out.println("A graph representing a maze");
        System.out.println(g);

        BareV source = builder.getEndVertex(g, true);
        BareV last = builder.getEndVertex(g, false);
        List<Integer> path = PathFinder.findPath(g, source, last);
        System.out.println("A shortest path in the maze");
        if ( path != null )
          System.out.println( path );
        else
          System.out.println( "No path exists" );
   }
    
   @Test
   public void testMain() {
       String[] args = { };
       Maze.main(args );
   }
}

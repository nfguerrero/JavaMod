package edu.iastate.cs228.hw5.autoTests;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import edu.iastate.cs228.hw5.shared.BareV;
import edu.iastate.cs228.hw5.shared.EE;
import edu.iastate.cs228.hw5.shared.Graph;
import edu.iastate.cs228.hw5.shared.MazeBuilder;
import edu.iastate.cs228.hw5.shared.PathFinder;

/**
 * If you aren't familiar with this style of unit test,
 * just google "junit parameterized tests" 
 * 
 * @author Robert Ward
 *
 */
@RunWith(Parameterized.class)
public class MazeTest {
    
    @Parameters 
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] { 
            { "mazes/maze.txt", "[0, 4, 2, 5, 7, 9]" },
            { "mazes/maze.10.10.50.txt","[0, 7, 9]" }, 
            { "mazes/maze.20.10.90.txt","[0, 11, 15, 19]" },
            { "mazes/maze.30.100.10.txt", "[0, 29]" },
            { "mazes/maze.30.50.75.txt","[0, 2, 29]" },
        });
    }

    public MazeTest(String dataRowXCol0, String dataRowXCol1){
         fileToUse = dataRowXCol0;
         expectedOutcome = dataRowXCol1;
    }
    
    public String fileToUse;
    public String expectedOutcome;
    
    @Test
    public void test() throws FileNotFoundException {
        assertEquals(expectedOutcome, executeWith(fileToUse));
    }
    
    public String executeWith(String fname) throws FileNotFoundException{
        MazeBuilder builder = new MazeBuilder();
        Graph<EE> g = builder.buildGraph(fname);
        BareV source = builder.getEndVertex(g, true);
        BareV last = builder.getEndVertex(g, false);
        List<Integer> path = PathFinder.findPath(g, source, last);
        System.out.println(fname+": "+path.toString() +", Cost="+PathFinder.lastCost); 
        return path.toString();
    }
}

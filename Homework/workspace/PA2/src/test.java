import java.io.FileNotFoundException;
import java.util.ArrayList;

public class test 
{
	public static void main(String[] args) throws FileNotFoundException
	{
		WGraph g = new WGraph("src/test.txt");
		
		for (int i = 0; i < g.connected; i++){
			System.out.print(g.vertices[i] + " ");
		} System.out.println("");
		for (int i = 0; i < g.edges.length; i++){
			System.out.print(g.edges[i] + " ");
		}
		
		ArrayList<Integer> vv = g.V2V(1, 2, 5, 6);
		System.out.println("\n" + vv);
		
		int[] s10 = {4,4, 5,6, 3,4};
		ArrayList<Integer> s1 = new ArrayList<Integer>();
		for (int i = 0; i < s10.length; i++){
			s1.add(s10[i]);
		}
		ArrayList<Integer> vs = g.V2S(1, 2, s1);
		System.out.println(vs);
		
		int[] s20 = {1,2, 2,2};
		ArrayList<Integer> s2a = new ArrayList<Integer>();
		for (int i = 0; i < s20.length; i++){
			s2a.add(s20[i]);
		}
		int[] s21 = {5,6, 4,4};
		ArrayList<Integer> s2b = new ArrayList<Integer>();
		for (int i = 0; i < s21.length; i++){
			s2b.add(s21[i]);
		}
		ArrayList<Integer> ss = g.S2S(s2a, s2b);
		System.out.println(ss);
	}
}

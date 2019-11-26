import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

public class Test 
{
	public static void main(String[] args)
	{
		
		CommunicationsMonitor cm = new CommunicationsMonitor();
		cm.addCommunication(1, 4, 12);
		cm.addCommunication(1, 2, 4);		
		cm.addCommunication(2, 4, 8);
		cm.addCommunication(3, 4, 8);		
		
		cm.createGraph();
		
		System.out.println(cm.queryInfection(3, 1, 8, 12));
		System.out.println(cm.queryInfection(1, 3, 4, 8));
		
		/**
		HashMap<Integer, ArrayList<Integer>> hm = new HashMap<Integer, ArrayList<Integer>>();
		hm.put(2, new ArrayList<Integer>());
		ArrayList<Integer> n1 = hm.get(1);
		n1 = new ArrayList<Integer>();
		n1.add(2);
		hm.get(2).add(3);
		
		System.out.println(hm);
		*/
	}
}

package edu.iastate.cs228.hw4;
import java.util.Arrays;

import edu.iastate.cs228.hw4.EntryTree;

public class Test 
{
	public static void main(String[] args)
	{
		EntryTree<Character, String> tree = new EntryTree<Character, String>();
		tree.showTree();
		Character[] chars = {'e','d','i','t'};
		tree.add(chars, "test");
		tree.showTree();
		Character[] chars2 = {'e','d','i','c','t'};
		tree.add(chars2, "test2");
		tree.showTree();
		Character[] chars3 = {'e','d','i','t','o','r'};
		tree.add(chars3, "test3");
		tree.showTree();
		Character[] chars4 = {'e','d','i','t','o','r','i','a','l'};
		//chars4 = tree.prefix(chars4);
		System.out.println(Arrays.toString(tree.prefix(chars4)));
		tree.remove(chars2);
		tree.showTree();
	}
}

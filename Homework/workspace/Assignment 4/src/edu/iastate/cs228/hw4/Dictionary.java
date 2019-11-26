package edu.iastate.cs228.hw4;

import java.util.Arrays;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.File;

/**
 * @author NFoxG
 * 
 *         An application class
 */
public class Dictionary {
	public static void main(String[] args) throws FileNotFoundException {
		// TODO
		Scanner scan = new Scanner(new File(args[0]));
		//Scanner scan = new Scanner(new File("src/infile.txt"));
		EntryTree<Character, String> tree = new EntryTree<Character, String>();
		while (scan.hasNextLine()) {
			String scanned = scan.next();
			if (scanned.equals("add")) {
				String Ks = scan.next();
				Character[] K = new Character[Ks.length()];
				for (int i = 0; i < Ks.length(); i++) {
					K[i] = Ks.charAt(i);
				}
				String V = scan.next();
				boolean result = tree.add(K, V);
				System.out.println("Command: add " + Ks + " " + V);
				System.out.println("Reuslt from add: " + result);
			}
			else if (scanned.equals("remove")) {
				String Ks = scan.next();
				Character[] K = new Character[Ks.length()];
				for (int i = 0; i < Ks.length(); i++) {
					K[i] = Ks.charAt(i);
				}
				String V = tree.remove(K);
				System.out.println("Command: remove " + Ks);
				System.out.println("Result from remove: " + V);
			}
			else if (scanned.equals("showTree")) {
				System.out.println("Command: showTree");
				System.out.println("Result from showTree\n");
				tree.showTree();				
			}
			else if (scanned.equals("search")) {
				String Ks = scan.next();
				Character[] K = new Character[Ks.length()];
				for (int i = 0; i < Ks.length(); i++) {
					K[i] = Ks.charAt(i);
				}
				String V = tree.search(K);
				System.out.println("Command: search " + Ks);
				System.out.println("Result from search: " + V);
			}
			else if (scanned.equals("prefix")) {
				String Ks = scan.next();
				Character[] K = new Character[Ks.length()];
				for (int i = 0; i < Ks.length(); i++) {
					K[i] = Ks.charAt(i);
				}
				System.out.println("Command: prefix " + Ks);
				System.out.println("Result from prefix: " + Arrays.toString(tree.prefix(K)));
			}
		}
	}
}

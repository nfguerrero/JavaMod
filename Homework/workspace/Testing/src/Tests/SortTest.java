package Tests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class SortTest {
	
	public static void main(String[] args)
	{
		String[] words = new String[10];
		ArrayList<String> testWords = new ArrayList<String>();
		testWords.add("apple");testWords.add("anus");testWords.add("bat");testWords.add("boy");testWords.add("cat");testWords.add("cow");testWords.add("dad");testWords.add("dog");testWords.add("eep");testWords.add("exact");
		testWords.add("fly");testWords.add("fuck");testWords.add("god");testWords.add("guts");testWords.add("hi");testWords.add("hit");testWords.add("in");testWords.add("int");testWords.add("just");testWords.add("justice");
		testWords.add("kar");testWords.add("karp");testWords.add("lose");testWords.add("loser");testWords.add("mom");testWords.add("momma");testWords.add("nic");testWords.add("nina");testWords.add("old");testWords.add("omnom");
		testWords.add("pen");testWords.add("penis");testWords.add("q");testWords.add("quotient");testWords.add("rage");testWords.add("rape");testWords.add("sex");testWords.add("slap");testWords.add("trap");testWords.add("turtle");
		testWords.add("ultra");testWords.add("umm");testWords.add("vagina");testWords.add("vip");testWords.add("wet");testWords.add("whirl");testWords.add("xap");testWords.add("xlag");testWords.add("yaxis");testWords.add("yup");
		testWords.add("zit");testWords.add("zone");
		char[] charOrdering = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
		LexTest lex = new LexTest(charOrdering);
		
		Random gen = new Random();
		for (int i = 0; i < words.length; i++) {
			words[i] = testWords.get(gen.nextInt(testWords.size()));
		}
		
		System.out.println(Arrays.toString(words));
		
		//mergeSort(words, lex);
		//selectSort(words, lex);
		quickSort(words, lex);
		
		System.out.println(Arrays.toString(words));
	}
	
	//This method will be called by the base class sort() method to 
	// actually perform the sort. 
	private static void mergeSort(String[] words, Comparator<String> comp) {
		//TODO: implement mergeSort. 
		if (words.length >= 2) {
			String[] first = new String[words.length/2];
			for (int i = 0; i < first.length; i++) {
				first[i] = new String(words[i]);
			}
			
			String[] second = new String[words.length-first.length];
			for (int i = 0; i < second.length; i++) {
				second[i] = new String(words[first.length+i]);
			}
			
			mergeSort(first, comp);
			mergeSort(second, comp);
			
			merge(first, second, words, comp);
		}
		
		
	}
	
	/**
	 * Helper method for the sortHelper method to merge the first and second halves of the list to be sorted
	 * @param first		String[] First half of list to be sorted.
	 * @param second	String[] Second half of list to be sorted.
	 * @param words		String[] Original list to be modified and sorted.
	 * @param comp		Comparator<String> comparator to use when comparing words to be sorted.
	 */
	private static void merge(String[] first, String[] second, String[] words, Comparator<String> comp) 
	{
		int w = 0; int f = 0; int s = 0;
		for (int i = 0; i < first.length + second.length; i++) {
			if (s >= second.length || (f < first.length && comp.compare(first[f], second[s]) <= 0)) {
				words[w] = first[f];
				f++;
			}
			else if (f >= first.length || (s < second.length && comp.compare(first[f], second[s]) > 0)) {
				words[w] = second[s];
				s++;
			}
			w++;
		}
	}
	
	//This method will be called by the base class sort() method to 
	// actually perform the sort. 
	private static void selectSort(String[] words, Comparator<String> comp) {
		//TODO: implement SelectionSort
		if (words.length > 1) {
			for (int i = 0; i < words.length; i++) {
				int min = i;
				for (int j = i+1; j < words.length; j++) {
					if (comp.compare(words[j], words[min]) < 0) {
						min = j;
					}
				}
				String temp = words[i];
				words[i] = words[min];
				words[min] = temp;
			}
		}
	}
		
	private static void quickSort(String[] words, Comparator<String> comp) {
		//TODO: implement QuickSort
		boolean allSame = true;
		int pivot = words.length/2;
		String pivotWord = words[pivot];
		int less = 0;
		
		for (int w = 0; w < words.length; w++) {
			if (!words[w].equals(pivotWord)) {
				allSame = false;
			}
			if (comp.compare(words[w], words[pivot]) < 0) {
				less++;
			}
		}
		
		if (!allSame) {
			
			String[]  partLess = new String[less];
			int part = 0;
			
			if (partLess.length > 0) {
				for (int i = 0; i < words.length; i++) {
					if (comp.compare(words[i], pivotWord) < 0) {
						partLess[part] = words[i];
						part++;
					}
					if (partLess[partLess.length-1] != null) {
						i = words.length;
					}
				}
			}
			
			String[] partMore = new String[words.length-partLess.length-1];
			part = 0;
			
			if (partMore.length > 0) {
				for (int i = 0; i < words.length; i++) {
					if (i != pivot && comp.compare(words[i], pivotWord) >= 0) {
						partMore[part] = words[i];
						part++;
					}
					if (partMore[partMore.length-1] != null) {
						i = words.length;
					}
				}
			}
			
			if (partLess.length > 1) {
				quickSort(partLess, comp);
			}
			if (partMore.length > 1) {
				quickSort(partMore, comp);
			}
			
			int w = 0;
			for (int i = 0; i < partLess.length; i++) {
				words[w] = partLess[i]; w++;
			}
			
			words[w] = pivotWord; w++;
			for (int i = 0; i < partMore.length; i++) {
				words[w] = partMore[i]; w++;
			}
		}
	}
}

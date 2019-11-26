package edu.iastate.cs228.hw2;

import java.util.Arrays;
import java.util.Comparator;

public class MergeSort extends SorterWithStatistics {
	
	//This method will be called by the base class sort() method to 
	// actually perform the sort. 
	@Override
	public void sortHelper(String[] words, Comparator<String> comp) {
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
			
			sortHelper(first, comp);
			sortHelper(second, comp);
			
			mergeSort(first, second, words, comp);
		}
		
		
	}
	
	/**
	 * Helper method for the sortHelper method to merge the first and second halves of the list to be sorted
	 * @param first		String[] First half of list to be merged.
	 * @param second	String[] Second half of list to be merged.
	 * @param words		String[] Original list to be modified and sorted.
	 * @param comp		Comparator<String> comparator to use when comparing words to be sorted.
	 */
	private void mergeSort(String[] first, String[] second, String[] words, Comparator<String> comp) 
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
}

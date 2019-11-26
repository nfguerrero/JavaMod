package edu.iastate.cs228.hw2;

import java.util.Arrays;
import java.util.Comparator;

public class SelectionSort extends SorterWithStatistics {
	
	//This method will be called by the base class sort() method to 
	// actually perform the sort. 
	@Override
	public void sortHelper(String[] words, Comparator<String> comp) {
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
}

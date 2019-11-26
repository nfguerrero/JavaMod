package edu.iastate.cs228.hw2;

import java.util.Arrays;
import java.util.Comparator;

public class QuickSort extends SorterWithStatistics {

	//This method will be called by the base class sort() method to 
	// actually perform the sort. 
	@Override
	public void sortHelper(String[] words, Comparator<String> comp) {
		//TODO: implement QuickSort;
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
				sortHelper(partLess, comp);
			}
			if (partMore.length > 1) {
				sortHelper(partMore, comp);
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

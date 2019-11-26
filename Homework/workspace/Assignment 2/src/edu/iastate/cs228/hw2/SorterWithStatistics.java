package edu.iastate.cs228.hw2;

import java.util.Comparator;

public abstract class SorterWithStatistics implements Sorter {

	private Stopwatch timer = new Stopwatch();
	private Stopwatch iTimer;
	private int wordsSorted;
	private int totalSorted;

	/***
	 * Default constructor
	 */
	public SorterWithStatistics() {
		// TODO
		this.totalSorted = 0;
	}

	/***
	 * Public interface to sortHelper that keeps track of performance
	 * statistics, including counting words sorted and timing sort instances.
	 * 
	 * @param words
	 *            input array to be sorted.
	 * @param comp
	 *            Comparator used to sort the input array.
	 */
	public void sort(String[] words, Comparator<String> comp) {
		// TODO
		this.wordsSorted = 0;
		this.wordsSorted += words.length;
		this.totalSorted += words.length;
		timer.start();
		iTimer = new Stopwatch();
		iTimer.start();
		this.sortHelper(words, comp);
	}

	/**
	 * Sorts the array words. 
	 * 
	 * @param words
	 *            input array to be sorted.
	 * @param comp
	 *            Comparator used to sort the input array.
	 */
	protected abstract void sortHelper(String[] words, Comparator<String> comp);

	/**
	 * Returns number of words sorted in last sort. Throws IllegalStateException
	 * if nothing has been sorted.
	 * 
	 * @return number of words sorted in last sort.
	 */
	public int getWordsSorted() {
		// TODO: return a meaningful result.
		return this.wordsSorted;
	}

	/**
	 * Returns time the last sort took. Throws IllegalStateException if nothing
	 * has been sorted.
	 * 
	 * @return time last sort took.
	 */
	public long getTimeToSortWords() {
		// TODO: return a meaningful result.
		return this.iTimer.getElapsedTime();
	}

	/**
	 * Returns total words sorted by this instance.
	 * 
	 * @return total number of words sorted.
	 */
	public int getTotalWordsSorted() {
		// TODO: return a meaningful result.
		return this.totalSorted;
	}

	/**
	 * Returns the total amount of time spent sorting by this instance.
	 * 
	 * @return total time spent sorting.
	 */
	public long getTotalTimeToSortWords() {
		// TODO: return a meaningful result.
		return this.timer.getElapsedTime();
	}

	/**
	 * @return a summary of statistics for the last sorting run.
	 * 
	 *         See the project description for details about what to include.
	 *         This method should NOT generate output directly.
	 */
	public String getReport() {
		// TODO: compose and return a meaningful result.
		String wordsLength = this.wordsSorted + "\n";
		String total = this.totalSorted + "\n";
		String totalTime = this.timer.getElapsedTime() + "\n";
		String average = ((double) this.timer.getElapsedTime())/this.wordsSorted + "\n";
		String wps = ((double) this.totalSorted)/this.timer.getElapsedTime() + "\n\n";
		
		return wordsLength + total + totalTime + average + wps;
	}
}

package edu.iastate.cs228.hw2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class EvalSorts {
	public static final int kNumberOfWordsToSort = 10000;

	/**
	 * Main is responsible for only: 
	 * <UL>
	 *     <LI>extracting file names from args
	 *         (the character order file and the word file),</LI>
	 *     <LI> reading the files, and </LI>
	 *     <LI> constructing an instance of the app 
	 *         configured with the input data.</LI> 
	 * </UL>
	 * All file related exceptions should be handled in main. 
	 * 
	 * @param args
	 */
	public static void main(String args[]) {		
		String[] words = new String[10];
		ArrayList<String> testWords = new ArrayList<String>();
		testWords.add("apple");testWords.add("anus");testWords.add("bat");testWords.add("boy");testWords.add("cat");testWords.add("cow");testWords.add("dad");testWords.add("dog");testWords.add("eep");testWords.add("exact");
		testWords.add("fly");testWords.add("fuck");testWords.add("god");testWords.add("guts");testWords.add("hi");testWords.add("hit");testWords.add("in");testWords.add("int");testWords.add("just");testWords.add("justice");
		testWords.add("kar");testWords.add("karp");testWords.add("lose");testWords.add("loser");testWords.add("mom");testWords.add("momma");testWords.add("nic");testWords.add("nina");testWords.add("old");testWords.add("omnom");
		testWords.add("pen");testWords.add("penis");testWords.add("q");testWords.add("quotient");testWords.add("rage");testWords.add("rape");testWords.add("sex");testWords.add("slap");testWords.add("trap");testWords.add("turtle");
		testWords.add("ultra");testWords.add("umm");testWords.add("vagina");testWords.add("vip");testWords.add("wet");testWords.add("whirl");testWords.add("xap");testWords.add("xlag");testWords.add("yaxis");testWords.add("yup");
		testWords.add("zit");testWords.add("zone");
		char[] charOrdering = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
		LexiconImpl lex = new LexiconImpl(charOrdering);
		
		Random gen = new Random();
		for (int i = 0; i < words.length; i++) {
			words[i] = testWords.get(gen.nextInt(testWords.size()));
		}
		EvalSorts theApp;   //main's ref to the app. 
		/*
		 * 
		 *      Here you should add code that extracts the file names from the args array,
		 *      opens and reads the data from the files,constructs an instance of Lexicon from the character order file, 
		 *      and then create an instance of this class (EvalSorts) to act as a configured
		 *      instance of the application. After you have constructed the configured
		 *      instance, you should start it running (see below). 
		 *      
		 *      Note: in production code main() is NOT the application. It is onely the code that bridges from 
		 *      the operating system's default user interface (the shell's command line) and the application. 
		 *      
		 *   
		 *  
		*/		
		

		//configure an instance of the app
		theApp = new EvalSorts(lex, words, kNumberOfWordsToSort);
		//now execute that instance
		theApp.runSorts();		
	}

	//these are instance member variables belonging to the app (not main())
	private String[] words; //the app's private ref to the word lit
	private Lexicon lex;    //the app's private ref to the relevant lexicon	
	private int numWordsToSort = kNumberOfWordsToSort;
	
	/**
	 * This constructor configures an instance of EvalSorts to sort input read
	 * my main, using the character order read by main and now embedded in
	 * an instance of Lexicon
	 * @param lex the instance of lexicon to be used
	 * @param wordList the wordlist (as array of string)  to be sorted
	 * @param numWordsToSort each sort will be repeated until it has sorted
	 *                       this many words. 
	 */
	public EvalSorts(Lexicon lex, String[] wordList, int numWordsToSort) {
		//TODO: capture what is needed to configure the app	
		//this.words = wordList;
		//this.lex = lex;
		this.numWordsToSort = numWordsToSort;
		try {this.words = readWordsFile("src/edu/words.txt", lex);}
		catch (FileNotFoundException e) {}
		catch (FileConfigurationException e) {}
		try { this.lex = new LexiconImpl(readCharacterOrdering("src/edu/ordering.txt"));}
		catch (FileNotFoundException e) {}
		catch (FileConfigurationException e) {}
	}

	/**
	 * runSorts() performs the sort evaluation. 
	 * 
	 * Note: The three sorters extend a common base
	 * so they share the same interface for starting the sort and collecting statistics. 
	 * Thus, you should create instances of the sorter and save references to each in an 
	 * array of base type. This allows you to use a simple loop to run all the reports and 
	 * collect the statistics. If you have 
	 * 
	 *   selectionsort.sort();
	 *   mergesort.sort();
	 *   quicksort.sort();
	 *   
	 *   or three cut and paste blocks (one for each sort) 
	 *   in your solution, you are not exploiting the polymorphic behavior of the sorters
	 *   and you will lose points for style.  
	 */
	public void runSorts(){
		
		SorterWithStatistics[] sorters = new SorterWithStatistics[3];
		//TODO: run the sorters (repeatedly), collect statistics, and generate 
		// a report (see the pdf for details). 
		sorters[0] = new SelectionSort();
		sorters[1] = new MergeSort();
		sorters[2] = new QuickSort();
		
		String[] temp = new String[this.words.length];
		for (int i = 0; i < words.length; i++) {
			temp[i] = this.words[i];
		}
		
		int sorted = 0;
		while (sorted < this.numWordsToSort) {
			for (int i = 0; i < temp.length; i++) {
				sorted += temp.length;
				sorters[0].sort(temp, this.lex);
				for (int w = 0; w < this.words.length; w++) {
					temp[w] = this.words[w];
				}
			}
		}
		System.out.println(sorters[0].getReport());
		
		sorted = 0;
		while (sorted < this.numWordsToSort) {
			for (int i = 0; i < temp.length; i++) {
				sorted += temp.length;
				sorters[1].sort(temp, this.lex);
				for (int w = 0; w < this.words.length; w++) {
					temp[w] = this.words[w];
				}
			}
		}
		System.out.println(sorters[1].getReport());
		
		sorted = 0;
		while (sorted < this.numWordsToSort) {
			for (int i = 0; i < temp.length; i++) {
				sorted += temp.length;
				sorters[2].sort(temp, this.lex);
				for (int w = 0; w < this.words.length; w++) {
					temp[w] = this.words[w];
				}
			}
		}
		System.out.println(sorters[2].getReport());
	}
	
	/**
	 * Reads the characters contained in filename and returns them as a character array.
	 * @param filename the file containing the list of characters
	 * @returns an char array representing the ordering of characters to be used 
	 *          or null if there is a FileNotFoundException.
	 */
	public static char[] readCharacterOrdering(String filename) 
			throws FileNotFoundException, FileConfigurationException {
		//TODO: write the method. return something meaningful. 
		try {
			File file = new File(filename);
			Scanner scan = new Scanner(file);
			ArrayList<Character> charsArray = new ArrayList<Character>();
			while (scan.hasNextLine()) {
				String next = scan.nextLine();
				if (next.length() != 1) {
					scan.close();
					throw new FileConfigurationException();
				}
				charsArray.add(("" + next).charAt(0));
			}
			char[] chars = new char[charsArray.size()];
			for (int i = 0; i < chars.length; i++) {
				chars[i] = charsArray.get(i);
			}
			scan.close();
			return chars;
		}
		catch (FileNotFoundException e) {
			return null; 
		}
	}
	
	/**
	 * Reads the words from the file and returns them as a String array.
	 * @param filename file containing words
	 * @return the words contained in the file or null if there was a FileNotFoundException.
	 */
	public static String[] readWordsFile(String filename, Lexicon comp)
			throws FileNotFoundException, FileConfigurationException {
		//TODO: write the method. return something meaningful. 
		try {
			File file = new File(filename);
			Scanner scan = new Scanner(file);
			ArrayList<String> wordsArray = new ArrayList<String>();
			while (scan.hasNextLine()) {
				wordsArray.add(scan.nextLine());
			}
			String[] words = new String[wordsArray.size()];
			for (int i = 0; i < words.length; i++) {
				words[i] = wordsArray.get(i);
			}
			scan.close();
			return words;
		}
		catch (FileNotFoundException e) {
			return null; 
		}	
	}

}

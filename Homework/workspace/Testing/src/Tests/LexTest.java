package Tests;

import java.util.Arrays;
import java.util.Comparator;
import java.io.File;

/**
 * 
 * @author Nic Fox G
 *
 */
public class LexTest implements Comparator<String> {

    /***
     * Lookup table mapping characters in lexicographical order to
     * to their input order. This is public to support automated grading. 
     */
	public CharacterValue[] characterOrdering; 

    /***
     * Creates an array of CharacterValue from characterOrdering.  Sorts
     * it using Arrays.sort().
     * @param characterOrdering character order from configuration file
     */	
	public LexTest(char[] characterOrdering) {
		//TODO:
		this.characterOrdering = new CharacterValue[characterOrdering.length];
		for (int i = 0; i < characterOrdering.length; i ++) {
			this.characterOrdering[i] = new CharacterValue(i, characterOrdering[i]);
		}
		
		Arrays.sort(this.characterOrdering, new CharacterValueComparator());
	}


    /***
     * Compares two words based on the configuration
     * @param a first word
     * @param b second word
     * @return negative if a<b, 0 if equal, positive if a>b
     */
	@Override
	public int compare(String a, String b) {
		//TODO: write it and return something meaningful. 
		if (a.equals(b)) {
			return 0;
		}
		for (int i = 0; i < Math.min(a.length(), b.length()); i++) {
			if (this.getCharacterOrdering(a.charAt(i)) < this.getCharacterOrdering(b.charAt(i))) {
				return -1;
			}
			else if (this.getCharacterOrdering(a.charAt(i)) > this.getCharacterOrdering(b.charAt(i))) {
				return 1;
			}
		}
		if (a.length() == b.length()) {
			return 0;
		}
		return a.length() - b.length();
	}
	
	/**
	 * Uses binary search to find the order of key.
	 * @param key
	 * @return ordering value for key or -1 if key is an invalid character.
	 */
	public int getCharacterOrdering(char key) {
		//TODO: write it and return something meaningful. 
		//Binary search
		int first = 0; int last = this.characterOrdering.length-1; int mid = (first + last) / 2;
		
		while (last >= first) {
			if (this.characterOrdering[mid].character > key) {
				last = mid - 1;
			}
			else if (this.characterOrdering[mid].character < key) {
				first = mid + 1;
			}
			else {
				return this.characterOrdering[mid].value;
			}
			mid = (first + last) / 2;
		}
		return -1;
	}

	/**
	 * Searches characterOrdering for key via binary search.
	 * This is public only to facilitate automated grading. 
	 * @param characterOrdering the specified sort order
         * @param key the search term
	 * @return ordering value for key or -1 if key is an invalid character.
	 */
	public static class CharacterValue{
		//TODO: 
		/**
		 * int value associated with index of character.
		 * char value associated with character value.
		 */
		public int value;
		public char character;
		
		/**
		 * Constructor for inner class CharacterValue.
		 * @param value		Index value associated with character.
		 * @param character		Character value to be assigned.
		 */
		public CharacterValue(int value, char character) {
			this.value = value;
			this.character = character;
		}
		
		
		/**
		 * Compares two instances of CharacterValue and returns boolean of equality status.
		 * @param cv	CharacterValue object to compare to this one.
		 * @return	boolean of equality status between the passed object and this one.
		 */
		public boolean equals(CharacterValue cv) {
			return this.character == cv.character;
		}
		
	}
	/**
	 * Comparator class for CharacterValue.	 *
	 */
	public static class CharacterValueComparator implements Comparator<CharacterValue>{

		/**
		 * Compares two instances of CharacterValue and returns equality relationship.
		 * @param cv1	First CharacterValue object to compare.
		 * @param cv2	Second CharacterValue object to compare.
		 * @return	int of difference between the characters assigned to the CharacterValue objects.
		 */
		@Override
		public int compare(CharacterValue cv1, CharacterValue cv2) {
			return cv1.character - cv2.character;
		}
		
	}
	
	/**
	 * Returns whether or not word is valid according to the alphabet
	 * known to this lexicon. 
	 * 
	 * @param word word to be checked.
	 *
	 * @return true if valid. false otherwise
	 */
	public boolean isValid(String word) {
		//TODO: write it and return something meaningful. 
		return false; 

	}
	
}
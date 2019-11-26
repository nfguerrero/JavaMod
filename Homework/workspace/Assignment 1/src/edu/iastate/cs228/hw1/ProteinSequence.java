package edu.iastate.cs228.hw1;

import java.util.ArrayList;

/*
 * @author - Nicolas Guerrero
*/

public class ProteinSequence extends Sequence
{
  public ProteinSequence(char[] psarr)
  {
	  super(psarr);
  }

  @Override
  public boolean isValidLetter(char aa)
  {
	  ArrayList<Character> others = new ArrayList<Character>();
	  others.add('b'); others.add('j'); others.add('o'); others.add('u'); others.add('x'); others.add('z');
	  if (Character.isAlphabetic(aa) && !others.contains(Character.toLowerCase(aa))) {
		  return true;
	  }
	  return false;
  }
}

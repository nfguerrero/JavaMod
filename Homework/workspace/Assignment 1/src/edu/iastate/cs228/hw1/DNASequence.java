package edu.iastate.cs228.hw1;

/*
 * @author - Nicolas Guerrero
*/

public class DNASequence extends Sequence
{
  public DNASequence(char[] dnaarr)
  {
	  super(dnaarr);
  }

  @Override
  public boolean isValidLetter(char let)
  {
	  if (!super.isValidLetter(let)) {
		  return false;
	  }
	  String[] acceptable = {"a", "c", "g", "t"};
	  String compare = let + ""; compare = compare.toLowerCase();
	  
	  for (String comparable : acceptable) {
		  if (compare.equals(comparable)) {
			  return true;
		  }
	  }
	  return false;
  }

}

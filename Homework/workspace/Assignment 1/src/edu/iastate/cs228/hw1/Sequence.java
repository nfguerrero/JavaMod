package edu.iastate.cs228.hw1;


/*
 * @author - Nicolas Guerrero
*/

public class Sequence
{
  protected char[] seqarr;

  public Sequence(char[] sarr)
  {
	  char[] temp = new char[sarr.length];
	  
	  for (int i = 0; i < sarr.length; i++) {
		  if (isValidLetter(sarr[i])) {
			  temp[i] = sarr[i];			 
		  }
		  else { throw new IllegalArgumentException("Invalid sequence letter for class " + getClass().getName());}
	  }
	  
	  this.seqarr = temp;
  }

  public int seqLength()
  {
	  return this.seqarr.length;
  }
  
  public char[] getSeq()
  {
	  char[] temp = new char[this.seqarr.length];
	  
	  for (int i = 0; i < temp.length; i++) {
		  temp[i] = this.seqarr[i];
	  }
	  
	  return temp;
  }

  public String toString()
  {
	  return this.seqarr.toString();
  }

  public boolean equals(Object obj)
  { 
	  if (obj == null || !obj.getClass().equals(getClass())) {
		  return false;
	  }
	  
	  
	  Sequence seq = (Sequence) obj;
	  char[] temp = seq.getSeq();
	  for (int i = 0; i < temp.length; i++) {
		  if (!(this.seqarr[i] + "").equalsIgnoreCase(temp[i] + "")) {
			  return false;
		  }
	  }
	  
	  return true;
  }

  public boolean isValidLetter(char let)
  {
     return (Character.isUpperCase(let) || Character.isLowerCase(let));
  }

}

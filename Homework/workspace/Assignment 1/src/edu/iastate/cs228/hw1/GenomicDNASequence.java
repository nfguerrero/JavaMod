package edu.iastate.cs228.hw1;

import java.util.ArrayList;

/*
 * @author - Nicolas Guerrero
*/

public class GenomicDNASequence extends DNASequence
{
  private boolean[] iscoding;

  public GenomicDNASequence(char[] gdnaarr)
  {
	  super(gdnaarr);
	  this.iscoding = new boolean[gdnaarr.length];
	  for (int i = 0; i < this.iscoding.length; i++) {
		  iscoding[i] = false;
	  }
  }

  public void markCoding(int first, int last)
  {
	  boolean fgl = first > last;
	  boolean flz = first < 0;
	  boolean lgel = last >= seqLength();
	  if (fgl || flz || lgel) {
		  throw new IllegalArgumentException("Coding border is out of bound");
	  }
	  else {
		  for (int i = first; i <= last; i++) {
			  this.iscoding[i] = true;
		  }
	  }
  }

  public char[] extractExons(int[] exonpos)
  {
	  if (exonpos.length == 0 || exonpos.length%2 != 0) {
		  throw new IllegalArgumentException("Empty array or odd number of array elements");
	  }
	  for (int i = 0; i < exonpos.length; i++) {
		  if (exonpos[i] < 0 || exonpos[i] >= seqLength()) {
			  throw new IllegalArgumentException("Exon position is out of bound");
		  }
		  if (i < exonpos.length-1 && exonpos[i] > exonpos[i+1]) {
			  throw new IllegalArgumentException("Exon positions are not in order");
		  }
		  if (!this.iscoding[exonpos[i]]) { //Potentially need to check spots between exonpos start and end
			  throw new IllegalStateException("Noncoding postion is found");
		  }
	  }
	  ArrayList<char[]> exons = new ArrayList<char[]>(); int e = 0;
	  for (int i = 0; i < exonpos.length-1; i += 2) {
		  char[] temp = new char[(exonpos[i+1]+1)-exonpos[i]];
		  e += ((exonpos[i+1]+1)-exonpos[i]);
		  for (int c = 0; c < temp.length; c++) {
			  temp[c] = getSeq()[exonpos[i]+c];
		  }
		  exons.add(temp);
	  }
	  char[] extracted = new char[e]; e = 0;
	  for (char[] ex : exons) {
		  for (int i = 0; i < ex.length; i++, e++) {
			  extracted[e] = ex[i];
		  }
	  }
	  return extracted;
  }

}

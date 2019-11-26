package edu.iastate.cs228.hw1;

import static org.junit.Assert.*;

import org.junit.Test;

public class AllTests {

	@Test
	public void test1() {
		try {
			String probst = new String("T$G");
			Sequence seqobj = new Sequence( probst.toCharArray() );
		}
		catch (Exception e) {
			assertTrue(e.getMessage().equals("Invalid sequence letter for class edu.iastate.cs228.hw1.Sequence"));
		}
	}
	
	@Test
	public void test2() {
		try {
			String probst2 = new String("TDG");
			DNASequence dnaseqobj = new DNASequence( probst2.toCharArray() );
		}
		catch (Exception e) {
			assertTrue(e.getMessage().equals("Invalid sequence letter for class edu.iastate.cs228.hw1.DNASequence"));
		}
	}
	
	@Test
	public void test3() {
		try {
			String probst3 = new String("TGCH");
			GenomicDNASequence gdnaobj = new GenomicDNASequence( probst3.toCharArray() );
		}
		catch (Exception e) {
			assertTrue(e.getMessage().equals("Invalid sequence letter for class edu.iastate.cs228.hw1.GenomicDNASequence"));
		}
	}
	
	@Test
	public void test4() {
		try {
			String probst4 = new String("BJU");
			ProteinSequence probj = new ProteinSequence( probst4.toCharArray() );
		}
		catch (Exception e) {
			assertTrue(e.getMessage().equals("Invalid sequence letter for class edu.iastate.cs228.hw1.ProteinSequence"));
		}
	}
}

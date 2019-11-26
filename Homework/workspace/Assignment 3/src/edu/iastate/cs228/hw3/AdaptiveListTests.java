package edu.iastate.cs228.hw3;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author Ryan Krause
 *
 * Use these tests to see if your code is working. These test do not cover all use cases.
 */
public class AdaptiveListTests {

	AdaptiveList<String> emptyList;
	AdaptiveList<String> twoElementList;
	ListIterator<String> listIterator;
	ListIterator<String> listIteratorAtEnd;
	
	@Before
	public void setUp() {
		emptyList = new AdaptiveList<String>();
		
		twoElementList = new AdaptiveList<String>();
		twoElementList.add("One");
		twoElementList.add("Two");
		
		listIterator = twoElementList.listIterator();
		listIteratorAtEnd = twoElementList.listIterator(2);
		
	}
	
	@Test
	public void checkSetUp() {
		assertTrue("Add doesn't work yet", twoElementList.head.link.data.equals("One") && twoElementList.head.link.link.data.equals("Two"));
		assertTrue("Size is not updated in add." , twoElementList.size() == 2);
	}
	
	@Test
	public void copyConstructor() {
		emptyList = new AdaptiveList<String>(twoElementList);
		assertTrue("Elements weren't added correctly", emptyList.head.link.data.equals("One") && emptyList.head.link.link.data.equals("Two"));
		assertTrue("Size is not updated." , twoElementList.size() == 2);
	}
	
	@Test
	public void testIsEmptyTrue() {
		assertTrue("New list doesn't return true", emptyList.isEmpty());
	}
	
	@Test
	public void testIsEmptyAddRemove() {
		emptyList.add("Test");
		emptyList.remove("Test");
		assertTrue("List doesn't return true after adding and removing objects", emptyList.isEmpty());
	}
	
	@Test
	public void testAddAllTrue() {
		emptyList.addAll(twoElementList);
		assertTrue("Elements weren't added correctly", emptyList.head.link.data.equals("One") && emptyList.head.link.link.data.equals("Two"));
		assertTrue("Size wasn't updated in add all", emptyList.size() == 2);			
	}
	
	@Test
	public void testAddAllFalse() {
		assertFalse("Adding an empty collection using addAll() should return false", emptyList.addAll(emptyList));
	}
	
	@Test
	public void testRemoveObjValid() {
		assertTrue("If an object is removed it should return false", twoElementList.remove("One"));
		assertTrue("String should be removed from list when remove is called", twoElementList.head.link.data.equals("Two"));
		assertTrue("When calling remove, size should be updated", twoElementList.size() == 1);
	}
	
	@Test
	public void testRemoveObjInvalid() {
		assertFalse("If an object isn't found in remove, should return false", twoElementList.remove("Bad"));
	}
	
	@Test
	public void testAddAtPosition() {
		twoElementList.add(1, "Middle");
		assertTrue("Element isn't added to the right place", twoElementList.head.link.data.equals("One") 
				&& twoElementList.head.link.link.data.equals("Middle")
				&& twoElementList.head.link.link.link.data.equals("Two"));
		assertTrue("Size isn't updated correctly", twoElementList.size() == 3);
	}
	
	@Test
	public void testRemoveAtPosition() {
		twoElementList.remove(1);
		assertTrue("Element wasn't removed correctly", twoElementList.head.link.data.equals("One"));
		assertTrue("Size wasn't updated correctly", twoElementList.size() == 1);
	}
	
	@Test
	public void testSet() {
		twoElementList.set(0, "NotOne");
		assertEquals("Element wasn't changed correctly", ((Object[])twoElementList.theArray)[0], "NotOne");
		assertTrue("Array needs to be updated when set is called", ((Object[])twoElementList.theArray)[1].equals("Two"));
	}
	
	@Test
	public void testGet() {
		assertEquals("Your array may be out of date or get isn't working", twoElementList.get(1), "Two");
	}
	
	@Test
	public void testAddAfterSet() {
		twoElementList.set(0, "NotOne");
		twoElementList.add("Three");
		assertTrue("List needs to be updated after set is called", twoElementList.head.link.data.equals("NotOne") 
				&& twoElementList.head.link.link.data.equals("Two")
				&& twoElementList.head.link.link.link.data.equals("Three"));
	}
	
	@Test
	public void testRemoveAfterSet() {
		twoElementList.set(0, "NotOne");
		assertTrue("List needs to be updated after set is called", twoElementList.remove("NotOne"));
	}
	
	@Test
	public void testContainsTrue() {
		assertTrue("Contains doesn't return true when the element is in the list", twoElementList.contains("One"));
	}
	
	@Test
	public void testContainsFalse() {
		assertFalse("Contains should return false for an element not in the list", twoElementList.contains("Wrong"));
	}
	
	@Test
	public void testContainsAfterSet() {
		twoElementList.set(0, "NotOne");
		assertTrue("Contains doesn't return true when the element is in the list after calling set", twoElementList.contains("NotOne"));
	}
	
	@Test
	public void testContainsAllSelf() {
		assertTrue("ContainsAll should return true for the same list", twoElementList.containsAll(twoElementList));
		assertTrue("ContainsAll should return true if the argument is an empty list", twoElementList.containsAll(emptyList));
	}	
	
	@Test
	public void testIndexOfValid() {
		assertEquals("IndexOf returns the wrong value", twoElementList.indexOf("Two"), 1);
	}
	
	@Test
	public void testIndexOfInvalid() {
		assertEquals("IndexOf should return -1 if the value is not found", twoElementList.indexOf("Wrong"), -1);
	}
	
	@Test
	public void testRemoveAllSelf() {
		assertTrue("removeAll should return true if list was changed", twoElementList.removeAll(twoElementList));
		assertTrue("List should be empty after removing all of self", twoElementList.head.link == twoElementList.tail);
		assertEquals("List size should be updated correctly", twoElementList.size(), 0);
	}
	
	@Test
	public void testRemoveAllEmpty() {
		assertFalse("removeAll should return false if list wasn't changed", twoElementList.removeAll(emptyList));
	}
	
	@Test
	public void testToArray() {
		assertArrayEquals("ToArray incorrect", twoElementList.toArray(), new String[]{"One", "Two"});
	}
	
	@Test
	public void testIteratorHasNextValid() {
		assertTrue("List iterator on two element list should have a next", listIterator.hasNext());
	}
	
	@Test
	public void testIteratorNext() {
		try {
		assertEquals("Next value incorrect", listIterator.next(), "One");
		assertEquals("Next value incorrect", listIterator.next(), "Two");
		}
		catch (NoSuchElementException e) {
			fail("List should have another element");
		}
	}
	
	@Test
	public void testIteratorHasPrevious() {
		assertTrue("Iterator at end should have a previous element", listIteratorAtEnd.hasPrevious());
	}
	
	@Test
	public void testIteratorPrevious() {
		try {
		assertEquals("Next value incorrect", listIteratorAtEnd.previous(), "Two");
		assertEquals("Next value incorrect", listIteratorAtEnd.previous(), "One");
		}
		catch (NoSuchElementException e) {
			fail("List should have another element");
		}
	}
	
	@Test
	public void testIteratorNextIndex() {
		listIterator.next();
		assertEquals("nextIndex incorrect", listIterator.nextIndex(), 1);
	}
	
	@Test
	public void testIteratorPreviousIndex() {
		assertEquals("previousIndex incorrect", listIteratorAtEnd.previousIndex(), 1);
	}
	
	@Test
	public void testIteratorAdd() {
		listIterator.next();
		listIterator.add("Middle");
		
		assertTrue("Element isn't added to the right place", twoElementList.head.link.data.equals("One") 
				&& twoElementList.head.link.link.data.equals("Middle")
				&& twoElementList.head.link.link.link.data.equals("Two"));
		assertTrue("Size isn't updated correctly", twoElementList.size() == 3);
	}
	
	@Test
	public void testIteratorRemoveNextValid() {
		listIterator.next();
		listIterator.remove();
		
		assertTrue("Remove should have removed the element it passed over last", twoElementList.head.link.data.equals("Two"));
		assertTrue("Size isn't updatd correctly", twoElementList.size() == 1);
	}
	
	@Test
	public void testIteratorRemovePreviousValid() {
		listIteratorAtEnd.previous();
		listIteratorAtEnd.previous();
		listIteratorAtEnd.remove();
		
		assertTrue("Remove should have removed the element it passed over last", twoElementList.head.link.data.equals("Two"));
		assertTrue("Size isn't updatd correctly", twoElementList.size() == 1);
	}
	
	@Test(expected = IllegalStateException.class)
	public void testIteratorInvalidRemove() {
		listIterator.remove();
	}
	
	@Test
	public void testIteratorSetValid() {
		listIterator.next();
		listIterator.set("NotOne");
		assertTrue("Set should update the value that was last passed over", twoElementList.head.link.data.equals("NotOne"));
	}
	
	@Test(expected = IllegalStateException.class)
	public void testIteratorSetInvalid() {
		listIterator.set("Bad");
	}
	
	@Test
	public void testRetainAll() {
		Collection<String> tempCollection = new LinkedList<String>();
		tempCollection.add("Two");
		twoElementList.retainAll(tempCollection);
		
		assertEquals("One should be removed", twoElementList.head.link.data, "Two");
		assertTrue("There should only be one element left in the list", twoElementList.head.link.link == twoElementList.tail);
		assertEquals("List size incorrect", twoElementList.size(), 1);
	}
	
}
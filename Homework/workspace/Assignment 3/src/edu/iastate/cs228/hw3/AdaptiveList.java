package edu.iastate.cs228.hw3;
/*
 *  @author nfoxg
 *
 *  An implementation of List<E> based on a doubly-linked list with an array for indexed reads/writes
 *
 */

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class AdaptiveList<E> implements List<E>
{
  protected class ListNode // private member of outer class
  {                     
    public E data;        // public members:
    public ListNode link; // used outside the inner class
    public ListNode prev; // used outside the inner class
    
    public ListNode(E item)
    {
      data = item;
      link = prev = null;
    }
  }
  
  public ListNode head;  // dummy node made public for testing.
  public ListNode tail;  // dummy node made public for testing.
  private int numItems;  // number of data items
  private boolean linkedUTD; // true if the linked list is up-to-date.

  public E[] theArray;  // the array for storing elements
  private boolean arrayUTD; // true if the array is up-to-date.

  public AdaptiveList()
  {
    clear();
  }

  @Override
  public void clear()
  {
    head = new ListNode(null);
    tail = new ListNode(null);
    head.link = tail;
    tail.prev = head;
    numItems = 0;
    linkedUTD = true;
    arrayUTD = false;
    theArray = null;
  }

  public boolean getlinkedUTD()
  {
    return linkedUTD;
  }

  public boolean getarrayUTD()
  {
    return arrayUTD;
  }
  
  /**
   * Constructs an AdaptiveList with a given list
   * 
   * @param c	List to be created into an AdaptiveList
   */
  public AdaptiveList(Collection<? extends E> c)
  {
    clear();
    addAll(c);
  }

  // Removes the node from the linked list.
  // This method should be used to remove a node from the linked list.
  private void unlink(ListNode toRemove)
  {
    if ( toRemove == head || toRemove == tail )
      throw new RuntimeException("An attempt to remove head or tail");
    toRemove.prev.link = toRemove.link;
    toRemove.link.prev = toRemove.prev;
  }

  // Inserts new node toAdd right after old node current.
  // This method should be used to add a node to the linked list.
  private void link(ListNode current, ListNode toAdd)
  {
    if ( current == tail )
      throw new RuntimeException("An attempt to link after tail");
    if ( toAdd == head || toAdd == tail )
      throw new RuntimeException("An attempt to add head/tail as a new node");
    toAdd.link = current.link;
    toAdd.link.prev = toAdd;
    toAdd.prev = current;
    current.link = toAdd;
  }
  
  private void updateArray() // makes theArray up-to-date.
  {
    if ( numItems < 0 )
      throw new RuntimeException("numItems is negative: " + numItems);
    if ( ! linkedUTD )
      throw new RuntimeException("linkedUTD is false");
    
    this.theArray = (E[]) new Object[this.numItems];
    if (this.numItems > 0) {
    	ListNode node = head.link;
    	for (int i = 0; i < this.theArray.length; i++) {
    		this.theArray[i] = node.data;
    		node = node.link;
    	}
    }
    this.arrayUTD = true;
  }

  private void updateLinked() // makes the linked list up-to-date.
  {
    if ( numItems < 0 )
      throw new RuntimeException("numItems is negative: " + numItems);
    if ( ! arrayUTD )
      throw new RuntimeException("arrayUTD is false");

    if ( theArray == null || theArray.length < numItems )
      throw new RuntimeException("theArray is null or shorter");

    head = new ListNode(null);
    tail = new ListNode(null);
    head.link = tail;
    tail.prev = head;
    this.numItems = 0;
    this.linkedUTD = true;
    for (int i = 0; i < this.numItems; i++) {
    	add(this.theArray[i]);
    }  
  }
  
  /**
   * Returns the number of items in the list
   * 
   * @return	int number of items in list
   */
  @Override
  public int size()
  {
    return this.numItems; // may need to be revised.
  }
  
  /**
   * Checks if there are zero elements in the list
   * 
   * @return	boolean if list has zero elements
   */
  @Override
  public boolean isEmpty()
  {
    return numItems == 0; // may need to be revised.
  }
  
  /**
   * Adds a new node to the end of the linked list, sets the arrayUTD to false, and updates numItems
   * 
   * @param	obj	object to be added
   */
  @Override
  public boolean add(E obj)
  {
	if (!this.linkedUTD) {updateLinked();}  
    ListNode toAdd = new ListNode(obj);
    ListNode last = this.tail.prev;
    link(last, toAdd);
    this.numItems++;
    this.arrayUTD = false;
    return true; // may need to be revised.
  }
  
  /**
   * Adds all items from Collection 
   * 
   * @param c	Collection< ? extends E> of items to add
   */
  @Override
  public boolean addAll(Collection< ? extends E> c)
  {
    for (E i : c) {
    	add(i);
    }
    return c.size() > 0; // may need to be revised.
  } // addAll 1
  
  /**
   * Removes object from list
   * 
   * @param obj	object to be removed
   * @return	Whether the list contained to object to remove
   */
  @Override
  public boolean remove(Object obj)
  {
	if (!this.linkedUTD) {updateLinked();} 
    boolean inList = false;
    ListNode node = this.head.link;
    if (this.numItems > 0) {
	    for (int i = 0; i < this.numItems; i++) {
	    	if (node.data.equals(obj)) {
	    		inList = true;
	    		unlink(node);
	    		this.numItems--;
		    	this.arrayUTD = false;
	    		i = this.numItems;
	    	}
	    	node = node.link;
	    }
    }
    return inList; // may need to be revised.
  }

  private void checkIndex(int pos) // a helper method
  {
    if ( pos >= numItems || pos < 0 )
     throw new IndexOutOfBoundsException(
       "Index: " + pos + ", Size: " + numItems);
  }

  private void checkIndex2(int pos) // a helper method
  {
    if ( pos > numItems || pos < 0 )
     throw new IndexOutOfBoundsException(
       "Index: " + pos + ", Size: " + numItems);
  }

  private void checkNode(ListNode cur) // a helper method
  {
    if ( cur == null || cur == tail )
     throw new RuntimeException(
      "numItems: " + numItems + " is too large");
  }

  private ListNode findNode(int pos)   // a helper method
  {
    ListNode cur = head;
    for ( int i = 0; i < pos; i++ )
    {
      checkNode(cur);
      cur = cur.link;
    }
    checkNode(cur);
    return cur;
  }
  
  /**
   * Adds an element to the list at a specific index
   */
  @Override
  public void add(int pos, E obj)
  {
	if (!this.linkedUTD) {updateLinked();} 
	checkIndex2(pos);
	ListNode node = findNode(pos+1);
	ListNode toAdd = new ListNode(obj);
	link(node, toAdd);
	this.numItems++;
    this.arrayUTD = false;
  }
  
  /**
   * Adds all elements to the list starting at a given index
   */
  @Override
  public boolean addAll(int pos, Collection< ? extends E> c)
  {
	int i = 0;
    for (E e : c) {
    	add(pos+i, e);
    }
	  
    return c.size() > 0; // may need to be revised.
  } // addAll 2
  
  /**
   * Removes the element at a given index
   * 
   * @return The element removed at the given index
   */
  @Override
  public E remove(int pos)
  {
	if (!this.linkedUTD) {updateLinked();} 
    checkIndex2(pos);
    E removed = null;
    if (this.numItems > 0) {
    	ListNode node = findNode(pos+1);
    	removed = node.data;
    	unlink(node);
    	this.numItems--;
        this.arrayUTD = false;
    }    
    return removed; // may need to be revised.
  }
  
  /**
   * Returns the object at the given index in the list
   * 
   * @param	int pos	The index to return the object
   * @return	The object at the given index in the list
   */
  @Override
  public E get(int pos)
  {
	if (!this.arrayUTD) {updateArray();}
	checkIndex2(pos);
    return this.theArray[pos]; // may need to be revised.
  }
  
  /**
   * Replaces the object at the given position in the list with the one given
   * 
   * @param	int pos		The index to change the current object
   * @param E obj	The object to replace the current one with
   * @return	The object that was previously at the given index
   */
  @Override
  public E set(int pos, E obj)
  {
    E old = get(pos);
    this.theArray[pos] = obj;
    this.linkedUTD = false;
    return old; // may need to be revised.
  } 
  
  /**
   * Returns whether the current list contains the given object
   * 
   * @return	Whether the current list contains the given object
   */
  @Override
  public boolean contains(Object obj)
  {
	if (!this.linkedUTD) {updateLinked();}
    boolean contains = false;
    if (this.numItems > 0) {
	    ListNode node = head.link;
	    for (int i = 0; i < this.numItems; i++) {
	    	if (node.data.equals(obj)) {
	    		contains = true;
	    		i = this.numItems;
	    	}
	    	node = node.link;
	    }
    }
    return contains; // may need to be revised.
  }
  
  /**
   * Returns whether the current list contains all of the objects given in the collection
   * 
   * @param	Collection < ? > c	Collection to check if all objects are in the current list
   * @return	Whether the current list contains all of the objects given in the collection
   */
  @Override
  public boolean containsAll(Collection< ? > c)
  {
    boolean contains = true;
    for (Object i : c) {
    	if (!contains(i)) {
    		contains = false;
    	}
    }
	return contains; // may need to be revised.
  } // containsAll

  /**
   * Returns the first index of the object given
   * 
   * @param	Object obj	Object to search for in the current list
   * @return	Index of the first instance of the object given in the list, returns -1 if list does not contain the object given
   */
  @Override
  public int indexOf(Object obj)
  {
	  if (!this.linkedUTD) {updateLinked();}
	    if (this.numItems > 0) {
		    ListNode node = head.link;
		    for (int i = 0; i < this.numItems; i++) {
		    	if (node.data.equals(obj)) {
		    		return i;
		    	}
		    	node = node.link;
		    }
	    }
	    return -1; // may need to be revised.
  }
  
  /**
   * Returns the last index of the object given
   * 
   * @param	Object obj	Object to search for in the current list
   * @return	Index of the last instance of the object given in the list, returns -1 if list does not contain the object given
   */
  @Override
  public int lastIndexOf(Object obj)
  {
	  if (!this.linkedUTD) {updateLinked();}
	    if (this.numItems > 0) {
		    ListNode node = tail.prev;
		    for (int i = 0; i < this.numItems; i++) {
		    	if (node.data.equals(obj)) {
		    		return i;
		    	}
		    	node = node.prev;
		    }
	    }
	    return -1; // may need to be revised.
  }
  
  /**
   * Removes all the objects in the collection given from the current list
   * 
   * @param Collection<?> c	Collection to remove all of from the current list
   * @return	Whether the current list was changed 
   */
  @Override
  public boolean removeAll(Collection<?> c)
  {
	boolean changed = false;
    for (Object i : c) {
    	while (contains(i)) {
    		remove(i);
    		changed = true;
    	}
    }
    return changed; // may need to be revised.
  }
  
  /**
   * Removes all the objects not given in the collection
   * 
   * @param Collection<?> c	Collection of objects to retain in the current list
   * @return	Whether the current list was changed
   */
  @Override
  public boolean retainAll(Collection<?> c)
  {
	if (!this.linkedUTD) {updateLinked();}
	boolean changed = false;
    if (this.numItems > 0) {
    	ListNode node = head.link;
    	for (int i = 0; i < this.numItems; i++) {
    		if (!c.contains(node.data)) {
    			unlink(node);
    			this.numItems--;
		    	this.arrayUTD = false;
		    	changed = true;
    		}
    		node = node.link;
    	}
    }
    return changed; // may need to be revised.
  }
  
  /**
   * Returns an array of the objects in the current list
   * 
   * @return	array of Objects in the current list
   */
  @Override
  public Object[] toArray()
  {
	if (!this.linkedUTD) {updateLinked();}
	Object[] array = new Object[this.numItems];
    if (this.numItems > 0 ) {
    	ListNode node = head.link;
    	for (int i = 0; i < array.length; i++) {
    		array[i] = node.data;
    		node = node.link;
    	}
    }
    return array; // may need to be revised.
  }
  
  /**
   * Returns an array of all the objects in the current list as the type given
   * 
   * @param T[] arr	Array with desired type for array to be returned as
   * @return	Array with desired type
   */
  @Override
  public <T> T[] toArray(T[] arr)
  {
	if (!this.linkedUTD) {updateLinked();}
    if (arr.length < this.numItems) {
    	arr = Arrays.copyOf(arr, this.numItems);
    }
    if (this.numItems > 0) {
    	ListNode node = head.link;
    	for (int i = 0; i < this.numItems; i++) {
    		arr[i] = (T) node.data;
    		node = node.link;
    	}
    	if (arr.length > this.numItems) {
    		arr[this.numItems] = null;
    	}
    }
    return arr; // may need to be revised.
  }

  @Override
  public List<E> subList(int fromPos, int toPos)
  {
    throw new UnsupportedOperationException();
  }

  private class AdaptiveListIterator implements ListIterator<E>
  {
    private int    index;  // index of next node;
    private ListNode cur;  // node at index - 1
    private ListNode last; // node last visited by next() or previous()
    
    /**
     * Default constructor for AdaptiveListIterator
     */
    public AdaptiveListIterator()
    {
      if ( ! linkedUTD ) updateLinked();
      this.index = 0;
      this.cur = head;
    }
      
    /**
     * Constructor for AdaptiveListIterator that takes in the position of the inital index
     */
    public AdaptiveListIterator(int pos)
    {
      if ( ! linkedUTD ) updateLinked();
      this.index = pos;
      this.cur = findNode(pos);
    }
    
    /**
     * Checks if the current node is linked to another normal node
     * 
     * @return	Whether the current node is linked to another normal node
     */
    @Override
    public boolean hasNext()
    {
      return !(this.cur.link == tail); // may need to be revised.
    }
    
    /**
     * Increases the cursor by one and returns the object passed over
     * 
     * @return	The object passed by the index cursor
     */
    @Override
    public E next()
    {
      if (hasNext()) {
	      this.last = this.cur;
	      this.index++;
	      this.cur = this.cur.link;
      }
      else {throw new NoSuchElementException();}
      return this.last.data; // may need to be revised.
    } 
    
    /**
     * Checks if the current node has a previous normal node
     * 
     * @return	Whether the current node has a previous normal node
     */
    @Override
    public boolean hasPrevious()
    {
      return !(this.cur.prev == head); // may need to be revised.
    }
    
    /**
     * Decreases the cursor by one and returns the object passed over
     * 
     * @return	The object passed by the index cursor
     */
    @Override
    public E previous()
    {
      if (hasPrevious()) {
    	  this.last = this.cur;
    	  this.index--;
    	  this.cur = this.cur.prev;
      }
      else {throw new NoSuchElementException();}
      return this.last.data; // may need to be revised.
    }
    
    /**
     * Returns the index of the element that would be returned by a call to next()
     * 
     * @return	Index of element that would be called by next()
     */
    @Override
    public int nextIndex()
    {
      if (this.index >= numItems-1) {
    	  return numItems;
      }
      return this.index+1; // may need to be revised.
    }
    
    /**
     * Returns the index of the element that would be returned by a call to previous()
     * 
     * @return	Index of element that would be called by previous()
     */
    @Override
    public int previousIndex()
    {
      if (this.index <= 0) {
    	  return -1;
      }
      return this.index-1; // may need to be revised.
    }

    /**
     * Removes the last node passed over
     */
    public void remove()
    {
      if (this.last != null) {
    	  unlink(this.last);
    	  this.last = null;
      }
    }
    
    /**
     * Adds a new node to where the index is
     */
    public void add(E obj)
    { 
      link(this.cur, new ListNode(obj));
    } // add
    
    /**
     * Sets the current node to have the object data given
     */
    @Override
    public void set(E obj)
    {
      this.cur.data = obj;
    } // set
  } // AdaptiveListIterator
  
  @Override
  public boolean equals(Object obj)
  {
    if ( ! linkedUTD ) updateLinked();
    if ( (obj == null) || ! ( obj instanceof List<?> ) )
      return false;
    List<?> list = (List<?>) obj;
    if ( list.size() != numItems ) return false;
    Iterator<?> iter = list.iterator();
    for ( ListNode tmp = head.link; tmp != tail; tmp = tmp.link )
    {
      if ( ! iter.hasNext() ) return false;
      Object t = iter.next();
      if ( ! (t == tmp.data || t != null && t.equals(tmp.data) ) )
         return false;
    }
    if ( iter.hasNext() ) return false;
    return true;
  } // equals

  @Override
  public Iterator<E> iterator()
  {
    return new AdaptiveListIterator();
  }

  @Override
  public ListIterator<E> listIterator()
  {
    return new AdaptiveListIterator();
  }

  @Override
  public ListIterator<E> listIterator(int pos)
  {
    checkIndex2(pos);
    return new AdaptiveListIterator(pos);
  }

  // Adopted from the List<E> interface.
  @Override
  public int hashCode()
  {
    if ( ! linkedUTD ) updateLinked();
    int hashCode = 1;
    for ( E e : this )
       hashCode = 31 * hashCode + ( e == null ? 0 : e.hashCode() );
    return hashCode;
  }

  // You should use the toString*() methods to see if your code works as expected.
  @Override
  public String toString()
  {
   String eol = System.getProperty("line.separator");
   return toStringArray() + eol + toStringLinked();
  }

  public String toStringArray()
  {
    String eol = System.getProperty("line.separator");
    StringBuilder strb = new StringBuilder();
    strb.append("A sequence of items from the most recent array:" + eol );
    strb.append('[');
    if ( theArray != null )
      for ( int j = 0; j < theArray.length; )
      {
        if ( theArray[j] != null )
           strb.append( theArray[j].toString() );
        else
           strb.append("-");
        j++;
        if ( j < theArray.length )
           strb.append(", ");
      }
    strb.append(']');
    return strb.toString();
  }

  public String toStringLinked()
  {
    return toStringLinked(null);
  }

  // iter can be null.
  public String toStringLinked(ListIterator<E> iter)
  {
    int cnt = 0;
    int loc = iter == null? -1 : iter.nextIndex();

    String eol = System.getProperty("line.separator");
    StringBuilder strb = new StringBuilder();
    strb.append("A sequence of items from the most recent linked list:" + eol );
    strb.append('(');
    for ( ListNode cur = head.link; cur != tail; )
    {
      if ( cur.data != null )
      {
        if ( loc == cnt )
        {
          strb.append("| ");
          loc = -1;
        }
        strb.append(cur.data.toString());
        cnt++;

        if ( loc == numItems && cnt == numItems )
        {
          strb.append(" |");
          loc = -1;
        }
      }
      else
         strb.append("-");
      
      cur = cur.link;
      if ( cur != tail )
         strb.append(", ");
    }
    strb.append(')');
    return strb.toString();
  }
}

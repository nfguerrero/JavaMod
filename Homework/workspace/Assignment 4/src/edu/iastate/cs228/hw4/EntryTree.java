package edu.iastate.cs228.hw4;

import java.util.Arrays;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.File;

/**
 * @author NFoxG
 *
 * An entry tree class
 * Add Javadoc comments to each method
 */
public class EntryTree<K, V> {
	/**
	 * dummy root node made public for grading
	 */
	public Node root;
	
	/**
	 * prefixlen is the largest index such that the keys in the subarray keyarr
	 * from index 0 to index prefixlen - 1 are, respectively, with the nodes on
	 * the path from the root to a node. prefixlen is computed by a private
	 * method shared by both search() and prefix() to avoid duplication of code.
	 */
	protected int prefixlen;

	protected class Node implements EntryNode<K, V> {
		protected Node child; // link to the first child node
		protected Node parent; // link to the parent node
		protected Node prev; // link to the previous sibling
		protected Node next; // link to the next sibling
		protected K key; // the key for this node
		protected V value; // the value at this node

		public Node(K aKey, V aValue) {
			key = aKey;
			value = aValue;
			child = null;
			parent = null;
			prev = null;
			next = null;
		}

		@Override
		public EntryNode<K, V> parent() {
			return this.parent;
		}

		@Override
		public EntryNode<K, V> child() {
			return this.child;
		}

		@Override
		public EntryNode<K, V> next() {
			return this.next;
		}

		@Override
		public EntryNode<K, V> prev() {
			return this.prev;
		}

		@Override
		public K key() {
			return this.key;
		}

		@Override
		public V value() {
			return this.value;
		}
	}
	
	/**
	 * Helper class to group prefix and last node of prefix
	 * @author Nic Fox G
	 *
	 */
	protected class NodeData {
		protected K[] prefix; //prefix found with given key sequence
		protected Node lastNode; //last node of the given key sequence
		
		/**
		 * Constructor of class NodeData
		 * @param aPrefix	The prefix of a key sequence given
		 * @param aNode		The node of the last key in the prefix
		 */
		public NodeData(K[] aPrefix, Node aNode) {
			this.prefix = aPrefix;
			this.lastNode = aNode;
		}
		
		/**
		 * Accessor method to return the prefix
		 * @return	K[]		key sequence prefix to return
		 */
		public K[] prefix() {
			return this.prefix;
		}
		
		/**
		 * Accessor method to return the last node of the key in the prefix
		 * @return	Node	last node of the prefix key sequence to return
		 */
		public Node lastNode() {
			return this.lastNode;
		}
	}

	public EntryTree() {
		root = new Node(null, null);
	}

	/**
	 * Returns the value of the entry with a specified key sequence, or null if
	 * this tree contains no entry with the key sequence.
	 * 
	 * @param keyarr
	 * @return	Value of entry of given key sequence. null if tree does not contain key sequence.
	 */
	public V search(K[] keyarr) {
		// TODO
		if (checkNull(keyarr)) {
			return null;
		}
		NodeData data = new NodeData(keyarr, this.root);
		data = nodeData(data);
		if (Arrays.equals(data.prefix(), keyarr)) {
			return data.lastNode().value();
		}
		return null;
	}

	/**
	 * The method returns an array of type K[] with the longest prefix of the
	 * key sequence specified in keyarr such that the keys in the prefix label
	 * the nodes on the path from the root to a node. The length of the returned
	 * array is the length of the longest prefix.
	 * 
	 * @param keyarr
	 * @return
	 */
	public K[] prefix(K[] keyarr) {
		// TODO
		if (checkNull(keyarr)) {
			return null;
		}
		NodeData data = new NodeData(keyarr, this.root);
		data = nodeData(data);
		this.prefixlen = data.prefix().length;
		return data.prefix(); // TODO
		// Hint: An array of the same type as keyarr can be created with
		// Arrays.copyOf().

	}

	/**
	 * The method locates the node P corresponding to the longest prefix of the
	 * key sequence specified in keyarr such that the keys in the prefix label
	 * the nodes on the path from the root to the node. If the length of the
	 * prefix is equal to the length of keyarr, then the method places aValue at
	 * the node P and returns true. Otherwise, the method creates a new path of
	 * nodes (starting at a node S) labelled by the corresponding suffix for the
	 * prefix, connects the prefix path and suffix path together by making the
	 * node S a child of the node P, and returns true.
	 * 
	 * @param keyarr
	 * @param aValue
	 * @return
	 */
	public boolean add(K[] keyarr, V aValue) {
		// TODO
		if (aValue == null || checkNull(keyarr)) {
			return false;
		}
		NodeData data = new NodeData(keyarr, this.root);
		data = nodeData(data);
		Node node = data.lastNode();
		if (Arrays.equals(data.prefix(),keyarr)) {
			node.value = aValue;
			return true;
		}
		
		Node child = (Node) node.child();
		if (child != null) {
			while (child.next() != null) {
				child = (Node) child.next();
			}
			child.next = new Node(keyarr[data.prefix().length], null);
			child.next.prev = child;
			child = child.next;
			child.parent = node;
		}
		else {
			node.child = new Node(keyarr[data.prefix().length], null);
			child = node.child;
			child.parent = node;
		}
		for (int i = data.prefix().length+1; i < keyarr.length; i++) {
			child.child = new Node(keyarr[i], null);
			child.child.parent = child;
			child = child.child;
		}
		child.value = aValue;
		
		return true;
	}

	/**
	 * Removes the entry for a key sequence from this tree and returns its value
	 * if it is present. Otherwise, it makes no change to the tree and returns
	 * null.
	 * 
	 * @param keyarr
	 * @return
	 */
	public V remove(K[] keyarr) {
		// TODO
		if (checkNull(keyarr)) {
			return null;
		}
		NodeData data = new NodeData(keyarr, this.root);
		data = nodeData(data);
		if (!Arrays.equals(data.prefix(), keyarr)) {
			return null;
		}
		Node node = data.lastNode();
		V value = node.value();
		node.value = null;
		remove2(node);
		return value;
	}
	
	/**
	 * Helper method for remove
	 * @param keyarr	current key sequence to begin removing
	 */
	private void remove2(Node node) {
		if (node == this.root || node.value() != null || node.child() != null) {
			return;
		}
		if (node.prev() != null) {
			node.prev.next = node.next;
			if (node.next() != null) {
				node.next.prev = node.prev;
			}
			return;
		}
		node.parent.child = node.next;
		if (node.next != null) {
			node = node.next;
			node.prev = null;
		}
		node = node.parent;
		remove2(node);
	}

	/**
	 * The method prints the tree on the console in the output format shown in
	 * an example output file.
	 */
	public void showTree() {
		// TODO
		showTree(this.root, 0);
	}
	
	/**
	 * Helper method to find prefix and last node of prefix with a given key sequence
	 * 
	 * @param start		NodeData to start the search at
	 * @return	NodeData	object that can access the prefix and last node of prefix with the given key sequence. 
	 */
	private NodeData nodeData(NodeData start) {
		K[] keyarr = (K[]) new Object[0];
		Node lastNode = start.lastNode();
		
		Node child = (Node) start.lastNode().child();
		K[] prefix = start.prefix();
		if ((child != null) && (prefix.length != 0)) {
			while (true) {			
				if (child.key().equals(prefix[0])) {
					keyarr = Arrays.copyOf(keyarr, keyarr.length+1);
					keyarr[keyarr.length-1] = child.key();
					lastNode = child;
					if (prefix.length > 1) {
						K[] keyarr2 = Arrays.copyOfRange(start.prefix(), 1, prefix.length);
						
						NodeData nextData = nodeData(new NodeData(keyarr2, lastNode));
						int index = keyarr.length;
						keyarr = Arrays.copyOf(keyarr, keyarr.length + nextData.prefix().length);
						for (int i = 0; i < nextData.prefix().length; i++) {
							keyarr[i+index] = nextData.prefix()[i];
						}
						lastNode = nextData.lastNode();
					}
					break;
				}				
				if (child.next() == null) {
					break;
				}				
				child = (Node) child.next();
			}
		}		
		return new NodeData(keyarr, lastNode);
	}
	
	/**
	 * Helper method that returns whether the key sequence is valid. Throws a NullPointerException if any values are null
	 * @param keyarr	K[] to pass to check if valid
	 * @return	True if the key sequence is null or has a length of 0
	 */
	private boolean checkNull(K[] keyarr) {
		if (keyarr == null || keyarr.length == 0) {
			return true;
		}
		for (int i = 0; i < keyarr.length; i++) {
			if (keyarr[i] == null) {
				throw new NullPointerException();
			}
		}
		return false;
	}
	
	/**
	 * Helper method to recursively print the tree
	 * @param node	EntryNode<K, V> to start recursive print method
	 * @param indent	int of amount of indentations to include to correctly format printed tree
	 */
	private void showTree(EntryNode<K, V> node, int indent) {
		String tab = "";
		for (int i = 0; i < indent; i++) {
			tab += "\t";
		}
		System.out.println(tab + node.key() + "->" + node.value());
		
		if (node.child() != null) {
			showTree(node.child(), indent+1);
		}
		if (node.next() != null) {
			showTree(node.next(), indent);
		}
	}
}

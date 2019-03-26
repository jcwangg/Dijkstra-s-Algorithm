/*
 * CS345 Project 5: Dijkstra’s Algorithm
 * 
 * Author: Joyce Wang
 * 
 * This class represents the priority queue used for Dijkstra's Algorithm in the form 
 * of a minimum heap stored in an array.
 */

import java.util.HashMap;
import java.util.HashSet;

public class PriorityQueue {

	private Node[] arr;
	//number of nodes in queue
	private int length;
	//mapping of all node names to node objects. Allows us to access a Node with
	//the name of the node, without having to iterate through the entire heap.
	private HashMap<String,Node> nodes = new HashMap<String,Node>();
	
	/*
	 * Constructor for the min heap with an empty array and length set to zero.
	 */
	public PriorityQueue() {
		this.arr = new Node[4];
		this.length = 0;
	}
	
	/*
	 * Reorders the heap so that each the min is at the root and all nodes in any
	 * subtree are smaller than the root of the subtree. 
	 */
	public void buildMinHeap()
	{
		//tree has one element, so return
		if (getLastParentIndx() == -1) {
			return;
		}
		// start with the lowest parent in the tree, and bubble it
		// down. Do that again on each parent going up the tree.
		for (int i=getLastParentIndx(); i>=0; i--) {
			bubbleDown(i);
		}
	}

	/*
	 * Maps the name of a node to the Node object in the hashmap
	 */
	public void storeNode(Node node) {
		nodes.put(node.name, node);
	}
	
	/*
	 * Remove the min Node from the root. Swaps the last node with the root and 
	 * returns the root node.
	 */
	public Node removeMin()
	{
		if (length == 0)
			throw new IllegalArgumentException("ERROR: The heap is already empty");
		// save away the root value, which we will eventually return
		Node retval = arr[0];
		
		// swap the tail into the root position, unless the value
		// we're about to return is the only one in the heap.
		if (length > 1) {
			arr[0] = arr[length-1];
			arr[length-1] = retval;
		}
		//update the size of the heap
		length--;
		// we only bubble down if more than one value remaining
		if (length > 1) {
			bubbleDown(0);
		}
		//return the min Node
		return retval;
	}

	/*
	 * Returns the Node object corresponding to the name of the Node. 
	 */
	public Node findNode(String s) {
		return nodes.get(s);
	}
	
	/*
	 * Inserts a node into the heap, unless the node is null. 
	 */
	public void insert(Node node)
	{
		if (node == null) {
			throw new IllegalArgumentException("ERROR: The value cannot be null");
		}
		//if array full, double the size and copy all elements over
		if (length == arr.length) {
			Node[] dup = new Node[arr.length*2];
			for (int i=0; i<arr.length; i++)
				dup[i] = arr[i];
			arr = dup;
		}
		// add the new value to the end of the array
		arr[length] = node;
		length++;
		// bubble it into position
		bubbleUp(length-1);
	}
	
	/*
	 * This method bubbles up the element at the index into it's proper location
	 * to satisfy the min heap property. 
	 */
	private void bubbleUp(int index) {
		// loop until we've put this thing at the root or until we
		// realize that we don't need to move any further.
		int i = index;
		while (i > 0) {
			int parentIndx = getParentIndx(i);
			//found proper location, so break
			if (arr[i].dist >= arr[parentIndx].dist) {
				break;
			}
			swap(arr, i,parentIndx);
			i = parentIndx;
		}
	}
	
	/*
	 * This method bubbles down the element at index indx into its proper location
	 * to satisfy the min heap property
	 */
	private void bubbleDown(int indx) {
		// loop until we're at a leaf node in the heap; if we find
		// that we are *DONE* bubbling down (because the parent is
		// OK compared to its children), then we'll break out
		// manually.
		int i = indx;
		while (i <= getLastParentIndx()) {
			int  leftIndx = getLeftChildIndx(i);
			int rightIndx = leftIndx+1;
			// is the parent <= both of its children?  If so,
			// then we'll break out of this loop.
			Node cur   = arr[i];
			Node left  = arr[leftIndx];
			Node right = null;  // overridden later, if possible

			if (rightIndx < length) {
				right = arr[rightIndx];
			}
			// we *MUST* compare the cur node to its left child;
			// we only check the right side if it exists
			if (cur.dist <=  left.dist && (right == null || cur.dist <= right.dist)){
				break;
			}

			// if we get here, then the current node is >= one (or
			// both) of its children.  Which one is smaller, that
			// we should swap with?
			if (right == null || left.dist <= right.dist) {
				// we want to swap with the left side, either
				// because the right doesn't exist, or because
				// the left is <= the right
				swap(arr, i,leftIndx);
				i = leftIndx;
			}
			else {
				swap(arr, i,rightIndx);
				i = rightIndx;
			}
		}
		// if we get here, we've either swapped to a leaf, or else
		// we've found a place where it doesn't need to go any
		// further.
		return;
	}


	/*
	 * Returns the index of the last parent in the heap, -1 if there is only
	 * one element in the heap. 
	 */
	public int getLastParentIndx()
	{
		if (length==1) {
			return -1;
		}
		return getParentIndx(length-1);
	}

	/*
	 * Returns the index of the parent of a node at indx. 
	 */
	public static int getParentIndx(int indx) {
		if (indx <= 0) {
			throw new IllegalArgumentException("getParentIndx(): The index must be positive.");
		}
			return (indx-1)/2;
	}
	
	/*
	 * returns the index of the left child of a node at indx.
	 */
	public static int getLeftChildIndx(int indx) {
		return 2*indx+1;
	}
	
	/*
	 * returns the index of the right child of a node at indx.
	 */
	public static int getRightChildIndx(int indx) {
		return 2*indx+2;
	}
	
	/*
	 * swaps nodes at index a and b in the array.
	 */
	public void swap(Node[] arr, int a,int b)
	{
		Node tmp = arr[a];
		arr[a] = arr[b];
		arr[b] = tmp;
	}
	
	/*
	 * returns true if the heap is empty, false if not.
	 */
	public boolean isEmpty() {
		if (length == 0) {
			return true;
		}
		return false;
	}
	
	/*
	 * returns a set of all Node objects.
	 */
	public HashSet<Node> getNodes(){
		return new HashSet<Node> (nodes.values());
	}
}

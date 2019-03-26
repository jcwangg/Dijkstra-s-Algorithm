/*
 * CS345 Project 5: Dijkstra’s Algorithm
 * 
 * Author: Joyce Wang
 * 
 * This class stores an Edge object by using the weight of the edge and the two nodes it connects.
 * The boolean isUndir indicates whether the graph is undirected or directed.
 */

public class Edge {
	//Edge is an out-edge of Node from, and in-edge to Node to
	Node from;
	Node to;
	//found is true if edge is selected
	boolean found;
	int weight;
	//isUndir is true if the graph is undirected (two edge objects in each direction are stored per edge)
	boolean isUndir;
	
	/*
	 * The constructor for edge. Stores both nodes and the weight. isUndir is default to false.
	 */
	public Edge(Node from, Node to, int weight) {
		this.from = from;
		this.to = to;
		this.weight = weight;
		this.isUndir = false;
		this.found = false;
	}
	
	public void setFound(boolean found) {
		this.found = found;
	}
	/*
	 * If an undirected graph, set isUndir to true on one of the two edge objects. 
	 */
	public void setUndir() {
		this.isUndir = true;
	}
	
	/*
	 * return the "from" vertex. 
	 */
	public Node getFrom() {
		return this.from;
	}
	
	/*
	 * return the "to" vertex.
	 */
	public Node getTo() {
		return this.to; 
	}
	
	/*
	 * return the weight as an int. 
	 */
	public int getWeight() {
		return this.weight;
	}
}

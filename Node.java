/*
 * CS345 Project 5: Dijkstra’s Algorithm
 * 
 * Author: Joyce Wang
 * 
 * This class stores a vertex in the graph and its characteristics: name, shortest distance
 * to the vertex from the start vertex, the predecessor of that node, and a list of all edges
 * coming out of the vertex.
 */

import java.util.HashSet;

public class Node {
	String name;
	//dist is updated with shortest distance as Dijkstra's Algorithm runs
	int dist;
	//stores predecessor of current node
	Node pred;
	//stores all edges going out from this node
	HashSet<Edge> edgeList = new HashSet<Edge>();
	
	/*
	 * The constructor for node. dist is defaulted to infinity and predecessor to null. 
	 */
	public Node(String name) {
		this.name = name;
		this.dist = 999999;
		this.pred = null;
	}
	
	/*
	 * returns current shortest distance found to start node.
	 */
	public int getDist() {
		return this.dist;
	}
	
	/*
	 * updates the dist value with a passed in int
	 */
	public void setDist(int dist) {
		this.dist = dist;
	}
}

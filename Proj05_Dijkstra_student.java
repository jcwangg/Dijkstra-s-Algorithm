/*
 * CS345 Project 5: Dijkstra’s Algorithm
 * 
 * Author: Joyce Wang
 * 
 * This class implements the Proj05_Dijkstra interface to run Dijkstra's Algorithm and output the results.
 * Stores nodes and edges in the graph and uses a priority queue to keep track of the minimum edge weights
 * for Dijkstra's Algorithm. 
 */

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;

public class Proj05_Dijkstra_student implements Proj05_Dijkstra {
	
	//true if is directed, false if undirected
	private boolean isDigraph; 
	private PriorityQueue heap;
	//set of all nodes in the graph
	private HashSet<Node> allNodes = new HashSet<Node>();
	private int debug; 
	//holds edges that are part of the final path, key is the name of the "to" node in the edge
	private HashMap<String,Edge> foundEdges = new HashMap<String,Edge>();
	
	/*
	 * Constructor for Proj05_Dijkstra_student. boolean di is true if input graph is digraph, false if not. 
	 * Initializes the priority queue. 
	 */
	public Proj05_Dijkstra_student(boolean di) {
		this.isDigraph = di;
		heap = new PriorityQueue();
		debug = 1;
	}
	
	/*
	 * This method adds a node from the input graph with the name, String s. The node is added to the heap. 
	 * @see Proj05_Dijkstra#addNode(java.lang.String)
	 */
	@Override
	public void addNode(String s) {
		Node node = new Node(s);
		heap.insert(node);
		heap.storeNode(node);
	}

	/*
	 * This method adds an edge from the input graph with the given weight and two Nodes already in the heap. 
	 * If directed, adds edge from one node to the other. If undirected, two edges are added in both directions,
	 * and setUndir() is called to indicate an undirected edge. 
	 * @see Proj05_Dijkstra#addEdge(java.lang.String, java.lang.String, int)
	 */
	@Override
	public void addEdge(String from, String to, int weight) {
		Node node1 = heap.findNode(from);
		Node node2 = heap.findNode(to);
		//add one edge to the from node's edge list. 
		if (isDigraph == true) {
			Edge edge = new Edge(node1,node2,weight);
			node1.edgeList.add(edge);
		}
		//undirected, so add one edge in each direction. 
		else {
			Edge edge = new Edge(node1,node2,weight);
			//if setUndir == true, skip the edge in the dot file
			edge.setUndir();
			node1.edgeList.add(edge);
			Edge edge2 = new Edge(node2,node1,weight);
			node2.edgeList.add(edge2);
		}
	}

	/*
	 * This method begins with the node with the passed in name and runs Dijkstra by removing the minimum
	 * node from the heap and updating the minimum distance of each node until a path is found for every node
	 * where a path exists.
	 * @see Proj05_Dijkstra#runDijkstra(java.lang.String)
	 */
	@Override
	public void runDijkstra(String startNodeName) {
		Node start = heap.findNode(startNodeName);
		//set starting node distance to 0
		start.setDist(0);
		//make the minimum heap 
		heap.buildMinHeap();
		//altPathDist stores the distance of a possible path 
		int altPathDist; 
		while (! heap.isEmpty()) {
			heap.buildMinHeap();
			//get the minimum node
			Node min = heap.removeMin();
			allNodes.add(min);
			//check all the outgoing edges and update those nodes if a shorter distance is found
			for (Edge e: min.edgeList) {
				Node to = e.getTo();
				int weight = e.getWeight();
				altPathDist = e.getFrom().getDist()+weight;					
				//if shorter path found from startV to adjV,
				//update adjV distance and predecessor, add found edge
				if (altPathDist < to.getDist()) {
					foundEdges.put(e.getTo().name, e);
					to.setDist(altPathDist);
					to.pred = e.getFrom();
				}
			}
		}
	}

	/*
	 * This method outputs the results of Dijkstra with the shortest path distance to each node and the path,
	 * or no path if no path exists to that node from the starting node. 
	 * @see Proj05_Dijkstra#printDijkstraResults(java.lang.String)
	 */
	@Override
	public void printDijkstraResults(String startNodeName) {
		for (Node n: allNodes) {
			String path = "";
			System.out.print(startNodeName+" -> "+ n.name+": ");
			//if distance still infinity, no path exists
			if (n.dist == 999999) {
				System.out.print("NO PATH");
			}
			else {
				System.out.print("best "+n.dist+": ");
				if (n.name.equals(startNodeName)) {
					System.out.print(startNodeName);
				}
				//get the path by getting predecessor of each node
				else {
					while (!(n.name.equals(startNodeName))) {
						path = n.name+" "+path;
						n = n.pred;
					}
				path = startNodeName+" "+path;
				System.out.print(path.trim());
				}
			}
			System.out.println();
		}
	}

	/*
	 * This method creates a new dot file and outputs the updated graph to a dot file. Edges that are part of the path
	 * are red. All edges have weights displayed. All nodes have node name and min distance, unless there is no path.
	 * @see Proj05_Dijkstra#writeSolutionDotFile()
	 */
	@Override
	public void writeSolutionDotFile() {
		if (isDigraph) {
			String filename = debug+"digraph.dot";
			debug++;
			PrintWriter out;
			try {
				out = new PrintWriter(new File(filename));
				out.printf("digraph {\n");
			} catch (IOException e) {
				e.printStackTrace();
				System.err.printf("ERROR: It was not possible to open the file '%s' for debugging!\n", filename);
				return;
			}
			for (Node n: allNodes) {
				if (n.dist == 999999) {
					out.print(n.name+";\n");
				}
				else{
					out.print(n.name+" [label=\""+n.name+"\\ndist: "+n.dist+"\"];\n");
				}
				for (Edge e: n.edgeList) {
					//found edges are labeled red
					if (foundEdges.containsValue(e)) {
						out.print(e.getFrom().name+" -> "+e.getTo().name
								+" [label="+e.getWeight()+",color=red];\n");
					}
					else {
						out.print(e.getFrom().name+" -> "+e.getTo().name
								+" [label="+e.getWeight()+"];\n");	
					}
				}	
			}
			out.printf("}\n");
			out.close();
		}
			else {
				String filename = debug+"graph.dot";
				debug++;
				PrintWriter out;
				try {
					out = new PrintWriter(new File(filename));
					out.printf("graph {\n");
				} catch (IOException e) {
					e.printStackTrace();
					System.err.printf("ERROR: It was not possible to open the file '%s' for debugging!\n", filename);
					return;
				}
				for (Node n: allNodes) {
					//no path found
					if (n.dist == 999999) {
						out.print(n.name+";\n");
					}
					else{
						out.print(n.name+" [label=\""+n.name+"\\ndist: "+n.dist+"\"];\n");
					}
					for (Edge e: n.edgeList) {
						//found edges are labeled red
						if (foundEdges.containsValue(e)) {
							out.print(e.getFrom().name+" -- "+e.getTo().name
								+" [label="+e.getWeight()+",color=red];\n");
						}
						//print other edges
						else if (!e.isUndir) {
							out.print(e.getFrom().name+" -- "+e.getTo().name
								+" [label="+e.getWeight()+"];\n");	
						}
					}
				}
			out.printf("}\n");
			out.close();
		}
	
	}
}

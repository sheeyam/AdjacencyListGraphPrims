/************************************************************************
* Programmer: Sheeyam Shellvacumar
* Date: April 13, 2018
* Environment: Eclipse Java IDE
* Purpose: 
* - Get No of Nodes and NodeLabels, No of Edges and it's labels with Source/Destination/Weights.
* - Implement a graph using Adjacency List Technique
* 		a) If you input a node label, it should output in- and out-degree for the node.
* 		b) If you input an edge label, it should output its source and destination edge.
* 		c) If you input a node label, it should output all incident edges for the node. 
* 		d) Implement Prim's Algorithm and find MST and it's cost.
* 
* Input: Nodes and Edges with Label and Weights
* Output: 
* - Adjacency List representation of a graph
* - In-Out Degrees of a Node, 
* - Incident Edges of a Node, 
* - Source and Destination of Edge
*
* Algorithms: 
* ----------------
* Prim's Algorithm
* ----------------
* As an initial input we have the graph (G) and a starting vertex (s).
* 1.  Make a queue (Q) with all the vertices of G (V);
* 2.  For each member of Q set the priority to INFINITY;
* 3.  Only for the starting vertex (s) set the priority to 0;
* 4.  The parent of (s) should be NULL;
* 5.  While Q isn’t empty
* 6.     Get the minimum from Q – let’s say (u); (priority queue);
* 7.     For each adjacent vertex to (v) to (u)
* 8.        If (v) is in Q and weight of (u, v) < priority of (v) then
* 9.           The parent of (v) is set to be (u)
* 10.          The priority of (v) is the weight of (u, v)
* 
* --------------------------------------------
* Adjacency List Data Structure Implementation
* --------------------------------------------
* A representation of a directed graph with n vertices using an array of n lists of vertices. 
* List i contains vertex j if there is an edge from vertex i to vertex j. 
* A weighted graph may be represented with a list of vertex/weight pairs. 
* An undirected graph may be represented by having vertex j in the list for vertex i 
* and vertex i in the list for vertex j.
* 
* For Example:-
*    1 2 3 4
* 1  1 1 1 1			1  -> 1 -> 2 -> 3 -> 4
* 2  1 0 0 0	-->		2  -> 1
* 3  0 1 0 1			3  -> 2 -> 4
* 4  0 1 1 0			4  -> 2 -> 3
***********************************************************************/
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

//Class - AdjacencyListGraphPrims
public class AdjacencyListGraphPrims {
	int MSTCost;
	int[] key;
	int[] added;
	static Node[] node;

	// Constructor
	public AdjacencyListGraphPrims(int v) {
		MSTCost = 0;
		key = new int[v];
		for (int i = 0; i < v; i++)
			key[i] = Integer.MAX_VALUE;
		added = new int[v];
	}

	// A user define class to represent a Vertex
	// This class has the properties of the node
	static class Node {
		LinkedList<Node> adj;
		int data;
		Node parent;

		public Node(int data) {
			this.data = data;
			parent = null;
			adj = new LinkedList<>();
		}
	}

	// A user define class to represent a Graph.
	// A Graph is an array of adjacency lists.
	// Size of array will be V (number of nodes in graph)
	static class Graph {
		int Nodes;// no of vertices
		LinkedList<Integer> adjListArray[];
		LinkedList<Integer> incidentArray[];
		LinkedList<Integer> inDegreeArray[];
		LinkedList<Integer> outDegreeArray[];

		// Constructor of the class
		@SuppressWarnings("unchecked")
		Graph(int Nodes) {
			this.Nodes = Nodes;

			// Define Set of LinkedLists for adjacent list, incident edge and in-out degrees
			// Define the size of LinkedLists as number of vertices
			node = new Node[Nodes];
			adjListArray = new LinkedList[Nodes];
			incidentArray = new LinkedList[Nodes];
			inDegreeArray = new LinkedList[Nodes];
			outDegreeArray = new LinkedList[Nodes];

			// Create a new linked list for each Node adjacent nodes, incident edges, in-out
			// edges degree can be stored
			for (int i = 0; i < Nodes; i++) {
				node[i] = new Node(i);
				adjListArray[i] = new LinkedList<>();
				incidentArray[i] = new LinkedList<>();
				inDegreeArray[i] = new LinkedList<>();
				outDegreeArray[i] = new LinkedList<>();
			}
		}
	}

	// This method will add incident edges according to graph type
	// Directed/UnDirected
	void addEdge(Graph graph, int src, int dest, int edgeLblId, int graphType) {
		if (graphType == 1) {
			node[src].adj.add(node[dest]);
			if (src != dest) { // otherwise 2 times added
				node[dest].adj.add(node[src]); // remove this line for directed graph
			}
			addUndirectedEdge(graph, src, dest);
		} else if (graphType == 2) {
			node[src].adj.add(node[dest]);
			addDirectedEdge(graph, src, dest);
		} else {
			// Do Nothing
		}
	}

	// This method will add incident edges to the graph
	void addIncidentEdge(Graph incident, int src, int dest, int edgeLblId) {
		// Add an edge from source to destination.
		incident.incidentArray[dest].addFirst(edgeLblId);
		incident.incidentArray[src].addFirst(edgeLblId);
	}

	// This method will add in and out edges to the graph
	void addInOutDegree(Graph inOutDegree, int src, int dest, int edgeLblId) {
		// Add an edge from source to destination.
		inOutDegree.inDegreeArray[dest].addFirst(edgeLblId);
		inOutDegree.outDegreeArray[src].addFirst(edgeLblId);
	}

	// This method will add an edge to an Undirected Graph
	void addUndirectedEdge(Graph graph, int src, int dest) {
		// In Undirected Graph the edge should be added both ways
		// Add an edge from source to destination to the adjacency list
		graph.adjListArray[src].addFirst(dest);

		// Add an edge from destination to source to the adjacency list
		graph.adjListArray[dest].addFirst(src);
	}

	// This method will add an edge to an Directed Graph
	void addDirectedEdge(Graph graph, int src, int dest) {
		// Add an edge from source to destination to the adjacency list
		graph.adjListArray[src].addFirst(dest);
	}

	// A utility function to print the adjacency list representation of graph
	void printGraph(String[] nodeLabelArray, Graph graph) {
		for (int i = 0; i < graph.Nodes; i++) {
			System.out.print("Adjacency list of Node " + nodeLabelArray[i] + ": ");
			for (Integer a : graph.adjListArray[i]) {
				System.out.print(nodeLabelArray[a] + " ");
			}
			System.out.println("\n");
		}
	}

	// A utility function to print the adjacency list representation of graph
	void printIncidentEdges(String[] nodeLabelArray, String[] edgeLabelArray, Graph incident, int nodeIndex) {
		System.out.println("Incident List of Node " + nodeLabelArray[nodeIndex] + ": ");
		for (Integer inc : incident.incidentArray[nodeIndex]) {
			System.out.print(edgeLabelArray[inc] + " ");
		}
		System.out.println("\n");
	}

	// A utility function to print the adjacency list representation of graph
	void printInOutDegrees(String[] nodeLabelArray, Graph inOutDegree, int nodeIndex) {
		int inDegree = 0, outDegree = 0;
		System.out.print("In-Degree of Node " + nodeLabelArray[nodeIndex] + ": ");
		for (Integer indeg : inOutDegree.inDegreeArray[nodeIndex]) {
			inDegree++;
		}
		System.out.println(inDegree);

		System.out.print("Out-Degree of Node " + nodeLabelArray[nodeIndex] + ": ");
		for (Integer outdeg : inOutDegree.outDegreeArray[nodeIndex]) {
			outDegree++;
		}
		System.out.println(outDegree + "\n");
	}

	// Method to find minimum edge node for Prims Algorithm
	int findMinimum(int nodes) {
		int min = Integer.MAX_VALUE;
		int index = -1;
		for (int i = 0; i < nodes; i++) {
			if (added[i] == 0 && key[i] < min) {
				min = key[i];
				index = i;
			}
		}
		return index;
	}

	// Prim's Algorithm and Logic for Finding MST and it's Cost
	void Prims(int[][] weight, int start, int nodes, String[] nodeLabelArray) {
		key[start] = 0;
		node[start].parent = null;
		int minNode = findMinimum(nodes);
		while (minNode != -1) {
			MSTCost += key[minNode];
			System.out.print("Node[" + nodeLabelArray[minNode] + "]");
			System.out.print(" ------ ");
			System.out.print("Weight[" + key[minNode] + "]");
			System.out.print(" ------ ");
			System.out.println("Cost[" + MSTCost + "]");
			for (Node ver : node[minNode].adj) {
				if (added[ver.data] == 0 && weight[minNode][ver.data] < key[ver.data]) {
					ver.parent = node[minNode];
					key[ver.data] = weight[minNode][ver.data];
				}
			}
			added[minNode] = 1;
			minNode = findMinimum(nodes);
		}
		System.out.println("\nMinimum Spanning Tree Cost: " + MSTCost);
	}

	// Main Method or Driver Program
	public static void main(String args[]) {
		int countN = 1;
		String source, dest;
		String edgeLabel, nodeLabel;
		int nodes, edges, weight;
		int graphType = 1;
		
		Scanner scanner = new Scanner(System.in);
		
		//Get Graph Type from User
		System.out.println("Select Graph Type : 1 - Undirected , 2 - Directed");
		graphType = scanner.nextInt();

		//Get Number of Nodes from User
		System.out.println("Enter number of Nodes: ");
		nodes = scanner.nextInt();
		String nodeLabelArray[] = new String[nodes];

		//Get Node Labels from User
		System.out.println("Enter the Node Labels :");
		while (countN <= nodes) {
			nodeLabel = scanner.next();
			nodeLabelArray[countN - 1] = nodeLabel;
			countN++;
		}

		//Get Number of Edges from User
		System.out.println("Enter number of Edges: ");
		edges = scanner.nextInt();
		String edgeLabelArray[] = new String[edges];

		HashMap<String, String> hm = new HashMap<String, String>();
		Graph graph = new Graph(nodes);

		//Get Edge Label, Nodes and Weights from User
		System.out.println("Enter the Edges in Graph Format : <Edge_Label> <Source_Node> <Destination_Node> <Weight>");
		AdjacencyListGraphPrims alg = new AdjacencyListGraphPrims(nodes);
		int[][] weightArray = new int[nodes][nodes];
		for (int i = 0; i < edges; i++) {
			edgeLabel = scanner.next();
			source = scanner.next();
			dest = scanner.next();
			weight = scanner.nextInt();
			edgeLabelArray[i] = edgeLabel;
			hm.put(edgeLabel, source + "->" + dest);

			// Get Indexes of Source and Destination
			int indexOfSource = Arrays.asList(nodeLabelArray).indexOf(source);
			int indexOfDest = Arrays.asList(nodeLabelArray).indexOf(dest);

			weightArray[indexOfSource][indexOfDest] = weight;
			weightArray[indexOfDest][indexOfSource] = weight;
			alg.addEdge(graph, indexOfSource, indexOfDest, i, graphType);
			alg.addInOutDegree(graph, indexOfSource, indexOfDest, i);
			alg.addIncidentEdge(graph, indexOfSource, indexOfDest, i);
		}

		System.out.println("\n");
		System.out.println("*** Graph representated in an Adjacency List ***");

		// Print the adjacency list representation of the above graph
		alg.printGraph(nodeLabelArray, graph);

		System.out.println("(a) Enter the Node Label to output in- and out-degree for the node:");
		nodeLabel = scanner.next();

		// Call function to Print In-Out Degrees of a Node
		alg.printInOutDegrees(nodeLabelArray, graph, Arrays.asList(nodeLabelArray).indexOf(nodeLabel));

		// Print Source and Destination of a Edge when EdgeLabel is given.
		System.out.println("(b) Enter the Edge Label to output its source and destination nodes:");
		edgeLabel = scanner.next();
		System.out.println("Edge " + edgeLabel + " : " + "Source --> Destination : " + hm.get(edgeLabel) + "\n");

		System.out.println("(c) Enter the Node Label to output all incident edges for the node:");
		nodeLabel = scanner.next();

		// Function to Print Incident Edges of a Node
		alg.printIncidentEdges(nodeLabelArray, edgeLabelArray, graph, Arrays.asList(nodeLabelArray).indexOf(nodeLabel));

		System.out.println("(d) Executing Prims Implementation for this Graph...");
		
		// Call Prims Function and Print Minimum Spanning Graph and it's Cost
		int start = 0;
		alg.Prims(weightArray, start, nodes, nodeLabelArray);
		scanner.close();
	}
}

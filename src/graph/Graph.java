package graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang.ArrayUtils;

public class Graph{

	public final int n;
	private int[][] graph;
	private final Node[] nodes;

	/**
	 * Basic graph constructor.  No edges will exist.
	 * @param size
	 */
	public Graph(int size){
		n = size;
		graph = new int[n][n];
		nodes = new Node[n];
		for (int i = 0; i < n; i++)
			nodes[i] = new Node(i, this);
	}

	/**
	 * Copy constructor
	 * @param toClone
	 */
	public Graph(Graph toClone){
		this(toClone.n);
		//copy edge data
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				graph[i][j] = toClone.graph[i][j];
	}

	public Graph(int[][] graph) {
		this.graph = graph;
		this.n = graph.length;
		this.nodes = new Node[n];
		
		for (int i=0; i<n; i++) {
			nodes[i] = new Node(i, this);
		}
	}

	/**
	 * @param g1
	 * @param g2
	 * @return Graph that contains an edge E iff the edge E exists in both g1 and in g2
	 */
	public static Graph intersect(Graph g1, Graph g2){
		if (g1.n != g2.n)
			throw new RuntimeException("Graphs must have the same number of nodes!");

		Graph ret = new Graph(g1.n);
		for (int i = 0; i < ret.n; i++)
			for (int j = 0; j < ret.n; j++)
				ret.graph[i][j] = g1.graph[i][j] == 1 && g2.graph[i][j] == 1 ? 1 : 0;
		return ret;
	}

	/**
	 * generate a random graph, with the given probability than any given edge exists
	 * @param probablilty
	 */
	public Graph randomize(double probability){
		//generate random links
		for (int i = 0; i < n; i++)
			for (int j = 0; j < i; j++)
				if (Math.random() < probability)
					addEdge(i, j);
				else
					removeEdge(i, j);
		return this;
	}

	/**
	 * randomly swap the nodes of this graph
	 */
	public Graph randomSwap(){
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				if (Math.random() < 0.5)
					swapNodes((int) (n * Math.random()), (int) (n * Math.random()));
		return this;
	}

	/**
	 * set this graph so that any edge exists iff the edge did not exist before
	 */
	public Graph invert(){
		for (int i = 0; i < n; i++)
			for (int j = 0; j < i; j++)
				if (isEdge(i, j))
					removeEdge(i, j);
				else
					addEdge(i, j);
		return this;
	}
	
	public int numEdges() {
		int count = 0;
		
		for(int i=0; i<n; i++) {
			for(int j=0; j<i+1; j++) {
				if(graph[i][j] == 1) {
					count++;
				}
			}
		}
		
		return count;
	}

	/**
	 * add an edge to the graph
	 * @param a
	 * @param b
	 * @return true if the graph was modified by this call
	 */
	public boolean addEdge(int a, int b){
		boolean ret = !isEdge(a, b);
		graph[a][b] = graph[b][a] = 1;
		return ret;
	}

	/**
	 * add an edge to the graph
	 * @param a
	 * @param b
	 * @return true if the graph was modified by this call
	 */
	public boolean addEdge(Node a, Node b){
		return addEdge(a.id, b.id);
	}

	/**
	 * remove an edge from the graph
	 * @param a
	 * @param b
	 * @return true if the graph was modified by this call
	 */
	public boolean removeEdge(int a, int b){
		boolean ret = isEdge(a, b);
		graph[a][b] = graph[b][a] = 0;
		return ret;
	}

	/**
	 * remove an edge from the graph
	 * @param a
	 * @param b
	 * @return true if the graph was modified by this call
	 */
	public boolean removeEdge(Node a, Node b){
		return removeEdge(a.id, b.id);
	}

	/**
	 * @param a
	 * @param b
	 * @return true if there is an edge between a and b, false otherwise
	 */
	public boolean isEdge(int a, int b){
		return graph[a][b] == 1;
	}

	/**
	 * @param a
	 * @param b
	 * @return true if there is an edge between a and b, false otherwise
	 */
	public boolean isEdge(Node a, Node b){
		return isEdge(a.id, b.id);
	}

	/**
	 * @return set that contains all nodes in the graph
	 */
	public SortedSet<Node> allNodes(){
		SortedSet<Node> ret = new TreeSet<Node>();
		//iterate through all nodes
		for (int i = 0; i < n; i++)
			//add every node
			ret.add(nodes[i]);
		return ret;
	}

	/**
	 * @param node
	 * @return set of all nodes that are adjacent to the specified node
	 */
	public SortedSet<Node> adjacentNodes(int node){
		SortedSet<Node> ret = new TreeSet<Node>();
		//iterate through all nodes
		for (int i = 0; i < n; i++)
			//add only adjacent nodes
			if (isEdge(node, i))
				ret.add(nodes[i]);
		return ret;
	}

	public SortedSet<Node> adjacentNodes(Node node){
		return adjacentNodes(node.id);
	}

	/**
	 * @param nodeSet a set of nodes
	 * @return all nodes which are adjacent to this set NOT INCLUDING any nodes that are in the input set
	 */
	public SortedSet<Node> adjacentNodes(Set<Node> nodeSet){
		SortedSet<Node> ret = new TreeSet<Node>();
		for (Node node : nodeSet)
			ret.addAll(adjacentNodes(node));
		ret.removeAll(nodeSet);
		return ret;
	}

	/**
	 * @param node
	 * @return set of all nodes that are NOT adjacent to the specified node
	 */
	public SortedSet<Node> nonAdjacentNodes(int node){
		SortedSet<Node> ret = new TreeSet<Node>();
		//iterate through all nodes
		for (int i = 0; i < n; i++)
			//add only non-adjacent nodes
			if (!isEdge(node, node))
				ret.add(nodes[i]);
		return ret;
	}

	/**
	 * @param nodeSet1 set of nodes
	 * @param nodeSet2 set of nodes
	 * @return number of edges which go between the sets
	 */
	public int numEdgesBetween(Set<Node> nodeSet1, Set<Node> nodeSet2){
		int num = 0;
		for (Node node1 : nodeSet1)
			for (Node node2 : nodeSet2)
				if (isEdge(node1, node2))
					num++;
		return num;
	}

	/**
	 * @param nodeSet set of nodes
	 * @return number of edges which go between the nodes
	 */
	public int numEdgesBetween(Set<Node> nodeSet){
		Set<Node> linked = new TreeSet<Node>();
		for (Node node : nodeSet)
			linked.addAll(adjacentNodes(node));
		linked.retainAll(nodeSet);
		return linked.size() / 2;
	}
	
	/**
	 * Map nodes from the current graph to the positions specified in 'permutation'.
	 * The result is an isomorphic graph.
	 * 
	 * @param permutation
	 */
	public Graph permute(int[] permutation) {
		if(permutation.length != n) {
			throw new IllegalArgumentException("The number of permutations does not match the number of nodes.");
		}
		
		int[][] permGraph = new int[n][n];
		
		for(int i=0; i<n; i++) {
			for(int j=0; j<i+1; j++) {
				if(graph[i][j] == 1) {
					int pi = permutation[i];
					int pj = permutation[j];
					
					permGraph[pi][pj] = 1;
					permGraph[pj][pi] = 1;
				}
			}
		}
		
		return new Graph(permGraph);
	}
	
	public Graph randomlyPermute() {
		ArrayList<Integer> permutation = new ArrayList<Integer>(n);
		for(int i=0; i<n; i++) {
			permutation.add(i);
		}
		
		Collections.shuffle(permutation);
		
		int[] perm_arr = new int[n];
		for(int i=0; i<n; i++) {
			perm_arr[i] = permutation.get(i);
		}
		
		return permute(perm_arr);
	}

	/**
	 * Swap the specified nodes.
	 * A link ar will exist iff br existed before, and
	 * a link br will exist iff ar existed before.
	 * @param a node in the graph
	 * @param b node in the graph
	 */
	public void swapNodes(int a, int b){
		for (int i = 0; i < n; i++)
			if (i != a && i != b) {
				int tmp = graph[i][a];
				graph[i][a] = graph[a][i] = graph[i][b];
				graph[i][b] = graph[b][i] = tmp;
			}
	}

	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < n; i++){
			sb.append(i == 0 ? '/' : i == n - 1 ? '\\' : '|');
			for (int j = 0; j < n; j++)
				sb.append(graph[i][j]).append(j < n - 1 ? "," : "");
			sb.append(i == 0 ? '\\' : i == n - 1 ? '/' : '|');
			if (i < n - 1)
				sb.append('\n');
		}
		return sb.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Graph)) {
			return false;
		}
		
		if(obj == this) {
			return true;
		}
		
		Graph other = (Graph) obj;
		
		if(this.n != other.n) {
			return false;
		}
		
		for(int i=0; i<n; i++) {
			for(int j=0; j<n; j++) {
				if(this.graph[i][j] != other.graph[i][j]) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	public static void main(String[] args) {
		// Testing
		
		// Permutations

		Graph g = new Graph(5);
		g.addEdge(0, 1);
		g.addEdge(2, 4);
		System.out.println(g);
		
		assert (g.numEdges() == 2);
		
		assert g.isEdge(0, 1);
		assert g.isEdge(1, 0);
		assert g.isEdge(2, 4);
		assert g.isEdge(4, 2);
		
		int[] permutation = {2,3,4,1,0};
		g = g.permute(permutation);
		System.out.println(g);
		
		assert g.isEdge(2, 3);
		assert g.isEdge(3, 2);
		assert g.isEdge(4, 0);
		assert g.isEdge(0, 4);
	}
}

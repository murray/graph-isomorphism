package graph;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

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
		Set<Node> linked = new TreeSet<Node>();
		for (Node node : nodeSet1)
			linked.addAll(adjacentNodes(node));
		linked.retainAll(nodeSet2);
		return linked.size();
	}

	/**
	 * Swap the specified nodes.
	 * A link ar will exist iff br existed before, and
	 * a link br will exist iff ar existed before.
	 * @param a node in the graph
	 * @param b node in the graph
	 */
	public void swapNodes(int a, int b){
		for (int i = 0; i < n; i++){
			int tmp = graph[a][i];
			graph[i][a] = graph[a][i] = graph[b][i];
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
}

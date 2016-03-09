import java.util.SortedSet;
import java.util.TreeSet;

public class Graph{
	
	/**
	 * never used github before
	 * just testing out with a comment
	 */

	public final int n;
	private int[][] graph;

	Graph(int size){
		n = size;
		graph = new int[n][n];
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
	 * @param a
	 * @param b
	 * @return true if there is an edge between a and b, false otherwise
	 */
	public boolean isEdge(int a, int b){
		return graph[a][b] == 1;
	}

	/**
	 * @param node
	 * @return set of all nodes that are adjacent to the specified node
	 */
	public SortedSet<Integer> adjacentNodes(int node){
		SortedSet<Integer> ret = new TreeSet<Integer>();
		//iterate through all nodes
		for (int i = 0; i < n; i++)
			//add only adjacent nodes
			if (isEdge(node, node))
				ret.add(i);
		return ret;
	}

	/**
	 * @param node
	 * @return set of all nodes that are NOT adjacent to the specified node
	 */
	public SortedSet<Integer> nonAdjacentNodes(int node){
		SortedSet<Integer> ret = new TreeSet<Integer>();
		//iterate through all nodes
		for (int i = 0; i < n; i++)
			//add only non-adjacent nodes
			if (!isEdge(node, node))
				ret.add(i);
		return ret;
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

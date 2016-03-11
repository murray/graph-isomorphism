package graph;
import java.util.SortedSet;

public class Node implements Comparable<Node>{
	final int id;
	final Graph graph;

	public Node(int id, Graph graph){
		this.id = id;
		this.graph = graph;
	}

	public SortedSet<Node> adjacentNodes(){
		return graph.adjacentNodes(id);
	}

	@Override
	public int compareTo(Node other){
		return Integer.compare(id, other.id);
	}

}

import java.util.Set;
import java.util.TreeSet;

import graph.Graph;
import graph.Node;
import graph.StaticGraph;

public class KentsAlgorithm{
	
	public static void main(String[] args){
		Graph g = new Graph(4);
		g.addEdge(0, 1);
		g.addEdge(1, 2);
		g.addEdge(2, 3);
		g = new StaticGraph(g);
		System.out.println(g);
		generateDescriptorStrings(g);
	}
	
	static void generateDescriptorStrings(Graph graph){
		for (Node node: graph.allNodes()){
			System.out.println("test");
			Set<Node> nodes = new TreeSet<Node>();
			
			
			String desc = "";
			nodes.add(node);
			Set<Node> adjacentNodes = node.adjacentNodes();
			System.out.println(adjacentNodes);
			
			while(!adjacentNodes.isEmpty()){
				desc+="{n:"+adjacentNodes.size();
				desc+=",to:"+graph.numEdgesBetween(nodes, adjacentNodes);
				desc+=",bet:"+graph.numEdgesBetween(adjacentNodes, adjacentNodes)+"} ";
				
				nodes.addAll(adjacentNodes);
				adjacentNodes = graph.adjacentNodes(nodes);
			}
			System.out.println(desc);
			
		}
	}
	
}

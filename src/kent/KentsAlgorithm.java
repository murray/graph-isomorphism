package kent;

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
		g.addEdge(0, 2);

		System.out.println(g);

		generateDescriptorStrings(new StaticGraph(g));
	}

	static void generateDescriptorStrings(StaticGraph graph){
		DescriptorMap descMap = new DescriptorMap(graph);
		
		for (int layer = 0; layer < graph.n; layer++){
			System.out.println("----------");
			DescriptorMap nextMap = new DescriptorMap();
			for (Node node : graph.allNodes()){
				System.out.println("-----");
				Set<Node> completedSet = new TreeSet<Node>();

				String desc = "";
				completedSet.add(node);
				Set<Node> adjacentNodes = node.adjacentNodes();

				while (!adjacentNodes.isEmpty()){
					desc += "{" + descMap.getMapString(adjacentNodes) + "}";

					completedSet.addAll(adjacentNodes);
					adjacentNodes = graph.adjacentNodes(completedSet);
					if (!adjacentNodes.isEmpty()) {
						desc += "-";
					}
				}
				System.out.println(desc);
				nextMap.add(node, desc);
				System.out.println(nextMap.toString());
			}
			System.out.println(nextMap.toString());
			descMap = nextMap;
		}
	}

}

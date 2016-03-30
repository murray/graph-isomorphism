package solvers.kent;

import java.util.Set;
import java.util.TreeSet;

import graph.Graph;
import graph.Node;
import graph.StaticGraph;
import solvers.GISolver;

/**
 * polynomial-time
 * @author Kent
 */
public class KentsAlgorithm implements GISolver{

	private static boolean draw = false;

	public static void main(String[] args){
		draw = true;
		Graph g = new Graph(5);
		g.addEdge(0, 1);
		g.addEdge(1, 2);
		g.addEdge(2, 3);
		g.addEdge(3, 4);
		g.addEdge(4, 0);
		g.addEdge(0, 2);
		g.addEdge(2, 4);

		//		draw = false;
		//		System.out.println(new KentsAlgorithm().isIsomorphism(new StaticGraph(g), new StaticGraph(g.randomSwap())));
		//		draw = true;

		Visualizer.startVisualizer(g);
		while (true){
			putInCircle(g.allNodes(), g);
			pause(300);

			//g.randomize(Math.random()/2);

			for (Node n : g.allNodes())
				Visualizer.setNodeColor(n, 0);

			System.out.println(g);
			generateDescriptorStrings(new StaticGraph(g));

			System.out.println("///////////////////////////////");
			System.out.println("//          done!!!          //");
			System.out.println("///////////////////////////////");
			putInCircle(g.allNodes(), g);
			pause(5000);
		}

	}

	@Override
	public boolean isIsomorphism(StaticGraph g1, StaticGraph g2){
		DescriptorMap descMap1 = generateDescriptorStrings(g1);
		DescriptorMap descMap2 = generateDescriptorStrings(g2);
		return descMap1.equals(descMap2);
	}

	static DescriptorMap generateDescriptorStrings(StaticGraph graph){
		Visualizer.SPEED = 1000;

		DescriptorMap descMap = new DescriptorMap(graph);
		int itterations = 0;

		int size = graph.n;
		while (size != descMap.numSets() && graph.n != descMap.numSets()){
			size = descMap.numSets();
			itterations++;
			//System.out.println("----------");
			DescriptorMap nextMap = new DescriptorMap();
			for (Node node : graph.allNodes()){
				Set<Node> completedSet = new TreeSet<Node>();

				String desc = "";
				completedSet.add(node);
				Set<Node> adjacentNodes = node.adjacentNodes();

				pause(Visualizer.SPEED);

				Visualizer.setNodePos(node, 400, 50);
				Visualizer.resetText();

				int layer = 0;
				while (!adjacentNodes.isEmpty()){
					layer++;
					
					String descConnectLast = descMap.getMapEdgeString(completedSet, adjacentNodes);
					
					String descPart = "";
					descPart += "{" + descMap.getMapNodeString(adjacentNodes) + "}";
					descPart += "[" + descMap.getMapEdgeString(adjacentNodes) + "]";

					Visualizer.setText(layer * 2 - 1, descConnectLast);
					Visualizer.setText(layer * 2, descPart);
					desc += "[" + descConnectLast + "]" + descPart;

					int ctr = 0;
					for (Node n : adjacentNodes){
						Visualizer.setNodePos(n, ctr++ * 30 + 400 - adjacentNodes.size() * 15 + 15, layer * 100 + 50);
					}

					completedSet.addAll(adjacentNodes);
					adjacentNodes = graph.adjacentNodes(completedSet);
					if (!adjacentNodes.isEmpty()) {
						desc += "-";
					}
				}
				Set<Node> disconnected = graph.allNodes();
				disconnected.removeAll(completedSet);
				putInCircle(disconnected, graph);
				nextMap.add(node, desc);
				System.out.println('"' + desc + "\" -> " + nextMap.getNodeSet(desc) + " (added " + node + ")");
			}
			pause(Visualizer.SPEED);
			Visualizer.SPEED = 1000;
			System.out.println("-----");
			descMap = nextMap;
			for (Node n : graph.allNodes())
				Visualizer.setNodeColor(n, descMap.getDescriptorIndex(n) / (float) descMap.numSets());
		}
		System.out.println("itterations: " + itterations);
		System.out.println(descMap);
		return descMap;
	}

	private static void putInCircle(Set<Node> nodes, Graph g){
		for (Node n : nodes){
			double angle = n.nodeId() * Math.PI * 2 / g.n;
			Visualizer.resetText();
			Visualizer.setNodePos(n, 120 + (int) (Math.cos(angle) * 100), 300 - (int) (Math.sin(angle) * 100));
		}
	}

	private static void pause(long time){
		if (draw)
			try{
				Thread.sleep(time);
			}catch (InterruptedException e){
			}
	}

}

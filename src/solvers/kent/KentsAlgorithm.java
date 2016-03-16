package solvers.kent;

import java.util.Set;
import java.util.TreeSet;

import graph.Graph;
import graph.Node;
import graph.StaticGraph;

/**
 * polynomial-time
 * @author Kent
 */
public class KentsAlgorithm{

	public static void main(String[] args){
		Graph g = new Graph(8);
		g.addEdge(0, 1);
		g.addEdge(1, 2);
		g.addEdge(2, 3);
		g.addEdge(3, 4);
		g.addEdge(4, 5);
		g.addEdge(5, 6);
		g.addEdge(6, 7);
		g.addEdge(7, 0);
		g.addEdge(1, 6);
		g.addEdge(2, 5);
		
		g.addEdge(0, 6);
		g.addEdge(6, 2);
		g.addEdge(2, 4);
		
		g.addEdge(7, 5);
		g.addEdge(5, 1);
		g.addEdge(1, 3);
		
		g.invert();
		
		
		

//		for (int i = 0; i < 6; i++){
//			g.addEdge(i, 6);
//			g.addEdge(i, 7);
//		}
//		
//		g.removeEdge(0, 6);
//		g.removeEdge(2, 7);

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
			pause(20000);
		}

	}

	static DescriptorMap generateDescriptorStrings(StaticGraph graph){
		Visualizer.SPEED=200;
		
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
				int layer = 0;
				while (!adjacentNodes.isEmpty()){
					layer++;
					desc += "{" + descMap.getMapString(adjacentNodes) + "}";
					desc+="["+graph.numEdgesBetween(adjacentNodes)+"]";

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
				System.out.println('"'+desc +"\" -> "+nextMap.getNodeSet(desc)+" (added "+node+")");
			}
			Visualizer.SPEED = 1000;
			System.out.println("-----");
			descMap = nextMap;
			for (Node n : graph.allNodes())
				Visualizer.setNodeColor(n, descMap.getDescriptorIndex(n)/(float)descMap.numSets());
		}
		System.out.println("itterations: " + itterations);
		System.out.println(descMap);
		return descMap;
	}

	private static void putInCircle(Set<Node> nodes, Graph g){
		for (Node n : nodes){
			double angle = n.nodeId() * Math.PI * 2 / g.n;
			Visualizer.setNodePos(n, 100 + (int) (Math.cos(angle) * 100), 300 - (int) (Math.sin(angle) * 100));
		}
	}

	private static void pause(long time){
		try{
			Thread.sleep(time);
		}catch (InterruptedException e){
		}
	}

}

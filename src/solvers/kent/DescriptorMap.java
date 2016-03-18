package solvers.kent;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import graph.Node;
import graph.StaticGraph;

public class DescriptorMap{
	private final Map<String, SortedSet<Node>> mapping = new TreeMap<String, SortedSet<Node>>();

	DescriptorMap(){
	}

	DescriptorMap(StaticGraph g){
		mapping.put("", g.allNodes());
	}

	public void add(Node node, String desc){
		SortedSet<Node> set = mapping.get(desc);
		if (set == null)
			mapping.put(desc, set = new TreeSet<Node>());
		set.add(node);
	}

	public String getDescriptor(Node n){
		for (Entry<String, SortedSet<Node>> entry : mapping.entrySet())
			if (entry.getValue().contains(n))
				return entry.getKey();
		throw new RuntimeException("Node " + n + " not found!");
	}

	public Set<Node> getNodeSet(String desc){
		return mapping.get(desc);
	}

	public int getDescriptorIndex(Node n){
		int ctr = 0;
		for (Entry<String, SortedSet<Node>> entry : mapping.entrySet()){
			if (entry.getValue().contains(n))
				return ctr;
			ctr++;
		}
		throw new RuntimeException("Node " + n + " not found!");
	}

	public int numSets(){
		return mapping.size();
	}

	public String getMapNodeString(Set<Node> nodes){
		String ret = "";
		boolean first = true;
		Set<Node> tmp = new TreeSet<Node>();
		int ctr = 0;
		for (Entry<String, SortedSet<Node>> entry : mapping.entrySet()){
			tmp.addAll(entry.getValue());
			tmp.retainAll(nodes);
			if (!tmp.isEmpty()) {
				ret += (first ? "" : ",") + tmp.size() + "\u2208" + ctr;
				first = false;
			}
			tmp.clear();
			ctr++;
		}
		return ret;
	}

	public String getMapEdgeString(Set<Node> nodes){
		return getMapEdgeString(nodes, nodes);
	}

	public String getMapEdgeString(Set<Node> nodeSet1, Set<Node> nodeSet2){
		String ret = "";
		boolean first = true;
		int ctr1 = 0;
		for (SortedSet<Node> entry1 : mapping.values()){
			int ctr2 = 0;
			for (SortedSet<Node> entry2 : mapping.values()){
				if (ctr1 <= ctr2) {
					int count = 0;
					for (Node node1 : entry1)
						if (nodeSet1.contains(node1))
							for (Node node2 : entry2)
								if (nodeSet2.contains(node2))
									if (ctr1 != ctr2 || nodeSet1 != nodeSet2 || node1.nodeId() < node2.nodeId())
										if (node1.isEdge(node2))
											count++;
					if (count > 0) {
						ret += (first ? "" : ",") + count + "\u2208" + ctr1 + "-" + ctr2;
						first = false;
					}
				}
				ctr2++;
			}
			ctr1++;
		}
		return ret;
	}

	@Override
	public boolean equals(Object other){
		if (!(other instanceof DescriptorMap))
			return super.equals(other);

		DescriptorMap descMap = (DescriptorMap) other;
		if (mapping.size() != descMap.mapping.size())
			return false;

		Iterator<Entry<String, SortedSet<Node>>> iter = mapping.entrySet().iterator();
		Iterator<Entry<String, SortedSet<Node>>> otherIter = descMap.mapping.entrySet().iterator();

		while (iter.hasNext()){
			Entry<String, SortedSet<Node>> entry = iter.next();
			Entry<String, SortedSet<Node>> otherEntry = otherIter.next();

			if (!entry.getKey().equals(otherEntry.getKey()))
				return false;

			if (entry.getValue().size() != otherEntry.getValue().size())
				return false;
		}
		return true;
	}

	public String toString(){
		String ret = "";
		boolean first = true;
		for (Entry<String, SortedSet<Node>> entry : mapping.entrySet()){
			ret += (first ? "" : "\n") + entry.getKey() + " -> " + entry.getValue();
			first = false;
		}
		return ret;
	}

}

package solvers.kent;

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

	public String getMapString(Set<Node> nodes){
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

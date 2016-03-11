package kent;

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

	public int getDescriptorIndex(Node n){
		int ctr = 0;
		for (Entry<String, SortedSet<Node>> entry : mapping.entrySet()){
			if (entry.getValue().contains(n))
				return ctr;
			ctr++;
		}
		throw new RuntimeException("Node " + n + " not found!");
	}

	public String getMapString(Set<Node> nodes){
		String ret = "";
		Set<Node> tmp = new TreeSet<Node>();
		int ctr = 0;
		for (Entry<String, SortedSet<Node>> entry : mapping.entrySet()){
			tmp.addAll(entry.getValue());
			tmp.retainAll(nodes);
			if (!tmp.isEmpty()) {
				ret += ""+tmp.size() + "\u2208" + ctr + ",";
			}
			tmp.clear();
			ctr++;
		}
		return ret;
	}
	
	public String toString(){
		return mapping.values().toString();
	}

}

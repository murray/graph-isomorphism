package benchmark;

import graph.StaticGraph;

/**
 * Simple struct for holding a GI instance.
 *
 */
public class GIInstance {
	public StaticGraph g1;
	public StaticGraph g2;
	public boolean isIsomorphism;
	
	public GIInstance(StaticGraph g1, StaticGraph g2, boolean isIsomorphism) {
		this.g1 = g1;
		this.g2 = g2;
		this.isIsomorphism = isIsomorphism;
	}
}

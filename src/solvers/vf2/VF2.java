package solvers.vf2;

import edu.ucla.sspace.graph.Graph;
import edu.ucla.sspace.graph.SimpleEdge;
import edu.ucla.sspace.graph.SparseUndirectedGraph;
import edu.ucla.sspace.graph.isomorphism.VF2IsomorphismTester;

public class VF2{
	
	public static class VF2Instance {
		public SparseUndirectedGraph g1;
		public SparseUndirectedGraph g2;
		public boolean isIsomorphism;
		
		public VF2Instance(SparseUndirectedGraph g1, SparseUndirectedGraph g2, boolean isIsomorphism) {
			this.g1 = g1;
			this.g2 = g2;
			this.isIsomorphism = isIsomorphism;
		}
	} 
	
	private static VF2IsomorphismTester vf2 = new VF2IsomorphismTester();

	public boolean isIsomorphism(SparseUndirectedGraph g1, SparseUndirectedGraph g2) {
		return vf2.areIsomorphic(g1, g2);
	}
}

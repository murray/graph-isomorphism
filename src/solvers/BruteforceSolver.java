package solvers;

import java.util.Arrays;

import org.apache.commons.lang.ArrayUtils;

import graph.Graph;
import graph.StaticGraph;

public class BruteforceSolver implements GISolver{

	
	private static int factorial(int n) {
		if(n < 0) {
			throw new IllegalArgumentException("n must be non-negative.");
		}
		
		int result = 1;
		for(int i=n; i>0; i--) {
			result *= i;
		}
		
		return result;
	}
	
	/**
	 * Return the next permutation sequence. e.g. 123, 132, 213, 231...
	 * https://stackoverflow.com/a/9368702
	 * 
	 * @param perm
	 * @return the next permutation sequence.
	 */
	private static int[] nextPermutation(int[] perm) {
		boolean isLast = true;
		
		for(int i=perm.length-1; i>0; i--) {
			if(perm[i-1] < perm[i]) {
				isLast = false;
				
				int leftNum = perm[i-1];
				
				int minIndexBigger = i;
				for(int j=i; j<perm.length; j++) {
					if(perm[j] < perm[minIndexBigger] && perm[j] > leftNum) {
						minIndexBigger = j;
					}
				}
				
				int temp = perm[i-1];
				perm[i-1] = perm[minIndexBigger];
				perm[minIndexBigger] = temp;
				
				Arrays.sort(perm, i, perm.length);
				
				break;
			}
		}
		
		if(isLast) {
			ArrayUtils.reverse(perm);
		}
		
		return perm;
	}
	
	@Override
	public boolean isIsomorphism(StaticGraph g1, StaticGraph g2) {
		
		if(g1.n != g2.n) {
			return false;
		}
		
		int[] permutation = new int[g1.n];
		for(int i=0; i<permutation.length; i++) {
			permutation[i] = i;
		}
		
		for(int i=0; i<factorial(g1.n); i++) {
			if(g1.equals(g2)) {
				return true;
			}
			
			nextPermutation(permutation);
			g1 = new StaticGraph(g1.permute(permutation));
		}
		
		return false;
	}
	
	public static void main(String[] args) {
		// Testing
		
		// factorial
		
		assert factorial(0) == 1;
		assert factorial(1) == 1;
		assert factorial(4) == 24;
		
		// nextPermutation
		
		int[] perm1 = {1,2,3,4};
		int[] perm2 = {4,3,2,1};
		nextPermutation(perm2);
		assert Arrays.equals(perm1, perm2);
		
		int[] perm3 = {1,2,4,3};
		nextPermutation(perm1);
		assert Arrays.equals(perm1, perm3);
		
		// isIsomorphism
		
		Graph g1 = new Graph(5);
		g1.randomize(0.5);
		int[] perm = {3,4,1,2,0};
		Graph g2 = g1.permute(perm);
		
		StaticGraph sg1 = new StaticGraph(g1);
		StaticGraph sg2 = new StaticGraph(g2);
		BruteforceSolver bfSolver = new BruteforceSolver();
		assert bfSolver.isIsomorphism(sg1, sg2);
		
		if(g2.isEdge(2, 3)) {
			g2.removeEdge(2, 3);
		} else {
			g2.addEdge(2, 3);
		}
		
		sg2 = new StaticGraph(g2);
		assert !bfSolver.isIsomorphism(sg1, sg2);
	}
}

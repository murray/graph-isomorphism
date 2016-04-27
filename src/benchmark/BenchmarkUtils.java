package benchmark;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.ucla.sspace.graph.SparseUndirectedGraph;
import edu.ucla.sspace.graph.SimpleEdge;
import solvers.GISolver;
import solvers.kent.KentsAlgorithm;
import solvers.vf2.VF2;
import solvers.vf2.VF2.VF2Instance;
import graph.Graph;
import graph.StaticGraph;


public class BenchmarkUtils {
	/**
	 * Time a GI solver on a specified instance.
	 * 
	 * @param solver The GI solver.
	 * @param instance A GI instance.
	 * @return The time it took to solve the instance, or -1 if the answer was incorrect.
	 */
	public static double timeSolver(GISolver solver, GIInstance instance) {
		final long startTime = System.currentTimeMillis();
		boolean answer = solver.isIsomorphism(instance.g1, instance.g2);
		final long endTime = System.currentTimeMillis();
		
		if(answer == instance.isIsomorphism) {
			return endTime - startTime;
		} else {
			return -1;
		}
	}
	
	/**
	 * Time over multiple instances.
	 * 
	 * @param solver
	 * @param instances
	 * @return
	 */
	public static double[] timeSolver(GISolver solver, List<GIInstance> instances) {
		double[] times = new double[instances.size()];
		
		for(int i=0; i<times.length; i++) {
			times[i] = timeSolver(solver, instances.get(i));
		}
		
		return times;
	}
	
	/**
	 * Time a GI solver on a specified instance.
	 * 
	 * @param solver The GI solver.
	 * @param instance A GI instance.
	 * @return The time it took to solve the instance, or -1 if the answer was incorrect.
	 */
	public static double timeSolver(VF2 solver, VF2Instance instance) {
		final long startTime = System.currentTimeMillis();
		boolean answer = solver.isIsomorphism(instance.g1, instance.g2);
		final long endTime = System.currentTimeMillis();
		
		if(answer == instance.isIsomorphism) {
			return endTime - startTime;
		} else {
			return -1;
		}
	}
	
	/**
	 * Time over multiple instances.
	 * 
	 * @param solver
	 * @param instances
	 * @return
	 */
	public static double[] timeSolver(VF2 solver, List<VF2Instance> instances) {
		double[] times = new double[instances.size()];
		
		for(int i=0; i<times.length; i++) {
			times[i] = timeSolver(solver, instances.get(i));
		}
		
		return times;
	}
	
	/**
	 * NOTE: This might still give an isomorphism... damn. Don't use.
	 * 
	 * Create a near-isomorphism that has the same number of nodes and edges.
	 * But if the given graph has no edges, add one.
	 * 
	 * @param g A graph.
	 * @return A near-isomorphism of g.
	 */
	public static Graph nearIsomorphism(Graph g) {
		if(g.numEdges() == 0) {
			Graph g2 = new Graph(g.n);
			g2.addEdge(0, 0);
			return g2;
		}
		
		boolean foundOne = false;
		boolean foundZero = false;
		int onex = 0;
		int oney = 0;
		int zerox = 0;
		int zeroy = 0;
		
		outerLoop:
		for(int i=0; i<g.n; i++) {
			for(int j=0; j<i+1; j++) {
				if(!foundOne && g.isEdge(i, j)) {
					onex = i;
					oney = j;
					foundOne = true;
				} else if(!foundZero && !g.isEdge(i, j)){
					zerox = i;
					zeroy = j;
					foundZero = true;
				}
				
				if(foundOne && foundZero) {
					break outerLoop;
				}
			}
		}
		
		Graph g2 = new Graph(g);
		
		g2.removeEdge(onex, oney);
		g2.addEdge(zerox, zeroy);
		
		return g2;
	}
	
	public static List<GIInstance> createInstances(int minSize, int maxSize, boolean yesInstances) {
		ArrayList<GIInstance> instances = new ArrayList<GIInstance>(maxSize-minSize+1);
		
		for(int size=minSize; size<maxSize+1; size++) {
			Graph g1 = new Graph(size);
			g1.randomize(0.5);
			
			Graph g2;
			if(yesInstances) {
				g2 = g1.randomlyPermute();
			} else {
				g2 = new Graph(g1);
				int x = (int) Math.random()*g2.n;
				int y = (int) Math.random()*g2.n;
				if(g2.isEdge(x, y)) {
					g2.removeEdge(x, y);
				} else {
					g2.addEdge(x, y);
				}
			}
			
			instances.add(new GIInstance(new StaticGraph(g1), new StaticGraph(g2), yesInstances));
		}
		
		return instances;
	}
	
	public static List<VF2Instance> convertToVF2Instances(List<GIInstance> instances) {
		List<VF2Instance> vf2instances = new ArrayList<VF2Instance>();
		
		for(GIInstance ins : instances) {
			SparseUndirectedGraph g1 = new SparseUndirectedGraph();
			SparseUndirectedGraph g2 = new SparseUndirectedGraph();
			
			int n = ins.g1.n;
			for(int i=0; i<n; i++) {
				g1.add(i);
			}
			
			for(int i=0; i<n; i++) {
				for(int j=0; j<n; j++) {
					if(ins.g1.isEdge(i, j)) {
						g1.add(new SimpleEdge(i, j));
					}
				}
			}
			
			n = ins.g2.n;
			for(int i=0; i<n; i++) {
				g2.add(i);
			}
			
			for(int i=0; i<n; i++) {
				for(int j=0; j<n; j++) {
					if(ins.g2.isEdge(i, j)) {
						g2.add(new SimpleEdge(i, j));
					}
				}
			}
			
			VF2Instance vf2ins = new VF2Instance(g1, g2, ins.isIsomorphism);
			vf2instances.add(vf2ins);
		}
		
		return vf2instances;
	}
	
	public static void saveTimesToFile(List<GIInstance> instances, double[] times, String filename) throws IOException {
		FileWriter fw = new FileWriter(filename);
		
		for(int i=0; i<instances.size(); i++) {
			fw.write(instances.get(i).g1.n + ", " + times[i] + "\n");
		}
		
		fw.flush();
		fw.close();
	}
	
	public static void saveVF2TimesToFile(List<VF2Instance> instances, double[] times, String filename) throws IOException {
		FileWriter fw = new FileWriter(filename);
		
		for(int i=0; i<instances.size(); i++) {
			fw.write(instances.get(i).g1.order() + ", " + times[i] + "\n");
		}
		
		fw.flush();
		fw.close();
	}
	
	public static void main(String[] agrs) throws IOException {
//		List<GIInstance> instances = createInstances(5, 200, true);
//		KentsAlgorithm kents = new KentsAlgorithm();
//		double[] kentTimes = timeSolver(kents, instances);
//		saveTimesToFile(instances, kentTimes, "kents-true-5-200.csv");
		
//		List<GIInstance> instances = createInstances(5, 15, true);
//		BruteforceSolver bfSolver = new BruteforceSolver();
//		double[] bfTimes = timeSolver(bfSolver, instances);
//		saveTimesToFile(instances, bfTimes, "bf-true-5-15.csv");
		
		List<GIInstance> instances = createInstances(5, 200, true);
		List<VF2Instance> vf2instances = convertToVF2Instances(instances);
		VF2 vf2 = new VF2();
		double[] times = timeSolver(vf2, vf2instances);
		saveVF2TimesToFile(vf2instances, times, "vf2-true-5-200.csv");
	}
}

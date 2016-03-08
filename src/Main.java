
public class Main{
	
	static int n = 12;
	
	public static void main(String[] args){
		System.out.println(randomGraph(n));
	}

	private static Graph randomGraph(int size){
		Graph graph = new Graph(size);
		//generate random links
		for (int i=0; i<size; i++)
			for (int j=0; j<i; j++)
				if (Math.random()<0.5)
					graph.addEdge(i, j);
				else
					graph.removeEdge(i, j);
		return graph;
	}
	
}

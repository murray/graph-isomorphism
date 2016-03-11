package graph;
public class StaticGraph extends Graph{

	public StaticGraph(Graph graph){
		//clone the given graph
		super(graph);
	}
	
	@Override
	public boolean addEdge(int a, int b){
		throw new ImmutableGraphException("Attempted to modify a static graph!");
	}
	
	@Override
	public boolean removeEdge(int a, int b){
		throw new ImmutableGraphException("Attempted to modify a static graph!");
	}
	
	@Override
	public void swapNodes(int a, int b){
		throw new ImmutableGraphException("Attempted to modify a static graph!");
	}
	
	static class ImmutableGraphException extends RuntimeException{
		private static final long serialVersionUID = 1221000617226246017L;
		ImmutableGraphException(String message){
			super(message);
		}
	}

}

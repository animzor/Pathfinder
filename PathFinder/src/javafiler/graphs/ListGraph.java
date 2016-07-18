package javafiler.graphs;

import java.util.*;
import java.util.Map.Entry;

public class ListGraph<N> implements GraphMethods<N> {

	private Map<N, List<Edge<N>>> nodes = new HashMap<>();

	public void add(N ny) {
		if (nodes.containsKey(ny))
			throw new IllegalArgumentException("Stad finns!");
		nodes.put(ny, new ArrayList<Edge<N>>());
	}

	public void connect(N from, N to, String namn, int vikt) {
		List<Edge<N>> fromList = nodes.get(from);
		List<Edge<N>> toList = nodes.get(to);
		if (fromList == null || toList == null)
			throw new NoSuchElementException("Vid connect");

		Edge<N> e1 = new Edge<N>(to, namn, vikt);
		fromList.add(e1);

		Edge<N> e2 = new Edge<N>(from, namn, vikt);
		toList.add(e2);

	}

	
	public Set<N> getNodes(){
		Set<N> nodesSet = new HashSet<N>();
		for(N N : nodes.keySet())
			nodesSet.add(N);
		return nodesSet;
		
	}
	
	public List<Edge<N>> getEdgesFrom(N nod){
		ArrayList<Edge<N>> edgeLista = new ArrayList<Edge<N>>();
		
		for(N n : nodes.keySet())
			if(n.equals(nod)){
			for(Edge<N> edge : nodes.get(n))
				edgeLista.add(edge);
			}
		return edgeLista;
	}
	
	public Map<N, List<Edge<N>>> getNodesMap(){
		return nodes;
	}

	public Edge<N> getEdgeBetween(N s1, N s2) {
		if (!nodes.containsKey(s1))
			throw new NoSuchElementException("Staden ej i grafen");
		for (Edge<N> e : nodes.get(s1))
			if (e.getDest().equals(s2))
				return e;

		return null;
	}

	public void dfs(N where, Set<N> besökta) {
		besökta.add(where);
		for (Edge<N> e : nodes.get(where)) 
			if (!besökta.contains(e.getDest()))
				dfs(e.getDest(), besökta);
			
			
		}
	
	public boolean pathExists(N from, N to) {
		Set<N> besökta = new HashSet<N>();
	    
		dfs(from, besökta);
		return besökta.contains(to);
	}

	public void dfs2(N where, N whereFrom, Set<N> b, Map<N, N> via) {
		b.add(where);
		via.put(where, whereFrom);
		for (Edge<N> e : nodes.get(where))
			if (!b.contains(e.getDest()))
				dfs2(e.getDest(), where, b, via);
	}

	public List<Edge<N>> anyPath(N from, N to) {
		Set<N> besökta = new HashSet<N>();
		Map<N, N> via = new HashMap<N, N>();
		dfs2(from, null, besökta, via);
		List<Edge<N>> vägen = new ArrayList<Edge<N>>();

		N current = to;
		while (!current.equals(from)) {
			N cameFrom = via.get(current);
			Edge<N> edge = getEdgeBetween(cameFrom, current);
			vägen.add(edge);
			current = cameFrom;
		}
		Collections.reverse(vägen);
		
		return vägen;

	}
	
	public void setConnectionWeight(N from, N to, int i){
		if(i < 0)
			throw new IllegalArgumentException();
		
		if(from == null || to == null || getEdgeBetween(from, to) == null)
			throw new NoSuchElementException();
		
		getEdgeBetween(from, to).setVikt(i);
		getEdgeBetween(to, from).setVikt(i);
	}
	
	public List<N> Dijkstra(N from, N to){
		List<N> visited = new ArrayList<N>();
		List<N> unvisited = new ArrayList<N>();
		unvisited.add(from);
		Map<N, N> predecessors = new HashMap<N, N>();
		predecessors.put(from, null);
		Map<N, Integer> distances = new HashMap<N, Integer>();
		
		for(N node : nodes.keySet()){
			if(node == from){
				distances.put(node, 0);
			} else {
				distances.put(node, Integer.MAX_VALUE);
			}
		}
		
		System.out.println(distances);
		
		while(!unvisited.isEmpty()){
			
			N evaluationNode = getMinimum(unvisited, distances);

			unvisited.remove(evaluationNode);
			visited.add(evaluationNode);
			evaluatedNeighbours(evaluationNode, visited, unvisited, distances, predecessors);
		}
		System.out.println(predecessors.toString());
		return shortestPath(predecessors, to);
	}

	public void evaluatedNeighbours(N evaluationNode, List<N> visited, List<N> unvisited,  Map<N, Integer> distances, Map<N, N> predecessors) {
		for(Edge<N> e : nodes.get(evaluationNode)){
			if(!visited.contains(e)){
				N destination = e.getDest();
				int edgeDistance = e.getVikt();
				int newDistance = distances.get(evaluationNode) + edgeDistance;
				if(distances.get(destination) > newDistance){
					distances.put(destination, newDistance);
					predecessors.put(destination, evaluationNode);
					unvisited.add(destination);
				}
			}
		}
		
		
	}
	
	private N getMinimum(List<N> nodes, Map<N, Integer> distances) {
	    N minimum = null;
	    for (N node : nodes) {
	      if (minimum == null) {
	        minimum = node;
	      } else {
	        if (getShortestDistance(node, distances) < getShortestDistance(minimum, distances)) {
	          minimum = node;
	        }
	      }
	    }
	    return minimum;
	  }
	
	private int getShortestDistance(N destination, Map<N, Integer> distances) {
	    Integer d = distances.get(destination);
	    if (d == null) {
	      return Integer.MAX_VALUE;
	    } else {
	      return d;
	    }
	  }

	
	public List<N> shortestPath(Map<N, N> preD, N to){
		List<N> shortestPath = new ArrayList<N>();
		N step = to;
		shortestPath.add(step);
		while(preD.get(step) != null){
			step = preD.get(step);
			shortestPath.add(step);
		}
		Collections.reverse(shortestPath);
		return shortestPath;

	}
	
	public String toString() {
		String str = "";
		for (Map.Entry<N, List<Edge<N>>> e : nodes.entrySet()) {
			str += e.getKey() + ": ";
			for (Edge<N> båge : e.getValue()) {
				str += båge + " ";
				str += "\n";
			}
		}
		return str;

	}


}

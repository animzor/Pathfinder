package javafiler.graphs;
import static org.junit.Assert.*;

import java.util.*;

import javafiler.Marker;

import org.junit.Test;


public class Testcase {
	private javafiler.graphs.ListGraph<String> listGraph = new javafiler.graphs.ListGraph<String>();
	HashMap<String, Edge<String>> nodes = new HashMap<String, Edge<String>>();
	HashMap<String, String> path = new HashMap<String, String>();
	
	@Test
	public void test() {
		String a = "a";
		String b = "b";
		String c = "c";
		String d = "d";
		String e = "e";
		String f = "f";
		
		listGraph.add(a);
		listGraph.add(b);
		listGraph.add(c);
		listGraph.add(d);
		listGraph.add(e);
		listGraph.add(f);
		
		listGraph.connect(a, b, "tåg", 1);
		listGraph.connect(a, c, "tåg", 3);
		listGraph.connect(a, d, "tåg", 9);
		listGraph.connect(b, d, "tåg", 3);
		listGraph.connect(b, e, "tåg", 8);
		listGraph.connect(c, e, "tåg", 4);
		listGraph.connect(e, f, "tåg", 1);
		System.out.println(listGraph.Dijkstra(a, f));
		
	}
	
	@Test
	public void testShortestPath(){
		
		path.put("b", "a");
		path.put("c", "b");
		path.put("d", "c");
		path.put("e", "d");
		path.put("f", "e");
		
		//System.out.println(listGraph.shortestPath(path, "f"));
		
	}
	
	@Test
	public void testEvaluated(){
		
		String a = "a";
		String b = "b";
		String c = "c";
		String d = "d";
		String e = "e";
		String f = "f";
		
		listGraph.add(a);
		listGraph.add(b);
		listGraph.add(c);
		listGraph.add(d);
		listGraph.add(e);
		listGraph.add(f);
		
		listGraph.connect(a, b, "tåg", 1);
		listGraph.connect(a, c, "tåg", 3);
		listGraph.connect(a, d, "tåg", 9);
		listGraph.connect(b, d, "tåg", 3);
		listGraph.connect(b, e, "tåg", 8);
		listGraph.connect(c, e, "tåg", 4);
		listGraph.connect(e, f, "tåg", 1);
		
		List<String> visited = new ArrayList<String>();
		List<String> unvisited = new ArrayList<String>();
		unvisited.add(a);
		Map<String, String> predecessors = new HashMap<String, String>();
		predecessors.put(a, null);
		Map<String, Integer> distances = new HashMap<String, Integer>();
		
		for(String node : listGraph.getNodesMap().keySet()){
			if(node == "a"){
				distances.put(node, 0);
			} else {
				distances.put(node, Integer.MAX_VALUE);
			}
		}
		
		String evaluationNode = "a";
		
		listGraph.evaluatedNeighbours(evaluationNode, visited, unvisited, distances, predecessors);
		
	}

}

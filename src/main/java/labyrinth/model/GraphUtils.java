package labyrinth.model;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.function.Function;
import java.util.function.Predicate;


/**
 * Utility class providing static methods for graph-related operations such as
 * subtree retrieval, breadth-first search, and pathfinding.
 */
public class GraphUtils {
	private GraphUtils(){}
	
	/**
     * Generates a list representing the subtree of a DAG starting from a specified node.
     * The subtree is constructed by recursively traversing child nodes until a node
     * satisfies the stop condition.
     * @param start the root node of the subtree
     * @param children a function that provides the children of a given node
     * @param stopAt a predicate defining nodes where traversal should stop
     * @return a list of nodes representing the subtree rooted at the start node
     */
	public static <T> List<T> subtree(T start, Function<T, List<T>> children,
		Predicate<T> stopAt) {
		if (stopAt.test(start)) {
			return new ArrayList<>();
		}
		List<T> strees = new ArrayList<>(List.of(start));
		for (T ch : children.apply(start)) {
			strees.addAll(subtree(ch, children, stopAt));
		}
		return strees;
	}

	/**
     * Performs a breadth-first search (BFS) on a graph starting from a specified node.
     * Tracks the path to each visited node, and optionally stops at specified nodes
	 * (the stopping node will be in the subgraph, but its children will
	 * not be, if they are not otherwise reachable).
     * @param inGraph a predicate defining which nodes are part of the graph
     * @param children a function that provides the children of a given node
     * @param stopAt a list of nodes where the search should stop, null if there are no such nodes
     * @param start the starting node for the search
     * @return a map of nodes to their respective parent nodes in the BFS tree
     */
	public static <T> Map<T,T> breadthFirstSearch(Predicate<T> inGraph, Function<T, List<T>> children,
			List<T> stopAt, T start) {
		if (stopAt == null) {
			stopAt = new ArrayList<>();
		}
		Map<T,T> been = new HashMap<>();
		been.put(start, null);
		if (stopAt.contains(start)){
			return been;
		}
		List<T> toCheck = new ArrayList<>();
		toCheck.add(start);

		while (! toCheck.isEmpty()) {
			T checking = toCheck.get(0);
			for (T nxt : children.apply(checking)){
				if (! been.containsKey(nxt) && inGraph.test(nxt)){
					been.put(nxt, checking);
					if (! stopAt.contains(nxt)){
						toCheck.add(nxt);
					}
				}
			}
			toCheck.remove(checking);
		}
		return been;
	}

	/**
     * Returns a set of nodes connected to a specified starting node within a graph.
     * The connectivity is determined using a breadth-first search. It is also possible to stop
	 * the search at specific nodes (the stopping node will be in the subgraph, but its children will
	 * not be, if they are not otherwise reachable).
     * @param nodes the list of nodes to consider in the search
     * @param children a function that provides the children of a given node
     * @param stopAt a list of nodes where the search should stop, null if there are no such nodes
     * @param startIdx the index of the starting node in the nodes list
     * @return a set of nodes connected to the start node
     */
	public static <T> Set<T> connectedTo(List<T> nodes, Function<T, List<T>> children,
			List<T> stopAt, int startIdx) {
		if (nodes.isEmpty()) {return new HashSet<>();}
		return breadthFirstSearch(nodes::contains, children, stopAt, nodes.get(startIdx)).keySet();
	}

	/**
     * Finds a path between two nodes in a graph using a breadth-first search.
     * Returns a list of nodes representing the path from the starting node to the destination node (both inclusive).
     * @param inGraph a predicate defining which nodes are part of the graph
     * @param children a function that provides the children of a given node
     * @param from the starting node
     * @param to the destination node
     * @return a list of nodes representing the path from the starting node to the destination node
     */
	public static <T> List<T> pathTo(Predicate<T> inGraph, Function<T, List<T>> children, T from, T to) {
		Map<T, T> tree = breadthFirstSearch(inGraph, children, null, to);
		List<T> path = new ArrayList<>();
		T checking = from;
		while (checking != null) {
			path.add(checking);
			checking = tree.get(checking);
		}
		return path;
	}

}

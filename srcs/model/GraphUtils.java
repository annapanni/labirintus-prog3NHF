package model;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.function.Function;
import java.util.function.Predicate;


public class GraphUtils {
	private GraphUtils(){}
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

	public static <T> Set<T> connectedTo(List<T> nodes, Function<T, List<T>> children,
			List<T> stopAt, int startIdx) {
		if (nodes.isEmpty()) {return new HashSet<>();}
		return breadthFirstSearch(nodes::contains, children, stopAt, nodes.get(startIdx)).keySet();
	}

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

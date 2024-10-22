package model;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.function.Function;


public class GraphUtils {
	public static <T> List<T> subtree(T start, Function<T, List<T>> children,
			Function<T, Boolean> stopAt) {
		if (stopAt.apply(start)) {
			return new ArrayList<>();
		}
		List<T> strees = new ArrayList<>(List.of(start));
		for (T ch : children.apply(start)) {
			strees.addAll(subtree(ch, children, stopAt));
		}
		return strees;
	}

	public static <T> Map<T,T> breadthFirstSearch(List<T> nodes, Function<T, List<T>> children,
			List<T> stopAt, int startIdx) {
		if (nodes.isEmpty()) {
			return new HashMap<>();
		}
		if (stopAt == null) {
			stopAt = new ArrayList<>();
		}
		Map<T,T> been = new HashMap<>();
		been.put(nodes.get(startIdx), null);
		if (stopAt.contains(nodes.get(startIdx))){
			return been;
		}
		List<T> toCheck = new ArrayList<>();
		toCheck.add(nodes.get(startIdx));

		while (! toCheck.isEmpty()) {
			T checking = toCheck.get(0);
			for (T nxt : children.apply(checking)){
				if (! been.containsKey(nxt) && nodes.contains(nxt)){
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
		return breadthFirstSearch(nodes, children, stopAt, startIdx).keySet();
	}

	public <T> List<T> pathTo(List<T> nodes, Function<T, List<T>> children, T from, T to) {
		int startIdx = nodes.lastIndexOf(to);
		Map<T, T> tree = breadthFirstSearch(nodes, children, null, startIdx);
		List<T> path = new ArrayList<>();
		T checking = from;
		while (checking != null) {
			path.add(checking);
			checking = tree.get(checking);
		}
		return path;
	}

}

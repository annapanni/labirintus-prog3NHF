package model;

import java.util.List;
import java.util.ArrayList;
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

	public static <T> List<T> depthFirstSearch(List<T> nodes, Function<T, List<T>> children,
			List<T> stopAt, int startIdx) {
		if (nodes.isEmpty()) {
			return new ArrayList<>();
		}
		if (stopAt == null) {
			stopAt = new ArrayList<>();
		}
		if (stopAt.contains(nodes.get(startIdx))){
			List<T> been = new ArrayList<>();
			been.add(nodes.get(startIdx));
			return been;
		}
		List<T> been = new ArrayList<>();
		List<T> toCheck = new ArrayList<>();
		toCheck.add(nodes.get(startIdx));

		while (! toCheck.isEmpty()) {
			for (T nxt : children.apply(toCheck.get(0))){
				if (! been.contains(nxt) && ! toCheck.contains(nxt) && nodes.contains(nxt)){
					if (stopAt.contains(nxt)){
						been.add(nxt);
					} else {
						toCheck.add(nxt);
					}
				}
			}
			been.add(toCheck.remove(0));
		}
		return been;
	}

}

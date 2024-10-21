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
}

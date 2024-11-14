package labyrinth.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

class GraphUtilsTest {

    private Map<Integer, List<Integer>> adjacencyList;
    private Function<Integer, List<Integer>> children;

    @BeforeEach
    void setUp() {
        adjacencyList = new HashMap<>();
        adjacencyList.put(1, List.of(2, 3));
        adjacencyList.put(2, List.of(4, 5));
        adjacencyList.put(3, List.of(6));
        adjacencyList.put(4, List.of());
        adjacencyList.put(5, List.of());
        adjacencyList.put(6, List.of());

        // Define the children function based on the adjacency list
        children = node -> adjacencyList.getOrDefault(node, List.of());
    }

    @Test
    void subtreeNoStopConditionTest() {
        List<Integer> result = GraphUtils.subtree(1, children, node -> false);
        List<Integer> expected = List.of(1, 2, 4, 5, 3, 6);
        assertEquals(expected, result, "Subtree with no stopping condition should return the entire subtree.");
    }

    @Test
    void subtreeWithStopConditionTest() {
        List<Integer> result = GraphUtils.subtree(1, children, node -> node == 2);
        List<Integer> expected = List.of(1, 3, 6); // Stops at node 2, excluding it and its children
        assertEquals(expected, result, "Subtree with a stop condition should exclude nodes when the condition is met.");
    }

    @Test
    void breadthFirstSearchNoStopConditionTest() {
        Predicate<Integer> inGraph = node -> adjacencyList.containsKey(node);
        Map<Integer, Integer> result = GraphUtils.breadthFirstSearch(inGraph, children, null, 1);

        Map<Integer, Integer> expected = new HashMap<>();
        expected.put(1, null);
        expected.put(2, 1);
        expected.put(3, 1);
        expected.put(4, 2);
        expected.put(5, 2);
        expected.put(6, 3);

        assertEquals(expected, result, "BFS without a stopping condition should explore all reachable nodes.");
    }

    @Test
    void breadthFirstSearchWithStopConditionTest() {
        Predicate<Integer> inGraph = node -> adjacencyList.containsKey(node);
        Map<Integer, Integer> result = GraphUtils.breadthFirstSearch(inGraph, children, List.of(2), 1);

        Map<Integer, Integer> expected = new HashMap<>();
        expected.put(1, null);
        expected.put(2, 1);
        expected.put(3, 1);
        expected.put(6, 3); // Nodes 4 and 5 under 2 are not included

        assertEquals(expected, result, "BFS with a stop condition should stop exploring from nodes in the stop list.");
    }

    @Test
    void connectedToAllNodesConnectedTest() {
        List<Integer> nodes = List.of(1, 2, 3, 4, 5, 6);
        Set<Integer> result = GraphUtils.connectedTo(nodes, children, null, 0);
        Set<Integer> expected = Set.of(1, 2, 3, 4, 5, 6);

        assertEquals(expected, result, "All nodes should be reachable in the connected component.");
    }

    @Test
    void connectedToWithStopConditionTest() {
        List<Integer> nodes = List.of(1, 2, 3, 4, 5, 6);
        Set<Integer> result = GraphUtils.connectedTo(nodes, children, List.of(2), 0);
        Set<Integer> expected = Set.of(1, 2, 3, 6);

        assertEquals(expected, result, "When stopping at node 2, only nodes not under 2 should be reachable.");
    }

    @Test
    void pathToFullPathTest() {
        Predicate<Integer> inGraph = node -> adjacencyList.containsKey(node);
        List<Integer> result = GraphUtils.pathTo(inGraph, children, 4, 1);
        List<Integer> expected = List.of(4, 2, 1);

        assertEquals(expected, result, "Path from node 4 to 1 should include 4, 2, and 1.");
    }

    @Test
    void pathToNoPathTest() {
        Predicate<Integer> inGraph = node -> adjacencyList.containsKey(node);
        List<Integer> result = GraphUtils.pathTo(inGraph, children, 5, 6);
        List<Integer> expected = List.of(5);

        assertEquals(expected, result, "When no path exists, the path should only contain the starting node.");
    }
}
package model;

import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.stream.IntStream;

import java.util.Optional;
import java.util.function.Predicate;

public class ConvexRoomFinder implements RoomFinder {
	int maxRad;

	public ConvexRoomFinder(int mr) {maxRad = mr;}

	public Room findRoomAt(Vector idx, Labyrinth lab) {
		List<Vector> stree = GraphUtils.subtree(idx, lab::getChildren, lab::inAnyRoom);
		List<Vector> reachable = new LinkedList<>();

		int startx = idx.x - maxRad;
		int endx = idx.x + maxRad + 1;
		int starty = idx.y - maxRad;
		int endy = idx.y + maxRad + 1;
		for (int y=starty; y < endy; y++) {
			for (int x=startx; x < endx; x++) {
				Vector v = new Vector(x, y);
				if (lab.inBound(v) && stree.contains(v) && ! lab.inAnyRoom(v) && lab.getDist2Between(idx, v) < maxRad*maxRad) {
					reachable.add(v);
				}
			}
		}

		List<Vector> outliers = reachable.stream().filter(n -> lab.getValidNeighbours(n).stream().filter(reachable::contains).count() < 2).toList();
		while (! outliers.isEmpty()) {
			reachable.removeAll(outliers);
			outliers = reachable.stream().filter(n -> lab.getValidNeighbours(n).stream().filter(reachable::contains).count() < 2).toList();
		}

		List<Vector> gates = reachable.stream().filter(n -> {
			List<Vector> notRoomNeighbours = lab.getAllNeighbours(n).stream().filter(x -> ! reachable.contains(x)).toList();
			return GraphUtils.connectedTo(notRoomNeighbours, lab::getAllNeighbours, null, 0).size() != notRoomNeighbours.size();
		}).toList();
		
		Optional<Vector> firstNotGate = reachable.stream().filter(Predicate.not(gates::contains)).findFirst();
		int notGateIdx = firstNotGate.isPresent() ? reachable.lastIndexOf(firstNotGate.get())	: 0;
		List<Vector> newReach = new LinkedList<>(GraphUtils.connectedTo(reachable, lab::getValidNeighbours, gates, notGateIdx));
		reachable.clear();
		reachable.addAll(newReach);

		return new ConvexRoom(lab, reachable);
	}
}

package model;

import java.util.List;
import java.util.LinkedList;
import java.util.Optional;
import java.util.function.Predicate;

public class ConcaveRoomFinder implements RoomFinder {
	private int maxRad;

	public ConcaveRoomFinder(int mr) {maxRad = mr;}

	public Room findRoomAt(Vector idx, Labyrinth lab) {
		List<Vector> stree = GraphUtils.subtree(idx, lab::getChildren, lab::inAnyRoom);
		List<Vector> reachable = new LinkedList<>();

		int startx = idx.getX()- maxRad;
		int endx = idx.getX()+ maxRad + 1;
		int starty = idx.getY() - maxRad;
		int endy = idx.getY() + maxRad + 1;
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
			List<Vector> notRoomNeighbours = lab.getAllNeighbours(n).stream().filter(Predicate.not(reachable::contains)).toList();
			return GraphUtils.connectedTo(notRoomNeighbours, lab::getAllNeighbours, null, 0).size() != notRoomNeighbours.size();
		}).toList();

		Optional<Vector> firstNotGate = reachable.stream().filter(Predicate.not(gates::contains)).findFirst();
		int notGateIdx = firstNotGate.isPresent() ? reachable.lastIndexOf(firstNotGate.get())	: 0;
		List<Vector> newReach = new LinkedList<>(GraphUtils.connectedTo(reachable, lab::getValidNeighbours, gates, notGateIdx));
		reachable.clear();
		reachable.addAll(newReach);

		return new ConcaveRoom(lab, reachable);
	}
}
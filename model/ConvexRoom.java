package model;

import java.util.stream.Collectors;
import java.util.List;
import java.util.LinkedList;;

public class ConvexRoom extends Room {
	private Vector sidx;
	private List<Vector> nodes;

	public ConvexRoom(Labyrinth l, List<Vector> ns) {
		super(l);
		nodes = ns;
	}

	public boolean idxInRoom(Vector idx) {
		return nodes.contains(idx);
	}

	public List<Vector> getBorderPoly() {
		List<Vector> border = nodes.stream().filter(n -> lab.onBound(n) ||
			lab.getValidNeighbours(n).stream().map(nodes::contains).toList().contains(false)
		).collect(Collectors.toCollection(LinkedList::new));

		List<Vector> orderedBorder = new LinkedList<>();
		orderedBorder.add(border.remove(0));
		while (! border.isEmpty()){
			List<Vector> neighbours = lab.getAllNeighbours(orderedBorder.getLast());
			int nnum = neighbours.size();
			boolean nextIsOk = false;
			for (int i=0; i<=nnum; i++) {
				Vector neighbour = neighbours.get(i % nnum);
				if (nextIsOk && border.contains(neighbour)){
					orderedBorder.add(neighbour);
					border.remove(neighbour);
					break;
				} else if (! nodes.contains(neighbour)) {
					nextIsOk = true;
				}
			}
		}
		return orderedBorder;
	}

	public int size() {return nodes.size();}
}

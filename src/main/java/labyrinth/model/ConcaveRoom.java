package labyrinth.model;

import java.util.stream.Collectors;
import java.util.List;
import java.util.LinkedList;

/**
 * Representation of a concave room in any labyrinth. The room can be any concave shape as long as
 * it does not have holes in it, and its border does not intersect itself.
 */
public class ConcaveRoom extends Room {
	private List<Vector> nodes;

	/**
     * Constructs a ConcaveRoom with the specified labyrinth and nodes.
     * @param l the labyrinth in which this room is contained
     * @param ns a list of vectors representing the nodes that define the room's shape
     */
	public ConcaveRoom(Labyrinth l, List<Vector> ns) {
		super(l);
		nodes = ns;
	}

	public int size() {return nodes.size();}

	public boolean idxInRoom(Vector idx) {
		return nodes.contains(idx);
	}

	/**This method constructs the room's border by filtering out only those nodes
     * on the room's boundary and arranges them in a connected order.*/
	public List<Vector> getBorderPoly() {
		List<Vector> border = nodes.stream().filter(n -> getLab().onBound(n) ||
			getLab().getAllNeighbours(n).stream().map(nodes::contains).toList().contains(false)
		).collect(Collectors.toCollection(LinkedList::new));

		List<Vector> orderedBorder = new LinkedList<>();
		orderedBorder.add(border.remove(0));
		while (! border.isEmpty()){
			List<Vector> neighbours = getLab().getAllNeighbours(orderedBorder.getLast());
			List<Vector> validNeighbours = getLab().getValidNeighbours(orderedBorder.getLast());
			int nnum = neighbours.size();
			boolean nextIsOk = false;
			for (int i=0; i<=nnum; i++) {
				Vector neighbour = neighbours.get(i % nnum);
				if (nextIsOk && border.contains(neighbour) && validNeighbours.contains(neighbour)){
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
}

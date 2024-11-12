package labyrinth.model;

import java.util.List;

/**
 * Representation of a rectangular room in any labyrinth. The room can only contains the nodes whose 
 * indeces are in a given [a;b]×[c;d] 2d interval.
 */
public class RectRoom extends Room {
	private Vector sidx;
	private int width;
	private int height;

	/**
     * Constructs a RectRoom with the specified labyrinth and nodes.
	 * @param l the labyrinth in which this room is contained
	 * @param si the upper left corner of the room
	 * @param w the width of the room
	 * @param h the height of the room
	 */
	public RectRoom(Labyrinth l, Vector si, int w, int h) {
		super(l);
		sidx = si;
		width = w;
		height = h;
	}

	public int size() {return width*height;}

	public boolean idxInRoom(Vector idx) {
		return sidx.getX() <= idx.getX() && sidx.getY() <= idx.getY() && idx.getX() < sidx.getX() + width
			&& idx.getY() < sidx.getY() + height;
	}

	public List<Vector> getBorderPoly() {
		return List.of(sidx, sidx.plus(new Vector(width-1, 0)),
			sidx.plus(new Vector(width-1, height-1)), sidx.plus(new Vector(0, height-1)));
	}

}

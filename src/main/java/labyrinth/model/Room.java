package labyrinth.model;

import java.util.List;

/** Abstract class for representing rooms in a labyrinth*/
public abstract class Room implements java.io.Serializable {
	private Labyrinth lab;

	/** Returns the labyrinth in which the rooms is contained */
	public Labyrinth getLab() {return lab;}

	protected Room(Labyrinth l) {lab = l;}

	/** Check whether given index is inside the room */
	public abstract boolean idxInRoom(Vector idx);
	/**Returns a list of vectors representing the ordered border of the room.*/
	public abstract List<Vector> getBorderPoly();
	/** Returns the number of nodes defining the room.*/
	public abstract int size();
}

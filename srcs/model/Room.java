package model;

import java.util.List;

public abstract class Room implements java.io.Serializable {
	private Labyrinth lab;

	public Labyrinth getLab() {return lab;}

	public Room(Labyrinth l) {lab = l;}

	public abstract boolean idxInRoom(Vector idx);
	public abstract List<Vector> getBorderPoly();
	public abstract int size();
}

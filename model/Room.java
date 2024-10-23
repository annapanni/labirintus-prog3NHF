package model;

import java.util.List;

public abstract class Room {
	protected Labyrinth lab;

	protected Room(Labyrinth l) {lab = l;}
	public Labyrinth getLab() {return lab;}

	public abstract boolean idxInRoom(Vector idx);
	public abstract List<Vector> getBorderPoly();
	public abstract int size();
}

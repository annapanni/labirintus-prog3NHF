package model;

import java.util.List;

public class RectRoom extends Room {
	private Vector sidx;
	private int width;
	private int height;

	public RectRoom(Labyrinth l, Vector si, int w, int h) {
		super(l);
		sidx = si;
		width = w;
		height = h;
	}

	public boolean idxInRoom(Vector idx) {
		return sidx.x <= idx.x && sidx.y <= idx.y && idx.x < sidx.x + width
			&& idx.y < sidx.y + height;
	}

	public List<Vector> getBorderPoly() {
		return List.of(sidx, sidx.plus(new Vector(width-1, 0)),
			sidx.plus(new Vector(width-1, height-1)), sidx.plus(new Vector(0, height-1)));
	}

	public int size() {return width*height;}
}

package controller;

import java.util.List;
import java.util.ArrayList;

import model.*;


public class RectRoomFinder implements RoomFinder, java.io.Serializable{
	private int maxsize;

	public RectRoomFinder(int ms) {maxsize = ms;}

	public Room findRoomAt(Vector idx, Labyrinth lab) {
		List<Vector> stree = GraphUtils.subtree(idx, lab::getChildren, lab::inAnyRoom);
		List<List<Boolean>> okmx = new ArrayList<>();
		for (int y=0; y < lab.getHeight(); y++) {
			List<Boolean> row = new ArrayList<>();
			for (int x=0; x < lab.getWidth(); x++) {
				Vector v = new Vector(x, y);
				row.add(stree.contains(v) && ! lab.inAnyRoom(v));
			}
			okmx.add(row);
		}

		RectRoom lgst = new RectRoom(lab, idx, 1, 1);
		for (int w=2; w<=maxsize; w++) {
			for (int sx=Math.max(0, idx.getX() - w + 1); sx <= Math.min(lab.getWidth()-w, idx.getX()); sx++) {
				if (okmx.get(idx.getY()).subList(sx, sx + w).contains(false)){
					continue;
				}
				for (int h=2; h<=maxsize; h++) {
					for (int sy=Math.max(0, idx.getY() - h + 1); sy <= Math.min(lab.getHeight()-h, idx.getY()); sy++) {
						final int sxFinal = sx;
						final int wFinal = w;
						boolean ok = ! okmx.stream().map(row -> ! row.subList(sxFinal, sxFinal + wFinal).contains(false)).toList().subList(sy, sy + h).contains(false);
						if (ok && w*h > lgst.size()) {
							lgst = new RectRoom(lab, new Vector(sx, sy), w, h);
						}
					}
				}
			}
		}

		return lgst;
	}
}

package model;

import java.util.List;
import java.util.ArrayList;

public class RectRoomFinder implements RoomFinder {
	int maxsize;

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
			for (int sx=Math.max(0, idx.x - w + 1); sx <= Math.min(lab.getWidth()-w, idx.x); sx++) {
				if (okmx.get(idx.y).subList(sx, sx + w).contains(false)){
					continue;
				}
				for (int h=2; h<=maxsize; h++) {
					for (int sy=Math.max(0, idx.y - h + 1); sy <= Math.min(lab.getHeight()-h, idx.y); sy++) {
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

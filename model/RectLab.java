package model;

import java.util.ArrayList;
import java.util.List;


public class RectLab extends Labyrinth {
	private List<List<Vector>> directions;

	public RectLab(int w, int h, RoomFinder rf) {
		super(w, h, rf);
		root = new Vector(0, 0);
		//initialize directions
		directions = new ArrayList<>();
		for (int y=0; y<h; y++) {
			List<Vector> row = new ArrayList<>();
			row.add(new Vector(0, -1));
			for (int x=1; x < w; x++) {row.add(new Vector(-1, 0));}
			directions.add(row);
		}
		directions.get(0).set(0, new Vector(0,0));
	}

	public Vector getDir(Vector idx) {
		return directions.get(idx.y).get(idx.x);
	}

	public void setDir(Vector idx, Vector dir) {
		directions.get(idx.y).set(idx.x, dir);
	}

	public double xPosition(Vector idx) {
		return idx.x;
	}

	public double yPosition(Vector idx) {
		return idx.y;
	}

	public boolean inBound(Vector idx){
		return 0 <= idx.x && idx.x < width && 0 <= idx.y && idx.y < height;
	}

	public boolean onBound(Vector idx){
		return inBound(idx) && (0 == idx.x || idx.x == width - 1 || 0 == idx.y || idx.y == height-1);
	}

	protected double getDist2Between(Vector idx1, Vector idx2) {
		double dx = Math.abs(idx1.x - idx2.x);
		double dy = Math.abs(idx1.y -idx2.y);
    return dx*dx + dy*dy;
	}

	protected List<Vector> getAllDirs() {
		return new ArrayList<>(List.of(new Vector(-1, 0), new Vector(0, -1),
			new Vector(1, 0),  new Vector(0, 1)));
	}

	protected List<Vector> getValidNeighbours(Vector idx) {
		return getAllDirs().stream().map(idx::plus).filter(this::inBound).toList();
	}

	protected List<Vector> getDiagonalDirs(){
		return new ArrayList<>(List.of(new Vector(-1, 0), new Vector(-1, -1), new Vector(0, -1),
			new Vector(1, -1), new Vector(1, 0), new Vector(1, 1), new Vector(0, 1), new Vector(-1, 1)));
	}

	protected List<Vector> getAllNeighbours(Vector idx){
		return getDiagonalDirs().stream().map(idx::plus).toList();
	}

	public Vector getRandomPos() {
    return new Vector(rand.nextInt(width - 1), rand.nextInt(height - 1));
	}

	protected List<Vector> getChildren(Vector idx) {
		List<Vector> ch = new ArrayList<>();
		for (Vector dir : getAllDirs()) {
			Vector n = idx.plus(dir);
			if (inBound(n) && getDir(n).equals(dir.neg())) {
				ch.add(n);
			}
		}
		return ch;
	}

}

package model;

import java.util.ArrayList;
import java.util.List;

import controller.RoomFinder;

public class RectLab extends Labyrinth {
	private List<List<Vector>> directions;

	public RectLab(int w, int h, double p, RoomFinder rf) {
		super(w, h, p, rf);
		setRoot(new Vector(0, 0));
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

	private List<Vector> getAllDirs() {
		return new ArrayList<>(List.of(new Vector(-1, 0), new Vector(0, -1),
			new Vector(1, 0),  new Vector(0, 1)));
	}

	private List<Vector> getDiagonalDirs(){
		return new ArrayList<>(List.of(new Vector(-1, 0), new Vector(-1, -1), new Vector(0, -1),
			new Vector(1, -1), new Vector(1, 0), new Vector(1, 1), new Vector(0, 1), new Vector(-1, 1)));
	}

	public double getDist2Between(Vector idx1, Vector idx2) {
		double dx = Math.abs(idx1.getX() - idx2.getX());
		double dy = Math.abs(idx1.getY() -idx2.getY());
		return dx*dx + dy*dy;
	}

	public List<Vector> getAllNeighbours(Vector idx){
		return getDiagonalDirs().stream().map(idx::plus).toList();
	}

	public List<Vector> getChildren(Vector idx) {
		List<Vector> ch = new ArrayList<>();
		for (Vector dir : getAllDirs()) {
			Vector n = idx.plus(dir);
			if (inBound(n) && getDir(n).equals(dir.neg())) {
				ch.add(n);
			}
		}
		return ch;
	}

	public List<Vector> getValidNeighbours(Vector idx) {
		return getAllDirs().stream().map(idx::plus).filter(this::inBound).toList();
	}

	public Vector getDir(Vector idx) {
		return directions.get(idx.getY()).get(idx.getX());
	}

	public void setDir(Vector idx, Vector dir) {
		directions.get(idx.getY()).set(idx.getX(), dir);
	}

	public boolean inBound(Vector idx){
		return 0 <= idx.getX() && idx.getX() < getWidth() && 0 <= idx.getY() && idx.getY() < getHeight();
	}

	public boolean onBound(Vector idx){
		return inBound(idx) && (0 == idx.getX() || idx.getX() == getWidth() - 1 || 0 == idx.getY() || idx.getY() == getHeight()-1);
	}

	public Vector getRandomPos() {
		return new Vector(getRand().nextInt(getWidth() - 1), getRand().nextInt(getHeight() - 1));
	}

	public double xPosition(Vector idx) {
		return idx.getX();
	}

	public double yPosition(Vector idx) {
		return idx.getY();
	}

	public Vector posToVec(double x, double y){
		return new Vector((int)Math.round(x), (int)Math.round(y));
	}

	public List<double[]> getNodePoly(Vector idx) {
		//rotate the directions to get the nodes of the polygon, and scale it down
		return getAllDirs().stream().map(v -> new double[]{
			(1 - getPadding()) * 0.707106781 * (v.getX() * 0.707106781 - v.getY() * 0.707106781) + xPosition(idx),
			(1 - getPadding()) * 0.707106781 * (v.getX() * 0.707106781 + v.getY() * 0.707106781) + yPosition(idx)
		}).toList();
	}

}

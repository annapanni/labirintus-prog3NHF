package model;

import java.util.ArrayList;
import java.util.List;

import controller.RoomFinder;

public class HexaLab extends Labyrinth {
	private int labHeight2;
	private int labWidth;
	private List<List<Vector>> directions;

	public HexaLab(int w, int h, double p, RoomFinder rf) {
		super(w, h, p, rf);
		setRoot(new Vector(w - 1, 0));
		labHeight2 = (h - 1) / 2;
		labWidth = w - labHeight2;
		//initialize directions
		directions = new ArrayList<>();
		List<Vector> firstRow = new ArrayList<>();
		for (int x=0; x < w-1; x++) {firstRow.add(new Vector(1, 0));}
		firstRow.add(new Vector(0, 0));
		directions.add(firstRow);
		for (int y=1; y<h; y++) {
			List<Vector> row = new ArrayList<>();
			for (int x=0; x < w; x++) {row.add(new Vector(0, -1));}
			directions.add(row);
		}
		for (int x=0; x<labHeight2; x++) {
			directions.get(labHeight2 - x).set(x, new Vector(1, -1));
		}
	}

	private Vector axialRounded(double x, double y){
		long q = Math.round(x);
		long r = Math.round(y);
		long s = Math.round(-x-y);
		if (Math.abs(q - x) > Math.abs(r - y) && Math.abs(q - x) > Math.abs(s + x + y)){
			q = -r -s;
		} else if (Math.abs(r - y) > Math.abs(s + x + y)) {
			r = -q-s;
		}
		return new Vector((int)q, (int)r);
	}

	private List<Vector> getAllDirs() {
		return new ArrayList<>(List.of(new Vector(-1, 0), new Vector(0, -1),
			new Vector(1, -1), new Vector(1, 0),  new Vector(0, 1), new Vector(-1, 1)));
	}

	public double getDist2Between(Vector idx1, Vector idx2) {
		double dx = Math.abs(idx1.getX() - idx2.getX());
		double dy = Math.abs(idx1.getY() -idx2.getY());
		return (dx + dy/2)*(dx + dy/2) + 3 * (dy/2)*(dy/2);
	}

	public List<Vector> getAllNeighbours(Vector idx){
		return getAllDirs().stream().map(idx::plus).toList();
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
		return 0 <= idx.getX() && idx.getX() < getWidth() && 0 <= idx.getY() && idx.getY() < labHeight2*2 + 1
			&& idx.getX() + idx.getY() >= labHeight2 && idx.getX() + idx.getY() <= labHeight2*2 + labWidth - 1;
	}

	public boolean onBound(Vector idx){
		return inBound(idx) && (0 == idx.getX() || idx.getX() == getWidth() - 1 || 0 == idx.getY() || idx.getY() == labHeight2*2
			|| idx.getX() + idx.getY() == labHeight2 || idx.getX() + idx.getY() == labHeight2*2 + labWidth - 1);
	}

	public Vector getRandomPos() {
		int maxIter = 100;
		Vector pos = new Vector(getRand().nextInt(getWidth() - 1), getRand().nextInt(getHeight() - 1));
		int i = 0;
		while (! inBound(pos) && i < maxIter){
				pos = new Vector(getRand().nextInt(getWidth() - 1), getRand().nextInt(getHeight() - 1));
				i++;
		}
		if (i == maxIter){
			throw new RuntimeException("Timeout, couldn't find random pos");
		}
		return pos;
	}

	public double xPosition(Vector idx) {
		return idx.getX()+ (double)idx.getY()/2;
	}

	public double yPosition(Vector idx) {
		return idx.getY() * 0.86602540378;
	}

	public Vector posToVec(double x, double y){
		return axialRounded(x - y / 0.86602540378 / 2.0, y / 0.86602540378);
	}

	public List<double[]> getNodePoly(Vector idx) {
		//rotate the directions to get the nodes of the polygon, and scale it down
		return getAllDirs().stream().map(v -> new double[]{
			(1 - getPadding()) * 0.5 / 0.86602540378 * (xPosition(v) * 0.86602540378 - yPosition(v) * 0.5) + xPosition(idx),
			(1 - getPadding()) * 0.5 / 0.86602540378 * (xPosition(v) * 0.5 + yPosition(v) * 0.86602540378) + yPosition(idx)
		}).toList();
	}

}

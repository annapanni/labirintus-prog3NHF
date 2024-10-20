package model;

import java.util.ArrayList;
import java.util.List;


public class HexaLab extends Labyrinth {
	private int labHeight2;
	private int labWidth;
	private List<List<Vector>> directions;

	public HexaLab(int w, int h) {
		super(w, h);
		root = new Vector(w - 1, 0);
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

	public Vector getDir(Vector idx) {
		return directions.get(idx.y).get(idx.x);
	}

	public void setDir(Vector idx, Vector dir) {
		directions.get(idx.y).set(idx.x, dir);
	}

	public double xPosition(Vector idx) {
		return idx.x + (double)idx.y/2;
	}

	public double yPosition(Vector idx) {
		return (double)idx.y * 0.8660254;
	}

	public boolean inBound(Vector idx){
		return 0 <= idx.x && idx.x < width && 0 <= idx.y && idx.y < labHeight2*2 + 1
		 	&& idx.x + idx.y >= labHeight2 && idx.x + idx.y <= labHeight2*2 + labWidth - 1;
	}

	protected double getDist2Between(Vector idx1, Vector idx2) {
		double dx = Math.abs(idx1.x - idx2.x);
		double dy = Math.abs(idx1.y -idx2.y);
    return (dx + dy/2)*(dx + dy/2) + 3 * (dy/2)*(dy/2);
	}

	protected List<Vector> getAllDirs() {
		return new ArrayList<>(List.of(new Vector(-1, 0), new Vector(0, -1),
			new Vector(1, -1), new Vector(1, 0),  new Vector(0, 1), new Vector(-1, 1)));
	}

	protected List<Vector> getValidNeighbours(Vector idx) {
		return getAllDirs().stream().map(idx::plus).filter(this::inBound).toList();
	}

	protected List<Vector> getAllNeighbours(Vector idx){
		List<Vector> dirs = new ArrayList<>(List.of(new Vector(-1, 0), new Vector(0, -1),
			new Vector(1, -1), new Vector(1, 0), new Vector(0, 1), new Vector(-1, 1)));
		return dirs.stream().map(idx::plus).toList();
	}

	public Vector getRandomPos() {
		int maxIter = 100;
		Vector pos = new Vector(rand.nextInt(width - 1), rand.nextInt(height - 1));
    int i = 0;
    while (! inBound(pos) && i < maxIter){
        pos = new Vector(rand.nextInt(width - 1), rand.nextInt(height - 1));
        i++;
		}
    if (i == maxIter){
			System.out.println("Timeout, couldn't find random pos"); // -------- should throw
			return new Vector(0,0);
		}
    return pos;
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

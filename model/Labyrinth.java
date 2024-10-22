package model;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public abstract class Labyrinth {
	protected Vector root;
	protected int width;
	protected int height;
	protected Random rand;
	protected List<Room> rooms;
	protected RoomFinder roomfinder;

	public Labyrinth(int w, int h, RoomFinder rf) {
		width = w;
		height = h;
		rand = new Random();
		roomfinder = rf;
		rooms = new ArrayList<>();
	}

	public int getWidth(){return width;}
	public int getHeight(){return height;}
	public List<Room> getRooms(){return rooms;}

	public abstract Vector getRandomPos();
 	public abstract Vector getDir(Vector idx);
	public abstract void setDir(Vector idx, Vector dir);
	public abstract double xPosition(Vector idx);
	public abstract double yPosition(Vector idx);
	public abstract boolean inBound(Vector idx);
	public abstract boolean onBound(Vector idx);

	protected abstract double getDist2Between(Vector idx1, Vector idx2);
	protected abstract List<Vector> getValidNeighbours(Vector idx);
	protected abstract List<Vector> getAllNeighbours(Vector idx);
	protected abstract List<Vector> getChildren(Vector idx);

	public Room inWhichRoom(Vector idx) {
		for (Room r : rooms) {
			if (r.idxInRoom(idx)) {
				return r;
			}
		}
		return null;
	}

	public boolean inAnyRoom(Vector idx) {
		return inWhichRoom(idx) != null;
	}

	public void coverWithRooms() {
		rooms = new ArrayList<Room>();
		for (int x = 0; x < width; x++){
			for (int y = 0; y < width; y++){
				Vector idx = new Vector(x, y);
				if (inAnyRoom(idx) || ! inBound(idx)) {continue;}
				Room nroom = roomfinder.findRoomAt(idx, this);
				if (nroom.size() >= 2) {
					rooms.add(nroom);
				}
			}
		}
	}

	public Vector getDir(int x, int y){
		return getDir(new Vector(x, y));
	}
	public void setDir(int x, int y, Vector dir){
		setDir(new Vector(x, y), dir);
	}

	protected void rerootTo(Vector idx){
		Vector currentIdx = idx.clone();
		Vector prevDir = new Vector(0, 0);
		while (! currentIdx.equals(root)) {
			Vector currentDir = getDir(currentIdx);
			setDir(currentIdx, prevDir.neg());
			currentIdx = currentIdx.plus(currentDir);
			prevDir = currentDir;
		}
		setDir(currentIdx, prevDir.neg());
		root = idx;
	}

	public void changeNTimesFrom(int n, Vector changeAt, boolean keepRoot) {
		Vector ogRoot = null;
		if (keepRoot) {
			ogRoot = root;
		}
		rerootTo(changeAt);
		for (int i=0; i < n; i++) {
			List<Vector> neighbours = getValidNeighbours(root);
      Vector nroot = neighbours.get(rand.nextInt(neighbours.size()));
      setDir(root, nroot.plus(root.neg()));
      setDir(nroot, new Vector(0,0));
      root = nroot;
		}
		if (keepRoot) {
			root = ogRoot;
		}
	}

	public void changeNTimes(int n){
		for (int i=0; i<n; i++){
			changeNTimesFrom(1, getRandomPos(), false);
		}
	}

	public List<Vector> findRoute(Vector from, Vector to){
		rerootTo(to);
		List<Vector> route = new LinkedList<>();
		route.add(from);
		while (! from.equals(to)){
			Vector dir = getDir(from);
			from = from.plus(dir);
			route.add(from);
		}
		List<Room> inRoom = route.stream().map(this::inWhichRoom).toList();
		List<Vector> toRM = new ArrayList<>();
		for (int i = 0; i < route.size();i++) { //i gets modified elsewhere too!
			if (inRoom.get(i) == null) {
				continue;
			}
			int lastIdx = inRoom.lastIndexOf(inRoom.get(i));
			if (i != lastIdx) {
				for (int j = i+1; j < lastIdx; j++){
					toRM.add(route.get(j));
				}
				i = lastIdx;
			}
		}
		route.removeAll(toRM);
		return route;
	}

}

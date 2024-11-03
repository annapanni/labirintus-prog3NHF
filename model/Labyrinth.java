package model;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import controller.RoomFinder;

public abstract class Labyrinth implements java.io.Serializable {
	private Vector root;
	private int width;
	private int height;
	private double padding;
	private List<Room> rooms;
	private RoomFinder roomfinder;

	protected static Random rand = new Random();

	public Vector getRoot(){return root;}
	public void setRoot(Vector nr){root = nr;}
	protected Random getRand() {return rand;}

	public List<Room> getRooms() {return rooms;}
	public void setRooms(List<Room> r) {rooms = r;}
	public int getWidth(){return width;}
	public int getHeight(){return height;}
	public double getPadding() {return padding;}
	public RoomFinder getRoomfinder() {return roomfinder;}

	protected Labyrinth(int w, int h, double p, RoomFinder rf) {
		width = w;
		height = h;
		padding = p;
		rooms = new ArrayList<>();
		roomfinder = rf;
	}

	public abstract double getDist2Between(Vector idx1, Vector idx2);
	public abstract List<Vector> getAllNeighbours(Vector idx);
	public abstract List<Vector> getChildren(Vector idx);
	public abstract List<Vector> getValidNeighbours(Vector idx);
 	public abstract Vector getDir(Vector idx);
	public abstract void setDir(Vector idx, Vector dir);
	public abstract boolean inBound(Vector idx);
	public abstract boolean onBound(Vector idx);
	public abstract Vector getRandomPos();
	public abstract double xPosition(Vector idx);
	public abstract double yPosition(Vector idx);
	public abstract Vector posToVec(double x, double y);
	public abstract List<double[]> getNodePoly(Vector idx);

	public Vector lastValidIdx(){
		int i = width * height - 1;
		while (! inBound(new Vector(i / width, i % width))) {i--;}
		return new Vector(i / width, i % width);
	}

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

	public void rerootTo(Vector idx){
		Vector currentIdx = idx;
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

	public List<Vector> getRoute(Vector from, Vector to){
		rerootTo(to);
		List<Vector> route = new LinkedList<>();
		route.add(from);
		while (! from.equals(to)){
			Vector dir = getDir(from);
			from = from.plus(dir);
			route.add(from);
		}
		List<Room> inRoom = route.stream().map(this::inWhichRoom).toList();
		int i = 0;
		while (i < route.size()) { //i gets modified elsewhere too!
			if (inRoom.get(i) == null) {
				i++;
				continue;
			}
			int lastIdx = inRoom.lastIndexOf(inRoom.get(i));
			if (i != lastIdx) {
				Vector lastCell = route.get(lastIdx);
				for (int j = i+1; j < lastIdx; j++){
					route.remove(i+1);
				}
				List<Vector> roomPath = GraphUtils.pathTo(inRoom.get(i)::idxInRoom, this::getValidNeighbours, route.get(i), lastCell);
				route.addAll(i+1, roomPath.subList(1, roomPath.size()-1));
				inRoom = route.stream().map(this::inWhichRoom).toList();
				i = inRoom.lastIndexOf(inWhichRoom(lastCell));//lastIdx may have changed
			}
			i++;
		}
		return route;
	}

	public List<Vector> inReachOf(double x, double y){
		Vector inside = posToVec(x, y);
		List<Vector> reach = new LinkedList<>();
		for (Vector n : getAllNeighbours(inside)) {
			double tx = (x - xPosition(n)) / (1.0 + padding) + xPosition(n);
			double ty = (y - yPosition(n)) / (1.0 + padding) + yPosition(n);
			if (posToVec(tx, ty).equals(n)){
				reach.add(n);
			}
		}
		return reach;
	}

}

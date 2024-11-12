package labyrinth.model;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

/**Represents any kind of labyrinth, where the corridors are determined by a directed tree, 
 * so it is always traversable. The labyrinth may also contain rooms, which can allow further 
 * access betwee to neighbouring nodes.
 */
public abstract class Labyrinth implements java.io.Serializable {
	private Vector root;
	private int width;
	private int height;
	private double padding;
	private List<Room> rooms;
	private RoomFinder roomfinder;

	protected static Random rand = new Random();
	protected static Random getRand() {return rand;}

	public Vector getRoot(){return root;}
	public void setRoot(Vector nr){root = nr;}
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

	/**
     * Calculates the squared distance between two points on the hexagonal grid.
     * @param idx1 the first vector
     * @param idx2 the second vector
     * @return the squared distance between the two points
     */
	public abstract double getDist2Between(Vector idx1, Vector idx2);
	/**Returns a list of all neighboring positions for a given vector index in clockwise order*/
	public abstract List<Vector> getAllNeighbours(Vector idx);
	/**
	 * Returns the list of its children nodes (nodes that are its not seperated from by walls
	 * and are not its parent node)
	 */
	public abstract List<Vector> getChildren(Vector idx);
	/** Returns a list of valid neighboring nodes that are within the bounds of the labyrinth.*/
	public abstract List<Vector> getValidNeighbours(Vector idx);
 	/**Returns which way is a cerain node's parent (offset of the node and its parent)*/
	public abstract Vector getDir(Vector idx);
	/**Set which way is a cerain node's parent is*/
	public abstract void setDir(Vector idx, Vector dir);
	/** Checks if a given vector index is within the bounds of the labyrinth.*/
	public abstract boolean inBound(Vector idx);
	/**Checks if a given vector index is on the boundary of the labyrinth.*/
	public abstract boolean onBound(Vector idx);
	/**Generates a random position within the bounds of the labyrinth.*/
	public abstract Vector getRandomPos();
	/**Calculates the x-coordinate of a position of a node depending.*/
	public abstract double xPosition(Vector idx);
	/**Calculates the y-coordinate of a position of a node depending.*/
	public abstract double yPosition(Vector idx);
	/** Converts a labyrinth position to the nearest node on the grid.*/
	public abstract Vector posToVec(double x, double y);
	/**Generate the polygon by which a certain node is represented by*/
	public abstract List<double[]> getNodePoly(Vector idx);
	
	/**Determines in which room a node is in, null if it is not in any*/
	protected Room inWhichRoom(Vector idx) {
		for (Room r : rooms) {
			if (r.idxInRoom(idx)) {
				return r;
			}
		}
		return null;
	}

	/**
	 * Determines the nodes that a certain point is in reach of. Meaning, if the padding between 
	 * cells is greater than 0, there are areas on the map that can belong to multiple nodes
	 * at the same time. This function returns all of these nodes.
	 * */
	protected List<Vector> inReachOf(double x, double y){
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

	/**Returns the last valid index of the labyrinth, last meaning the furthest down and right*/
	public Vector lastValidIdx(){
		int i = width * height - 1;
		while (! inBound(new Vector(i % width, i / width))) {i--;}
		return new Vector(i % width, i / width);
	}
	/**Determines if the node is in any room in the labyrinth*/
	public boolean inAnyRoom(Vector idx) {
		return inWhichRoom(idx) != null;
	}

	/**Change the root of the labyrinth to a certain node */
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

	/**
	 * Find a route between two nodes. The route in not guaranteed to be the shortest. 
	 * @return list of nodes in the route, both ends included
	 */
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

}

package labyrinth.controller;

import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import labyrinth.model.*;

/**
 * Controller class responsible for editing and managing the state of a labyrinth.
 */
public class LabEditControl {
	private LabState labState;

	public void setLabState(LabState ls) {labState = ls;}

	private static Random rand = new Random();

	/**
     * Constructs a new LabEditControl for the given labyrinth state.
     * @param laby the labyrinth state to be controlled
     */
	public LabEditControl(LabState laby) {
		labState = laby;
	}

	/**
     * Generates a new labyrinth by performing random modifications and adding rooms.
     * @param lab the labyrinth to modify
     * @return a new LabState representing the modified labyrinth
     */
	public static LabState generateLabyrinth(Labyrinth lab) {
		changeNTimes(lab, lab.getWidth() * lab.getHeight() * 10);
		coverWithRooms(lab, lab.getRoomfinder());
		return new LabState(lab, 3, 5);
	}

	/**
     * Performs a series of random changes to the labyrinth, starting from a specified position.
	 * All changes preserve the tree-like property of the labyrinth's corridors.
     * @param lab the labyrinth to modify
     * @param n the number of changes to perform
     * @param changeAt the starting position for the changes
     * @param keepRoot whether to restore the original root position after changes
     */
	private static void changeNTimesFrom(Labyrinth lab, int n, Vector changeAt, boolean keepRoot) {
		Vector ogRoot = null;
		if (keepRoot) {
			ogRoot = lab.getRoot();
		}
		lab.rerootTo(changeAt);
		for (int i=0; i < n; i++) {
			List<Vector> neighbours = lab.getValidNeighbours(lab.getRoot());
      Vector nroot = neighbours.get(rand.nextInt(neighbours.size()));
      lab.setDir(lab.getRoot(), nroot.plus(lab.getRoot().neg()));
      lab.setDir(nroot, new Vector(0,0));
      lab.setRoot(nroot);
		}
		if (keepRoot) {
			lab.rerootTo(ogRoot);
			lab.setRoot(ogRoot);
		}
	}

	/**
     * Performs random changes to the labyrinth a specified number of times.
	 * All changes preserve the tree-like property of the labyrinth's corridors.
	 * May change the root of the labyrinth.
     * @param lab the labyrinth to modify
     * @param n the number of changes to perform
     */
	private static void changeNTimes(Labyrinth lab, int n){
		for (int i=0; i<n; i++){
			changeNTimesFrom(lab, 1, lab.getRandomPos(), false);
		}
	}

	/**
     * Fills the labyrinth with rooms based on a given room-finding algorithm.
	 * May change the root of the labyrinth.
     * @param lab the labyrinth to modify
     * @param roomfinder the room-finding algorithm
     */
	private static void coverWithRooms(Labyrinth lab, RoomFinder roomfinder) {
		// so the rooms are located in smaller subtress therefore the size of the loops will as small as possible
		lab.rerootTo(lab.lastValidIdx());
		lab.setRooms(new ArrayList<>());
		for (int x = 0; x < lab.getWidth(); x++){
			for (int y = 0; y < lab.getHeight(); y++){
				Vector idx = new Vector(x, y);
				if (lab.inAnyRoom(idx) || ! lab.inBound(idx)) {continue;}
				Room nroom = roomfinder.findRoomAt(idx, lab);
				if (nroom.size() >= 2) {
					lab.getRooms().add(nroom);
				}
			}
		}
	}

	/**
     * Toggles the presence of a key at a specified position. If a key exists, it is removed;
     * otherwise, a new key is added.
     * @param x the x-coordinate of the key
     * @param y the y-coordinate of the key
     */
	public void addDeleteKey(double x, double y) {
		Vector vclick = labState.getLab().posToVec(x, y);
		if (! labState.getLab().inBound(vclick)) {return;}
		Optional<Key> optK = labState.getKeys().filter(k -> k.getInCell().equals(vclick)).findFirst();
		if (optK.isPresent()) {
			Key k = optK.get();
			labState.getItems().remove(k);
			labState.getObjects().remove(k);
		} else {
			Key k = new Key(labState.getLab(), vclick);
			k.setPosition(x, y);
			labState.getItems().add(k);
			labState.getObjects().add(k);
		}
	}

	/**
     * Toggles the presence of a brazier at a specified position. If a brazier exists, it is removed;
     * otherwise, a new brazier is added.
     * @param x the x-coordinate of the brazier
     * @param y the y-coordinate of the brazier
     */
	public void addDeleteBrazier(double x, double y) {
		Vector vclick = labState.getLab().posToVec(x, y);
		if (! labState.getLab().inBound(vclick)) {return;}
		Optional<Storable> optB = labState.getObjects().stream()
			.filter(b -> b.getInCell().equals(vclick) && b.getSprite() == ModelSprite.BRAZIER).findFirst();
		if (optB.isPresent()) {
			Storable b = optB.get();
			labState.getObjects().remove(b);
		} else {
			Storable b = Storable.brazier(labState.getLab(), vclick);
			b.setPosition(x, y);
			labState.getObjects().add(b);
		}
	}

	 /**
     * Toggles the presence of an map at a specified position. If an exit exists and there
     * is more than one map, it is removed; otherwise, a new map is added.
     * @param x the x-coordinate of the map
     * @param y the y-coordinate of the map
     */
	public void addDeleteMap(double x, double y) {
		Vector vclick = labState.getLab().posToVec(x, y);
		if (! labState.getLab().inBound(vclick)) {return;}
		Optional<MapPlan> optM = labState.getMaps().filter(m -> m.getInCell().equals(vclick)).findFirst();
		if (optM.isPresent()) {
			MapPlan m = optM.get();
			labState.getItems().remove(m);
			labState.getObjects().remove(m);
		} else {
			MapPlan m = new MapPlan(labState.getLab(), vclick);
			m.setPosition(x, y);
			labState.getItems().add(m);
			labState.getObjects().add(m);
		}
	}

	/**
	 * Set's the player's starting position to a specified point in the labyrinth
	 * @param x x coordinate of the new starting position
	 * @param y y coordinate of the new starting position
	 */
	public void chageStartPos(double x, double y) {
		Vector vclick = labState.getLab().posToVec(x, y);
		if (! labState.getLab().inBound(vclick)) {return;}
		labState.setStartPos(vclick);
		labState.getPlayer().setCell(vclick);
	}

	/**
     * Toggles the presence of an exit at a specified position. If an exit exists and there
     * is more than one exit, it is removed; otherwise, a new exit is added.
     * @param x the x-coordinate of the exit
     * @param y the y-coordinate of the exit
     */
	public void addDeleteExit(double x, double y) {
		Vector vclick = labState.getLab().posToVec(x, y);
		if (! labState.getLab().inBound(vclick)) {return;}
		Optional<Exit> optE = labState.getExits().filter(k -> k.getInCell().equals(vclick)).findFirst();
		if (optE.isPresent() && labState.getExits().count() > 1) {
			Exit e = optE.get();
			labState.getItems().remove(e);
			labState.getObjects().remove(e);
		} else {
			Exit e = new Exit(labState.getLab(), vclick);
			e.setPosition(x, y);
			labState.getItems().add(e);
			labState.getObjects().add(e);
		}
	}

	/**
     * Modifies the labyrinth's structure starting from the specified position.
     * @param x the x-coordinate of the position
     * @param y the y-coordinate of the position
     */
	public void changeLabAt(double x, double y) {
		Vector vclick = labState.getLab().posToVec(x, y);
		if (! labState.getLab().inBound(vclick)) {return;}
		changeNTimesFrom(labState.getLab(), 3, vclick, true);
		coverWithRooms(labState.getLab(), labState.getLab().getRoomfinder());
	}


}

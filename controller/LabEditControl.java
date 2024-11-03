package controller;

import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import model.*;

public class LabEditControl {
	private LabState labState;

	public void setLabState(LabState ls) {labState = ls;}

	private static Random rand = new Random();

	public LabEditControl(LabState laby) {
		labState = laby;
	}

	public static LabState generateLabyrinth(Labyrinth lab, RoomFinder roomfinder) {
		Storable exit = Storable.exit(lab, lab.getRandomPos());
		changeNTimes(lab, lab.getWidth() * lab.getHeight() * 10);
		coverWithRooms(lab, roomfinder);
		LabState labState = new LabState(lab, 3);
		return labState;
	}

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

	private static void changeNTimes(Labyrinth lab, int n){
		for (int i=0; i<n; i++){
			changeNTimesFrom(lab, 1, lab.getRandomPos(), false);
		}
	}

	private static void coverWithRooms(Labyrinth lab, RoomFinder roomfinder) {
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

	public void handleClick(double x, double y){
		Vector vclick = labState.getLab().posToVec(x, y);
		if (! labState.getLab().inBound(vclick)) {return;}
		Storable br = Storable.brazier(labState.getLab(), vclick);
		br.setPosition(x, y);
		labState.getObjects().add(br);
	}

	public void addDeleteKey(double x, double y) {
		Vector vclick = labState.getLab().posToVec(x, y);
		if (! labState.getLab().inBound(vclick)) {return;}
		Optional<Key> optK = labState.getKeys().stream().filter(k -> k.getInCell().equals(vclick)).findFirst();
		if (optK.isPresent()) {
			Key k = optK.get();
			labState.getKeys().remove(k);
			labState.getObjects().remove(k);
		} else {
			Key k = new Key(labState.getLab(), vclick);
			k.setPosition(x, y);
			labState.getKeys().add(k);
			labState.getObjects().add(k);
		}
	}

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

	public void chageStartPos(double x, double y) {
		Vector vclick = labState.getLab().posToVec(x, y);
		if (! labState.getLab().inBound(vclick)) {return;}
		labState.setStartPos(vclick);
		labState.getPlayer().setCell(vclick);
	}

	public void chageExit(double x, double y) {
		Vector vclick = labState.getLab().posToVec(x, y);
		if (! labState.getLab().inBound(vclick)) {return;}
		labState.getExit().setCell(vclick);
	}

	public void changeLabAt(double x, double y) {
		Vector vclick = labState.getLab().posToVec(x, y);
		if (! labState.getLab().inBound(vclick)) {return;}
		changeNTimesFrom(labState.getLab(), 3, vclick, true);
	}


}

package controller;

import java.util.Random;
import java.util.List;
import java.util.ArrayList;

import model.*;

public class LabEditControl {
	private LabState labState;

	private static Random rand = new Random();

	public LabEditControl(LabState laby) {
		labState = laby;
	}

	public static LabState generateLabyrinth(Labyrinth lab, RoomFinder roomfinder) {
		PlayerCharacter player = new PlayerCharacter(lab, new Vector(12, 12), 0.003);
		player.setLight(new Light(player, 3, 0.8, 0.0));
		changeNTimes(lab, lab.getWidth() * lab.getHeight() * 10);
		coverWithRooms(lab, roomfinder);
		LabState labState = new LabState(lab, player, 0.2);
		labState.getObjects().add(new Key(lab, new Vector(5, 5)));
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
		Storable key = new Key(labState.getLab(), vclick);
		key.setPosition(x, y);
		labState.getObjects().add(key);
	}

 	public void handleMouseMove(double x, double y) {
		/*double dx = (x - labState.getPlayer().getXPos()) / 10;
		double dy = (y - labState.getPlayer().getYPos()) / 10;
		double fi = Math.atan2(dy, dx);
		labState.getPlayer().setDir(fi);*/
	}

	public void step(){
		/*Iterator<Moving> it = toMove.iterator();
		while (it.hasNext()) {
			Moving m = it.next();
			boolean done = m.step(dTime);
			if (done) {
				//it.remove();
			}
		}*/
	}
}

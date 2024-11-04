package controller;

import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import java.util.Optional;

import model.*;

public class LabGameControl {
	private LabState labState;
	private List<Mover> toMove;
	private CharMover playerMover;
	private Vector routeFrom;
	private int dTime;
	private double mouseX;
	private double mouseY;

	public void switchLockedPos() {playerMover.switchLockedPos();}

	public void setLabState(LabState ls) {
		labState = ls;
		initMovers(ls.getObjects());
	}

	public LabGameControl(LabState laby, int dt) {
		labState = laby;
		dTime = dt;
		initMovers(laby.getObjects());
	}

	private void initMovers(List<Storable> objs) {
		toMove = new LinkedList<>();
		for (Storable obj : objs) {
			Mover mv = Mover.create(obj);
			if (mv != null) {
				toMove.add(mv);
				if (mv instanceof CharMover) playerMover = (CharMover)mv;
			}
			if (obj.getLight() != null) {
				toMove.add(Mover.create(obj.getLight()));
			}
		}
	}

	public void interactAt(double x, double y, Runnable onVictory){
		Vector vclick = labState.getLab().posToVec(x, y);
		if (! labState.getLab().inBound(vclick)) {return;}
		if (! labState.getPlayer().getInCell().equals(vclick)) {return;}
		Optional<Key> optK = labState.getKeys().stream()
			.filter(k -> !k.getCollected() && k.getInCell().equals(vclick)).findFirst();
		if (optK.isPresent()) {
			Key k = optK.get();
			k.setCollected(true);
			labState.getObjects().remove(k);
		}
		if (labState.getExit().getInCell().equals(vclick) && labState.getUncollectedKeyNum() == 0) {
			onVictory.run();
			playerMover.switchLockedPos();
		}
		if (labState.getMap().getInCell().equals(vclick)) {
			labState.setMapCollected(true);
			labState.getObjects().remove(labState.getMap());
		}
	}

	private void startFireflyTo(Vector to){
		if (labState.getFireflyNum() == labState.getUsedFireflyNum()) return;
		Firefly fly = new Firefly(labState.getLab(), labState.getPlayer().getInCell(), to, 0.003);
		labState.getObjects().add(fly);
		toMove.add(Mover.create(fly));
		toMove.add(Mover.create(fly.getLight()));
		labState.setUsedFireflyNum(labState.getUsedFireflyNum() + 1);
	}

	public void startFirefly(){
		Optional<Key> optK = labState.getKeys().stream().filter(k -> !k.getCollected()).findFirst();
		if (optK.isPresent()) {
			startFireflyTo(optK.get().getInCell());
			return;
		}
		startFireflyTo(labState.getExit().getInCell());
	}

 	public void setMousePos(double x, double y) {
		mouseX = x;
		mouseY = y;
	}

	public void step(){
		double dx = (mouseX - labState.getPlayer().getXPos()) / 10;
		double dy = (mouseY - labState.getPlayer().getYPos()) / 10;
		double fi = Math.atan2(dy, dx);
		labState.getPlayer().setDir(fi);
		Iterator<Mover> it = toMove.iterator();
		while (it.hasNext()) {
			Mover m = it.next();
			m.step(dTime);
		}
	}
}

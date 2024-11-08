package labyrinth.controller;

import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import java.util.Optional;

import labyrinth.model.*;

public class LabGameControl {
	private LabState labState;
	private List<Mover> toMove;
	private List<Interactable> interactors;
	private CharMover playerMover;
	private int dTime;
	private double mouseX;
	private double mouseY;

	public void switchLockedPos() {playerMover.switchLockedPos();}

	public void setLabState(LabState ls) {
		labState = ls;
		initMovers(ls.getObjects());
		initInteract(ls.getObjects());
	}

	public LabGameControl(LabState laby, int dt) {
		dTime = dt;
		setLabState(laby);
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

	private void initInteract(List<Storable> objs){
		interactors = new LinkedList<>();
		for (Storable obj : objs) {
			Interactable i = InteractFactory.create(obj);
			if (i != null) {
				interactors.add(i);
			}
		}
	}

	public Exit exitedOn() {
		Optional<Exit> exited = labState.getExits().filter(Item::getCollected).findFirst();
		if (exited.isPresent()) {
			return exited.get();
		} else {
			return null;
		}
	}

	public void interactAt(double x, double y){
		Vector vclick = labState.getLab().posToVec(x, y);
		if (! labState.getLab().inBound(vclick)) {return;}
		if (! labState.getPlayer().getInCell().equals(vclick)) {return;}
		for (Interactable i : interactors) {
			i.interact(labState, vclick);
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
		Optional<Key> optK = labState.getKeys().filter(k -> !k.getCollected()).findFirst();
		if (optK.isPresent()) {
			startFireflyTo(optK.get().getInCell());
			return;
		}
		startFireflyTo(labState.getExits().findFirst().get().getInCell());
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

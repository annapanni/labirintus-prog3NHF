package controller;

import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;

import model.*;

public class LabGameControl {
	private LabState labState;
	private List<Mover> toMove;
	private Vector routeFrom;
	private int dTime;
	private double mouseX;
	private double mouseY;

	public void setLabState(LabState ls) {labState = ls;}

	public LabGameControl(LabState laby, int dt) {
		labState = laby;
		dTime = dt;
		toMove = new LinkedList<>();
		for (Storable obj : labState.getObjects()) {
			Mover mv = Mover.create(obj);
			if (mv != null) {
				toMove.add(mv);
			}
			if (obj.getLight() != null) {
				toMove.add(Mover.create(obj.getLight()));
			}
		}
	}

	public void handleClick(double x, double y){
		Vector vclick = labState.getLab().posToVec(x, y);
		if (! labState.getLab().inBound(vclick)) {return;}
		if (routeFrom == null) {
			routeFrom = vclick;
		} else {
			Firefly fly = new Firefly(labState.getLab(), routeFrom, vclick, 0.003);
			labState.getObjects().add(fly);
			toMove.add(Mover.create(fly));
			toMove.add(Mover.create(fly.getLight()));
			routeFrom = null;
		}
	}

 	public void handleMouseMove(double x, double y) {
		mouseX = x;
		mouseY = y;
	}

	public void step(){
		Iterator<Mover> it = toMove.iterator();
		while (it.hasNext()) {
			Mover m = it.next();
			m.step(dTime);
		}
		double dx = (mouseX - labState.getPlayer().getXPos()) / 10;
		double dy = (mouseY - labState.getPlayer().getYPos()) / 10;
		double fi = Math.atan2(dy, dx);
		labState.getPlayer().setDir(fi);
	}
}

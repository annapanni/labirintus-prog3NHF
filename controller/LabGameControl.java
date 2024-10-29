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
			Light li = new Light(fly, 0.6, 0.3, 0.4, ModelColor.YELLOW);
			fly.setLight(li); //maybe somhow in firefly ctr
			labState.getObjects().add(fly);
			toMove.add(Mover.create(fly));
			toMove.add(Mover.create(li));
			routeFrom = null;
		}
	}

 	public void handleMouseMove(double x, double y) {
		double dx = (x - labState.getPlayer().getXPos()) / 10;
		double dy = (y - labState.getPlayer().getYPos()) / 10;
		double fi = Math.atan2(dy, dx);
		labState.getPlayer().setDir(fi);
	}

	public void step(){
		Iterator<Mover> it = toMove.iterator();
		while (it.hasNext()) {
			Mover m = it.next();
			m.step(dTime);
		}
	}
}

package controller;

import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;

import model.*;

public class LabControl {
	private LabState labState;
	private List<Moving> toMove;
	private Vector routeFrom;
	private int dTime;

	public LabControl(LabState laby, int dt) {
		labState = laby;
		dTime = dt;
		toMove = new LinkedList<>();
		for (Storable obj : labState.getObjects()) {
			if (obj instanceof Moving) {
				toMove.add((Moving)obj);
			}
			if (obj.getLight() != null) {
				toMove.add((Moving)obj.getLight());
			}
		}
	}

	public void handleClick(double x, double y){
		Vector vclick = labState.getLab().posToVec(x, y);
		if (! labState.getLab().inBound(vclick)) {return;}
		if (routeFrom == null) {
			routeFrom = vclick;
		} else {
			Storable fly = new Firefly(labState.getLab(), routeFrom, vclick, 0.003 * dTime);
			Light li = new Light(fly, 0.6, 0.3, 0.4, ModelColor.YELLOW);
			fly.setLight(li);
			labState.getObjects().add(fly);
			toMove.add((Moving)fly);
			toMove.add((Moving)li);
			routeFrom = null;
		}
	}

 	public void handleMouseMove(double x, double y) {
		double dx = (x - labState.getPlayer().getXPos()) / 10;
		double dy = (y - labState.getPlayer().getYPos()) / 10;
		double max = 0.003 * dTime;
		if (Math.sqrt(dx*dx + dy*dy) > max) {
			double q = max / Math.sqrt(dx*dx + dy*dy);
			dx = dx * q;
			dy = dy * q;
		}
		labState.getPlayer().setPosition(labState.getPlayer().getXPos() + dx, labState.getPlayer().getYPos() + dy);
	}

	public void step(){
		Iterator<Moving> it = toMove.iterator();
		while (it.hasNext()) {
			Moving m = it.next();
			boolean done = m.step();
			if (done) {
				//it.remove();
			}
		}
	}
}

package controller;

import java.util.Iterator;

import model.*;

public class LabControl {
	private LabState labState;
	private Vector routeFrom;
	private int dTime;

	public LabControl(LabState laby, int dt) {
		labState = laby;
		dTime = dt;
	}

	public void handleClick(double x, double y){
		Vector vclick = labState.getLab().posToVec(x, y);
		if (! labState.getLab().inBound(vclick)) {return;}
		if (routeFrom == null) {
			routeFrom = vclick;
		} else {
			Storable fly = new Firefly(labState.getLab(), routeFrom, vclick, 0.003 * dTime);
			Light li = new Light(fly, 0.8, 0.3, ModelColor.YELLOW);
			fly.setLight(li);
			labState.getObjects().add(fly);
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
		Iterator<Storable> it = labState.getObjects().iterator();
		while (it.hasNext()) {
			Storable obj = it.next();
			if (obj instanceof Moving) {
				Moving mov = (Moving)obj;
				boolean done = mov.step();
				if (done) {
					it.remove();
				}
			}
		}
	}
}

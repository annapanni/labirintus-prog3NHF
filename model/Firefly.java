package model;

import java.util.List;
import java.util.Random;

public class Firefly extends Storable implements Moving {
	private List<Vector> route;
	private double stepDist;
	private double xAnchor;
	private double yAnchor;
	private double fi;
	private Random rand;

	public Firefly(Labyrinth lab, Vector from, Vector to, double s) {
		super(lab, lab.findRoute(from, to).get(0));
		route = lab.findRoute(from, to);
		route.remove(0);
		stepDist = s;
		xAnchor = getXPos();
		yAnchor = getYPos();
		rand = new Random();
	}

	public boolean step() {
		if (route.isEmpty()) {
			return true;
		}
		double dx = getLab().xPosition(route.get(0)) - xAnchor;
		double dy = getLab().yPosition(route.get(0)) - yAnchor;
		double scale = stepDist / Math.sqrt(dx*dx + dy*dy);
		double xNoise = Math.cos(fi) * rand.nextDouble(0.05, 0.15);
		double yNoise = Math.sin(fi) * rand.nextDouble(0.05, 0.15);
		fi += 2 * Math.PI * stepDist;
		if (isValidPosition(xAnchor + dx*scale + xNoise, yAnchor + dy*scale + yNoise)){
			xAnchor = xAnchor + dx*scale;
			yAnchor = yAnchor + dy*scale;
			setPosition(xAnchor + dx*scale + xNoise, yAnchor + dy*scale + yNoise);
			double anchorOffsetX = getLab().xPosition(getInCell()) - xAnchor;
			double anchorOffsetY = getLab().yPosition(getInCell()) - yAnchor;
			if (getInCell().equals(route.get(0)) && Math.abs(anchorOffsetX) <= stepDist && Math.abs(anchorOffsetY) <= stepDist){
				route.remove(0);
			}
		}
		return false;
	}

}

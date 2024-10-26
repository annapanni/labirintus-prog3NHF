package model;

import java.util.List;
import java.util.Random;

public class Firefly extends Storable {
	private List<Vector> route;
	private double step;
	private double xAnchor;
	private double yAnchor;
	private double fi;
	private Random rand;

	public Firefly(Labyrinth lab, Vector from, Vector to, double s) {
		super(lab, lab.findRoute(from, to).get(0));
		route = lab.findRoute(from, to);
		route.remove(0);
		step = s;
		xAnchor = getXPos();
		yAnchor = getYPos();
		rand = new Random();
	}

	public boolean stepOne() {
		if (route.isEmpty()) {
			return false;
		}
		double dx = getLab().xPosition(route.get(0)) - xAnchor;
		double dy = getLab().yPosition(route.get(0)) - yAnchor;
		double scale = step / Math.sqrt(dx*dx + dy*dy);
		double xNoise = Math.cos(fi) * rand.nextDouble(0.05, 0.15);
		double yNoise = Math.sin(fi) * rand.nextDouble(0.05, 0.15);
		fi += 2 * Math.PI * step;
		if (isValidPosition(xAnchor + dx*scale + xNoise, yAnchor + dy*scale + yNoise)){
			xAnchor = xAnchor + dx*scale;
			yAnchor = yAnchor + dy*scale;
			setPosition(xAnchor + dx*scale + xNoise, yAnchor + dy*scale + yNoise);
			double anchorOffsetX = getLab().xPosition(getInCell()) - xAnchor;
			double anchorOffsetY = getLab().yPosition(getInCell()) - yAnchor;
			if (getInCell().equals(route.get(0)) && Math.abs(anchorOffsetX) <= step && Math.abs(anchorOffsetY) <= step){
				route.remove(0);
			}
		}
		return true;
	}

}

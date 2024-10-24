package model;

import java.util.List;
import java.util.Random;

public class Firefly extends Storable {
	List<Vector> route;
	double step;
	double xAnchor;
	double yAnchor;
	double fi;
	Random rand;

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
		double dx = lab.xPosition(route.get(0)) - xAnchor;
		double dy = lab.yPosition(route.get(0)) - yAnchor;
		double scale = step / Math.sqrt(dx*dx + dy*dy);
		double xNoise = Math.cos(fi) * rand.nextDouble(0.05, 0.15);
		double yNoise = Math.sin(fi) * rand.nextDouble(0.05, 0.15);
		fi += 2 * Math.PI * step;
		if (isValidPosition(xAnchor + dx*scale + xNoise, yAnchor + dy*scale + yNoise)){
			xAnchor = xAnchor + dx*scale;
			yAnchor = yAnchor + dy*scale;
			setPosition(xAnchor + dx*scale + xNoise, yAnchor + dy*scale + yNoise);
			double anchorOffsetX = lab.xPosition(inCell) - xAnchor;
			double anchorOffsetY = lab.yPosition(inCell) - yAnchor;
			if (getCell().equals(route.get(0)) && Math.abs(anchorOffsetX) <= step && Math.abs(anchorOffsetY) <= step){
				route.remove(0);
			}
		}
		return true;
	}

}

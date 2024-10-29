package model;

import java.util.List;

public class Firefly extends Storable implements Moving {
	private List<Vector> route;
	private double stepDist;
	private double xAnchor;
	private double yAnchor;
	private double fi;
	private double ogLightRad;

	public Firefly(Labyrinth lab, Vector from, Vector to, double s) {
		super(lab, lab.findRoute(from, to).get(0));
		route = lab.findRoute(from, to);
		route.remove(0);
		stepDist = s;
		xAnchor = getXPos();
		yAnchor = getYPos();
		setSprite(ModelSprite.FIREFLY);
	}

	public void setLight(Light li){
		super.setLight(li);
		ogLightRad = li.getRadius();
	}

	public boolean step(int dTime) {
		double thisStep = stepDist * dTime;
		boolean done = false;
		double xNoise = Math.cos(fi) * Storable.rand.nextDouble(0.05, 0.15);
		double yNoise = Math.sin(fi) * Storable.rand.nextDouble(0.05, 0.15);
		fi += 2 * Math.PI * thisStep;
		if (route.isEmpty()) {
			setPosition(xAnchor + xNoise, yAnchor + yNoise);
			done = true;
		} else {
			double dx = getLab().xPosition(route.get(0)) - xAnchor;
			double dy = getLab().yPosition(route.get(0)) - yAnchor;
			double scale =  thisStep / Math.sqrt(dx*dx + dy*dy);
			if (isValidPosition(xAnchor + dx*scale + xNoise, yAnchor + dy*scale + yNoise)){
				xAnchor = xAnchor + dx*scale;
				yAnchor = yAnchor + dy*scale;
				setPosition(xAnchor + dx*scale + xNoise, yAnchor + dy*scale + yNoise);
				double anchorOffsetX = getLab().xPosition(getInCell()) - xAnchor;
				double anchorOffsetY = getLab().yPosition(getInCell()) - yAnchor;
				if (getInCell().equals(route.get(0)) && Math.abs(anchorOffsetX) <= thisStep && Math.abs(anchorOffsetY) <= thisStep){
					route.remove(0);
				}
			}
			done = false;
		}
		return done;
	}

}

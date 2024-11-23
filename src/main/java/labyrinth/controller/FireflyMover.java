package labyrinth.controller;


import java.util.List;

import labyrinth.model.Firefly;
import labyrinth.model.Vector;

/**
 * Mover class responsible for moving fireflies in the labyrinth.
 */
public class FireflyMover extends Mover {
	private Firefly fly;
	private double xAnchor;
	private double yAnchor;
	private double fi;

	public FireflyMover(Firefly f) {
			fly = f;
			xAnchor = fly.getXPos();
			yAnchor = fly.getYPos();
	}

	@Override
	public void step(int dTime) {
		double thisStep = fly.getStepDist() * dTime;
		List<Vector> route = fly.getRoute();
		double xNoise = Math.cos(fi) * Mover.rand.nextDouble(0.05, 0.15);
		double yNoise = Math.sin(fi) * Mover.rand.nextDouble(0.05, 0.15);
		fi += 2 * Math.PI * thisStep;
		if (route.isEmpty()) {
			fly.setPosition(xAnchor + xNoise, yAnchor + yNoise);
		} else {
			double dx = fly.getLab().xPosition(route.get(0)) - xAnchor;
			double dy = fly.getLab().yPosition(route.get(0)) - yAnchor;
			double scale =  thisStep / Math.sqrt(dx*dx + dy*dy);
			if (fly.isValidPosition(xAnchor + dx*scale + xNoise, yAnchor + dy*scale + yNoise)){
				xAnchor = xAnchor + dx*scale;
				yAnchor = yAnchor + dy*scale;
				fly.setPosition(xAnchor + dx*scale + xNoise, yAnchor + dy*scale + yNoise);
				Vector cell = fly.getInCell();
				double anchorOffsetX = fly.getLab().xPosition(cell) - xAnchor;
				double anchorOffsetY = fly.getLab().yPosition(cell) - yAnchor;
				if (cell.equals(route.get(0)) && Math.abs(anchorOffsetX) <= thisStep && Math.abs(anchorOffsetY) <= thisStep){
					route.remove(0);
				}
			}
		}
	}

}

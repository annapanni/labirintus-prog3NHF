package labyrinth.controller;

import labyrinth.model.Light;

/**
 * Mover class responsible for moving the lights in the labyrinth.
 */
public class LightMover extends Mover {
	private Light li;
	private double ogRadius = Double.POSITIVE_INFINITY;

	public LightMover(Light l) {
			li = l;
			ogRadius = li.getRadius();
	}

	@Override
	public void step(int dTime){
		double fl = li.getFlicker();
		if (Mover.rand.nextDouble(0.0, 1.0) < fl * dTime/30.0) {
			double newRad = li.getRadius() + li.getRadius() * Mover.rand.nextDouble(-fl/4, fl/4);
			if (newRad < ogRadius * (1 + fl) && ogRadius * (1 - fl) < newRad) {
				li.setRadius(newRad);
			}
		}
	}


}

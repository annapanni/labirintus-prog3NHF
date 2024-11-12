package labyrinth.model;

import java.util.List;

/**
 * A Firefly object that can travel on a certain route
 */
public class Firefly extends Storable {
	private List<Vector> route;
	private double stepDist;

	/** Returns the distance the fireflies goes in one step*/
	public double getStepDist(){return stepDist;}
	/**Return the route that the firefly travels along */
	public List<Vector> getRoute(){return route;}

	/**
	 * Create a new Firefly object
	 * @param lab the labyrinth the firefly travels in
	 * @param from the index of the node it starts from
	 * @param to the index of the node it travels towards
	 * @param s the distance it can go each step
	 */
	public Firefly(Labyrinth lab, Vector from, Vector to, double s) {
		super(lab, lab.getRoute(from, to).get(0));
		route = lab.getRoute(from, to);
		route.remove(0);
		stepDist = s;
		setSprite(ModelSprite.FIREFLY);
		setLight(new Light(this, 0.6, 0.3, 0.4, ModelColor.FIREFLY));
	}
}

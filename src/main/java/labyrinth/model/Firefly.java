package labyrinth.model;

import java.util.List;

public class Firefly extends Storable {
	private List<Vector> route;
	private double stepDist;

	public double getStepDist(){return stepDist;}
	public List<Vector> getRoute(){return route;}

	public Firefly(Labyrinth lab, Vector from, Vector to, double s) {
		super(lab, lab.getRoute(from, to).get(0));
		route = lab.getRoute(from, to);
		route.remove(0);
		stepDist = s;
		setSprite(ModelSprite.FIREFLY);
		setLight(new Light(this, 0.6, 0.3, 0.4, ModelColor.FIREFLY));
	}
}

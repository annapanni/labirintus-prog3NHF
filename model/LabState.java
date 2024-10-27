package model;

import java.util.List;
import java.util.ArrayList;

public class LabState {
	private Labyrinth lab;
	private List<Storable> objects;
	private Storable player;
	private Light lineOfSight;
	private double darknessOpacity;

	public Labyrinth getLab(){return lab;};
	public List<Storable> getObjects() {return objects;}
	public Storable getPlayer() {return player;}
	public Light getLineOfSight() {return lineOfSight;}
	public double getdarknessOpacity() {return darknessOpacity;}

	public LabState(Labyrinth l, Storable pl, double dop, boolean lof) {
		lab = l;
		player = pl;
		darknessOpacity = dop;
		objects = new ArrayList<>();
		if (player != null) {
			objects.add(player);
			if (lof) {
				lineOfSight = new Light(player);
			}
		}
	}

	public LabState(Labyrinth l, Storable pl, double dop) {
		this(l, pl, dop, true);
	}
}

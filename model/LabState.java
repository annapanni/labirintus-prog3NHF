package model;

import java.util.List;
import java.util.ArrayList;

public class LabState {
	private Labyrinth lab;
	private List<Storable> objects;
	private Storable player;
	private Light sightRange;

	public Labyrinth getLab(){return lab;};
	public List<Storable> getObjects() {return objects;}
	public Storable getPlayer() {return player;}
	public Light getSightRange() {return sightRange;}

	public LabState(Labyrinth l, Storable pl) {
		lab = l;
		player = pl;
		objects = new ArrayList<>();
		if (player != null) {
			objects.add(player);
			sightRange = player.getLight();
		}
	}
}

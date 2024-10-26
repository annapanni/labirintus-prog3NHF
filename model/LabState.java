package model;

import java.util.List;
import java.util.ArrayList;

public class LabState {
	private Labyrinth lab;
	private List<Storable> objects;
	private Storable player;

	public Labyrinth getLab(){return lab;};
	public List<Storable> getObjects() {return objects;}
	public Storable getPlayer() {return player;}

	public LabState(Labyrinth l, Storable pl) {
		lab = l;
		player = pl;
		objects = new ArrayList<>();
		if (player != null) {objects.add(player);}
	}
}

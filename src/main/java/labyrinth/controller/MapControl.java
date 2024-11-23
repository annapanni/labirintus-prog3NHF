package labyrinth.controller;

import labyrinth.model.MapPlan;
import labyrinth.model.LabState;
import labyrinth.model.Vector;

/**
 * Class handling the player interactions with a given MapPlay instance.
 */
public class MapControl implements Interactable{
	private MapPlan map;

	public MapControl(MapPlan m) {map = m;}

	public void interact(LabState labState, Vector at) {
		if (! map.getInCell().equals(at)) return ;
		map.setCollected(true);
		if (labState.getObjects().contains(map)) labState.getObjects().remove(map);
		labState.setMapCollected(true);
	}
}

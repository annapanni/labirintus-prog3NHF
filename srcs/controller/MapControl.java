package controller;

import model.Map;
import model.LabState;
import model.Vector;

public class MapControl implements Interactable{
	private Map map;

	public MapControl(Map m) {map = m;}

	public void interact(LabState labState, Vector at) {
		if (! map.getInCell().equals(at)) return ;
		map.setCollected(true);
		if (labState.getObjects().contains(map)) labState.getObjects().remove(map);
		labState.setMapCollected(true);
	}
}

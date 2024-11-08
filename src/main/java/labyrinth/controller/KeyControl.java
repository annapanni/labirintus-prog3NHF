package labyrinth.controller;

import labyrinth.model.Key;
import labyrinth.model.LabState;
import labyrinth.model.Vector;

public class KeyControl implements Interactable{
	private Key k;

	public KeyControl(Key key) {k = key;}

	public void interact(LabState labState, Vector at) {
		if (! k.getInCell().equals(at)) return ;
		k.setCollected(true);
		if (labState.getObjects().contains(k)) labState.getObjects().remove(k);
	}
}

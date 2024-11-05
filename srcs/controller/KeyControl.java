package controller;

import model.Key;
import model.LabState;
import model.Vector;

public class KeyControl implements Interactable{
	private Key k;

	public KeyControl(Key key) {k = key;}

	public void interact(LabState labState, Vector at) {
		if (! k.getInCell().equals(at)) return ;
		k.setCollected(true);
		if (labState.getObjects().contains(k)) labState.getObjects().remove(k);
	}
}

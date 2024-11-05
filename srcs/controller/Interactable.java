package controller;

import model.LabState;
import model.Vector;

public interface Interactable {
	public void interact(LabState labState, Vector at);
}

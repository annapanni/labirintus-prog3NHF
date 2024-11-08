package labyrinth.controller;

import labyrinth.model.LabState;
import labyrinth.model.Vector;

public interface Interactable {
	public void interact(LabState labState, Vector at);
}

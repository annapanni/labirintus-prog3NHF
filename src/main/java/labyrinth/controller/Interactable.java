package labyrinth.controller;

import labyrinth.model.LabState;
import labyrinth.model.Vector;

/**
 * Interface for any object in the labyrinth that can be interacted with.
 */
public interface Interactable {
	/**
	 * Action that should be performed when the player interacts with the given object in a labyrinth at a given position
	 * @param labState labyrinth that the interaction occured in
	 * @param at position the interaction occured at
	 */
	public void interact(LabState labState, Vector at);
}

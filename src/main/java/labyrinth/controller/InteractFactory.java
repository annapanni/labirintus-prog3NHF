package labyrinth.controller;

import labyrinth.model.*;

/**
 * Factory class for classes implementing the Interactable interface
 */
public class InteractFactory {
	private InteractFactory(){}
	/**
	 * Returns a control class for a given Storable if it has one implementing the Interactable interface, otherwise null
	 */
	public static Interactable create(Storable s) {
		if (s instanceof Key) return new KeyControl((Key)s);
		if (s instanceof MapPlan) return new MapControl((MapPlan)s);
		if (s instanceof Exit) return new ExitControl((Exit)s);
		return null;
	}
}

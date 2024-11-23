package labyrinth.controller;

import java.util.Random;

import labyrinth.model.*;

/**
 * Abstract class responsible for moving objects in the labyrinth. Also serves as a factory for it's child classes.
 */
public abstract class Mover {
	protected static Random rand = new Random();

	/**Factory method creating a FireflyMover */
	public static Mover create(Firefly f) {return new FireflyMover(f);}
	/**Factory method creating a CharMover */
	public static Mover create(PlayerCharacter p) {return new CharMover(p);}
	/**Factory method creating a FireflyMover */
	public static Mover create(Light l) {return new LightMover(l);}
	/**Factory method creating a mover for a given Storable by dynamically casting, returns null if there is mover availbale for the given class */
	public static Mover create(Storable s) {
		if (s instanceof PlayerCharacter)
			return new CharMover((PlayerCharacter)s);
		if (s instanceof Firefly)
			return new FireflyMover((Firefly)s);
		return null;
	}
	/**
	 * Action that should be performed after a certain amount of time has passed
	 * @param dTime delta time in miliseconds
	 */
	public abstract void step(int dTime);
}

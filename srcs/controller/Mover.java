package controller;

import java.util.Random;

import model.*;

public class Mover {
	protected static Random rand = new Random();

	public static Mover create(Firefly f) {return new FireflyMover(f);}
	public static Mover create(PlayerCharacter p) {return new CharMover(p);}
	public static Mover create(Light l) {return new LightMover(l);}
	public static Mover create(Storable s) {
		if (s instanceof PlayerCharacter)
			return new CharMover((PlayerCharacter)s);
		if (s instanceof Firefly)
			return new FireflyMover((Firefly)s);
		return null;
	}

	public void step(int dTime) {}
}

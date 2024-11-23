package labyrinth.controller;

import labyrinth.model.PlayerCharacter;

/**
 * Mover class responsible for moving the player character in the labyrinth.
 */
public class CharMover extends Mover {
	private PlayerCharacter pl;
	private boolean lockedPos = false;

	public void switchLockedPos() {lockedPos = !lockedPos;}

	public CharMover(PlayerCharacter p) {
			pl = p;
	}

	@Override
	public void step(int dTime) {
		if (! lockedPos) {
			double dx = dTime * pl.getStepDist() * Math.cos(pl.getDir());
			double dy = dTime * pl.getStepDist() * Math.sin(pl.getDir());
			pl.setPosition(pl.getXPos() + dx, pl.getYPos() + dy);
		}
	}

}

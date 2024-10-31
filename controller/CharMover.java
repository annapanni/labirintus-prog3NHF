package controller;

import model.PlayerCharacter;;

public class CharMover extends Mover {
	private PlayerCharacter pl;

	public CharMover(PlayerCharacter p) {
			pl = p;
	}

	@Override
	public void step(int dTime) {
		double dx = dTime * pl.getStepDist() * Math.cos(pl.getDir());
		double dy = dTime * pl.getStepDist() * Math.sin(pl.getDir());
		pl.setPosition(pl.getXPos() + dx, pl.getYPos() + dy);
	}

}
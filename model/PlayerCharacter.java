package model;


public class PlayerCharacter extends Storable implements Moving {
	private double dir;
	private double stepDist;

	public PlayerCharacter(Labyrinth l, Vector idx, double stepD){
		super(l, idx);
		stepDist = stepD;
	}

	public double getDir() {return dir;}
	public void setDir(double d) {dir = d;}

	public boolean step(int dTime) {
		double dx = dTime * stepDist * Math.cos(dir);
		double dy = dTime * stepDist * Math.sin(dir);
		setPosition(getXPos() + dx, getYPos() + dy);
		return false;
	}

}

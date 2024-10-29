package model;


public class PlayerCharacter extends Storable {
	private double dir;
	private double stepDist;

	public PlayerCharacter(Labyrinth l, Vector idx, double stepD){
		super(l, idx);
		stepDist = stepD;
		setSprite(ModelSprite.CHARACTER);
	}

	public double getDir() {return dir;}
	public void setDir(double d) {dir = d;}
	public double getStepDist(){return stepDist;}

}

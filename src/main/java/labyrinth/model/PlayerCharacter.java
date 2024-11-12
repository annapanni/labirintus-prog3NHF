package labyrinth.model;

/**
 * Represents a player character in a labyrinth.
 */
public class PlayerCharacter extends Storable {
	private double dir;
	private double stepDist;

	/** Returns the direction the character is currently facing */
	public double getDir() {return dir;}
	/** Sets the direction the character is currently facing */
	public void setDir(double d) {dir = d;}
	/** Returns the distance the player can go in each step */
	public double getStepDist(){return stepDist;}

	/**
	 * Creates a new playe character in a labyrinth
	 * @param l the labyrinth where the character is in
	 * @param idx the node that the character is in
	 * @param stepD the distance it can go each step
	 */
	public PlayerCharacter(Labyrinth l, Vector idx, double stepD){
		super(l, idx);
		stepDist = stepD;
		setLight(new Light(this, 3.5, 0.3, 0.0, ModelColor.NONE));
		setSprite(ModelSprite.CHARACTER);
	}

}

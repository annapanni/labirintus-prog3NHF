package model;


public class Key extends Storable {
	private boolean collected;

	public boolean getCollected(){return collected;}
	public void setCollected(boolean b){collected = b;}

	public Key(Labyrinth l, Vector idx){
		super(l, idx);
		collected = false;
		setLight(new Light(this, 0.3, 0.1, 0.0, ModelColor.YELLOW));
		setSprite(ModelSprite.KEY);
	}
}

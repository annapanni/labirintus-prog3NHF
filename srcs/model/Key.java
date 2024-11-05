package model;


public class Key extends Item {
	public Key(Labyrinth l, Vector idx){
		super(l, idx);
		setLight(new Light(this, 0.3, 0.1, 0.0, ModelColor.YELLOW));
		setSprite(ModelSprite.KEY);
	}
}

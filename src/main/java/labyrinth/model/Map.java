package labyrinth.model;

public class Map extends Item {
	public Map(Labyrinth l, Vector idx){
		super(l, idx);
		setLight(new Light(this, 0.5, 0.1, 0.0, ModelColor.BLUE));
		setSprite(ModelSprite.MAP);
	}
}

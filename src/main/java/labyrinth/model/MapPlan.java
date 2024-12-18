package labyrinth.model;

/** Represents a paper map in the labyrinth, can be stored and collected */
public class MapPlan extends Item {
	public MapPlan(Labyrinth l, Vector idx){
		super(l, idx);
		setLight(new Light(this, 0.5, 0.1, 0.0, ModelColor.BLUE));
		setSprite(ModelSprite.MAP);
	}
}

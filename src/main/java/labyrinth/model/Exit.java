package labyrinth.model;


public class Exit extends Item {
	public Exit(Labyrinth l, Vector idx){
		super(l, idx);
		setSprite(ModelSprite.EXIT);
	}
}

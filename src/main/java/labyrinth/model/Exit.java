package labyrinth.model;

/**
 * Class to note the exits of the maze
 */
public class Exit extends Item {
	public Exit(Labyrinth l, Vector idx){
		super(l, idx);
		setSprite(ModelSprite.EXIT);
	}
}

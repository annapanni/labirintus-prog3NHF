package labyrinth.model;

/**Represents anything stored in the labyrinth that behaves like any item eg. can be collected */
public class Item extends Storable {
	private boolean collected;

	public boolean getCollected(){return collected;}
	public void setCollected(boolean b){collected = b;}

	public Item(Labyrinth l, Vector idx){
		super(l, idx);
		collected = false;
	}
}

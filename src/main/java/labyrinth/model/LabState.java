package labyrinth.model;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Stream;

/**
 * Represents the current state of the labyrinth game, including the labyrinth itself,
 * player state, items, and environmental properties.
 */
public class LabState implements java.io.Serializable {
	private Labyrinth lab;
	private List<Storable> objects;
	private List<Item> items;
	private PlayerCharacter player;
	private Vector startPos;
	private Light lineOfSight;
	private double darknessOpacity;
	private String name;
	private int fireflyNum;
	private int usedFireflyNum;
	private boolean mapCollected=false;

	/** Retrieves the labyrinth associated with this state.*/
	public Labyrinth getLab(){return lab;}
	/** Retrieves all stored objects in the game state.*/
	public List<Storable> getObjects() {return objects;}
	/** Retrieves all items in the game state. */
	public List<Item> getItems() {return items;}
	/** Returns the number of uncollected keys in the game. */
	public long getUncollectedKeyNum() {return items.stream().filter(k -> !k.getCollected() && k instanceof Key).count();}
	/** Returns a stream of all keys in the game. */
	public Stream<Key> getKeys() {return items.stream().filter(Key.class::isInstance).map(k -> (Key)k);}
	/** Returns a stream of all exits in the game.*/
	public Stream<Exit> getExits() {return items.stream().filter(Exit.class::isInstance).map(e -> (Exit)e);}
	/** Returns a stream of all (collected and uncollected) maps in the game.*/
	public Stream<MapPlan> getMaps() {return items.stream().filter(MapPlan.class::isInstance).map(m -> (MapPlan)m);}
	/** Retrieves the player character in the game state. */
	public PlayerCharacter getPlayer() {return player;}
	/** Retrieves the line of sight of the player */
	public Light getLineOfSight() {return lineOfSight;}
	/** Sets the line of sight light for the player. */
	public void setLineOfSight(boolean on) {lineOfSight = on ? new Light(player) : null;}
	/** Retrieves the current darkness opacity. */
	public double getdarknessOpacity() {return darknessOpacity;}
	/** Sets the darkness opacity level. */
	public void setDarknessOpacity(double op) {darknessOpacity = op;}
	/** Retrieves the name of this game state. */
	public String getName(){return name;}
	/**  Sets the name of this game state. */
	public void setName(String n){name = n;}
	/** Retrieves the starting position of the player. */
	public Vector getStartPos(){return startPos;}
	/** Sets the starting position of the player. */
	public void setStartPos(Vector s){startPos = s;}
	/** Retrieves the total number of fireflies available. */
	public int getFireflyNum(){return fireflyNum;}
	/** Sets the total number of fireflies available. */
	public void setFireflyNum(int n){fireflyNum = n;}
	/** Retrieves the number of used fireflies. */
	public int getUsedFireflyNum(){return usedFireflyNum;}
	/** Sets the number of used fireflies. */
	public void setUsedFireflyNum(int n){usedFireflyNum = n;}
	/** Returns whether the map has been collected. */
	public boolean getMapCollected(){return mapCollected;}
	/** Sets the map collected status. */
	public void setMapCollected(boolean b){mapCollected = b;}

	/**
     * Constructs a new game state with the specified labyrinth, key count, and firefly count.
     * @param l the labyrinth
     * @param kNum the number of keys to place in the labyrinth
     * @param fNum the number of fireflies available
     */
	public LabState(Labyrinth l, int kNum, int fNum) {
		lab = l;
		darknessOpacity = 1.0;
		objects = new ArrayList<>();
		startPos = l.getRandomPos();
		items = new ArrayList<>();
		for (int i=0; i<kNum; i++){
			Key k = new Key(l, l.getRandomPos());
			items.add(k);
			objects.add(k);
		}
		Exit exit = new Exit(l, l.getRandomPos());
		objects.add(exit);
		items.add(exit);
		MapPlan map = new MapPlan(l, l.getRandomPos());
		items.add(map);
		objects.add(map);
		fireflyNum = fNum;
		player = new PlayerCharacter(l, startPos, 0.002);
		objects.add(player);
		lineOfSight = new Light(player);
	}

	/**
	 * Sets labyrinth to its initial conditions eg. removes fireflies and adds uncollected keys again.
	 */
	public void toInitialConditions() {
		player.setCell(getStartPos());
		setUsedFireflyNum(0);
		objects.removeAll(objects.stream().filter(o -> !(o instanceof Firefly)).toList());
		items.stream().forEach(i -> {
			i.setCollected(false);
			if(! objects.contains(i)) objects.add(0,i);
		});
		setMapCollected(false);
	}
}

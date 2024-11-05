package model;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Stream;

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

	public Labyrinth getLab(){return lab;};
	public List<Storable> getObjects() {return objects;}
	public List<Item> getItems() {return items;}
	public long getUncollectedKeyNum() {return items.stream().filter(k -> !k.getCollected() && k instanceof Key).count();}
	public Stream<Key> getKeys() {return items.stream().filter(k -> k instanceof Key).map(k -> (Key)k);}
	public Stream<Exit> getExits() {return items.stream().filter(e -> e instanceof Exit).map(e -> (Exit)e);}
	public PlayerCharacter getPlayer() {return player;}
	public Light getLineOfSight() {return lineOfSight;}
	public void setLineOfSight(boolean on) {lineOfSight = on ? new Light(player) : null;}
	public double getdarknessOpacity() {return darknessOpacity;}
	public void setDarknessOpacity(double op) {darknessOpacity = op;}
	public String getName(){return name;}
	public void setName(String n){name = n;}
	public Vector getStartPos(){return startPos;}
	public void setStartPos(Vector s){startPos = s;}
	public int getFireflyNum(){return fireflyNum;}
	public void setFireflyNum(int n){fireflyNum = n;}
	public int getUsedFireflyNum(){return usedFireflyNum;}
	public void setUsedFireflyNum(int n){usedFireflyNum = n;}
	public boolean getMapCollected(){return mapCollected;}
	public void setMapCollected(boolean b){mapCollected = b;}

	public LabState(Labyrinth l, int kNum, int fNum) {
		lab = l;
		darknessOpacity = 1.0;
		objects = new ArrayList<>();
		startPos = l.getRandomPos();
		player = new PlayerCharacter(l, startPos, 0.002);
		objects.add(player);
		lineOfSight = new Light(player);
		items = new ArrayList<>();
		for (int i=0; i<kNum; i++){
			Key k = new Key(l, l.getRandomPos());
			items.add(k);
			objects.add(k);
		}
		Exit exit = new Exit(l, l.getRandomPos());
		objects.add(exit);
		items.add(exit);
		Map map = new Map(l, l.getRandomPos());
		items.add(map);
		objects.add(map);
		fireflyNum = fNum;
	}
}

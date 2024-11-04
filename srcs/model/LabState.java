package model;

import java.util.List;
import java.util.ArrayList;

public class LabState implements java.io.Serializable {
	private Labyrinth lab;
	private List<Storable> objects;
	private List<Key> keys;
	private PlayerCharacter player;
	private Storable exit;
	private Vector startPos;
	private Storable map;
	private Light lineOfSight;
	private double darknessOpacity;
	private String name;
	private int fireflyNum;
	private int usedFireflyNum;
	private boolean mapCollected=false;

	public Labyrinth getLab(){return lab;};
	public List<Storable> getObjects() {return objects;}
	public List<Key> getKeys() {return keys;}
	public long getUncollectedKeyNum() {return keys.stream().filter(k -> !k.getCollected()).count();}
	public PlayerCharacter getPlayer() {return player;}
	public Light getLineOfSight() {return lineOfSight;}
	public void setLineOfSight(boolean on) {lineOfSight = on ? new Light(player) : null;}
	public double getdarknessOpacity() {return darknessOpacity;}
	public void setDarknessOpacity(double op) {darknessOpacity = op;}
	public String getName(){return name;}
	public void setName(String n){name = n;}
	public Vector getStartPos(){return startPos;}
	public void setStartPos(Vector s){startPos = s;}
	public Storable getExit(){return exit;}
	public Storable getMap(){return map;}
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
		keys = new ArrayList<>();
		for (int i=0; i<kNum; i++){
			Key k = new Key(l, l.getRandomPos());
			keys.add(k);
			objects.add(k);
		}
		exit = Storable.exit(l, l.getRandomPos());
		objects.add(exit);
		map = Storable.map(l, l.getRandomPos());
		objects.add(map);
		fireflyNum = fNum;
	}
}

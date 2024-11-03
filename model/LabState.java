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
	private Light lineOfSight;
	private double darknessOpacity;
	private String name;
	private int fireflyNum;
	private int usedFireflyNum;

	public Labyrinth getLab(){return lab;};
	public List<Storable> getObjects() {return objects;}
	public List<Key> getKeys() {return keys;}
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
	public int getFireflyNum(){return fireflyNum;}
	public void setFireflyNum(int n){fireflyNum = n;}
	public void setUsedFireflyNum(int n){usedFireflyNum = n;}

	public LabState(Labyrinth l, int kNum) {
		lab = l;
		darknessOpacity = 1.0;
		objects = new ArrayList<>();
		startPos = l.getRandomPos();
		exit = Storable.exit(l, l.getRandomPos());
		objects.add(exit);
		player = new PlayerCharacter(l, startPos, 0.002);
		objects.add(player);
		lineOfSight = new Light(player);
		keys = new ArrayList<>();
		for (int i=0; i<kNum; i++){
			Key k = new Key(l, l.getRandomPos());
			keys.add(k);
			objects.add(k);
		}
	}
}

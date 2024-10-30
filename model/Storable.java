package model;

import java.util.List;


public class Storable implements java.io.Serializable  {
	private Labyrinth lab;
	private Vector inCell;
	private double xOffset;
	private double yOffset;
	private Light light;
	private ModelSprite sprite = ModelSprite.DEFAULT;

	public Labyrinth getLab(){return lab;}
	public Vector getInCell(){return inCell;}
	public Light getLight(){return light;}
	public void setLight(Light li){light = li;}
	public ModelSprite getSprite(){return sprite;}
	public void setSprite(ModelSprite s){sprite = s;}

	public Storable(Labyrinth l, Vector idx){
		lab = l;
		inCell = idx;
	}

	public double getXPos(){
		return lab.xPosition(inCell) + xOffset;
	}

	public double getYPos(){
		return lab.yPosition(inCell) + yOffset;
	}

	public boolean isValidPosition(double x, double y){
		Vector in = lab.posToVec(x, y);
		if (! lab.inBound(in)) {return false;}
		List<Vector> reach = lab.inReachOf(x, y);
		Room inRoom = lab.inWhichRoom(in);
		List<Vector> corridorTo = lab.getChildren(in);
		corridorTo.add(in.plus(lab.getDir(in))); // the parent
		for (Vector v : reach) {
			if (! (inRoom != null && inRoom.equals(lab.inWhichRoom(v)) || corridorTo.contains(v))) {
				return false;
			}
		}
		return true;
	}

	public void setPosition(double x, double y){
		if (isValidPosition(x, y)) {
			Vector in = lab.posToVec(x, y);
			double dx = x - lab.xPosition(in);
			double dy = y - lab.yPosition(in);
			inCell = in;
			xOffset = dx;
			yOffset = dy;
		}
	}

}

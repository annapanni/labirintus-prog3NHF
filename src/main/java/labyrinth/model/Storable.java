package labyrinth.model;

import java.util.List;

/**
 * Represents a storable object in the labyrinth in a given node with offset. It may emit light.
 */
public class Storable implements java.io.Serializable  {
	private Labyrinth lab;
	private Vector inCell;
	private double xOffset;
	private double yOffset;
	private Light light;
	private ModelSprite sprite = ModelSprite.DEFAULT;

	public Labyrinth getLab(){return lab;}
	/** Returns the node that the object is in */
	public Vector getInCell(){return inCell;}
	/** Returns the light object that it emits */
	public Light getLight(){return light;}
	protected void setLight(Light li){light = li;}
	/** Returns the sprite that represents this object */
	public ModelSprite getSprite(){return sprite;}
	protected void setSprite(ModelSprite s){sprite = s;}

	/**
     * Constructs a storable object at a specified cell within a labyrinth.
     * @param l   the labyrinth instance
     * @param idx the index of the node it is contained by
     */
	public Storable(Labyrinth l, Vector idx){
		lab = l;
		inCell = idx;
	}

	/**Gets the x-coordinate of this object's position in the labyrinth, taking the object's offset into account.*/
	public double getXPos(){
		return lab.xPosition(inCell) + xOffset;
	}

	/**Gets the y-coordinate of this object's position in the labyrinth, taking the object's offset into account.*/
	public double getYPos(){
		return lab.yPosition(inCell) + yOffset;
	}

	/**
     * Determines whether a specified position is valid for this object within the labyrinth.
     * @param x the x-coordinate of the position
     * @param y the y-coordinate of the position
     */
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

	/**
     * Sets the position of this object to a specified location if the position is valid.
     * @param x the x-coordinate of the new position
     * @param y the y-coordinate of the new position
     */
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

	/** Sets the cell position of this object within the labyrinth to be in the middle of the specified cell.*/
	public void setCell(Vector idx) {
		if (lab.inBound(idx)){
			inCell = idx;
			xOffset = 0;
			yOffset = 0;
		}
	}

	/**
     * Creates a brazier storable object at a specified cell with a preset light and sprite.
     * @param l   the labyrinth instance
     * @param idx the cell position as a Vector
     */
	public static Storable brazier(Labyrinth l, Vector idx){
		Storable b = new Storable(l, idx);
		b.setLight(new Light(b, 2.5, 0.1, 0.15, ModelColor.ORANGE));
		b.setSprite(ModelSprite.BRAZIER);
		return b;
	}

}

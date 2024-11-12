package labyrinth.model;

/** A 2D integer vector with simple operations */
public class Vector implements java.io.Serializable  {
	private int x;
	private int y;

	public int getX() {return x;}
	public int getY() {return y;}

	public Vector(int x, int y){
		this.x = x;
		this.y = y;
	}

	/** Returns the opposite of the vector*/
	public Vector neg(){
		return new Vector(-x, -y);
	}

	/** Adds the two vectors together, creating and returning a new one */
	public Vector plus(Vector o){
		return new Vector(x + o.getX(), y + o.y);
	}

	/** Equality by value between vectors */
	public boolean equals(Object o){
		if (! (o instanceof Vector)) {
			return false;
		}
		Vector idx = (Vector)o;
		return idx.x == x && idx.y == y;
	}

	/** "(x, y)" representation of the vector */
	public String toString(){
		return "(" + x + ", " + y + ")";
	}

	/** Returns the hashcode of the vector */
	public int hashCode() {
		return (x + ", " + y).hashCode();
	}
}

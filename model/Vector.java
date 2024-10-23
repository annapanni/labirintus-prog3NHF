package model;

public class Vector {
	private int x;
	private int y;

	public Vector(int x, int y){
		this.x = x;
		this.y = y;
	}

	public int getX() {return x;}
	public int getY() {return y;}

	public Vector neg(){
		return new Vector(-x, -y);
	}

	public Vector plus(Vector o){
		return new Vector(x + o.getX(), y + o.y);
	}

	public boolean equals(Object o){
		if (! (o instanceof Vector)) {
			return false;
		}
		Vector idx = (Vector)o;
		return idx.x == x && idx.y == y;
	}

	public int hashCode() {
		return (x + ", " + y).hashCode();
	}

	public Vector clone(){
		return new Vector(x, y);
	}
}

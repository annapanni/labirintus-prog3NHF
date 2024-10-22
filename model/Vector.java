package model;

public class Vector {
	 public int x;
	 public int y;

	 public Vector(int x, int y){
		 this.x = x;
		 this.y = y;
	 }

	 public Vector neg(){
		 return new Vector(-x, -y);
	 }

	 public Vector plus(Vector o){
		 return new Vector(x + o.x, y + o.y);
	 }

	 public boolean equals(Object o){
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

package labyrinth.model;

/**
 * Interface for classes who are capable of finding and creating rooms in
 * a labyrinth
 */
public interface RoomFinder extends java.io.Serializable{
	/** Finds and creates a room in a labyrinth from a given node*/
	public abstract Room findRoomAt(Vector idx, Labyrinth lab);
}

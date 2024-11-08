package labyrinth.model;

public interface RoomFinder{
	public abstract Room findRoomAt(Vector idx, Labyrinth lab);
}

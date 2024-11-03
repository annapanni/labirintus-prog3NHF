package controller;

import model.*;

public interface RoomFinder{
	public abstract Room findRoomAt(Vector idx, Labyrinth lab);
}

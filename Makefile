model = Labyrinth HexaLab Vector Room RectRoom GraphUtils RoomFinder RectRoomFinder ConvexRoomFinder ConvexRoom

srcs = DisplayGraphics.java $(model:%=model/%.java)

main = DisplayGraphics

run:
	javac $(srcs)
	java $(main)

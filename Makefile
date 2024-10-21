model = Labyrinth HexaLab Vector Room RectRoom GraphUtils RoomFinder RectRoomFinder

srcs = DisplayGraphics.java $(model:%=model/%.java)

main = DisplayGraphics

run:
	javac $(srcs)
	java $(main)

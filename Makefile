model = Labyrinth HexaLab Vector Room RectRoom GraphUtils RoomFinder RectRoomFinder \
 ConcaveRoomFinder ConcaveRoom RectLab Storable Firefly Light LabState Moving ModelColor

view = LabView DisplayGraphics

srcs = $(view:%=view/%.java) $(model:%=model/%.java)

main = view.DisplayGraphics

run:
	javac $(srcs)
	java $(main)

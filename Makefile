model = Labyrinth HexaLab Vector Room RectRoom GraphUtils RoomFinder RectRoomFinder \
 ConvexRoomFinder ConvexRoom RectLab Storable Firefly Darkness LabState Moving

view = LabView DisplayGraphics

srcs = $(view:%=view/%.java) $(model:%=model/%.java)

main = view.DisplayGraphics

run:
	javac $(srcs)
	java $(main)

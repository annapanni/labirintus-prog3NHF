model = Labyrinth HexaLab Vector Room RectRoom GraphUtils RoomFinder RectRoomFinder \
 ConcaveRoomFinder ConcaveRoom RectLab Storable Firefly Light LabState Moving ModelColor PlayerCharacter

view = LabView DisplayGraphics

controller = LabGameControl LabEditControl

srcs = $(view:%=view/%.java) $(model:%=model/%.java) $(controller:%=controller/%.java)

main = view.DisplayGraphics

run:
	javac $(srcs)
	java $(main)

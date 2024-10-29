model = Labyrinth HexaLab Vector Room RectRoom GraphUtils ConcaveRoom RectLab \
	Storable Firefly Light LabState Moving ModelColor PlayerCharacter

view = LabView DisplayGraphics

controller = LabGameControl LabEditControl RoomFinder RectRoomFinder ConcaveRoomFinder

srcs = $(view:%=view/%.java) $(model:%=model/%.java) $(controller:%=controller/%.java)

main = view.DisplayGraphics

run:
	javac $(srcs)
	java $(main)

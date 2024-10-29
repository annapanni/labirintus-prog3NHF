model = Labyrinth HexaLab Vector Room RectRoom GraphUtils ConcaveRoom RectLab \
	Storable Firefly Light LabState ModelColor PlayerCharacter

view = LabView DisplayGraphics

controller = LabGameControl LabEditControl RoomFinder RectRoomFinder ConcaveRoomFinder Mover \
	CharMover LightMover FireflyMover

srcs = $(view:%=view/%.java) $(model:%=model/%.java) $(controller:%=controller/%.java)

main = view.DisplayGraphics

run:
	javac $(srcs)
	java $(main)

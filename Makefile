model = Labyrinth HexaLab Vector Room RectRoom GraphUtils ConcaveRoom RectLab \
	Storable Firefly Light LabState ModelColor PlayerCharacter ModelSprite Key

view = LabView DisplayGraphics CustomTimerTask EditPanel GamePanel SimplePopup ModePanel StructSettings

controller = LabGameControl LabEditControl RoomFinder RectRoomFinder ConcaveRoomFinder Mover \
	CharMover LightMover FireflyMover FileManager

srcs = $(view:%=view/%.java) $(model:%=model/%.java) $(controller:%=controller/%.java)

main = view.DisplayGraphics

run:
	javac $(srcs)
	java $(main)

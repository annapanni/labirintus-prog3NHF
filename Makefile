model = Labyrinth HexaLab Vector Room RectRoom GraphUtils ConcaveRoom RectLab \
	Storable Firefly Light LabState ModelColor PlayerCharacter ModelSprite Key RoomFinder Item Map Exit

view = LabView MainDisplay CustomTimerTask EditPanel GamePanel SimplePopup ModePanel StructSettings MapView

controller = LabGameControl LabEditControl RectRoomFinder ConcaveRoomFinder Mover \
	CharMover LightMover FireflyMover FileManager Interactable ExitControl KeyControl MapControl InteractFactory

srcs = $(view:%=view/%.java) $(model:%=model/%.java) $(controller:%=controller/%.java)

main = view.MainDisplay

run:
	javac $(srcs:%=srcs/%)
	java -classpath srcs $(main)

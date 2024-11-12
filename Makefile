model = Labyrinth HexaLab Vector Room RectRoom GraphUtils ConcaveRoom RectLab \
	Storable Firefly Light LabState ModelColor PlayerCharacter ModelSprite Key RoomFinder Item Map Exit

view = LabView MainDisplay CustomTimerTask EditPanel GamePanel SimplePopup ModePanel StructSettings MapView

controller = LabGameControl LabEditControl RectRoomFinder ConcaveRoomFinder Mover \
	CharMover LightMover FireflyMover FileManager Interactable ExitControl KeyControl MapControl InteractFactory

srcs = $(view:%=view/%.java) $(model:%=model/%.java) $(controller:%=controller/%.java)
classes = $(view:%=view/%.class) $(model:%=model/%.class) $(controller:%=controller/%.class)

main = labyrinth.view.MainDisplay

run:
	javac $(srcs:%=src/main/java/labyrinth/%)
	java -classpath src/main/java/ $(main)

clean:
	rm $(classes:%=src/main/java/labyrinth/%)

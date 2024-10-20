model = Labyrinth HexaLab Vector

srcs = DisplayGraphics.java $(model:%=model/%.java)

main = DisplayGraphics

run:
	javac $(srcs)
	java $(main)

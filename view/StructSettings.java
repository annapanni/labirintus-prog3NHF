package view;

import javax.swing.JPanel;
import java.util.Random;

import model.*;
import controller.*;

public class StructSettings extends JPanel {
	private static Random rand = new Random();
	private static String[] lTypes = new String[]{"Hexagonal", "Rectangular"};
	private static String[] rTypes = new String[]{"Concave", "Rectangular"};
	private int lWidth;
	private int lHeight;
	private String lType;
	private int rSize;
	private String rType;

	private ModePanel pan;

	public StructSettings(ModePanel mp) {
		lWidth = rand.nextInt(25) + 5;
		lHeight = (int)((rand.nextDouble() + 0.5) * lWidth);
		lType = lTypes[rand.nextInt(lTypes.length)];
		int randSize = rand.nextInt(10) + 2;
		rSize = (randSize >= Math.min(lWidth, lHeight)) ? Math.min(lWidth, lHeight) / 2 : randSize;
		rType = rTypes[rand.nextInt(rTypes.length)];
		pan = mp;
	}

	public LabState generate(){
		RoomFinder rf;
		Labyrinth lab;
		switch (rType) {
			case "Concave":
				rf = new ConcaveRoomFinder(rSize); break; //TODO size instead of radius
			case "Rectangular":
				rf = new RectRoomFinder(rSize); break;
			default:
				rf = new RectRoomFinder(rSize);  // TODO none roomfinder
		}
		if (lType.equals("Hexagonal")) {
			lab = new HexaLab(lWidth, lHeight, 0.3);
		} else {
			lab = new RectLab(lWidth, lHeight, 0.3);
		}
		return LabEditControl.generateLabyrinth(lab, rf);
	}

	public void generateAndSet(){
		pan.setLabState(generate());
	}

}

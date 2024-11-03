package view;

import javax.swing.*;
import java.awt.*;
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
	private MainDisplay disp;

	public StructSettings(){this(null, null);}

	public StructSettings(ModePanel mp){this(null, mp);}

	public StructSettings(MainDisplay d, ModePanel mp) {
		lWidth = rand.nextInt(25) + 5;
		lHeight = (int)((rand.nextDouble() + 0.5) * lWidth);
		lType = lTypes[rand.nextInt(lTypes.length)];
		int randSize = rand.nextInt(10) + 2;
		rSize = (randSize >= Math.min(lWidth, lHeight)) ? Math.min(lWidth, lHeight) / 2 : randSize;
		rType = rTypes[rand.nextInt(rTypes.length)];
		pan = mp;
		disp = d;

		JSpinner lwInp = new JSpinner(new SpinnerNumberModel(lWidth, 2, 50, 1));
		lwInp.addChangeListener(e -> lWidth = (Integer)lwInp.getValue());
		JLabel lwLabel = new JLabel("Width:");
		lwLabel.setLabelFor(lwInp);
		JSpinner lhInp = new JSpinner(new SpinnerNumberModel(lHeight, 2, 50, 1));
		lhInp.addChangeListener(e -> lHeight = (Integer)lhInp.getValue());
		JLabel lhLabel = new JLabel("Height:");
		lhLabel.setLabelFor(lhInp);
		JComboBox ltInp = new JComboBox(lTypes);
		ltInp.setSelectedItem(lType);
		ltInp.addActionListener(e -> lType = (String)ltInp.getSelectedItem());
		JLabel ltLabel = new JLabel("Labyrinth type:");
		ltLabel.setLabelFor(ltInp);
		JSpinner rsInp = new JSpinner(new SpinnerNumberModel(rSize, 1, 50, 1));
		rsInp.addChangeListener(e -> rSize = (Integer)rsInp.getValue());
		JLabel rsLabel = new JLabel("Max room size:");
		rsLabel.setLabelFor(rsInp);
		JComboBox rtInp = new JComboBox(rTypes);
		rtInp.setSelectedItem(rType);
		rtInp.addActionListener(e -> rType = (String)rtInp.getSelectedItem());
		JLabel rtLabel = new JLabel("Room type:");
		rtLabel.setLabelFor(rtInp);

		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints con = new GridBagConstraints();
		con.insets = new Insets(10, 10, 10, 10);
		setLayout(gridbag);
		Component[][] comps = new Component[][]{
			new Component[]{lwLabel, lwInp},
			new Component[]{lhLabel, lhInp},
			new Component[]{ltLabel, ltInp},
			new Component[]{rtLabel, rtInp},
			new Component[]{rsLabel, rsInp},
		};
		for (int y=0; y < comps.length; y++){
			for (int x=0; x < comps[y].length; x++){
				con.gridx = x; con.gridy = y;
				gridbag.setConstraints(comps[y][x], con);
				add(comps[y][x]);
			}
		}

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
			lab = new HexaLab(lWidth, lHeight, 0.3, rf);
		} else {
			lab = new RectLab(lWidth, lHeight, 0.3, rf);
		}
		return LabEditControl.generateLabyrinth(lab);
	}

	public void generateAndSet(){
		if (pan != null) {
			pan.setLabState(generate());
		}
		if (disp != null) {
			disp.switchTo(pan);
		}
	}

}
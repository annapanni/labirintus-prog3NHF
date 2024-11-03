package view;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import controller.LabEditControl;
import model.LabState;

public class EditPanel extends ModePanel {
	LabEditControl labControl;
	Mode currMode = Mode.KEY;

	private enum Mode {KEY, BRAZIER, EXIT, STARTPOS, LAB}

	@Override
	public void setLabState(LabState ls){
		super.setLabState(ls);
		labControl.setLabState(ls);
		ls.getPlayer().setCell(ls.getStartPos());
		// TODO uncollect keys
	}

	private JPanel createOptionPanel() {
		JButton keyButt = new JButton("Add/delete key");
		keyButt.setAlignmentX(Component.CENTER_ALIGNMENT);
		keyButt.addActionListener(e -> currMode = Mode.KEY);
		JButton brazButt = new JButton("Add/delete brazier");
		brazButt.setAlignmentX(Component.CENTER_ALIGNMENT);
		brazButt.addActionListener(e -> currMode = Mode.BRAZIER);
		JButton exitButt = new JButton("Change exit");
		exitButt.setAlignmentX(Component.CENTER_ALIGNMENT);
		exitButt.addActionListener(e -> currMode = Mode.EXIT);
		JButton startButt = new JButton("<html><center>Change starting<br/>position</center></html>");
		startButt.setAlignmentX(Component.CENTER_ALIGNMENT);
		startButt.addActionListener(e -> currMode = Mode.STARTPOS);
		JButton changeButt = new JButton("Change Labyrinth");
		changeButt.setAlignmentX(Component.CENTER_ALIGNMENT);
		changeButt.addActionListener(e -> currMode = Mode.LAB);
		List<JButton> btns = new ArrayList<>(List.of(keyButt, brazButt, exitButt, startButt, changeButt));
		JPanel pan = new JPanel();
		pan.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		GridLayout gl = new GridLayout(7,0);
		gl.setVgap(10);
		pan.setLayout(gl);
		for (JButton b : btns) {
			pan.add(b);
		}
		return pan;
	}

	private JPanel createSettingsPanel() {
		StructSettings sett = new StructSettings(this);
		JButton createButt = new JButton("Create");
		createButt.addActionListener(e -> sett.generateAndSet());
		createButt.setAlignmentX(Component.CENTER_ALIGNMENT);
		JPanel structPanel = new JPanel();
		structPanel.setLayout(new BoxLayout(structPanel, BoxLayout.Y_AXIS));
		structPanel.add(sett);
		structPanel.add(createButt);
		structPanel.add(Box.createVerticalStrut(10));
		structPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		JPanel pan = new JPanel();
		pan.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		GridLayout gl = new GridLayout(2,0);
		gl.setVgap(10);
		pan.setLayout(gl);
		pan.add(structPanel);

		return pan;
	}

	public EditPanel(LabState ls, int dt) {
		super(ls, dt);
		setLayout(new BorderLayout());
		add(createOptionPanel(), BorderLayout.WEST);
		add(createSettingsPanel(), BorderLayout.EAST);
		labView = new LabView(labState, 50, 30, 0.2);
		add(labView, BorderLayout.CENTER);
		labControl = new LabEditControl(labState);

		labView.addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e) {
				double xpos = labView.pxToLabPos(e.getX());
				double ypos = labView.pxToLabPos(e.getY());
				switch (currMode) {
					case Mode.KEY:
						labControl.addDeleteKey(xpos, ypos); break;
					case Mode.BRAZIER:
						labControl.addDeleteBrazier(xpos, ypos); break;
					case Mode.EXIT:
						labControl.chageExit(xpos, ypos); break;
					case Mode.STARTPOS:
						labControl.chageStartPos(xpos, ypos); break;
					case Mode.LAB:
						labControl.changeLabAt(xpos, ypos); break;
				}
			}
		});
	}
}

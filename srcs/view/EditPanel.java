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
		ls.setUsedFireflyNum(0);
		ls.getKeys().stream().forEach(k -> {
			k.setCollected(false);
			if(! ls.getObjects().contains(k))  ls.getObjects().add(k);
		});
		ls.setMapCollected(false);
		if(!ls.getObjects().contains(ls.getMap())) ls.getObjects().add(ls.getMap());
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

	private JPanel createGameplayPanel(){
		JToggleButton show = new JToggleButton("OFF");
		show.addChangeListener(e -> {
			labView.setVisiblityOverride(show.isSelected() ? -1 : 0.2 );
			show.setText(show.isSelected() ? "ON" : "OFF");
		});
		show.setPreferredSize(show.getPreferredSize());
		JSpinner dOp = new JSpinner(new SpinnerNumberModel(labState.getdarknessOpacity(), 0.0, 1.0, 0.05));
		dOp.addChangeListener(e -> labState.setDarknessOpacity((Double)dOp.getValue()));
		JFormattedTextField jftf = ((JSpinner.DefaultEditor) dOp.getEditor()).getTextField();
		jftf.setColumns(3);
		JToggleButton los = new JToggleButton("OFF", labState.getLineOfSight() != null);
		los.setPreferredSize(los.getPreferredSize()); //initialized as "OFF" to make sure both texts fit
		los.setText(labState.getLineOfSight() != null ? "ON" : "OFF");
		los.addChangeListener(e -> {
			labState.setLineOfSight(los.isSelected());
			los.setText(los.isSelected() ? "ON" : "OFF");
		});
		JSpinner fNum = new JSpinner(new SpinnerNumberModel(labState.getFireflyNum(), 0, 50, 1));
		fNum.addChangeListener(e -> labState.setFireflyNum((Integer)fNum.getValue()));
		JLabel showLabel = new JLabel("<html><body style='text-align: right'>Show visiblity settings<br>in edit mode:");
		showLabel.setLabelFor(show);
		JLabel dopLabel = new JLabel("Darkness opacity:");
		dopLabel.setLabelFor(dOp);
		JLabel losLabel = new JLabel("Enable line of sight:");
		losLabel.setLabelFor(los);
		JLabel fNumLabel = new JLabel("Number of usable fireflies:");
		fNumLabel.setLabelFor(fNum);
		JPanel pan = new JPanel();
		pan.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		GridBagLayout gl = new GridBagLayout();
		GridBagConstraints con = new GridBagConstraints();
		con.insets = new Insets(10, 10, 10, 10);
		pan.setLayout(gl);
		Component[][] comps = new Component[][] {
			new Component[]{showLabel, show},
			new Component[]{dopLabel, dOp},
			new Component[]{losLabel, los},
			new Component[]{fNumLabel, fNum}
		};
		for (int y=0; y<comps.length; y++) {
			for (int x=0; x<comps[y].length; x++) {
				con.gridy = y; con.gridx  = x;
				con.anchor = x % 2 == 0 ? GridBagConstraints.EAST : GridBagConstraints.WEST;
				gl.setConstraints(comps[y][x], con);
				pan.add(comps[y][x]);
			}
		}
		pan.setBorder(BorderFactory.createLineBorder(Color.BLACK));
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
		pan.add(createGameplayPanel());
		return pan;
	}

	public EditPanel(LabState ls, int dt) {
		super(ls, dt);
		setLayout(new BorderLayout());
		add(createOptionPanel(), BorderLayout.WEST);
		add(createSettingsPanel(), BorderLayout.EAST);
		labView = new LabView(labState, 30, 0.2);
		labView.requestFocus();
		add(labView, BorderLayout.CENTER);
		labControl = new LabEditControl(labState);

		labView.addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e) {
				labView.requestFocus();
				double xpos = labView.xpxToLabPos(e.getX());
				double ypos = labView.ypxToLabPos(e.getY());
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

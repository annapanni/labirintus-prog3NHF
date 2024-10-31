package view;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

import controller.LabEditControl;
import model.LabState;

public class EditPanel extends ModePanel {
	LabEditControl labControl;

	@Override
	public void setLabState(LabState ls){
		super.setLabState(ls);
		labControl.setLabState(ls);
	}

	private JPanel createOptionPanel() {
		JButton keyButt = new JButton("Add/delete key");
		keyButt.setAlignmentX(Component.CENTER_ALIGNMENT);
		JButton changeButt = new JButton("Change Labyrinth");
		changeButt.setAlignmentX(Component.CENTER_ALIGNMENT);

		JPanel pan = new JPanel();
		pan.setLayout(new BoxLayout (pan, BoxLayout.Y_AXIS));
		pan.add(keyButt);
		pan.add(changeButt);

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
		GridBagLayout gb = new GridBagLayout();
		GridBagConstraints con = new GridBagConstraints();
		con.insets = new Insets(10, 10, 10, 10);
		pan.setLayout(gb);

		con.gridx = 0; con.gridy = 0;
		gb.setConstraints(structPanel, con);
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
				labControl.handleClick(labView.pxToLabPos(e.getX()), labView.pxToLabPos(e.getY()));
			}
		});
	}
}

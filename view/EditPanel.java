package view;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Timer;

import controller.LabEditControl;
import model.LabState;

public class EditPanel extends JPanel {
	LabState labState;
	LabView labView;
	LabEditControl labControl;
	Timer timer = new Timer();
	int dTime = 30;

	public LabState getLabState() {return labState;}
	public void setLabState(LabState ls){
		labState = ls;
		labControl.setLabState(ls);
		labView.setLabState(ls);
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
		JButton keyButt = new JButton("sett");
		keyButt.setAlignmentX(Component.CENTER_ALIGNMENT);
		JButton changeButt = new JButton("sett2");
		changeButt.setAlignmentX(Component.CENTER_ALIGNMENT);

		JPanel pan = new JPanel();
		pan.setLayout(new BoxLayout (pan, BoxLayout.Y_AXIS));
		pan.add(keyButt);
		pan.add(changeButt);

		return pan;
	}

	public EditPanel(LabState ls) {
		labState = ls;
		timer = new Timer();
		setLayout(new BorderLayout());
		add(createOptionPanel(), BorderLayout.WEST);
		add(createSettingsPanel(), BorderLayout.EAST);
		labView = new LabView(labState, 50, 30);
		add(labView, BorderLayout.CENTER);
		labControl = new LabEditControl(labState);

		labView.addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e) {
				labControl.handleClick(labView.pxToLabPos(e.getX()), labView.pxToLabPos(e.getY()));
			}
		});
	}

	public void exitEdit(){
		timer.cancel();
		timer.purge();
	}

	public void startEditing(){
		timer = new Timer();
		timer.scheduleAtFixedRate(new CustomTimerTask(labView::repaint), 0l, (long)dTime);
	}
}

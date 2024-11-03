package view;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

import controller.LabGameControl;
import model.LabState;


public class GamePanel extends ModePanel {
	LabGameControl labControl;

	@Override
	public void setLabState(LabState ls){
		super.setLabState(ls);
		labControl.setLabState(ls);
	}

	private JPanel createHelpPanel() {
		JPanel pan = new JPanel();
		Color defBg = pan.getBackground();
		JLabel info = new JLabel(
			"<html><h3>Objectives</h3>" +
			"<ul style=margin-left:20px><li>collect all the keys</li><li>reach the exit</li>" +
			"<li>to help you find your next<br/>goal you may release a firefly</li></ul>" +
			"<h3>Game controls</h3><ul style=margin-left:20px>" +
			"<li>arrow keys: pan the map</li><li>mouse wheel: zoom</li>" +
			"<li>c key: center the map</li><li>f key: realease a firefly</li>" +
			"<li>space: (un)lock player position</li><li>click: interact with objects</li>" +
			"</ul></html>"
		);
		info.setAlignmentX(Component.RIGHT_ALIGNMENT);
		JToggleButton togInfo = new JToggleButton("?", true);
		togInfo.addChangeListener(e -> {
			info.setVisible(togInfo.isSelected());
			pan.setBackground(togInfo.isSelected() ? defBg : Color.black);
		});
		togInfo.setAlignmentX(Component.RIGHT_ALIGNMENT);
		pan.setLayout(new BoxLayout(pan, BoxLayout.Y_AXIS));
		pan.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		pan.add(togInfo);
		pan.add(info);
		return pan;
	}

	public GamePanel(LabState ls, int dt) {
		super(ls, dt);
		setLayout(new BorderLayout());
		add(createHelpPanel(), BorderLayout.WEST);
		labView = new LabView(labState, 30);
		labView.requestFocus();
		add(labView, BorderLayout.CENTER);
		labControl = new LabGameControl(labState, dTime);

		labView.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_SPACE) labControl.switchLockedPos();
				if (e.getKeyCode() == KeyEvent.VK_F) labControl.startFirefly();
	    }
		});

		labView.addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e) {
				labView.requestFocus();
				double xpos = labView.xpxToLabPos(e.getX());
				double ypos = labView.ypxToLabPos(e.getY());
				labControl.interactAt(xpos, ypos, () -> SimplePopup.message("Congratulations! You exited the maze.").startPopup(GamePanel.this));
			}
		});

		labView.addMouseMotionListener(new MouseMotionAdapter(){
			@Override
			public void mouseMoved(MouseEvent e) {
					labControl.setMousePos(labView.xpxToLabPos(e.getX()), labView.ypxToLabPos(e.getY()));
			}
		});
	}

	@Override
	public void startMode(){
		super.startMode();
		timer.scheduleAtFixedRate(new CustomTimerTask(labControl::step), 0l, (long)dTime);
	}
}

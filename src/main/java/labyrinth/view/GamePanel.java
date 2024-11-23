package labyrinth.view;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

import labyrinth.controller.LabGameControl;
import labyrinth.model.LabState;
import labyrinth.model.Exit;

/**
 * Panel for a the gameplay. Includes the view of the labyrinth and all associated event handling.
 */
public class GamePanel extends ModePanel {
	LabGameControl labControl;

	@Override
	public void setLabState(LabState ls){
		super.setLabState(ls);
		labControl.setLabState(ls);
		labView.setVisiblityOverride(-1);
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
			"<li>c key: center the map</li><li>f key: realease a firefly</li><li>m key: toggle collected map</li>" +
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

	/**
	 * Creates a new game panel
	 * @param ls labyrinth the game takes place in
	 * @param dt time between screen refresh and object movements in miliseconds
	 */
	public GamePanel(LabState ls, int dt) {
		super(ls, dt);
		setLayout(new BorderLayout());
		add(createHelpPanel(), BorderLayout.WEST);
		labView.requestFocus();
		add(labView, BorderLayout.CENTER);
		labControl = new LabGameControl(labState, dTime);

		labView.addKeyListener(new KeyAdapter() {
			@Override
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
				labControl.interactAt(xpos, ypos);
				Exit exited = labControl.exitedOn();
				if (exited != null) {
					SimplePopup.message("<html>Congratulations!<br/> You exited the maze.").startPopup(GamePanel.this);
					labView.setVisiblityOverride(0.0);
					labControl.switchLockedPos();
					exited.setCollected(false);
				}
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
		timer.scheduleAtFixedRate(new CustomTimerTask(labControl::step), 0l, dTime);
	}
}

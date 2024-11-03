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
		JButton keyButt = new JButton("help ");
		JButton changeButt = new JButton("help2");

		pan.add(keyButt);
		pan.add(changeButt);

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

		labView.addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e) {
				labView.requestFocus();
				labControl.handleClick(labView.xpxToLabPos(e.getX()), labView.ypxToLabPos(e.getY()));
			}
		});

		labView.addMouseMotionListener(new MouseMotionAdapter(){
			@Override
			public void mouseMoved(MouseEvent e) {
					labControl.handleMouseMove(labView.xpxToLabPos(e.getX()), labView.ypxToLabPos(e.getY()));
			}
		});
	}

	@Override
	public void startMode(){
		super.startMode();
		timer.scheduleAtFixedRate(new CustomTimerTask(labControl::step), 0l, (long)dTime);
	}
}

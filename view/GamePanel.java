package view;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Timer;

import controller.LabGameControl;
import model.LabState;


public class GamePanel extends JPanel {
	LabState labState;
	LabView labView;
	LabGameControl labControl;
	Timer timer = new Timer();
	int dTime = 30;

	public LabState getLabState() {return labState;}

	private JPanel createHelpPanel() {
		JPanel pan = new JPanel();
		JButton keyButt = new JButton("help ");
		JButton changeButt = new JButton("help2");

		pan.add(keyButt);
		pan.add(changeButt);

		return pan;
	}

	public GamePanel(LabState ls) {
		labState = ls;
		timer = new Timer();
		setLayout(new BorderLayout());
		add(createHelpPanel(), BorderLayout.WEST);
		labView = new LabView(labState, 50, 30);
		add(labView, BorderLayout.CENTER);
		labControl = new LabGameControl(labState, dTime);

		labView.addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e) {
				labControl.handleClick(labView.pxToLabPos(e.getX()), labView.pxToLabPos(e.getY()));
			}
		});

		labView.addMouseMotionListener(new MouseMotionAdapter(){
			@Override
			public void mouseMoved(MouseEvent e) {
					labControl.handleMouseMove(labView.pxToLabPos(e.getX()), labView.pxToLabPos(e.getY()));
			}
		});
	}

	public void exitGame(){
		timer.cancel();
		timer.purge();
	}

	public void startGame(){
		timer = new Timer();
		timer.scheduleAtFixedRate(new CustomTimerTask(labView::repaint), 0l, (long)dTime);
		timer.scheduleAtFixedRate(new CustomTimerTask(labControl::step), 0l, (long)dTime);
	}
}

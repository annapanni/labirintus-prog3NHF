package view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.lang.Thread;
import java.lang.InterruptedException;

import model.*;
import controller.*;

public class DisplayGraphics extends JPanel{
	public LabView lab;

	public DisplayGraphics(LabView lv) {lab = lv;}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		setBackground(Color.DARK_GRAY);
		lab.drawAll(g2);
	}

	public static void main(String[] args) {
		int dTime = 1000 / 30;
		Labyrinth lab = new RectLab(20, 20, 0.3, new RectRoomFinder(5));
		PlayerCharacter player = new PlayerCharacter(lab, new Vector(12, 12), 0.003);
		player.setLight(new Light(player, 3, 0.8, 0.0));
		LabState labState = new LabState(lab, player, 0.8);
		labState.getObjects().add(new Key(lab, new Vector(5, 5)));
		LabView lv = new LabView(labState, 60, 30);
		LabControl lctrl = new LabControl(labState, dTime);
		DisplayGraphics disp = new DisplayGraphics(lv);
		JFrame frame = new JFrame();
		frame.add(disp);
		frame.setSize(900,800);

		lab.changeNTimes(1000);
		lab.coverWithRooms();

		frame.addWindowListener(new WindowAdapter() { // alternate solution on lecture TODO
    	public void windowClosing(WindowEvent windowEvent){
          System.exit(0);
       }
    });

		disp.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
      public void mouseReleased(MouseEvent e) {}
      public void mouseEntered(MouseEvent e) {}

      public void mousePressed(MouseEvent e) {
				lctrl.handleClick(lv.pxToLabPos(e.getX()), lv.pxToLabPos(e.getY()));
			}
		});

		disp.addMouseMotionListener(new MouseMotionListener(){
			@Override
			public void mouseDragged(MouseEvent e) {}
      public void mouseMoved(MouseEvent e) {
				lctrl.handleMouseMove(lv.pxToLabPos(e.getX()), lv.pxToLabPos(e.getY()));
			}
		});

		frame.setVisible(true);

		while(true) { //something better? TODO
			lctrl.step();
			disp.repaint();
			try {Thread.sleep(dTime);}
			catch (InterruptedException e) {}
		}
	}
}

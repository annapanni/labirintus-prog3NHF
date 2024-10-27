package view;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.event.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

import model.*;

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
		Labyrinth lab = new HexaLab(20, 20, 0.3, new ConcaveRoomFinder(5));
		Storable player = new Storable(lab, new Vector(12, 12));
		player.setLight(new Light(player, 3, 0.8));
		LabState labState = new LabState(lab, player);
		LabView lv = new LabView(labState, 60, 30);
		DisplayGraphics disp = new DisplayGraphics(lv);
		JFrame frame = new JFrame();
		frame.add(disp);
		frame.setSize(900,800);
		frame.setVisible(true);

		lab.changeNTimes(1000);
		lab.coverWithRooms();

		frame.addWindowListener(new WindowAdapter() {
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
				disp.lab.handleClick(e.getX(), e.getY());
				disp.repaint();
			}
		});

		disp.addMouseMotionListener(new MouseMotionListener(){
			@Override
			public void mouseDragged(MouseEvent e) {}
      public void mouseMoved(MouseEvent e) {
				disp.lab.handleMouseMove(e.getX(), e.getY());
				disp.repaint();
			}
		});
	}
}

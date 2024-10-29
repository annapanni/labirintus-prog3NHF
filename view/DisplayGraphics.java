package view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.lang.Thread;
import java.lang.InterruptedException;

import model.*;
import controller.*;

public class DisplayGraphics{
	public static void main(String[] args) {
		int dTime = 1000 / 30;
		LabState labState = LabEditControl.generateLabyrinth(new RectLab(20, 20, 0.3), new RectRoomFinder(5));
		LabView lv = new LabView(labState, 60, 30);
		LabGameControl lctrl = new LabGameControl(labState , dTime);
		JFrame frame = new JFrame();
		frame.add(lv);
		frame.setSize(900,800);

		frame.addWindowListener(new WindowAdapter() { // alternate solution on lecture TODO
    	public void windowClosing(WindowEvent windowEvent){
          System.exit(0);
       }
    });

		lv.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
      public void mouseReleased(MouseEvent e) {}
      public void mouseEntered(MouseEvent e) {}

      public void mousePressed(MouseEvent e) {
				lctrl.handleClick(lv.pxToLabPos(e.getX()), lv.pxToLabPos(e.getY()));
			}
		});

		lv.addMouseMotionListener(new MouseMotionListener(){
			@Override
			public void mouseDragged(MouseEvent e) {}
      public void mouseMoved(MouseEvent e) {
				lctrl.handleMouseMove(lv.pxToLabPos(e.getX()), lv.pxToLabPos(e.getY()));
			}
		});

		frame.setVisible(true);

		while(true) { //something better? TODO
			lctrl.step();
			lv.repaint();
			try {Thread.sleep(dTime);}
			catch (InterruptedException e) {}
		}
	}
}

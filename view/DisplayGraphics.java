package view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.lang.Thread;
import java.lang.InterruptedException;

import model.*;
import controller.*;

public class DisplayGraphics{
	static boolean game = true;
	private static JMenuBar menuSetup(){
		JMenuBar mb = new JMenuBar();
    JMenu eMenu = new JMenu("Edit");
		JMenu pMenu = new JMenu("Play");
		JMenuItem edThis = new JMenuItem("Edit this map");
		JMenuItem edNew = new JMenuItem("Create new map");
		JMenuItem edLoad = new JMenuItem("Load map");
		JMenuItem pThis = new JMenuItem("Play on this map");
		JMenuItem pNew = new JMenuItem("Random new map");
		JMenuItem pCNew = new JMenuItem("Configure random new map");
    JMenuItem pLoad = new JMenuItem("Load map");
		eMenu.add(edThis);
		eMenu.add(edNew);
		eMenu.add(edLoad);
		pMenu.add(pThis);
		pMenu.add(pNew);
		pMenu.add(pCNew);
		pMenu.add(pLoad);

		mb.add(eMenu);
		mb.add(pMenu);

		edThis.addActionListener(e -> game=false);
		pThis.addActionListener(e -> game=true);

		return mb;
	}

	public static void main(String[] args) {
		int dTime = 1000 / 30;
		LabState labState = LabEditControl.generateLabyrinth(new RectLab(20, 20, 0.3), new RectRoomFinder(5));
		LabView lv = new LabView(labState, 60, 30);
		LabGameControl gameControl = new LabGameControl(labState, dTime);
		LabEditControl editControl = new LabEditControl(labState);
		JFrame frame = new JFrame();

		lv.setPreferredSize(new Dimension(800, 900));
		frame.add(lv);

		frame.setJMenuBar(menuSetup());

		frame.pack();

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		while (true) {
			lv.addMouseListener(new MouseListener(){
				@Override
				public void mouseClicked(MouseEvent e) {}
				public void mouseExited(MouseEvent e) {}
	      public void mouseReleased(MouseEvent e) {}
	      public void mouseEntered(MouseEvent e) {}

	      public void mousePressed(MouseEvent e) {
					if (game) {
						gameControl.handleClick(lv.pxToLabPos(e.getX()), lv.pxToLabPos(e.getY()));
					} else {
						editControl.handleClick(lv.pxToLabPos(e.getX()), lv.pxToLabPos(e.getY()));
					}
				}
			});

			lv.addMouseMotionListener(new MouseMotionListener(){
				@Override
				public void mouseDragged(MouseEvent e) {}
	      public void mouseMoved(MouseEvent e) {
					if (game) {
						gameControl.handleMouseMove(lv.pxToLabPos(e.getX()), lv.pxToLabPos(e.getY()));
					}
				}
			});

			frame.setVisible(true);

			while(true) { //something better? TODO
				if (game) {
					gameControl.step();
				}
				lv.repaint();
				try {Thread.sleep(dTime);}
				catch (InterruptedException e) {}
			}
		}

	}
}

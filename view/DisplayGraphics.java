package view;

import java.awt.*;
import javax.swing.*;

import java.lang.Thread;
import java.lang.InterruptedException;

import model.*;
import controller.*;

public class DisplayGraphics{
	JPanel mainPage;
	EditPanel editPanel;
	GamePanel gamePanel;

	private JMenuBar menuSetup(){
		CardLayout lout = (CardLayout)mainPage.getLayout();
		JMenuItem edThis = new JMenuItem("Edit this map");
		edThis.addActionListener(e -> {
			gamePanel.exitGame();
			editPanel.startEditing();
			lout.first(mainPage);
		});
		JMenuItem edNew = new JMenuItem("Create new map");
		JMenuItem edLoad = new JMenuItem("Load map");
		JMenuItem edSave = new JMenuItem("Save");
		JMenuItem edSaveAs = new JMenuItem("Save as");
		JMenuItem pThis = new JMenuItem("Play on this map");
		pThis.addActionListener(e -> {
			gamePanel.startGame();
			editPanel.exitEdit();
			lout.last(mainPage);
		});
		JMenuItem pNew = new JMenuItem("Random new map");
		JMenuItem pCNew = new JMenuItem("Configure random new map");
    JMenuItem pLoad = new JMenuItem("Load map");
		JMenu eMenu = new JMenu("Edit");
		JMenu pMenu = new JMenu("Play");
		eMenu.add(edThis);
		eMenu.add(edNew);
		eMenu.add(edLoad);
		eMenu.add(edSave);
		eMenu.add(edSaveAs);
		pMenu.add(pThis);
		pMenu.add(pNew);
		pMenu.add(pCNew);
		pMenu.add(pLoad);

		JMenuBar mb = new JMenuBar();
		mb.add(eMenu);
		mb.add(pMenu);

		return mb;
	}

	public void createApplication() {
		int dTime = 1000 / 30;
		LabState labState = LabEditControl.generateLabyrinth(new RectLab(20, 20, 0.3), new RectRoomFinder(5));

		mainPage = new JPanel();
		mainPage.setLayout(new CardLayout());
		editPanel = new EditPanel(labState);
		mainPage.add(editPanel);
		editPanel.startEditing();
		gamePanel = new GamePanel(labState);
		mainPage.add(gamePanel);

		JFrame frame = new JFrame();
		frame.add(mainPage);
		frame.setJMenuBar(menuSetup());
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setVisible(true);
	}

	public static void main(String[] args) {
		DisplayGraphics disp = new DisplayGraphics();
		disp.createApplication();
	}
}

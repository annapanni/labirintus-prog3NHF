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

	public void toEditMode() {
		CardLayout lout = (CardLayout)mainPage.getLayout();
		gamePanel.exitGame();
		editPanel.startEditing();
		lout.first(mainPage);
	}

	public void toGameMode() {
		CardLayout lout = (CardLayout)mainPage.getLayout();
		gamePanel.startGame();
		editPanel.exitEdit();
		lout.last(mainPage);
	}

	private JMenuBar menuSetup(){
		JMenuItem edThis = new JMenuItem("Edit this map");
		edThis.addActionListener(e -> toEditMode());
		JMenuItem edNew = new JMenuItem("Create new map");
		JMenuItem edLoad = new JMenuItem("Load map");
		edLoad.addActionListener(e -> SimplePopup.load(this, editPanel).startPopup());
		JMenuItem pThis = new JMenuItem("Play on this map");
		pThis.addActionListener(e -> toGameMode());
		JMenuItem pNew = new JMenuItem("Random new map");
		JMenuItem pCNew = new JMenuItem("Configure random new map");
		JMenuItem pLoad = new JMenuItem("Load map");
		JMenuItem save = new JMenuItem("Save map");
    JMenuItem saveAs = new JMenuItem("Save as...");
		saveAs.addActionListener(e -> SimplePopup.save(editPanel.getLabState()).startPopup()); // TODO not always the active panel

		JMenu eMenu = new JMenu("Edit");
		eMenu.add(edThis);
		eMenu.add(edNew);
		eMenu.add(edLoad);
		JMenu pMenu = new JMenu("Play");
		pMenu.add(pThis);
		pMenu.add(pNew);
		pMenu.add(pCNew);
		pMenu.add(pLoad);
		JMenu sMenu = new JMenu("Save");
		sMenu.add(save);
		sMenu.add(saveAs);

		JMenuBar mb = new JMenuBar();
		mb.add(eMenu);
		mb.add(pMenu);
		mb.add(sMenu);

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

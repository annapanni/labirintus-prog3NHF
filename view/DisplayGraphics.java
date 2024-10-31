package view;

import java.awt.*;
import javax.swing.*;

import java.lang.Thread;
import java.lang.InterruptedException;
import java.io.IOException;

import model.*;
import controller.*;

public class DisplayGraphics{
	JPanel mainPage;
	EditPanel editPanel;
	GamePanel gamePanel;
	ModePanel current;

	public void switchTo(ModePanel m) {
		if (m == current) return;
		CardLayout lout = (CardLayout)mainPage.getLayout();
		if (m == editPanel) {
			gamePanel.exitMode();
			editPanel.startMode();
			lout.first(mainPage);
		} else {
			editPanel.exitMode();
			gamePanel.startMode();
			lout.last(mainPage);
		}
		current = m;
	}

	private JMenuBar menuSetup(){
		JMenuItem edCont = new JMenuItem("Continue edititing");
		edCont.addActionListener(e -> switchTo(editPanel));
		JMenuItem edThis = new JMenuItem("Edit this map");
		edThis.addActionListener(e -> {
			editPanel.setLabState(current.getLabState());
			switchTo(editPanel);
		});
		JMenuItem edNew = new JMenuItem("Create new map");
		JMenuItem edLoad = new JMenuItem("Load map");
		edLoad.addActionListener(e -> SimplePopup.load(this, editPanel).startPopup());
		JMenuItem pCont = new JMenuItem("Continue playing");
		pCont.addActionListener(e -> switchTo(gamePanel));
		JMenuItem pThis = new JMenuItem("Play on this map");
		pThis.addActionListener(e -> {
			gamePanel.setLabState(current.getLabState());
			switchTo(gamePanel);
		});
		JMenuItem pNew = new JMenuItem("Random new map");
		JMenuItem pCNew = new JMenuItem("Configure random new map");
		JMenuItem pLoad = new JMenuItem("Load map");
		pLoad.addActionListener(e -> SimplePopup.load(this, gamePanel).startPopup());
		JMenuItem save = new JMenuItem("Save map");
		save.addActionListener(e -> {
			if (current.getLabState().getName() == null) {
				SimplePopup.save(current.getLabState()).startPopup();
			} else {
				try {FileManager.save(current.getLabState());}
				catch (IOException ex) {SimplePopup.message("Failed to save map").startPopup();}
			}
		});

    JMenuItem saveAs = new JMenuItem("Save as...");
		saveAs.addActionListener(e -> SimplePopup.save(current.getLabState()).startPopup());

		JMenu eMenu = new JMenu("Edit");
		eMenu.add(edCont);
		eMenu.add(edThis);
		eMenu.add(edNew);
		eMenu.add(edLoad);
		JMenu pMenu = new JMenu("Play");
		pMenu.add(pCont);
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
		editPanel = new EditPanel(labState, dTime);
		mainPage.add(editPanel);
		gamePanel = new GamePanel(labState, dTime);
		mainPage.add(gamePanel);
		switchTo(editPanel);

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

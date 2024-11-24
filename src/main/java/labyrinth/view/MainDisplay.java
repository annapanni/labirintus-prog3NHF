package labyrinth.view;

import java.awt.*;
import javax.swing.*;
import java.io.IOException;

import labyrinth.model.*;
import labyrinth.controller.*;

/**The main function of the application, represents the frame the whole application is running in */
public class MainDisplay extends JFrame{
	JPanel mainPage;
	EditPanel editPanel;
	GamePanel gamePanel;
	ModePanel current;

	/**Switched to the specified mode */
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
		edCont.addActionListener(e -> {
			if (editPanel.labState == gamePanel.labState)
				SimplePopup.ask("<html>Are you sure? <br> All unsaved in-game progress will be lost", () -> switchTo(editPanel))
				.startPopup(this);
			else switchTo(editPanel);
		});
		JMenuItem edThis = new JMenuItem("Edit this map");
		edThis.addActionListener(e ->
			SimplePopup.ask("<html>Are you sure? <br> All unsaved in-game progress will be lost", () -> {
				editPanel.setLabState(current.getLabState());
				switchTo(editPanel);
			}).startPopup(this)
		);
		JMenuItem edNew = new JMenuItem("Create new map");
		edNew.addActionListener(e -> (new StructSettings(this, editPanel)).generateAndSet());
		JMenuItem edLoad = new JMenuItem("Load map");
		edLoad.addActionListener(e -> SimplePopup.load(this, editPanel).startPopup(this));
		JMenuItem pCont = new JMenuItem("Continue playing");
		pCont.addActionListener(e -> switchTo(gamePanel));
		JMenuItem pThis = new JMenuItem("Play on this map");
		pThis.addActionListener(e -> {
			gamePanel.setLabState(current.getLabState());
			switchTo(gamePanel);
		});
		JMenuItem pNew = new JMenuItem("Random new map");
		pNew.addActionListener(e -> (new StructSettings(this, gamePanel)).generateAndSet());
		JMenuItem pCNew = new JMenuItem("Configure random new map");
		pCNew.addActionListener(e -> {
			StructSettings sett = new StructSettings(this, gamePanel);
			SimplePopup.from(sett, sett::generateAndSet, "Create").startPopup(this);
		});
		JMenuItem pLoad = new JMenuItem("Load map");
		pLoad.addActionListener(e -> SimplePopup.load(this, gamePanel).startPopup(this));
		JMenuItem save = new JMenuItem("Save map");
		save.addActionListener(e -> {
			if (current.getLabState().getName() == null) {
				SimplePopup.save(current.getLabState()).startPopup(this);
			} else {
				try {FileManager.save(current.getLabState());}
				catch (IOException ex) {SimplePopup.message("Failed to save map").startPopup(this);}
			}
		});
    JMenuItem saveAs = new JMenuItem("Save as...");
		saveAs.addActionListener(e -> SimplePopup.save(current.getLabState()).startPopup(this));
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

	/**Creates and initializes the application's components */
	public MainDisplay() {
		int dTime = 1000 / 30;
		LabState labState = (new StructSettings()).generate();
		mainPage = new JPanel();
		mainPage.setLayout(new CardLayout());
		editPanel = new EditPanel(labState, dTime);
		mainPage.add(editPanel);
		gamePanel = new GamePanel(labState, dTime);
		mainPage.add(gamePanel);
		switchTo(editPanel);
		add(mainPage);
		setJMenuBar(menuSetup());
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gamePanel.init();
		editPanel.init();
		setVisible(true);
		if (LabView.failedToLoadTexture()) {
			SimplePopup.message("<html>Failed to load resources.<br/> Defaulting to basic characterset").startPopup(this);
		}
	}

	/**main functinon, creates an instance of MainDisplay to run the application */
	public static void main(String[] args) {
		System.setProperty("sun.java2d.opengl", "true");
		MainDisplay disp = new MainDisplay();
	}
}

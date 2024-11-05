package view;

import javax.swing.JPanel;
import java.util.Timer;

import model.LabState;


public abstract class ModePanel extends JPanel {
	protected LabState labState;
	protected LabView labView;
	protected Timer timer;
	protected int dTime = 30;

	public LabState getLabState() {return labState;}

	protected ModePanel(LabState l, int dt) {
		labState = l;
		timer = new Timer();
		dTime = dt;
		setFocusable(true);
	}

	public void setLabState(LabState ls){
		labState = ls;
		labView.setLabState(ls);
	}

	public void init() {
		labView.setLabState(labState); //to call centering once jpanel size is determined
	}

	public void startMode() {
		timer = new Timer();
		timer.scheduleAtFixedRate(new CustomTimerTask(labView::repaint), 0l, dTime);
		requestFocusInWindow();
		labView.requestFocus();
	}

	public void exitMode(){
		timer.cancel();
		timer.purge();
	}
}

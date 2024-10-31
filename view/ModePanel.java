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

	public ModePanel(LabState l, int dt) {
		labState = l;
		timer = new Timer();
		dTime = dt;
	}

	public void setLabState(LabState ls){
		labState = ls;
		labView.setLabState(ls);
	}

	public void startMode() {
		timer = new Timer();
		timer.scheduleAtFixedRate(new CustomTimerTask(labView::repaint), 0l, (long)dTime);
	}

	public void exitMode(){
		timer.cancel();
		timer.purge();
	}
}
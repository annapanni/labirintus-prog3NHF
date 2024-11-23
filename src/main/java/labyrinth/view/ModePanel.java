package labyrinth.view;

import javax.swing.JPanel;
import java.util.Timer;

import labyrinth.model.LabState;

/**
 * Abstract panel for different modes the application can run in
 */
public abstract class ModePanel extends JPanel {
	/**Labyrinth the mode is operating on */
	protected LabState labState;
	/**View of the labyrinth the mode is operating on*/
	protected LabView labView;
	/**A timer that performs actions at every refresh */
	protected Timer timer;
	/**Minimum time between too refreshes in miliseconds*/
	protected int dTime = 30;

	public LabState getLabState() {return labState;}

	/**
	 * Creates a new panel
	 * @param ls labyrinth the applicatin is operating on
	 * @param dt time between screen refresh and/or object movements in miliseconds
	 */
	protected ModePanel(LabState l, int dt) {
		labView = new LabView(l, 30);
		labState = l;
		timer = new Timer();
		dTime = dt;
		setFocusable(true);
	}

	/**
	 * Sets the labyrinth to be edited
	 */
	public void setLabState(LabState ls){
		labState = ls;
		labView.setLabState(ls);
	}

	/**
	 * Initializes the size of its components
	 */
	public void init() {
		labView.setLabState(labState); //to call centering once jpanel size is determined
	}

	/**
	 * Actions to perform when this mode is started
	 */
	public void startMode() {
		timer = new Timer();
		timer.scheduleAtFixedRate(new CustomTimerTask(labView::repaint), 0l, dTime);
		requestFocusInWindow();
		labView.requestFocus();
		setLabState(labState); //for initialization
	}

	/**
	 * Actions to perform when this mode is exited
	 */
	public void exitMode(){
		timer.cancel();
		timer.purge();
	}
}

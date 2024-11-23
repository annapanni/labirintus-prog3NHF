package labyrinth.view;

import java.util.TimerTask;

/**
 * A custom timer that can execute a task that implements the Runnable interface
 */
public class CustomTimerTask extends TimerTask {
	Runnable task;

	/**
	 * Create a new CustomTimerTask with a specified task
	 * @param t - task to execute
	 */
	public CustomTimerTask(Runnable t) {
		task = t;
	}

	@Override
	public void run() {
		task.run();
	}

}

package labyrinth.view;

import java.util.TimerTask;

public class CustomTimerTask extends TimerTask {
	Runnable task;

	public CustomTimerTask(Runnable t) {
		task = t;
	}

	@Override
	public void run() {
		task.run();
	}

}

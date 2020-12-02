package elixe.utils.misc;

import java.util.Date;

public class TimerUtils {

	private long time, dif = 0;

	public TimerUtils() {
		time = new Date().getTime();
	}

	public boolean hasTimePassed(int ms) {

		long trueDelay = ms - dif;
		long timeDif = new Date().getTime() - time;

		if (timeDif >= trueDelay) {
			if (ms > timeDif - trueDelay) {
				dif = timeDif - trueDelay;
			} else {
				dif = 0;
			}

			return true;
		} else {
			return false;
		}
	}

	public void reset() {
		time = new Date().getTime();
	}

}

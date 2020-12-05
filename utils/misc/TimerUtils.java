package elixe.utils.misc;

import java.util.Date;

public class TimerUtils {
	public class MilisecondTimer {
		private long time, dif = 0;

		public MilisecondTimer() {
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

	public class TickTimer {
		private int ticks = 0;
		
		public void update() {
			ticks++;			
		}
		
		public boolean hasTimePassed(int passedTicks) {
			if (ticks >= passedTicks) {
				return true;
			} else {
				return false;
			}
		}
		
		public void reset() {
			ticks = 0;
		}
	}
}

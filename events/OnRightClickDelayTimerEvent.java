package elixe.events;

public class OnRightClickDelayTimerEvent {
	private int rightClickDelayTimer;

	public OnRightClickDelayTimerEvent(int rightClickDelayTimer) {
		super();
		this.rightClickDelayTimer = rightClickDelayTimer;
	}

	public int getRightClickDelayTimer() {
		return rightClickDelayTimer;
	}

	public void setRightClickDelayTimer(int rightClickDelayTimer) {
		this.rightClickDelayTimer = rightClickDelayTimer;
	}

}

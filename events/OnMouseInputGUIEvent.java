package elixe.events;

public class OnMouseInputGUIEvent {
	int eventButton;
	boolean eventButtonState;

	public OnMouseInputGUIEvent(int eventButton, boolean eventButtonState) {
		super();
		this.eventButton = eventButton;
		this.eventButtonState = eventButtonState;
	}

	public int getEventButton() {
		return eventButton;
	}

	public void setEventButton(int eventButton) {
		this.eventButton = eventButton;
	}

	public boolean getEventButtonState() {
		return eventButtonState;
	}

	public void setEventButtonState(boolean eventButtonState) {
		this.eventButtonState = eventButtonState;
	}


}

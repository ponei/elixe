package elixe.events;

import me.zero.alpine.event.type.Cancellable;

public class OnOverlayDrawEvent {
	
	private boolean skip = false;
	private int overlayType; //0 = pumpkin, 1 = scoreboard
	
	public void skip() {
		skip = true;
	}

	public boolean shouldSkip() {
		return skip;
	}
	
	public void setType(int type) {
		this.overlayType = type;
		skip = false;
	}
	
	public int getType() {
		return overlayType;
	}
	
	public OnOverlayDrawEvent(int overlayType) {
		super();
		this.overlayType = overlayType;
	} 	
}

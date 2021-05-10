package elixe.events;

//moveEntity(double, double, double) : void - net.minecraft.entity.Entity
//L:632
public class OnGoingToFallEvent {
	private int state;
	
	private boolean shouldBlock = false;
	private boolean willFall = false;

	public OnGoingToFallEvent(int state) {
		super();
		this.state = state;
	}

	public int getState() {
		return state;
	}

	public boolean shouldBlockFall() {
		return shouldBlock;
	}

	public void setShouldBlock(boolean shouldBlock) {
		this.shouldBlock = shouldBlock;
	}

	public void setState(int state) {
		this.state = state;
	}

	public boolean isGoingToFall() {
		return willFall;
	}

	public void setWillFall(boolean willFall) {
		this.willFall = willFall;
	}
	
	
}

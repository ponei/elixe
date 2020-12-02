package elixe.events;

//onLivingUpdate() : void - net.minecraft.client.entity.EntityPlayerSP
//L:741
public class OnLivingUpdateEvent {
	private boolean holdingSprinting;

	public OnLivingUpdateEvent(boolean holdingSprinting) {
		super();
		this.holdingSprinting = holdingSprinting;
	}

	public boolean isHoldingSprinting() {
		return holdingSprinting;
	}

	public void setHoldingSprinting(boolean holdingSprinting) {
		this.holdingSprinting = holdingSprinting;
	}
	
	
}

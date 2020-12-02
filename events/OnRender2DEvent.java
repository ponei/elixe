package elixe.events;

//renderGameOverlay(float) : void - net.minecraft.client.gui.GuiIngame
//L:352
public class OnRender2DEvent {
	private float tickDelta;

	
	public OnRender2DEvent(float tickDelta) {
		this.tickDelta = tickDelta;
	}

	public float getTickDelta() {
		return tickDelta;
	}
}

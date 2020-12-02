package elixe.events;

//renderWorldPass(int, float, long) : void - net.minecraft.client.renderer.EntityRenderer
//L:1912
public class OnRender3DEvent {
	private float tickDelta;

	
	public OnRender3DEvent(float tickDelta) {
		this.tickDelta = tickDelta;
	}

	public float getTickDelta() {
		return tickDelta;
	}
}

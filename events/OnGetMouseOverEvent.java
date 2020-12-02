package elixe.events;

import me.zero.alpine.event.type.Cancellable;
import net.minecraft.client.renderer.EntityRenderer;

//getMouseOver(float) : void - net.minecraft.client.renderer.EntityRenderer
//L:468
public class OnGetMouseOverEvent extends Cancellable {
	private float partialTicks;
	private EntityRenderer entityRenderer;
	
	public OnGetMouseOverEvent(float partialTicks, EntityRenderer entityRenderer) {
		super();
		this.partialTicks = partialTicks;
		this.entityRenderer = entityRenderer;
	}

	public float getPartialTicks() {
		return partialTicks;
	}

	public EntityRenderer getEntityRenderer() {
		return entityRenderer;
	}
}

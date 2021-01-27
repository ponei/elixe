package elixe.events;

import me.zero.alpine.event.type.Cancellable;
import net.minecraft.entity.Entity;

//renderName(T, double, double, double) : void - net.minecraft.client.renderer.entity.RendererLivingEntity
//L:603
public class OnRenderNameEvent extends Cancellable {
	private Entity entity;

	public OnRenderNameEvent(Entity entity) {
		super();
		this.entity = entity;
	}

	public Entity getEntity() {
		return entity;
	}
	
	
}

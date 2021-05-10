package elixe.events;

import me.zero.alpine.event.type.Cancellable;
import net.minecraft.client.renderer.ItemRenderer;

//renderItemInFirstPerson(float) : void - net.minecraft.client.renderer.ItemRenderer
//L:336
public class OnRenderItemFirstPersonEvent extends Cancellable {
	private ItemRenderer itemRenderer;
	private float partialTicks;

	public OnRenderItemFirstPersonEvent(ItemRenderer itemRenderer, float partialTicks) {
		super();
		this.itemRenderer = itemRenderer;
		this.partialTicks = partialTicks;
	}

	public ItemRenderer getItemRenderer() {
		return itemRenderer;
	}

	public float getPartialTicks() {
		return partialTicks;
	}

}

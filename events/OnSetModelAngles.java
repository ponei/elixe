package elixe.events;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;

public class OnSetModelAngles {
	private float scale;
	private ModelBiped model;
	private Entity entity;

	public OnSetModelAngles(float scale, ModelBiped model, Entity entity) {
		super();
		this.scale = scale;
		this.model = model;
		this.entity = entity;
	}

	public float getScale() {
		return scale;
	}

	public ModelBiped getModel() {
		return model;
	}

	public Entity getEntity() {
		return entity;
	}

}

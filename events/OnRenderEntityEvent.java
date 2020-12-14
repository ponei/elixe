package elixe.events;

import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;

//doRender(T, double, double, double, float, float) : void - net.minecraft.client.renderer.entity.RendererLivingEntity
//L:122
public class OnRenderEntityEvent {
	private RendererLivingEntity renderer;
	private EntityLivingBase entity;
	private double x, y, z;
	private float entityYaw, partialTicks;
	private int state;
	
	public RendererLivingEntity getRenderer() {
		return renderer;
	}

	public EntityLivingBase getEntity() {
		return entity;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}

	public float getEntityYaw() {
		return entityYaw;
	}

	public float getPartialTicks() {
		return partialTicks;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	
	public OnRenderEntityEvent(RendererLivingEntity renderer, EntityLivingBase entity, double x,
			double y, double z, float entityYaw, float partialTicks) {
		super();
		this.renderer = renderer;
		this.entity = entity;
		this.x = x;
		this.y = y;
		this.z = z;
		this.entityYaw = entityYaw;
		this.partialTicks = partialTicks;
	}
	
	
}

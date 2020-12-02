package elixe.events;

import me.zero.alpine.event.type.Cancellable;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;

//renderLayers(T, float, float, float, float, float, float, float) : void - net.minecraft.client.renderer.entity.RendererLivingEntity
//L:583
public class OnRenderLayersEvent extends Cancellable {

	RendererLivingEntity rendererLivingEntity;
	EntityLivingBase entitylivingbaseIn;
	float partialTicks;
	float limbSwing, limbSwingAmount;
	float renderAgeInTicks;
	float renderHeadYaw, renderHeadPitch;
	float scale;
	
	public OnRenderLayersEvent(RendererLivingEntity rendererLivingEntity, EntityLivingBase entitylivingbaseIn,
			float partialTicks, float limbSwing, float limbSwingAmount, float renderAgeInTicks, float renderHeadYaw,
			float renderHeadPitch, float scale) {
		super();
		this.rendererLivingEntity = rendererLivingEntity;
		this.entitylivingbaseIn = entitylivingbaseIn;
		this.partialTicks = partialTicks;
		this.limbSwing = limbSwing;
		this.limbSwingAmount = limbSwingAmount;
		this.renderAgeInTicks = renderAgeInTicks;
		this.renderHeadYaw = renderHeadYaw;
		this.renderHeadPitch = renderHeadPitch;
		this.scale = scale;
	}

	public RendererLivingEntity getRendererLivingEntity() {
		return rendererLivingEntity;
	}

	public EntityLivingBase getEntitylivingbaseIn() {
		return entitylivingbaseIn;
	}

	public float getPartialTicks() {
		return partialTicks;
	}

	public float getLimbSwing() {
		return limbSwing;
	}

	public float getLimbSwingAmount() {
		return limbSwingAmount;
	}

	public float getRenderAgeInTicks() {
		return renderAgeInTicks;
	}

	public float getRenderHeadYaw() {
		return renderHeadYaw;
	}

	public float getRenderHeadPitch() {
		return renderHeadPitch;
	}

	public float getScale() {
		return scale;
	}
	
	
}

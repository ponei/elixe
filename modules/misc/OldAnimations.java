package elixe.modules.misc;

import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL11;

import elixe.events.OnRenderItemFirstPersonEvent;
import elixe.events.OnRenderLayersEvent;
import elixe.events.OnTickEvent;
import elixe.modules.Module;
import elixe.modules.ModuleCategory;
import elixe.modules.option.ModuleBoolean;
import elixe.modules.option.ModuleFloat;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemMap;
import net.minecraft.src.Config;
import net.optifine.EmissiveTextures;
import net.optifine.shaders.Shaders;

public class OldAnimations extends Module {

	public OldAnimations() {
		super("Old Animations", ModuleCategory.MISC);
		
		moduleOptions.add(oldBlockHitOption);
		moduleOptions.add(oldArmorAnimationOption);
		moduleOptions.add(redIntensityOption);
	}

	boolean oldBlockHit = false;
	ModuleBoolean oldBlockHitOption = new ModuleBoolean("old block hit", false) {
		public void valueChanged() {
			oldBlockHit = (boolean) this.getValue();
		}
	};
	
	boolean oldArmorAnimation = false;
	ModuleBoolean oldArmorAnimationOption = new ModuleBoolean("old armor animation", false) {
		public void valueChanged() {
			oldArmorAnimation = (boolean) this.getValue();
		}
	};
	
	float redIntensity = 0.3f;
	ModuleFloat redIntensityOption = new ModuleFloat("red intensity", 0.3f, 0f, 1f) {
		public void valueChanged() {
			redIntensity = (float) this.getValue();
		}
	};

	@EventHandler
	private Listener<OnRenderItemFirstPersonEvent> onRenderItemFirstPersonEvent = new Listener<>(e -> {
		e.cancel();
		ItemRenderer itemRenderer = e.getItemRenderer();
		float partialTicks = e.getPartialTicks();
		
		if (!Config.isShaders() || !Shaders.isSkipRenderHand())
        {
            float f = 1.0F - (itemRenderer.prevEquippedProgress + (itemRenderer.equippedProgress - itemRenderer.prevEquippedProgress) * partialTicks);
            AbstractClientPlayer abstractclientplayer = this.mc.thePlayer;
            float f1 = abstractclientplayer.getSwingProgress(partialTicks);
            float f2 = abstractclientplayer.prevRotationPitch + (abstractclientplayer.rotationPitch - abstractclientplayer.prevRotationPitch) * partialTicks;
            float f3 = abstractclientplayer.prevRotationYaw + (abstractclientplayer.rotationYaw - abstractclientplayer.prevRotationYaw) * partialTicks;
            itemRenderer.func_178101_a(f2, f3);
            itemRenderer.func_178109_a(abstractclientplayer);
            itemRenderer.func_178110_a((EntityPlayerSP)abstractclientplayer, partialTicks);
            GlStateManager.enableRescaleNormal();
            GlStateManager.pushMatrix();

            if (itemRenderer.itemToRender != null)
            {
                if (itemRenderer.itemToRender.getItem() instanceof ItemMap)
                {
                	itemRenderer.renderItemMap(abstractclientplayer, f2, f, f1);
                }
                else if (abstractclientplayer.getItemInUseCount() > 0)
                {
                    EnumAction enumaction = itemRenderer.itemToRender.getItemUseAction();

                    switch (enumaction)
                    {
                        case NONE:
                        	itemRenderer.transformFirstPersonItem(f, 0.0F);
                            break;

                        case EAT:
                        case DRINK:
                        	itemRenderer.func_178104_a(abstractclientplayer, partialTicks);
                        	itemRenderer.transformFirstPersonItem(f, oldBlockHit ? f1 : 0.0F);
                            break;

                        case BLOCK:
                        	itemRenderer.transformFirstPersonItem(f, oldBlockHit ? f1 : 0.0F);
                        	itemRenderer.func_178103_d();
                            break;

                        case BOW:
                        	itemRenderer.transformFirstPersonItem(f, oldBlockHit ? f1 : 0.0F);
                        	itemRenderer.func_178098_a(partialTicks, abstractclientplayer);
                    }
                }
                else
                {
                	itemRenderer.func_178105_d(f1);
                	itemRenderer.transformFirstPersonItem(f, f1);
                }

                itemRenderer.renderItem(abstractclientplayer, itemRenderer.itemToRender, ItemCameraTransforms.TransformType.FIRST_PERSON);
            }
            else if (!abstractclientplayer.isInvisible())
            {
            	itemRenderer.func_178095_a(abstractclientplayer, f, f1);
            }

            GlStateManager.popMatrix();
            GlStateManager.disableRescaleNormal();
            RenderHelper.disableStandardItemLighting();
        }
	});
	
	@EventHandler
	private Listener<OnRenderLayersEvent> onRenderLayersEvent = new Listener<>(e -> {
		e.cancel();
		RendererLivingEntity<EntityLivingBase> rendererLiving = e.getRendererLivingEntity();
		float partialTicks = e.getPartialTicks();
		EntityLivingBase entitylivingbaseIn = e.getEntitylivingbaseIn();
		 
		for (LayerRenderer<EntityLivingBase> layerrenderer : rendererLiving.layerRenderers) {
			boolean flag;
			if (layerrenderer instanceof LayerBipedArmor) { //layer de armadura
				flag = setBrightness(rendererLiving, entitylivingbaseIn, partialTicks, oldArmorAnimation ? true : layerrenderer.shouldCombineTextures());
			} else {
				flag = setBrightness(rendererLiving, entitylivingbaseIn, partialTicks, layerrenderer.shouldCombineTextures());
			}
			 

			if (EmissiveTextures.isActive()) {
				EmissiveTextures.beginRender();
			}

			if (rendererLiving.renderLayersPushMatrix) {
				GlStateManager.pushMatrix();
			}

			layerrenderer.doRenderLayer(entitylivingbaseIn, e.getLimbSwing(), e.getLimbSwingAmount(), partialTicks, e.getRenderAgeInTicks(),
					e.getRenderHeadYaw(), e.getRenderHeadPitch(), e.getScale());

			if (rendererLiving.renderLayersPushMatrix) {
				GlStateManager.popMatrix();
			}


			if (flag) {
				rendererLiving.unsetBrightness();
			}
		}
	});
	
	public boolean setBrightness(RendererLivingEntity<EntityLivingBase> rendererLiving, EntityLivingBase entitylivingbaseIn, float partialTicks, boolean combineTextures)
    {
        float f = entitylivingbaseIn.getBrightness(partialTicks);
        int i = rendererLiving.getColorMultiplier(entitylivingbaseIn, f, partialTicks);
        boolean flag = (i >> 24 & 255) > 0;
        boolean flag1 = entitylivingbaseIn.hurtTime > 0 || entitylivingbaseIn.deathTime > 0;

        if (!flag && !flag1)
        {
            return false;
        }
        else if (!flag && !combineTextures)
        {
            return false;
        }
        else
        {
            GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
            GlStateManager.enableTexture2D();
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, OpenGlHelper.GL_COMBINE);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_COMBINE_RGB, GL11.GL_MODULATE);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.defaultTexUnit);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PRIMARY_COLOR);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND0_RGB, GL11.GL_SRC_COLOR);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND1_RGB, GL11.GL_SRC_COLOR);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_COMBINE_ALPHA, GL11.GL_REPLACE);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.defaultTexUnit);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND0_ALPHA, GL11.GL_SRC_ALPHA);
            GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
            GlStateManager.enableTexture2D();
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, OpenGlHelper.GL_COMBINE);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_COMBINE_RGB, OpenGlHelper.GL_INTERPOLATE);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.GL_CONSTANT);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PREVIOUS);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE2_RGB, OpenGlHelper.GL_CONSTANT);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND0_RGB, GL11.GL_SRC_COLOR);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND1_RGB, GL11.GL_SRC_COLOR);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND2_RGB, GL11.GL_SRC_ALPHA);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_COMBINE_ALPHA, GL11.GL_REPLACE);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.GL_PREVIOUS);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND0_ALPHA, GL11.GL_SRC_ALPHA);
            rendererLiving.brightnessBuffer.position(0);

            if (flag1)
            {
            	rendererLiving.brightnessBuffer.put(1.0F);
            	rendererLiving.brightnessBuffer.put(0.0F);
            	rendererLiving.brightnessBuffer.put(0.0F);
            	rendererLiving.brightnessBuffer.put(redIntensity);

                if (Config.isShaders())
                {
                    Shaders.setEntityColor(1.0F, 0.0F, 0.0F, 0.3F);
                }
            }
            else
            {
                float f1 = (float)(i >> 24 & 255) / 255.0F;
                float f2 = (float)(i >> 16 & 255) / 255.0F;
                float f3 = (float)(i >> 8 & 255) / 255.0F;
                float f4 = (float)(i & 255) / 255.0F;
                rendererLiving.brightnessBuffer.put(f2);
                rendererLiving.brightnessBuffer.put(f3);
                rendererLiving.brightnessBuffer.put(f4);
                rendererLiving.brightnessBuffer.put(1.0F - f1);

                if (Config.isShaders())
                {
                    Shaders.setEntityColor(f2, f3, f4, 1.0F - f1);
                }
            }

            rendererLiving.brightnessBuffer.flip();
            GL11.glTexEnv(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_COLOR, (FloatBuffer)rendererLiving.brightnessBuffer);
            GlStateManager.setActiveTexture(OpenGlHelper.GL_TEXTURE2);
            GlStateManager.enableTexture2D();
            GlStateManager.bindTexture(rendererLiving.field_177096_e.getGlTextureId());
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, OpenGlHelper.GL_COMBINE);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_COMBINE_RGB, GL11.GL_MODULATE);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.GL_PREVIOUS);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.lightmapTexUnit);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND0_RGB, GL11.GL_SRC_COLOR);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND1_RGB, GL11.GL_SRC_COLOR);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_COMBINE_ALPHA, GL11.GL_REPLACE);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.GL_PREVIOUS);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND0_ALPHA, GL11.GL_SRC_ALPHA);
            GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
            return true;
        }
    }
}

package elixe.modules.render;

import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL11;

import elixe.events.OnBrightnessEntityEvent;
import elixe.events.OnRender3DEvent;
import elixe.events.OnRenderEntityEvent;
import elixe.events.OnTickEvent;
import elixe.modules.AModuleOption;
import elixe.modules.Module;
import elixe.modules.ModuleCategory;
import elixe.modules.option.ModuleArray;
import elixe.modules.option.ModuleArrayMultiple;
import elixe.modules.option.ModuleBoolean;
import elixe.modules.option.ModuleColor;
import elixe.modules.option.ModuleKey;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.src.Config;
import net.optifine.shaders.Shaders;

public class Chams extends Module {

	public Chams() {
		super("Chams", ModuleCategory.RENDER);
		moduleOptions.add(allowedEntitiesOption);

		moduleOptions.add(lightOption);

		moduleOptions.add(useColorOption);
		moduleOptions.add(coloringModeOption);
		moduleOptions.add(visibleColorOption);
		moduleOptions.add(invisibleColorOption);
		moduleOptions.add(flatColorOption);
		moduleOptions.add(hurtColorOption);

	}

	boolean[] allowedEntities;
	ModuleArrayMultiple allowedEntitiesOption = new ModuleArrayMultiple("allowed entities", new boolean[] { true, false, false, false },
			new String[] { "player", "animal", "monster", "villager" }) {
		public void valueChanged() {
			allowedEntities = (boolean[]) this.getValue();
		}
	};
	
	

	boolean addLight;
	ModuleBoolean lightOption = new ModuleBoolean("add light", false) {
		public void valueChanged() {
			addLight = (boolean) this.getValue();
		}
	};

	boolean useColor;
	ModuleBoolean useColorOption = new ModuleBoolean("use color", false) {
		public void valueChanged() {
			useColor = (boolean) this.getValue();
		}
	};

	float[] visibleColor;
	ModuleColor visibleColorOption = new ModuleColor("visible color", 255, 0, 0) {
		public void valueChanged() {
			visibleColor = this.getGLRGB();
		}
	};

	float[] invisibleColor;
	ModuleColor invisibleColorOption = new ModuleColor("invisible color", 150, 0, 0) {
		public void valueChanged() {
			invisibleColor = this.getGLRGB();
		}
	};

	boolean flatColor;
	ModuleBoolean flatColorOption = new ModuleBoolean("flat color", false) {
		public void valueChanged() {
			flatColor = (boolean) this.getValue();
		}
	};

	boolean hurtColor;
	ModuleBoolean hurtColorOption = new ModuleBoolean("hurt color", false) {
		public void valueChanged() {
			hurtColor = (boolean) this.getValue();
		}
	};
	
	//color, buffer
	AModuleOption[][] coloringModuleOptions = new AModuleOption[][] { {flatColorOption, hurtColorOption}, {} };
	
	int coloringMode;
	ModuleArray coloringModeOption = new ModuleArray("coloring mode", 0, new String[] { "color", "buffer" }, true) {
		public void valueChanged() {
			coloringMode = (int) this.getValue();
			for (int i = 0; i < coloringModuleOptions.length; i++) {			
				for (int j = 0; j < coloringModuleOptions[i].length; j++) {
					if (i == coloringMode) {
						coloringModuleOptions[i][j].setShow(true);
					} else {
						coloringModuleOptions[i][j].setShow(false);
					}
					
				}
			}
		}
	};

	@EventHandler
	private Listener<OnTickEvent> onTickEvent = new Listener<>(e -> {
		if (mc.theWorld == null)
			return;
		for (Entity ent : mc.theWorld.loadedEntityList) {
			if (!ent.rendered) {
				continue;
			}
			if (ent == mc.thePlayer) {
				continue;
			}
			ent.chamsAllowed = ((ent instanceof EntityPlayer && allowedEntities[0]) || (ent instanceof EntityAnimal && allowedEntities[1])
					|| ((ent instanceof EntityMob || ent instanceof EntitySlime) && allowedEntities[2])
					|| (ent instanceof EntityVillager && allowedEntities[3]));

		}
	});

	@EventHandler
	private Listener<OnBrightnessEntityEvent> onBrightnessEntityEvent = new Listener<>(e -> {
		if (addLight && e.getEnt().chamsAllowed) {
			e.setLight(13631696);
		}
	});

	@EventHandler
	private Listener<OnRenderEntityEvent> onRenderEntityEvent = new Listener<>(e -> {
		if (e.getEntity().chamsAllowed) {
			switch (e.getState()) {
			case 0: // pre all
				// nao da setup quando tem cor, o depth test ja faz esse trabalho
				// a diferença é que com o polygon fill não tem como fazer o efeito de duas
				// cores do depth
				if (!useColor) {
					preRender();
				}
				break;

			case 1: // model
				if (useColor) {
					e.setAlreadyRendered(true); // ignora na classe
					boolean hurt = false;
					

					GL11.glPushMatrix();

					// teste de profundidade
					GL11.glDisable(GL11.GL_DEPTH_TEST);
				

					if (flatColor) { // ignora luz
						GL11.glDisable(GL11.GL_LIGHTING);
					}

					
					switch (coloringMode) {
					case 0:
						// setup
						GL11.glDisable(GL11.GL_TEXTURE_2D);
						
						if (hurtColor) {
							hurt = e.setBrightness();
						}
						
						GL11.glColor3f(invisibleColor[0], invisibleColor[1], invisibleColor[2]); // invis color
						e.renderModel();
						
						GL11.glColor3f(visibleColor[0], visibleColor[1], visibleColor[2]); // vis cor
						GL11.glEnable(GL11.GL_DEPTH_TEST); //profundidade
						e.renderModel();
						
						// setup reset
						

						GL11.glEnable(GL11.GL_TEXTURE_2D);
						GL11.glColor4f(1, 1, 1, 1);
						
						if (hurt) {
							e.unsetBrightness();
						}
						break;

					case 1:				
						setColor(e.getRenderer(), invisibleColor[0], invisibleColor[1], invisibleColor[2]); // invis color
						
						e.renderModel();
						unsetColor();		
						
						setColor(e.getRenderer(), visibleColor[0], visibleColor[1], visibleColor[2]); // vis cor	
						
						GL11.glEnable(GL11.GL_DEPTH_TEST); //profundidade
						e.renderModel();
						unsetColor();
						break;
					}


					if (flatColor) { // reset na luz
						GL11.glEnable(GL11.GL_LIGHTING);
					}

					GL11.glPopMatrix();
				}
				break;

			case 2: // layers
				if (useColor) { // nao foi usado cor entao o check la de cima falhou
					preRender();
				}
				break;

			case 3: // post all
				postRender();
				break;

			default:
				break;
			}
		}
	});

	private void setColor(RendererLivingEntity<EntityLivingBase> renderer, float r, float g, float b) {
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
		renderer.brightnessBuffer.position(0);

		renderer.brightnessBuffer.put(r);
		renderer.brightnessBuffer.put(g);
		renderer.brightnessBuffer.put(b);
		renderer.brightnessBuffer.put(1F);

		renderer.brightnessBuffer.flip();
		GL11.glTexEnv(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_COLOR, (FloatBuffer) renderer.brightnessBuffer);
		GlStateManager.setActiveTexture(OpenGlHelper.GL_TEXTURE2);
		GlStateManager.enableTexture2D();
		GlStateManager.bindTexture(renderer.field_177096_e.getGlTextureId());
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
	}

	private void unsetColor() {
		GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
		GlStateManager.enableTexture2D();
		GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, OpenGlHelper.GL_COMBINE);
		GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_COMBINE_RGB, GL11.GL_MODULATE);
		GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.defaultTexUnit);
		GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PRIMARY_COLOR);
		GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND0_RGB, GL11.GL_SRC_COLOR);
		GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND1_RGB, GL11.GL_SRC_COLOR);
		GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_COMBINE_ALPHA, GL11.GL_MODULATE);
		GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.defaultTexUnit);
		GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE1_ALPHA, OpenGlHelper.GL_PRIMARY_COLOR);
		GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND0_ALPHA, GL11.GL_SRC_ALPHA);
		GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND1_ALPHA, GL11.GL_SRC_ALPHA);
		GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, OpenGlHelper.GL_COMBINE);
		GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_COMBINE_RGB, GL11.GL_MODULATE);
		GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND0_RGB, GL11.GL_SRC_COLOR);
		GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND1_RGB, GL11.GL_SRC_COLOR);
		GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE0_RGB, GL11.GL_TEXTURE);
		GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PREVIOUS);
		GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_COMBINE_ALPHA, GL11.GL_MODULATE);
		GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND0_ALPHA, GL11.GL_SRC_ALPHA);
		GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE0_ALPHA, GL11.GL_TEXTURE);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.setActiveTexture(OpenGlHelper.GL_TEXTURE2);
		GlStateManager.disableTexture2D();
		GlStateManager.bindTexture(0);
		GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, OpenGlHelper.GL_COMBINE);
		GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_COMBINE_RGB, GL11.GL_MODULATE);
		GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND0_RGB, GL11.GL_SRC_COLOR);
		GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND1_RGB, GL11.GL_SRC_COLOR);
		GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE0_RGB, GL11.GL_TEXTURE);
		GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PREVIOUS);
		GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_COMBINE_ALPHA, GL11.GL_MODULATE);
		GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND0_ALPHA, GL11.GL_SRC_ALPHA);
		GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE0_ALPHA, GL11.GL_TEXTURE);
		GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
	}

	private void preRender() {

		GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
		GL11.glPolygonOffset(1.0F, -1000000F);
	}

	private void postRender() {
		GL11.glPolygonOffset(1.0F, 1000000F);
		GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
	}
}

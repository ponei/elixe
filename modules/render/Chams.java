package elixe.modules.render;

import org.lwjgl.opengl.GL11;

import elixe.events.OnBrightnessEntityEvent;
import elixe.events.OnRender3DEvent;
import elixe.events.OnRenderEntityEvent;
import elixe.events.OnTickEvent;
import elixe.modules.Module;
import elixe.modules.ModuleCategory;
import elixe.modules.option.ModuleArrayMultiple;
import elixe.modules.option.ModuleBoolean;
import elixe.modules.option.ModuleColor;
import elixe.modules.option.ModuleKey;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;

public class Chams extends Module {

	public Chams() {
		super("Chams", ModuleCategory.RENDER);
		moduleOptions.add(allowedEntitiesOption);

		moduleOptions.add(lightOption);

		moduleOptions.add(useColorOption);
		moduleOptions.add(flatColorOption);
		moduleOptions.add(hurtColorOption);
		moduleOptions.add(visibleColorOption);
		moduleOptions.add(invisibleColorOption);
	}

	boolean[] allowedEntities;
	ModuleArrayMultiple allowedEntitiesOption = new ModuleArrayMultiple("allowed entities",
			new boolean[] { true, false, false, false }, new String[] { "player", "animal", "monster", "villager" }) {
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
			ent.chamsAllowed = ((ent instanceof EntityPlayer && allowedEntities[0])
					|| (ent instanceof EntityAnimal && allowedEntities[1])
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
					if (hurtColor) {
						hurt = e.setBrightness();
					}

					GL11.glPushMatrix();

					// teste de profundidade
					GL11.glDisable(GL11.GL_DEPTH_TEST);

					// setup
					GL11.glDisable(GL11.GL_TEXTURE_2D);
					GL11.glEnable(GL11.GL_BLEND);

					if (flatColor) { // ignora luz
						GL11.glDisable(GL11.GL_LIGHTING);
					}

					GL11.glColor3f(invisibleColor[0], invisibleColor[1], invisibleColor[2]); // invis color

					e.renderModel();

					GL11.glColor3f(visibleColor[0], visibleColor[1], visibleColor[2]); // vis cor

					// ativa profundidade
					GL11.glEnable(GL11.GL_DEPTH_TEST);

					e.renderModel();

					// setup reset
					GL11.glEnable(GL11.GL_TEXTURE_2D);
					GL11.glDisable(GL11.GL_BLEND);
					GL11.glColor4f(1, 1, 1, 1);

					if (flatColor) { // reset na luz
						GL11.glEnable(GL11.GL_LIGHTING);
					}

					GL11.glPopMatrix();
					
					if (hurt) {
						e.unsetBrightness();
					}

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

	public void preRender() {

		GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
		GL11.glPolygonOffset(1.0F, -1000000F);
	}

	public void postRender() {
		GL11.glPolygonOffset(1.0F, 1000000F);
		GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
	}
}

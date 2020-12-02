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
	}

	boolean[] allowedEntities = { true, false, false, false };
	ModuleArrayMultiple allowedEntitiesOption = new ModuleArrayMultiple("allowed entities",
			new boolean[] { true, false, false, false }, new String[] { "player", "animal", "monster", "villager" }) {
		public void valueChanged() {
			allowedEntities = (boolean[]) this.getValue();
		}
	};

	boolean addLight = false;
	ModuleBoolean lightOption = new ModuleBoolean("add light", false) {
		public void valueChanged() {
			addLight = (boolean) this.getValue();
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
			case 0:
				preRender();
				break;

			case 2:
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

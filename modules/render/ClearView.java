package elixe.modules.render;

import elixe.events.OnFireFirstPersonEvent;
import elixe.events.OnNauseaScaleEvent;
import elixe.events.OnOverlayDrawEvent;
import elixe.events.OnTickEvent;
import elixe.modules.Module;
import elixe.modules.ModuleCategory;
import elixe.modules.option.ModuleBoolean;
import elixe.modules.option.ModuleFloat;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;

public class ClearView extends Module {

	public ClearView() {
		super("Clear View", ModuleCategory.RENDER);

		moduleOptions.add(hideScoreboardOption);
		moduleOptions.add(fireHeightOption);
		moduleOptions.add(ignorePumpkinOption);
		moduleOptions.add(ignoreNauseaOption);
	}

	boolean hideScoreboard;
	ModuleBoolean hideScoreboardOption = new ModuleBoolean("hide scoreboard", false) {
		public void valueChanged() {
			hideScoreboard = (boolean) this.getValue();
		}
	};

	float fireHeight;
	ModuleFloat fireHeightOption = new ModuleFloat("fire height", -0.7f, -1f, 0f) {
		public void valueChanged() {
			fireHeight = (float) this.getValue();
		}
	};

	boolean ignoreNausea;
	ModuleBoolean ignoreNauseaOption = new ModuleBoolean("ignore nausea", false) {
		public void valueChanged() {
			ignoreNausea = (boolean) this.getValue();
		}
	};

	boolean ignorePumpkin;
	ModuleBoolean ignorePumpkinOption = new ModuleBoolean("ignore pumpkin", false) {
		public void valueChanged() {
			ignorePumpkin = (boolean) this.getValue();
		}
	};

	@EventHandler
	private Listener<OnFireFirstPersonEvent> onFireFirstPersonEvent = new Listener<>(e -> {
		e.setHeight(fireHeight);
	});

	@EventHandler
	private Listener<OnOverlayDrawEvent> onOverlayDrawEvent = new Listener<>(e -> {
		switch (e.getType()) {
		case 0:
			if (ignorePumpkin) {
				e.skip();
			}
			break;

		case 1:
			if (hideScoreboard) {
				e.skip();
			}
			break;
		}

	});

	@EventHandler
	private Listener<OnNauseaScaleEvent> onNauseaScaleEvent = new Listener<>(e -> {
		if (ignoreNausea) {
			e.cancel();
		}
	});
}

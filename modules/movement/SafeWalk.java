package elixe.modules.movement;

import org.lwjgl.input.Keyboard;

import elixe.events.OnGoingToFallEvent;
import elixe.events.OnLivingUpdateEvent;
import elixe.events.OnPlayerMoveStateEvent;
import elixe.modules.Module;
import elixe.modules.ModuleCategory;
import elixe.modules.option.ModuleBoolean;
import elixe.modules.option.ModuleFloat;
import elixe.modules.option.ModuleInteger;
import elixe.ui.clickgui.ElixeMenu;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.gui.GuiChat;

public class SafeWalk extends Module {
	public SafeWalk() {
		super("SafeWalk", ModuleCategory.MOVEMENT);

		moduleOptions.add(onAirOption);
		moduleOptions.add(sneakAtEdgeOption);
		moduleOptions.add(sneakCooldownOption);
		moduleOptions.add(pitchRequirementOption);
		moduleOptions.add(minimumPitchOption);
	}

	boolean onAir = false;
	ModuleBoolean onAirOption = new ModuleBoolean("on air", false) {
		public void valueChanged() {
			onAir = (boolean) this.getValue();
		}
	};

	boolean sneakAtEdge = false;
	ModuleBoolean sneakAtEdgeOption = new ModuleBoolean("sneak at edge", false) {
		public void valueChanged() {
			sneakAtEdge = (boolean) this.getValue();
		}
	};

	int sneakCooldown;
	ModuleInteger sneakCooldownOption = new ModuleInteger("sneak cooldown", 2, 0, 6) {
		public void valueChanged() {
			sneakCooldown = (int) this.getValue();
		}
	};
	
	boolean pitchRequirement = false;
	ModuleBoolean pitchRequirementOption = new ModuleBoolean("require pitch", false) {
		public void valueChanged() {
			pitchRequirement = (boolean) this.getValue();
		}
	};

	float minimumPitch;
	ModuleFloat minimumPitchOption = new ModuleFloat("minimum pitch", 40f, 0f, 90f) {
		public void valueChanged() {
			minimumPitch = (float) this.getValue();
		}
	};

	public void onEnable() {
		super.onEnable();
		sneakCool = 0;
		shouldSneak = false;
	}
	
	boolean shouldSneak = false;
	int sneakCool = 0;
	@EventHandler
	private Listener<OnGoingToFallEvent> onGoingToFallEvent = new Listener<>(e -> {
		switch (e.getState()) {
		case 0:
			if (!onAir) {
				if (!mc.thePlayer.onGround) {
					return;
				}
			}

			if (pitchRequirement) {
				if (minimumPitch > mc.thePlayer.rotationPitch) {
					return;
				}
			}
			
			e.setShouldBlock(true);
			break;

		case 1:
			sneakCool--;
			if (e.isGoingToFall()) {
				shouldSneak = true;
				sneakCool = sneakCooldown;
			} else {
				if (0 >= sneakCool) {
					shouldSneak = false;
				} 
			}
			
			break;
		}

	});

	@EventHandler
	private Listener<OnPlayerMoveStateEvent> onPlayerMoveStateEvent = new Listener<>(e -> {
		if (shouldSneak && sneakAtEdge) {
			e.setSneak(true);
		}
	});
}

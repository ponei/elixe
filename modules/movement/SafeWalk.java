package elixe.modules.movement;

import org.lwjgl.input.Keyboard;

import elixe.events.OnGoingToFallEvent;
import elixe.events.OnPlayerMoveStateEvent;
import elixe.modules.Module;
import elixe.modules.ModuleCategory;
import elixe.modules.option.ModuleBoolean;
import elixe.modules.option.ModuleFloat;
import elixe.modules.option.ModuleInteger;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;

public class SafeWalk extends Module {
	public SafeWalk() {
		super("SafeWalk", ModuleCategory.MOVEMENT);

		moduleOptions.add(onAirOption);
		moduleOptions.add(sneakAtEdgeOption);
		moduleOptions.add(sneakCooldownOption);
		moduleOptions.add(needPitchOption);
		moduleOptions.add(minimumPitchOption);
		moduleOptions.add(needBlockOption);
		moduleOptions.add(needBackwardsOption);
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

	boolean needPitch = false;
	ModuleBoolean needPitchOption = new ModuleBoolean("require pitch", false) {
		
		public void valueChanged() {
			needPitch = (boolean) this.getValue();
		}
	};

	float minimumPitch;
	ModuleFloat minimumPitchOption = new ModuleFloat("minimum pitch", 40f, 0f, 90f) {
		
		public void valueChanged() {
			minimumPitch = (float) this.getValue();
		}
	};

	boolean needBlock = false;
	ModuleBoolean needBlockOption = new ModuleBoolean("holding block", false) {
		
		public void valueChanged() {
			needBlock = (boolean) this.getValue();
		}
	};

	boolean needBackwards = false;
	ModuleBoolean needBackwardsOption = new ModuleBoolean("holding backwards", false) {
		
		public void valueChanged() {
			needBackwards = (boolean) this.getValue();
		}
	};

	
	public void onEnable() {
		super.onEnable();
		sneakCool = 0;
		shouldSneak = false;
	}

	boolean shouldSneak = false;
	int sneakCool = 0, failsafe = 4;
	@EventHandler
	private Listener<OnGoingToFallEvent> onGoingToFallEvent = new Listener<>(e -> {
		switch (e.getState()) {
		case 0:
			if (!onAir) {
				if (!mc.thePlayer.onGround) {
					return;
				}
			}

			if (needPitch) {
				if (minimumPitch > mc.thePlayer.rotationPitch) {
					return;
				}
			}

			if (needBlock) {
				if (!conditionals.isHoldingBlock()) {
					return;
				}
			}

			if (needBackwards) {
				if (!Keyboard.isKeyDown(mc.gameSettings.keyBindBack.getKeyCode())) {
					return;
				}
			}

			e.setShouldBlock(true);
			break;

		case 1:
			sneakCool--;
			
			//vai cair
			if (e.isGoingToFall()) {
				//devemos bloquear
				if (e.shouldBlockFall()) {
					shouldSneak = true;
					sneakCool = sneakCooldown;
					failsafe = 4;
				} else {
					//nao devemos bloquear, mas vai cair ainda
					//se o ultimo estado ainda for sneak, continua por mais 4 ticks
					//failsafe caso delay de sneak for mt baixo
					if (shouldSneak) {
						failsafe--;
						if (0 >= failsafe) {
							shouldSneak = false;
						}
					}
				}				
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

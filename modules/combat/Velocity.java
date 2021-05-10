package elixe.modules.combat;

import java.util.Random;

import elixe.events.OnPacketReceiveEvent;
import elixe.modules.Module;
import elixe.modules.ModuleCategory;
import elixe.modules.option.ModuleBoolean;
import elixe.modules.option.ModuleFloat;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.play.server.S12PacketEntityVelocity;

public class Velocity extends Module {
	public Velocity() {
		super("Velocity", ModuleCategory.COMBAT);

		moduleOptions.add(activationChanceOption);

		moduleOptions.add(horizontalMultiplierOption);
		moduleOptions.add(verticalMultiplierOption);

		moduleOptions.add(needSprintOption);
		moduleOptions.add(needSpeedOption);
		moduleOptions.add(needAttackButtonOption);
		moduleOptions.add(needWeaponOption);

		moduleOptions.add(waterCheckOption);
		moduleOptions.add(airCheckOption);
	}

	float activationChance;
	ModuleFloat activationChanceOption = new ModuleFloat("chance to reduce", 80f, 0f, 100f) {
		
		public void valueChanged() {
			activationChance = (float) this.getValue();
		}
	};

	float horizontalMultiplier;
	ModuleFloat horizontalMultiplierOption = new ModuleFloat("horizontal multiplier", 0.9f, 0f, 1f) {
		
		public void valueChanged() {
			horizontalMultiplier = (float) this.getValue();
		}
	};

	float verticalMultiplier;
	ModuleFloat verticalMultiplierOption = new ModuleFloat("vertical multiplier", 0.9f, 0f, 1f) {
		
		public void valueChanged() {
			verticalMultiplier = (float) this.getValue();
		}
	};

	boolean needSprint;
	ModuleBoolean needSprintOption = new ModuleBoolean("require sprint", false) {
		
		public void valueChanged() {
			needSprint = (boolean) this.getValue();
		}
	};

	boolean needSpeed;
	ModuleBoolean needSpeedOption = new ModuleBoolean("require speed", false) {
		
		public void valueChanged() {
			needSpeed = (boolean) this.getValue();
		}
	};

	boolean needAttackButton;
	ModuleBoolean needAttackButtonOption = new ModuleBoolean("require attack button", false) {
		
		public void valueChanged() {
			needAttackButton = (boolean) this.getValue();
		}
	};

	boolean needWeapon;
	ModuleBoolean needWeaponOption = new ModuleBoolean("require weapon", false) {
		
		public void valueChanged() {
			needWeapon = (boolean) this.getValue();
		}
	};

	boolean waterCheck;
	ModuleBoolean waterCheckOption = new ModuleBoolean("water check", false) {
		
		public void valueChanged() {
			waterCheck = (boolean) this.getValue();
		}
	};

	boolean airCheck;
	ModuleBoolean airCheckOption = new ModuleBoolean("air check", false) {
		
		public void valueChanged() {
			airCheck = (boolean) this.getValue();
		}
	};

	Random r = new Random();
	@EventHandler
	private Listener<OnPacketReceiveEvent> onPacketReceiveEvent = new Listener<>(e -> {
		if (e.getPacket() instanceof S12PacketEntityVelocity) {
			S12PacketEntityVelocity vel = (S12PacketEntityVelocity) e.getPacket();
			if (vel.getEntityID() == mc.thePlayer.getEntityId()) {
				if (shouldModifyVelocity()) {
					if (activationChance > r.nextFloat() * 100f) {
						vel.setMotionX((int) (vel.getMotionX() * horizontalMultiplier));
						vel.setMotionZ((int) (vel.getMotionZ() * horizontalMultiplier));

						vel.setMotionY((int) (vel.getMotionY() * verticalMultiplier));

						e.setPacket(vel);
					}
				}
			}
		}
	});

	private boolean shouldModifyVelocity() {

		if (needSprint) {
			if (!conditionals.isSprinting()) {
				return false;
			}
		}

		if (needSpeed) {
			if (!conditionals.hasSpeed()) {
				return false;
			}
		}

		if (needAttackButton) {
			if (!conditionals.isHoldingAttack()) {
				return false;
			}
		}

		if (needWeapon) {
			if (!conditionals.isHoldingWeapon()) {
				return false;
			}
		}

		if (waterCheck) {
			if (conditionals.isInWater()) {
				return false;
			}
		}

		if (airCheck) {
			if (conditionals.isOnAir()) {
				return false;
			}
		}

		return true;
	}
}

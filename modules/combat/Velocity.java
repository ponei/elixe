package elixe.modules.combat;

import elixe.events.OnPacketReceiveEvent;
import elixe.events.OnRenderNameEvent;
import elixe.modules.Module;
import elixe.modules.ModuleCategory;
import elixe.modules.option.ModuleBoolean;
import elixe.modules.option.ModuleFloat;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;

public class Velocity extends Module {
	public Velocity() {
		super("Velocity", ModuleCategory.COMBAT);

		moduleOptions.add(horizontalMultiplierOption);
		moduleOptions.add(verticalMultiplierOption);

		moduleOptions.add(needSprintOption);
		moduleOptions.add(needAttackButtonOption);
		moduleOptions.add(needWeaponOption);
		moduleOptions.add(waterCheckOption);
	}

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

	@EventHandler
	private Listener<OnPacketReceiveEvent> onPacketReceiveEvent = new Listener<>(e -> {
		if (e.getPacket() instanceof S12PacketEntityVelocity) {
			S12PacketEntityVelocity vel = (S12PacketEntityVelocity) e.getPacket();
			if (vel.getEntityID() == mc.thePlayer.getEntityId()) {
				if (shouldModifyVelocity()) {
					vel.setMotionX((int) (vel.getMotionX() * horizontalMultiplier));
					vel.setMotionZ((int) (vel.getMotionZ() * horizontalMultiplier));

					vel.setMotionY((int) (vel.getMotionY() * verticalMultiplier));

					e.setPacket(vel);
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

		return true;
	}
}

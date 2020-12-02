package elixe.modules.combat;

import elixe.events.OnPacketReceiveEvent;
import elixe.events.OnRenderNameEvent;
import elixe.modules.Module;
import elixe.modules.ModuleCategory;
import elixe.modules.option.ModuleFloat;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.play.server.S12PacketEntityVelocity;

public class Velocity extends Module {
	public Velocity() {
		super("Velocity", ModuleCategory.COMBAT);

		moduleOptions.add(horizontalMultiplierOption);
		moduleOptions.add(verticalMultiplierOption);
	}

	float horizontalMultiplier = 0.9f;
	ModuleFloat horizontalMultiplierOption = new ModuleFloat("horizontal multiplier", 0.9f, 0f, 1f) {
		public void valueChanged() {
			horizontalMultiplier = (float) this.getValue();
		}
	};

	float verticalMultiplier = 0.9f;
	ModuleFloat verticalMultiplierOption = new ModuleFloat("vertical multiplier", 0.9f, 0f, 1f) {
		public void valueChanged() {
			verticalMultiplier = (float) this.getValue();
		}
	};

	@EventHandler
	private Listener<OnPacketReceiveEvent> onPacketReceiveEvent = new Listener<>(e -> {
		if (e.getPacket() instanceof S12PacketEntityVelocity) {
			S12PacketEntityVelocity vel = (S12PacketEntityVelocity) e.getPacket();
			if (vel.getEntityID() == mc.thePlayer.getEntityId()) {
				vel.setMotionX((int) (vel.getMotionX() * horizontalMultiplier));
				vel.setMotionZ((int) (vel.getMotionZ() * horizontalMultiplier));

				vel.setMotionY((int) (vel.getMotionY() * verticalMultiplier));

				e.setPacket(vel);
			}
		}
	});
}

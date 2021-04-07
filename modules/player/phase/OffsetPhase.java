package elixe.modules.player.phase;

import elixe.Elixe;
import elixe.events.OnLivingUpdateEvent;
import elixe.events.OnMoveEvent;
import elixe.events.OnPacketSendEvent;
import elixe.modules.AModuleOption;
import elixe.utils.player.Rotations;
import net.minecraft.client.Minecraft;

public class OffsetPhase implements IPhaseType {

	Minecraft mc = Elixe.INSTANCE.mc;

	boolean clip;
	int step;

	double direction;
	@Override
	public void OnMove(OnMoveEvent event) {
		if (mc.thePlayer.isCollidedHorizontally) {
			clip = true;
		}
		if (!clip)
			return;

		event.setX(0);
		event.setZ(0);
		step++;

		switch (step) {
		case 1:
			direction = Rotations.getDirection(mc.thePlayer);
			clipOffset(0.0625D);
			break;
		case 2:
			clipOffset(1.5D);
			break;
		case 3:
			step = 0;
			direction = 0;
			clip = false;
			break;
		}
	}
	
	private void clipOffset(double offset) {
		mc.thePlayer.setPosition(mc.thePlayer.posX + (-Math.sin(direction) * offset), mc.thePlayer.posY,
				mc.thePlayer.posZ + (Math.cos(direction) * offset));
	}

	@Override
	public void OnLivingUpdate(OnLivingUpdateEvent event) {
		
	}

	@Override
	public void OnPacket(OnPacketSendEvent event) {
		
	}

	public String getName() {
		return "offset";
	}
}

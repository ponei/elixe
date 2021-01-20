package elixe.utils.player;

import elixe.Elixe;
import elixe.events.OnTickEvent;
import me.zero.alpine.event.EventPriority;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listenable;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;

public class Rotations implements Listenable {
	Minecraft mc = Elixe.INSTANCE.mc;

	private boolean changedRotations = false;

	@EventHandler
	private Listener<OnTickEvent> onTickEvent = new Listener<>(e -> {
		changedRotations = false;
	}, EventPriority.HIGHEST);

	public void markAsChanged() {
		changedRotations = true;
	}

	public boolean hasChanged() {
		return changedRotations;
	}

	// FUNCTIONS
	// https://github.com/CCBlueX/LiquidBounce/blob/master/shared/main/java/net/ccbluex/liquidbounce/utils/RotationUtils.java:getAngleDifference
	public static float getAngleDifference(final float a, final float b) {
		return ((((a - b) % 360F) + 540F) % 360F) - 180F;
	}

	public static double getDirection(EntityPlayerSP ent) {

		float rotationYaw = ent.rotationYaw;
		if (ent.moveForward < 0f)
			rotationYaw += 180f;
		float forward = 1f;
		if (ent.moveForward < 0f) {
			forward = -0.5f;
		} else if (ent.moveForward > 0f)
			forward = 0.5f;
		if (ent.moveStrafing > 0f)
			rotationYaw -= 90f * forward;
		if (ent.moveStrafing < 0f)
			rotationYaw += 90f * forward;
		return Math.toRadians(rotationYaw);
	}

	public static float[] rotationUntilTarget(Entity ent, Entity toEnt) {
		if (ent != null) {
			final double x = ent.posX - toEnt.posX;
			final double y = (ent.posY + ent.getEyeHeight()) - (toEnt.posY + toEnt.getEyeHeight());
			final double z = ent.posZ - toEnt.posZ;
			double yaw = Math.atan2(x, z) * 57.29577951308232;
			yaw = -yaw;
			double pitch = Math.asin(y / Math.sqrt(x * x + y * y + z * z)) * 57.29577951308232;
			pitch = -pitch;
			return new float[] { (float) yaw, (float) pitch };
		}
		return null;
	}
}

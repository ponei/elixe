package elixe.utils.player;

import elixe.Elixe;
import elixe.events.OnTickEvent;
import elixe.utils.misc.ChatUtils;
import me.zero.alpine.event.EventPriority;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listenable;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;

public class Rotations implements Listenable {
	Minecraft mc = Elixe.INSTANCE.mc;

	private boolean lastTickChange = false;
	private boolean changedRotations = false;

	private float yaw = 0f, pitch = 0f;
	private float prevYaw, prevPitch;

	@EventHandler
	private Listener<OnTickEvent> onTickEvent = new Listener<>(e -> {
		if (mc.thePlayer != null) {
			if (yaw != 0f) {
				prevYaw = yaw;
			} else {
				prevYaw = mc.thePlayer.prevRotationYaw;
			}

			if (pitch != 0f) {
				prevPitch = pitch;
			} else {
				prevPitch = mc.thePlayer.prevRotationPitch;
			}

			yaw = 0f;
			pitch = 0f;

			lastTickChange = changedRotations;
			changedRotations = false;
		}
	}, EventPriority.HIGHEST);

	public float getYaw() {
		return yaw;
	}

	public float getPrevYaw() {
		return prevYaw;
	}

	public float getPrevPitch() {
		return prevPitch;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
		markAsChanged();
	}

	public float getPitch() {
		return pitch;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
		markAsChanged();
	}

	public void markAsChanged() {
		changedRotations = true;
	}

	public boolean hasChanged() {
		return changedRotations;
	}

	public boolean didLastTickChange() {
		return lastTickChange;
	}

	// se esse tick a rotação na mudou mas no anterior sim, vamos mandar o pacote da
	// rotação original
	// se por acaso, eu mudei a rotação pra 1000 e o original era 100, vai ficar uma
	// descrepancia horrivel
	// precisa normalizar isso
	public float[] normalizeAfterDisable(float originalYaw, float originalPitch) {

		float difYaw = getAngleDifference(prevYaw, originalYaw);
		float difPitch = getAngleDifference(prevPitch, originalPitch);

		ChatUtils.message(mc, "rotacao original -> " + originalYaw);
		ChatUtils.message(mc, "rotacao final custom -> " + prevYaw);
		ChatUtils.message(mc, "dif -> " + difYaw);
//		System.out.println("original -> " + originalYaw + " / " + originalPitch);
//		System.out.println("prev -> " + prevYaw + " / " + prevPitch);
//		System.out.println("dif -> " + difYaw + " / " + difPitch);

		return new float[] { prevYaw - difYaw, prevPitch + difPitch };
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

	public static float[] rotationUntilXYZ(double toX, double toY, double toZ, Entity toEnt) {
		final double x = toX - toEnt.posX;
		final double y = toY - (toEnt.posY + toEnt.getEyeHeight());
		final double z = toZ - toEnt.posZ;
		double yaw = Math.atan2(x, z) * 57.29577951308232;
		yaw = -yaw;
		double pitch = Math.asin(y / Math.sqrt(x * x + y * y + z * z)) * 57.29577951308232;
		pitch = -pitch;
		return new float[] { (float) yaw, (float) pitch };
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

package elixe.utils.player;

import net.minecraft.entity.Entity;

public class RotationUtils {
	// https://github.com/CCBlueX/LiquidBounce/blob/master/shared/main/java/net/ccbluex/liquidbounce/utils/RotationUtils.java:getAngleDifference
	public static float getAngleDifference(final float a, final float b) {
		return ((((a - b) % 360F) + 540F) % 360F) - 180F;
	}

	public static float[] rotationUntilTarget(Entity ent, Entity toEnt) {
        if (ent != null) {
            final double x = ent.posX - toEnt.posX;
            final double y = ent.posY - toEnt.posY;
            final double z = ent.posZ - toEnt.posZ;
            double yaw = Math.atan2(x, z) * 57.29577951308232;
            yaw = -yaw;
            double pitch = Math.asin(y / Math.sqrt(x * x + y * y + z * z)) * 57.29577951308232;
            pitch = -pitch;
            return new float[] {(float) yaw, (float) pitch};
        }
        return null;
    }
}

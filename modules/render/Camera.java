package elixe.modules.render;

import elixe.events.OnOrientCameraEvent;
import elixe.modules.Module;
import elixe.modules.ModuleCategory;
import elixe.modules.option.ModuleBoolean;
import elixe.modules.option.ModuleFloat;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class Camera extends Module {
	public Camera() {
		super("Camera", ModuleCategory.RENDER);
		
		moduleOptions.add(clipCameraOption);
		moduleOptions.add(distanceCameraOption);
	}
	
	boolean clipCamera;
	ModuleBoolean clipCameraOption = new ModuleBoolean("clip", false) {
		
		public void valueChanged() {
			clipCamera = (boolean) this.getValue();
		}
	};
	
	float distanceCamera;
	ModuleFloat distanceCameraOption = new ModuleFloat("distance", 4f, 0f, 15f) {
		
		public void valueChanged() {
			distanceCamera = (float) this.getValue();
		}
	};

	@EventHandler
	private Listener<OnOrientCameraEvent> onOrientCameraEvent = new Listener<>(e -> {
		e.cancel();
		Entity camera = e.getCameraEntity();
		double x = e.getX(), y = e.getY(), z=e.getZ();
		
		double d3 = distanceCamera;

        if (this.mc.gameSettings.debugCamEnable)
        {
            GlStateManager.translate(0.0F, 0.0F, (float)(-d3));
        }
        else
        {
            float f1 = camera.rotationYaw;
            float f2 = camera.rotationPitch;

            if (this.mc.gameSettings.thirdPersonView == 2)
            {
                f2 += 180.0F;
            }

            if (!clipCamera) {
            double d4 = -MathHelper.sin(f1 / 180.0F * (float)Math.PI) * MathHelper.cos(f2 / 180.0F * (float)Math.PI) * d3;
            double d5 = MathHelper.cos(f1 / 180.0F * (float)Math.PI) * MathHelper.cos(f2 / 180.0F * (float)Math.PI) * d3;
            double d6 = (-MathHelper.sin(f2 / 180.0F * (float)Math.PI)) * d3;

            for (int i = 0; i < 8; ++i)
            {
                float f3 = (i & 1) * 2 - 1;
                float f4 = (i >> 1 & 1) * 2 - 1;
                float f5 = (i >> 2 & 1) * 2 - 1;
                f3 = f3 * 0.1F;
                f4 = f4 * 0.1F;
                f5 = f5 * 0.1F;
                MovingObjectPosition movingobjectposition = this.mc.theWorld.rayTraceBlocks(new Vec3(x + f3, y + f4, z + f5), new Vec3(x - d4 + f3 + f5, y - d6 + f4, z - d5 + f5));

                if (movingobjectposition != null)
                {
                    double d7 = movingobjectposition.hitVec.distanceTo(new Vec3(x, y, z));

                    if (d7 < d3)
                    {
                        d3 = d7;
                    }
                }
            }
            }

            if (this.mc.gameSettings.thirdPersonView == 2)
            {
                GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
            }

            GlStateManager.rotate(camera.rotationPitch - f2, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(camera.rotationYaw - f1, 0.0F, 1.0F, 0.0F);
            GlStateManager.translate(0.0F, 0.0F, (float)(-d3));
            GlStateManager.rotate(f1 - camera.rotationYaw, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(f2 - camera.rotationPitch, 1.0F, 0.0F, 0.0F);
        }
	});
}

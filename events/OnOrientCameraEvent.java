package elixe.events;

import me.zero.alpine.event.type.Cancellable;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.entity.Entity;

public class OnOrientCameraEvent extends Cancellable {
	Entity cameraEntity;
	double x, y, z;
	float partialTicks;

	public OnOrientCameraEvent(Entity cameraEntity, double x, double y, double z, float partialTicks) {
		super();
		this.cameraEntity = cameraEntity;
		this.x = x;
		this.y = y;
		this.z = z;
		this.partialTicks = partialTicks;
	}

	public Entity getCameraEntity() {
		return cameraEntity;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}

	public float getPartialTicks() {
		return partialTicks;
	}

	
}

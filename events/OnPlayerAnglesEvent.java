package elixe.events;

import me.zero.alpine.event.type.Cancellable;

//func_181560_a(float, long) : void - net.minecraft.client.renderer.EntityRenderer
//L:1318
public class OnPlayerAnglesEvent extends Cancellable {
private float yaw, pitch;

public OnPlayerAnglesEvent(float yaw, float pitch) {
	super();
	this.yaw = yaw;
	this.pitch = pitch;
}

public float getYaw() {
	return yaw;
}

public void setYaw(float yaw) {
	this.yaw = yaw;
}

public float getPitch() {
	return pitch;
}

public void setPitch(float pitch) {
	this.pitch = pitch;
}

}

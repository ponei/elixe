package elixe.events;

import me.zero.alpine.event.type.Cancellable;

//moveEntity(double, double, double) : void - net.minecraft.client.entity.EntityPlayerSP
//L:191
public class OnMoveEvent extends Cancellable {
	private double x, y, z;

	public OnMoveEvent(double x, double y, double z) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}

}

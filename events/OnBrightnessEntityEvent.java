package elixe.events;

import net.minecraft.entity.Entity;

//getBrightnessForRender(float) : int - net.minecraft.entity.Entity
//L:1250
public class OnBrightnessEntityEvent {
	private int light;
	private Entity ent;
	
	
	public OnBrightnessEntityEvent(Entity ent, int light) {
		this.light = light;
		this.ent = ent;
	}

	public int getLight() {
		return light;
	}

	public void setLight(int light) {
		this.light = light;
	}

	public Entity getEnt() {
		return ent;
	}	
}

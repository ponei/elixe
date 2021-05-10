package elixe.modules.misc;

import java.util.ArrayList;

import elixe.events.OnTickEvent;
import elixe.modules.Module;
import elixe.modules.ModuleCategory;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;

public class AntiBot extends Module {
	public AntiBot() {
		super("AntiBot", ModuleCategory.MISC);

	}
	
	ArrayList<int[]> onGround = new ArrayList<int[]>(); //id, ticks
	
	@EventHandler
	private Listener<OnTickEvent> onTickEvent = new Listener<>(e -> {
		for (Entity ent : mc.theWorld.loadedEntityList) {
			if (ent == mc.thePlayer) {
				continue;
			}
			if (ent instanceof EntityOtherPlayerMP) {
				
				String name = ((EntityOtherPlayerMP) ent).getNameClear();
				if (!name.startsWith("[NPC]")) {
					System.out.println(name + " -> " + ent.onGround);
				}
				
			}
		}
	});
	
}

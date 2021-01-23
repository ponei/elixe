package elixe.modules.player;

import elixe.Elixe;
import elixe.events.OnTickEvent;
import elixe.modules.Module;
import elixe.modules.ModuleCategory;
import elixe.utils.player.Rotations;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;

public class Derp extends Module {
	public Derp() {
		super("Derp", ModuleCategory.PLAYER);
	}
	
	@EventHandler
	private Listener<OnTickEvent> onTickEvent = new Listener<>(e -> {
		Rotations rot = Elixe.INSTANCE.ROTATIONS;
	
		if (!rot.hasChanged()) {
			rot.setYaw(rot.getPrevYaw() + 20);			
		}
	});
}

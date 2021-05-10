package elixe.modules.movement;

import elixe.events.OnLivingUpdateEvent;
import elixe.modules.Module;
import elixe.modules.ModuleCategory;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;

public class Sprint extends Module {
	public Sprint() {
		super("Sprint", ModuleCategory.MOVEMENT);
	}
	
	@EventHandler
	private Listener<OnLivingUpdateEvent> onLivingUpdateEvent = new Listener<>(e -> {
		e.setHoldingSprinting(true);
	});
}

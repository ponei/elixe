package elixe.modules.world;

import elixe.events.OnPlayerMoveStateEvent;
import elixe.events.OnRightClickDelayTimerEvent;
import elixe.modules.Module;
import elixe.modules.ModuleCategory;
import elixe.modules.option.ModuleBoolean;
import elixe.modules.option.ModuleFloat;
import elixe.modules.option.ModuleInteger;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;

public class FastPlace extends Module {

	public FastPlace() {
		super("FastPlace", ModuleCategory.WORLD);
		
		moduleOptions.add(rightClickTimerOption);
		moduleOptions.add(needBlockOption);
	} 
	
	int rightClickTimer;
	ModuleInteger rightClickTimerOption = new ModuleInteger("right click timer", 3, 0, 10) {
		public void valueChanged() {
			rightClickTimer = (int) this.getValue();
		}
	};
	
	boolean needBlock = false;
	ModuleBoolean needBlockOption = new ModuleBoolean("holding block", false) {
		public void valueChanged() {
			needBlock = (boolean) this.getValue();
		}
	};
	
	@EventHandler
	private Listener<OnRightClickDelayTimerEvent> onRightClickDelayTimerEvent = new Listener<>(e -> {
		if (needBlock) {
			if (!conditionals.isHoldingBlock()) {
				return;
			}
		}
		e.setRightClickDelayTimer(rightClickTimer);
	});
}

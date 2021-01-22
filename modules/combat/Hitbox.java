package elixe.modules.combat;

import elixe.events.OnGetCollisionBorderEvent;
import elixe.events.OnPlayerAnglesEvent;
import elixe.modules.Module;
import elixe.modules.ModuleCategory;
import elixe.modules.option.ModuleFloat;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;

public class Hitbox extends Module {

	public Hitbox() {
		super("Hitbox", ModuleCategory.COMBAT);
		
		moduleOptions.add(expandAmountOption);
	}
	
	float expandAmount;
	ModuleFloat expandAmountOption = new ModuleFloat("expand amount", 0.1f, 0f, 1f) {
		public void valueChanged() {
			expandAmount = (float) this.getValue();
		}
	};
	
	@EventHandler
	private Listener<OnGetCollisionBorderEvent> onGetCollisionBorderEvent = new Listener<>(e -> {
		e.setBorderSize(expandAmount);
	});

}

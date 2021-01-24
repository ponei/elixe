package elixe.modules.combat;

import elixe.events.OnGetCollisionBorderEvent;
import elixe.events.OnPlayerAnglesEvent;
import elixe.modules.Module;
import elixe.modules.ModuleCategory;
import elixe.modules.option.ModuleBoolean;
import elixe.modules.option.ModuleFloat;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;

public class Hitbox extends Module {

	public Hitbox() {
		super("Hitbox", ModuleCategory.COMBAT);
		
		moduleOptions.add(expandAmountOption);
		moduleOptions.add(requireWeaponOption);
	}
	
	float expandAmount;
	ModuleFloat expandAmountOption = new ModuleFloat("expand amount", 0.1f, 0f, 1f) {
		public void valueChanged() {
			expandAmount = (float) this.getValue();
		}
	};
	
	boolean requireWeapon;
	ModuleBoolean requireWeaponOption = new ModuleBoolean("require weapon", false) {
		public void valueChanged() {
			requireWeapon = (boolean) this.getValue();
		}
	};
	
	@EventHandler
	private Listener<OnGetCollisionBorderEvent> onGetCollisionBorderEvent = new Listener<>(e -> {
		if (requireWeapon) {
			if (!conditionals.isHoldingWeapon()) {
				return;
			}
		}
		
		e.setBorderSize(expandAmount);
	});

}

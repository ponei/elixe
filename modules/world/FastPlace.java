package elixe.modules.world;

import java.util.Random;

import elixe.events.OnRightClickDelayTimerEvent;
import elixe.modules.Module;
import elixe.modules.ModuleCategory;
import elixe.modules.option.ModuleBoolean;
import elixe.modules.option.ModuleInteger;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;

public class FastPlace extends Module {

	public FastPlace() {
		super("FastPlace", ModuleCategory.WORLD);
		
		moduleOptions.add(cpsMinOption);
		moduleOptions.add(cpsMaxOption);
		moduleOptions.add(needBlockOption);
	} 
	
	int cpsMin;
	ModuleInteger cpsMinOption = new ModuleInteger("cps min", 6, 0, 20) {
		
		public void valueChanged() {
			int newCps = (int) this.getValue();

			if (cpsMaxOption != null) {
				if (newCps > cpsMax) {
					// fix
					cpsMax = newCps;
					if (cpsMaxOption.getButton() != null) {
						cpsMaxOption.getButton().setValue(newCps);
					}

					cpsMaxOption.setValueSilent(newCps);
				}
			}

			cpsMin = newCps;
		}
	};

	int cpsMax;
	ModuleInteger cpsMaxOption = new ModuleInteger("cps max", 10, 0, 20) {
		
		public void valueChanged() {
			int newCps = (int) this.getValue();

			if (cpsMinOption != null) {
				if (cpsMin > newCps) {
					// fix
					cpsMin = newCps;
					if (cpsMinOption.getButton() != null) {
						cpsMinOption.getButton().setValue(newCps);
					}
					cpsMinOption.setValueSilent(newCps);
				}
			}

			cpsMax = newCps;
		}
	};
	
	boolean needBlock = false;
	ModuleBoolean needBlockOption = new ModuleBoolean("holding block", false) {
		
		public void valueChanged() {
			needBlock = (boolean) this.getValue();
		}
	};
	
	Random r = new Random();
	
	@EventHandler
	private Listener<OnRightClickDelayTimerEvent> onRightClickDelayTimerEvent = new Listener<>(e -> {
		if (needBlock) {
			if (!conditionals.isHoldingBlock()) {
				return;
			}
		}
		int cps = r.nextInt(cpsMax - cpsMin + 1)+cpsMin;
		//System.out.println(20 / cps);
		e.setRightClickDelayTimer(20 / cps);
	});
}

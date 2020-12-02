package elixe.modules.combat;

import java.util.Random;

import org.lwjgl.input.Mouse;

import elixe.events.OnTickEvent;
import elixe.modules.Module;
import elixe.modules.ModuleCategory;
import elixe.modules.option.ModuleBoolean;
import elixe.modules.option.ModuleFloat;
import elixe.modules.option.ModuleInteger;
import elixe.utils.misc.TimerUtils;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;

public class AutoClicker extends Module {
	public AutoClicker() {
		super("AutoClicker", ModuleCategory.COMBAT);

		moduleOptions.add(cpsMinOption);
		moduleOptions.add(cpsMaxOption);
		moduleOptions.add(breakBlocksOption);
	}

	int cpsMin = 6;
	ModuleInteger cpsMinOption = new ModuleInteger("cps min", 6, 0, 20) {
		public void valueChanged() {
			int newCps = (int) this.getValue();

			if (newCps > cpsMax) {
				// fix
				cpsMax = newCps;
				if (cpsMaxOption.getButton() != null) {
					cpsMaxOption.getButton().setValue(newCps);
				}

				cpsMaxOption.setValueSilent(newCps);
			}

			cpsMin = newCps;
		}
	};

	int cpsMax = 10;
	ModuleInteger cpsMaxOption = new ModuleInteger("cps max", 10, 0, 20) {
		public void valueChanged() {
			int newCps = (int) this.getValue();

			if (cpsMin > newCps) {
				// fix
				cpsMin = newCps;
				if (cpsMinOption.getButton() != null) {
					cpsMinOption.getButton().setValue(newCps);
				}
				cpsMinOption.setValueSilent(newCps);
			}

			cpsMax = newCps;
		}
	};

	boolean breakBlocks = false;
	ModuleBoolean breakBlocksOption = new ModuleBoolean("break blocks", false) {
		public void valueChanged() {
			breakBlocks = (boolean) this.getValue();
		}
	};

	// randomizacao
	TimerUtils clickTimer = new TimerUtils();
	Random r = new Random();

	int cps = 0;
	int tickreset = 0;

	int clickDelay;
	boolean breaking = false; // se ta quebrando bloco
	boolean attacking = false; // se ta atacando
	boolean firstClick = true; // identificar se é primeiro click depois de segurar
	@EventHandler
	private Listener<OnTickEvent> onTickEvent = new Listener<>(e -> {
//		tickreset++;
//		if (tickreset == 20) {
//			System.out.println("cps: " + cps);
//			cps = 0;
//			tickreset = 0;
//		} 

		int attack = mc.gameSettings.keyBindAttack.getKeyCode();
		int attackN = attack + 100;

		int use = mc.gameSettings.keyBindUseItem.getKeyCode();
		int useN = use + 100;

		if (mc.currentScreen == null) {
			if (mc.objectMouseOver != null) {
				if (breakBlocks && mc.objectMouseOver.typeOfHit == MovingObjectType.BLOCK) {
					if (Mouse.isButtonDown(attackN) && !breaking) { // botao ta pra baixo mas com bloco na frente
						if (!attacking) { // ta estado em abaixado
							breaking = true;
							KeyBinding.setKeyBindState(attack, true);
						} else {
							attacking = false;
							KeyBinding.setKeyBindState(attack, false);
						}
					}
					return;
				}
			}

			// botao esquerdo ta segurado e nao ta quebrando bloco
			// se vier do check ali em cima, vai estar false
			if (Mouse.isButtonDown(attackN) && !breaking) {
				// check de botao direito aqui em baixo pra nao fazer o firstclick mudar

				if (firstClick) { // primeiro click pra nao ter chande de dar double click
					firstClick = false;
					clickTimer.reset();
					setNewClickDelay();
				} else {
					// tempo passou, dá click
					if (clickTimer.hasTimePassed(clickDelay)) {
//							cps++;

						attacking = true;
						KeyBinding.setKeyBindState(attack, true);
						KeyBinding.onTick(attack);
						// reset no timer, coloca novo delay
						clickTimer.reset();
						setNewClickDelay();
					} else {
						// tempo nao passou ainda, abaixa click
						attacking = false;
						KeyBinding.setKeyBindState(attack, false);
					}
				}

			} else {
				firstClick = true;
			}
			breaking = false;

		}
	});

	// seta novo delay pra esperar pro proximo click
	private void setNewClickDelay() {
		// max é o menor, pode confundir
		int max = 1000 / cpsMax;
		int min = 1000 / cpsMin;
		clickDelay = r.nextInt((min - max) + 1) + max;
	}
}

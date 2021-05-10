package elixe.modules.combat;

import java.util.Random;

import org.lwjgl.input.Mouse;

import elixe.events.OnMouseInputGUIEvent;
import elixe.events.OnTickEvent;
import elixe.modules.Module;
import elixe.modules.ModuleCategory;
import elixe.modules.option.ModuleBoolean;
import elixe.modules.option.ModuleInteger;
import elixe.ui.clickgui.ElixeMenu;
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

		moduleOptions.add(workOnGuiOption);
		moduleOptions.add(requireWeaponOption);
		moduleOptions.add(breakBlocksOption);
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

	boolean workOnGui;
	ModuleBoolean workOnGuiOption = new ModuleBoolean("work on gui", false) {
		
		public void valueChanged() {
			workOnGui = (boolean) this.getValue();
		}
	};

	boolean breakBlocks;
	ModuleBoolean breakBlocksOption = new ModuleBoolean("break blocks", false) {
		
		public void valueChanged() {
			breakBlocks = (boolean) this.getValue();
		}
	};

	boolean requireWeapon;
	ModuleBoolean requireWeaponOption = new ModuleBoolean("require weapon", false) {
		
		public void valueChanged() {
			requireWeapon = (boolean) this.getValue();
		}
	};

	// randomizacao
	TimerUtils.MilisecondTimer clickTimer = new TimerUtils().new MilisecondTimer();
	Random r = new Random();

	int cps = 0;
	int tickreset = 0;

	int clickDelay;
	boolean breaking = false; // se ta quebrando bloco
	boolean attacking = false; // se ta atacando
	boolean firstClick = true; // identificar se é primeiro click depois de segurar

	int attackCode, useCode;

	boolean handleGuiMouse = false;
	@EventHandler
	private Listener<OnTickEvent> onTickEvent = new Listener<>(e -> {

		boolean nullScreen = mc.currentScreen == null;
		boolean elixeScreen = mc.currentScreen instanceof ElixeMenu;
		
		if (requireWeapon && nullScreen) {
			if (!conditionals.isHoldingWeapon()) {
				return;
			}
		}

		int attack = mc.gameSettings.keyBindAttack.getKeyCode();
		int attackCode = attack + 100;

		// logica pra quebrar bloco
		if (nullScreen) {
			if (mc.objectMouseOver != null) {
				if (breakBlocks && mc.objectMouseOver.typeOfHit == MovingObjectType.BLOCK) {
					if (Mouse.isButtonDown(attackCode) && !breaking) { // botao ta pra baixo mas com bloco na frente
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
		}

		// botao esquerdo ta segurado e nao ta quebrando bloco
		// se vier do check ali em cima, vai estar false
		if (Mouse.isButtonDown(attackCode) && !breaking) {
			// check de botao direito aqui em baixo pra nao fazer o firstclick mudar

			if (firstClick) { // primeiro click pra nao ter chande de dar double click
				firstClick = false;
				clickTimer.reset();
				setNewClickDelay();
			} else {
				// tempo passou, dá click
				try {
					if (clickTimer.hasTimePassed(clickDelay)) {
						attacking = true;
						if (nullScreen) {
							KeyBinding.setKeyBindState(attack, true);
							KeyBinding.onTick(attack);
						} else {
							if (workOnGui && !elixeScreen) {
								handleGuiMouse = true;
								mc.currentScreen.handleMouseInput();
							}
						}

						// reset no timer, coloca novo delay
						clickTimer.reset();
						setNewClickDelay();
					} else {
						if (nullScreen) {
							// tempo nao passou ainda, abaixa click
							attacking = false;
							KeyBinding.setKeyBindState(attack, false);
						} else {
							if (workOnGui && !elixeScreen) {
								if (attacking) {
									attacking = false;
									handleGuiMouse = true;
									mc.currentScreen.handleMouseInput();
								}
							}
						}

					}
				} catch (Exception clickerE) {

				}
			}

		} else {
			firstClick = true;
		}
		breaking = false;

	});

	@EventHandler
	private Listener<OnMouseInputGUIEvent> onMouseInputGUIEvent = new Listener<>(e -> {
		// System.out.println(e.getEventButton() + ", " + e.getEventButtonState());
		if (handleGuiMouse) {
			e.setEventButton(attackCode);
			e.setEventButtonState(attacking);
			handleGuiMouse = false;
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

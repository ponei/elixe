package elixe.modules.movement;

import org.lwjgl.input.Keyboard;

import elixe.events.OnPlayerMoveStateEvent;
import elixe.modules.Module;
import elixe.modules.ModuleCategory;
import elixe.modules.option.ModuleBoolean;
import elixe.ui.clickgui.ElixeMenu;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.gui.GuiChat;

public class InventoryMove extends Module {
	public InventoryMove() {
		super("InventoryMove", ModuleCategory.MOVEMENT);

		moduleOptions.add(onlyClickGUIOption);
	}

	boolean onlyClickGUI = false;
	ModuleBoolean onlyClickGUIOption = new ModuleBoolean("only clickgui", false) {
		
		public void valueChanged() {
			onlyClickGUI = (boolean) this.getValue();
		}
	};

	@EventHandler
	private Listener<OnPlayerMoveStateEvent> onPlayerMoveStateEvent = new Listener<>(e -> {
		if (!(mc.currentScreen instanceof GuiChat) && mc.currentScreen != null) {
			if (onlyClickGUI) {
				if (!(mc.currentScreen instanceof ElixeMenu)) {
					return;
				}
			}
			e.setForward(Keyboard.isKeyDown(mc.gameSettings.keyBindForward.getKeyCode()));
			e.setBack(Keyboard.isKeyDown(mc.gameSettings.keyBindBack.getKeyCode()));
			e.setLeft(Keyboard.isKeyDown(mc.gameSettings.keyBindLeft.getKeyCode()));
			e.setRight(Keyboard.isKeyDown(mc.gameSettings.keyBindRight.getKeyCode()));
			e.setJump(Keyboard.isKeyDown(mc.gameSettings.keyBindJump.getKeyCode()));
			e.setSneak(Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.getKeyCode()));
		}
	});
}

package elixe.commands.action;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import elixe.commands.CommandManager;
import elixe.modules.Module;
import elixe.utils.misc.ChatUtils;

public class BindAction implements IAction {

	@Override
	public String[] getPrefixes() {
		return new String[] { "b", "bind" };
	}

	@Override
	public String getArguments() {
		return "<module> <key>";
	}

	@Override
	public int necessaryArguments() {
		return 2;
	}

	@Override
	public String getName() {
		return "bind";
	}

	@Override
	public String getDescription() {
		return "binds a key to a module";
	}

	@Override
	public void execute(CommandManager commandManager, String[] args) {
		Module module = commandManager.getModule(args[1]);
		if (module != null) {
			int keyboardKey = Keyboard.getKeyIndex(args[2].toUpperCase());
			int mouseKey = Mouse.getButtonIndex(args[2].toUpperCase());

			if (keyboardKey != 0) {
				module.setKey(keyboardKey);
				ChatUtils.message(mc, module.getName().toLowerCase() + " is now bound to " + args[2].toLowerCase() + ".");
			} else if (args[2].toLowerCase().equals("none")) {
				module.setKey(0);
				ChatUtils.message(mc, module.getName().toLowerCase() + " is now bound to nothing.");
			} else if (mouseKey != -1) {
				module.setKey(mouseKey - 100);
				ChatUtils.message(mc, module.getName().toLowerCase() + " is now bound to " + args[2].toLowerCase() + ".");
			} else {
				commandManager.errorMessage(this);
			}
		} else {
			commandManager.errorMessage(this);
		}
	}

}

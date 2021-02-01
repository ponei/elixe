package elixe.commands.action;

import elixe.commands.CommandManager;
import elixe.modules.Module;
import elixe.utils.misc.ChatUtils;

public class ToggleAction implements IAction {

	@Override
	public String[] getPrefixes() {
		return new String[] {"t", "toggle"};
	}

	@Override
	public String getArguments() {
		return "<module>";
	}
	
	@Override
	public int necessaryArguments() {
		return 1;
	}


	@Override
	public String getName() {
		return "toggle";
	}

	@Override
	public String getDescription() {
		return "toggles a module";
	}

	@Override
	public void execute(CommandManager commandManager, String[] args) {	
		Module module = commandManager.getModule(args[1]);
		if (module != null) {
			module.toggle();
			ChatUtils.message(mc, module.getName().toLowerCase() + " is now " + (module.isToggled()  ? "on" : "off"));
		} else {
			commandManager.errorMessage(this);
		}
	}

	
}

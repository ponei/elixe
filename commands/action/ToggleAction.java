package elixe.commands.action;

import elixe.commands.CommandManager;
import elixe.modules.Module;
import elixe.utils.misc.ChatUtils;

public class ToggleAction implements IAction {

	
	public String[] getPrefixes() {
		return new String[] {"t", "toggle"};
	}

	
	public String getArguments() {
		return "<module>";
	}
	
	
	public int necessaryArguments() {
		return 1;
	}


	
	public String getName() {
		return "toggle";
	}

	
	public String getDescription() {
		return "toggles a module";
	}

	
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

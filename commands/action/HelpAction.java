package elixe.commands.action;

import elixe.commands.CommandManager;
import elixe.utils.misc.ChatUtils;

public class HelpAction implements IAction {

	
	public String[] getPrefixes() {
		return new String[] {"h", "help"};
	}

	
	public String getArguments() {
		return "<page number>";
	}

	
	public int necessaryArguments() {
		return 0;
	}

	
	
	public String getName() {
		return "help";
	}

	
	public String getDescription() {
		return "shows a list of commands";
	}

	
	public void execute(CommandManager commandManager, String[] args) {
		for (IAction action : commandManager.getCommands()) {
			String actionArgs = String.join(", ", action.getArguments());
			ChatUtils.message(mc, false, "§r" + action.getName() + " §7" + actionArgs + "§r - " + action.getDescription());
		}
	}

}

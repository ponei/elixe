package elixe.commands.action;

import elixe.commands.CommandManager;
import elixe.utils.misc.ChatUtils;

public class HelpAction implements IAction {

	@Override
	public String[] getPrefixes() {
		return new String[] {"h", "help"};
	}

	@Override
	public String getArguments() {
		return "<page number>";
	}

	@Override
	public int necessaryArguments() {
		return 0;
	}

	
	@Override
	public String getName() {
		return "help";
	}

	@Override
	public String getDescription() {
		return "shows a list of commands";
	}

	@Override
	public void execute(CommandManager commandManager, String[] args) {
		for (IAction action : commandManager.getCommands()) {
			String actionArgs = String.join(", ", action.getArguments());
			ChatUtils.message(mc, false, "§r" + action.getName() + " §7" + actionArgs + "§r - " + action.getDescription());
		}
	}

}

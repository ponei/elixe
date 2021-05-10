package elixe.commands.action;

import elixe.commands.CommandManager;
import elixe.utils.misc.ChatUtils;

public class PingAction implements IAction {
	
	public String[] getPrefixes() {
		return new String[] {"p", "ping"};
	}

	
	public String getArguments() {
		return "";
	}
	
	
	public int necessaryArguments() {
		return 0;
	}


	
	public String getName() {
		return "ping";
	}

	
	public String getDescription() {
		return "your response time to the server";
	}

	
	public void execute(CommandManager commandManager, String[] args) {
		ChatUtils.message(mc, "your ping is " + mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID()).getResponseTime() + ".");
		
	}
}

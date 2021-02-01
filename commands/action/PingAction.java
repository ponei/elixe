package elixe.commands.action;

import elixe.commands.CommandManager;
import elixe.utils.misc.ChatUtils;

public class PingAction implements IAction {
	@Override
	public String[] getPrefixes() {
		return new String[] {"p", "ping"};
	}

	@Override
	public String getArguments() {
		return "";
	}
	
	@Override
	public int necessaryArguments() {
		return 0;
	}


	@Override
	public String getName() {
		return "ping";
	}

	@Override
	public String getDescription() {
		return "your response time to the server";
	}

	@Override
	public void execute(CommandManager commandManager, String[] args) {
		ChatUtils.message(mc, "your ping is " + mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID()).getResponseTime() + ".");
		
	}
}

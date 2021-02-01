package elixe.commands;

import elixe.Elixe;
import elixe.commands.action.*;
import elixe.modules.Module;
import elixe.utils.misc.ChatUtils;
import net.minecraft.client.Minecraft;

public class CommandManager {
	public static String COMMAND_PREFIX = ".";
	
	private Minecraft mc = Elixe.INSTANCE.mc;	
	
	private IAction[] commands = new IAction[] {
			new HelpAction(), //h
			new BindAction(), //b
			new ToggleAction(), //t
			new ValueAction(), //v
			new PingAction()
	};

	public void executeCommand(String command) {
		command = command.substring(COMMAND_PREFIX.length()); //remove prefix
		String[] args = command.split(" ");
		
		boolean foundAction = false;
		//pra cada acao, checa se o primeiro argumento é algum prefix
		for (IAction action : commands) {
			for (String prefix : action.getPrefixes()) {
				if (args[0].equalsIgnoreCase(prefix)) {
					if (args.length - 1 >= action.necessaryArguments()) {
						action.execute(this, args);
					} else {
						errorMessage(action);
					}
								
					foundAction = true;
					break;
				}
			}
			if (foundAction) {
				break;
			}
		}
		
		if (!foundAction) {
			ChatUtils.message(mc, "no command found. type \"§r.help§7\" if you're lost.");
		}
	}
	
	public void errorMessage(IAction action) {
		ChatUtils.message(mc, "wrong arguments. §f" + action.getName() + " §7requires §f" + action.getArguments() + "§7.");
	}
	
	public Module getModule(String arg) {
		for (Module module : Elixe.INSTANCE.MODULE_MANAGER.getModules()) {
			//nome do module sem espaço e ignora case igual ao arg
			if (module.getName().replace(" ", "").equalsIgnoreCase(arg)) {
				return module;
			}
		}
		return null;
	}

	public IAction[] getCommands() {
		return commands;
	}
	
	
}

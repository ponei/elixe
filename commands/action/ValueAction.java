package elixe.commands.action;

import elixe.commands.CommandManager;
import elixe.modules.AModuleOption;
import elixe.modules.Module;
import elixe.modules.option.ModuleBoolean;
import elixe.modules.option.ModuleFloat;
import elixe.modules.option.ModuleInteger;
import elixe.utils.misc.ChatUtils;

public class ValueAction implements IAction {

	
	public String[] getPrefixes() {
		return new String[] { "v", "value" };
	}

	
	public String getArguments() {
		return "<module> <option> <value>";
	}

	
	public int necessaryArguments() {
		return 3;
	}

	
	public String getName() {
		return "value";
	}

	
	public String getDescription() {
		return "changes a certain value of a module";
	}

	
	public void execute(CommandManager commandManager, String[] args) {
		Module module = commandManager.getModule(args[1]);
		if (module != null) {
			boolean foundOption = false;
			for (AModuleOption option : module.getOptions()) {
				if (option.getName().replace(" ", "").equalsIgnoreCase(args[2])) {
					foundOption = true;
					try {

						if (option instanceof ModuleInteger) {
							int numberArg = Integer.parseInt(args[3]);

							if (option.getButton() != null) {
								option.getButton().setValue(numberArg);
							}

							option.setValue(numberArg);
							ChatUtils.message(mc, "\"�r" + option.getName() + "�7\" is now �r" + numberArg + "�7.");
						} else if (option instanceof ModuleFloat) {
							float floatArg = Float.parseFloat(args[3]);

							if (option.getButton() != null) {
								option.getButton().setValue(floatArg);
							}

							option.setValue(floatArg);
							ChatUtils.message(mc, "\"�r" + option.getName() + "�7\" is now �r" + floatArg + "�7.");
						} else if (option instanceof ModuleBoolean) {
							boolean boolArg = Boolean.parseBoolean(args[3]);

							if (option.getButton() != null) {
								option.getButton().setValue(boolArg);
							}

							option.setValue(boolArg);
							ChatUtils.message(mc, "\"�r" + option.getName() + "�7\" is now " + (boolArg ? "�ron�7." : "�roff�7."));
						} else {
							ChatUtils.message(mc, "\"�r" + option.getName() + "�7\" is not implemented yet.");
						}

						break;
					} catch (Exception e) {
						commandManager.errorMessage(this);
					}
				}
			}
			if (!foundOption) {
				commandManager.errorMessage(this);
			}
		} else {
			commandManager.errorMessage(this);
		}
	}

}

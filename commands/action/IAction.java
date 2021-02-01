package elixe.commands.action;

import elixe.Elixe;
import elixe.commands.CommandManager;
import net.minecraft.client.Minecraft;

public interface IAction {
	public Minecraft mc = Elixe.INSTANCE.mc;
	
	public String[] getPrefixes();

	public int necessaryArguments();
	
	public String getArguments();
	
	public String getName();

	public String getDescription();

	public void execute(CommandManager commandManager, String[] args);
}

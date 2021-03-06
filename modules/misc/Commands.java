package elixe.modules.misc;

import org.lwjgl.input.Keyboard;

import elixe.commands.CommandManager;
import elixe.events.OnPacketSendEvent;
import elixe.modules.Module;
import elixe.modules.ModuleCategory;
import elixe.ui.clickgui.ElixeMenu;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.network.play.client.C01PacketChatMessage;

public class Commands extends Module {

	CommandManager commandManager = new CommandManager();
	
	public Commands() {
		super("Commands", ModuleCategory.MISC);
		
	}

	@EventHandler
	private Listener<OnPacketSendEvent> onPacketSendEvent = new Listener<>(e -> {
		if (e.getPacket() instanceof C01PacketChatMessage) {
			String message = ((C01PacketChatMessage) e.getPacket()).getMessage();
			if (message.startsWith(commandManager.COMMAND_PREFIX)) {
				e.cancel();
				commandManager.executeCommand(message);
			}
		}
	});
}

package elixe.modules.render;

import elixe.events.OnDrawChatLineEvent;
import elixe.events.OnRender2DEvent;
import elixe.modules.Module;
import elixe.modules.ModuleCategory;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;

public class NameProtect extends Module {

	public NameProtect() {
		super("Name Protect", ModuleCategory.RENDER);
	}
	
	@EventHandler
	private Listener<OnDrawChatLineEvent> onDrawChatLineEvent = new Listener<>(e -> {
		String line = e.getChatLine();
		String user = mc.session.getUsername();
		if (line.contains(mc.session.getUsername())) {
			e.setChatLine(line.replace(user, "§k" + user + "§r"));
		}
	});
}

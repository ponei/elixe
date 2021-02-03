package elixe.modules.render;

import elixe.events.OnDrawChatLineEvent;
import elixe.events.OnRender2DEvent;
import elixe.events.OnScoreboardPlayerNameEvent;
import elixe.modules.Module;
import elixe.modules.ModuleCategory;
import elixe.modules.option.ModuleBoolean;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;

public class NameProtect extends Module {

	public NameProtect() {
		super("Name Protect", ModuleCategory.RENDER);

		moduleOptions.add(chatOption);
		moduleOptions.add(scoreboardOption);
	}

	boolean chat;
	ModuleBoolean chatOption = new ModuleBoolean("chat", false) {
		public void valueChanged() {
			chat = (boolean) this.getValue();
		}
	};

	boolean scoreboard;
	ModuleBoolean scoreboardOption = new ModuleBoolean("scoreboard", false) {
		public void valueChanged() {
			scoreboard = (boolean) this.getValue();
		}
	};

	@EventHandler
	private Listener<OnScoreboardPlayerNameEvent> onScoreboardPlayerNameEvent = new Listener<>(e -> {
		if (scoreboard) {
			String line = e.getName();
			String user = mc.session.getUsername();
			if (line.contains(mc.session.getUsername())) {
				e.setName(line.replace(user, "§k" + user + "§r"));
			}
		}
	});
	
	@EventHandler
	private Listener<OnDrawChatLineEvent> onDrawChatLineEvent = new Listener<>(e -> {
		if (chat) {
			String line = e.getChatLine();
			String user = mc.session.getUsername();
			if (line.contains(mc.session.getUsername())) {
				e.setChatLine(line.replace(user, "§k" + user + "§r"));
			}
		}
	});
}

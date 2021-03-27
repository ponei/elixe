package elixe.modules.render;

import elixe.events.OnDrawChatLineEvent;
import elixe.events.OnRender2DEvent;
import elixe.events.OnScoreboardPlayerNameEvent;
import elixe.events.OnSetSessionEvent;
import elixe.events.OnTabPlayerNameEvent;
import elixe.events.OnTickEvent;
import elixe.modules.Module;
import elixe.modules.ModuleCategory;
import elixe.modules.option.ModuleBoolean;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;

public class NameProtect extends Module {

	public NameProtect() {
		super("Name Protect", ModuleCategory.RENDER);

		moduleOptions.add(chatOption);
		moduleOptions.add(tabOption);
		moduleOptions.add(scoreboardOption);
	}

	boolean chat;
	ModuleBoolean chatOption = new ModuleBoolean("chat", false) {
		public void valueChanged() {
			chat = (boolean) this.getValue();
		}
	};

	boolean tab;
	ModuleBoolean tabOption = new ModuleBoolean("tab", false) {
		public void valueChanged() {
			tab = (boolean) this.getValue();
		}
	};

	boolean scoreboard;
	ModuleBoolean scoreboardOption = new ModuleBoolean("scoreboard", false) {
		public void valueChanged() {
			scoreboard = (boolean) this.getValue();
		}
	};

	String sessionUsername;

	public void onEnable() {
		super.onEnable();
		sessionUsername = mc.getSession().getUsername();
	}

	@EventHandler
	private Listener<OnSetSessionEvent> onSetSessionEvent = new Listener<>(e -> {
		sessionUsername = mc.getSession().getUsername();
	});

	@EventHandler
	private Listener<OnScoreboardPlayerNameEvent> onScoreboardPlayerNameEvent = new Listener<>(e -> {
		if (scoreboard) {
			String line = e.getLine();
			if (line.contains(sessionUsername)) {
				e.setLine(line.replace(sessionUsername, "§k" + sessionUsername + "§r"));
			}
		}
	});

	@EventHandler
	private Listener<OnTabPlayerNameEvent> onTabPlayerNameEvent = new Listener<>(e -> {
		if (tab) {
			String line = e.getName();
			if (line.contains(sessionUsername)) {
				e.setName(line.replace(sessionUsername, "§k" + sessionUsername + "§r"));
			}
		}
	});

	@EventHandler
	private Listener<OnDrawChatLineEvent> onDrawChatLineEvent = new Listener<>(e -> {
		if (chat) {
			String line = e.getChatLine();
			if (line.contains(sessionUsername)) {
				e.setChatLine(line.replace(sessionUsername, "§k" + sessionUsername + "§r"));
			}
		}
	});
}

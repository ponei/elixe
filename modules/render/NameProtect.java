package elixe.modules.render;

import elixe.events.OnDrawChatLineEvent;
import elixe.events.OnDrawTitleEvent;
import elixe.events.OnScoreboardPlayerNameEvent;
import elixe.events.OnSetSessionEvent;
import elixe.events.OnTabPlayerNameEvent;
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
		moduleOptions.add(titleOption);
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
	
	boolean title;
	ModuleBoolean titleOption = new ModuleBoolean("title", false) {
		
		public void valueChanged() {
			title = (boolean) this.getValue();
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
	private Listener<OnDrawTitleEvent> onDrawTitleEvent = new Listener<>(e -> {
		if (title) {		
			e.setTitle(e.getTitle().replace(sessionUsername, "§k" + sessionUsername + "§r"));
			e.setSubtitle(e.getSubtitle().replace(sessionUsername, "§k" + sessionUsername + "§r"));
		}
	});
	
	@EventHandler
	private Listener<OnScoreboardPlayerNameEvent> onScoreboardPlayerNameEvent = new Listener<>(e -> {
		if (scoreboard) {
			e.setLine(e.getLine().replace(sessionUsername, "§k" + sessionUsername + "§r"));
		}
	});

	@EventHandler
	private Listener<OnTabPlayerNameEvent> onTabPlayerNameEvent = new Listener<>(e -> {
		if (tab) {
			e.setName(e.getName().replace(sessionUsername, "§k" + sessionUsername + "§r"));
		}
	});

	@EventHandler
	private Listener<OnDrawChatLineEvent> onDrawChatLineEvent = new Listener<>(e -> {
		if (chat) {
			e.setChatLine(e.getChatLine().replace(sessionUsername, "§k" + sessionUsername + "§r"));
		}
	});
}

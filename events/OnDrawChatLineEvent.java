package elixe.events;

//drawChat(int) : void - net.minecraft.client.gui.GuiNewChat
//L:87
public class OnDrawChatLineEvent {
	private String chatLine;

	public OnDrawChatLineEvent(String chatLine) {
		super();
		this.chatLine = chatLine;
	}

	public String getChatLine() {
		return chatLine;
	}

	public void setChatLine(String chatLine) {
		this.chatLine = chatLine;
	}
	
	
}

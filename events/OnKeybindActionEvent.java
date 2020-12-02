package elixe.events;

//runTick() : void - net.minecraft.client.Minecraft
//L:1902
public class OnKeybindActionEvent {
	//used for the three main actions: attack, use and pick
	private boolean pressed;
	private int key;

	public OnKeybindActionEvent(boolean pressed, int key) {
		super();
		this.pressed = pressed;
		this.key = key;
	}

	public boolean isPressed() {
		return pressed;
	}

	public int getKey() {
		return key;
	}

}

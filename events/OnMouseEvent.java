package elixe.events;

//runTick() : void - net.minecraft.client.Minecraft
//L:1655
public class OnMouseEvent {
	private boolean state;
	private int button;
	
	public OnMouseEvent(boolean state, int button) {
		super();
		this.state = state;
		this.button = button;
	}
	
	public boolean isState() {
		return state;
	}
	
	public int getButton() {
		return button;
	}
	
	
}

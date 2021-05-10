package elixe.events;

//runTick() : void - net.minecraft.client.Minecraft
//L:1708
public class OnKeyEvent {
	private boolean state;
    private int key;
    
    
    
	public OnKeyEvent(boolean state, int key) {
		super();
		this.state = state;
		this.key = key;
	}
	
	public boolean isState() {
		return state;
	}
	public int getKey() {
		return key;
	}



}
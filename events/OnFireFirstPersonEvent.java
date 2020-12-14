package elixe.events;

public class OnFireFirstPersonEvent {
	private float height;

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public OnFireFirstPersonEvent(float height) {
		super();
		this.height = height;
	}

}

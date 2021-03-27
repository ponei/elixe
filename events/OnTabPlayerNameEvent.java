package elixe.events;

public class OnTabPlayerNameEvent {
	private String name;

	public OnTabPlayerNameEvent(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}

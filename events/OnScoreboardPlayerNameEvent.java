package elixe.events;

public class OnScoreboardPlayerNameEvent {
	private String name;

	public OnScoreboardPlayerNameEvent(String name) {
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

package elixe.events;

public class OnScoreboardPlayerNameEvent {
	private String line;

	public OnScoreboardPlayerNameEvent(String line) {
		super();
		this.line = line;
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}
}

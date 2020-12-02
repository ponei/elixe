package elixe.file;

import java.io.IOException;

public interface FileConfig {
	public void loadConfig() throws IOException;

	public void saveConfig() throws IOException;
}

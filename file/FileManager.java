package elixe.file;

import java.io.File;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import elixe.Elixe;
import elixe.file.config.ModuleConfig;
import elixe.file.config.ModulePersonal;
import elixe.utils.misc.LoggingUtils;
import net.minecraft.client.Minecraft;

public class FileManager {
	private Minecraft mc;
	private File dir;

	public ModuleConfig MODULE_CONFIG;
	public ModulePersonal MODULE_PERSONAL;
	
	public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	
	public FileManager() {
		mc = Elixe.INSTANCE.mc;
		dir = new File(mc.mcDataDir, "elixe");
		
		initialize();
		
		try {
			MODULE_CONFIG = new ModuleConfig(new File(dir, "modules.json"));
			MODULE_PERSONAL = new ModulePersonal(new File(dir, "modules_personal.json"));
		} catch (IOException e) {
			LoggingUtils.out("error trying to initialize module configs");
		}
		
	}

	public void initialize() {
		if (!dir.exists()) {
			dir.mkdir();
		}
	}
}

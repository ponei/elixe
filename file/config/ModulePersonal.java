package elixe.file.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import elixe.Elixe;
import elixe.file.FileConfig;
import elixe.file.FileManager;
import elixe.modules.AModuleOption;
import elixe.modules.Module;
import elixe.modules.option.ModuleKey;
import elixe.modules.render.ClickGUI;

public class ModulePersonal implements FileConfig {

	private File dir;

	public ModulePersonal(File dir) throws IOException {
		this.dir = dir;

		initialize();
	}

	public void initialize() throws IOException {
		if (!dir.exists()) {
			dir.createNewFile();
			saveConfig();
		} else {
			loadConfig();
		}
	}

	
	public void loadConfig() throws IOException {
		JsonObject jsonObject = (JsonObject) new JsonParser().parse(new BufferedReader(new FileReader(dir)));

		Iterator<Map.Entry<String, JsonElement>> iterator = jsonObject.entrySet().iterator();

		while (iterator.hasNext()) {
			Map.Entry<String, JsonElement> entry = iterator.next();

			Module module = Elixe.INSTANCE.MODULE_MANAGER.getModuleByName(entry.getKey());

			if (module != null) {
				JsonObject jsonModule = (JsonObject) entry.getValue();

				// state
				if (!(module instanceof ClickGUI)) {
					JsonElement elementState = jsonModule.get("state");
					if (elementState != null) {
						if (elementState.getAsBoolean()) {
							module.toggle();
						}
					}
				}

				// keys
				for (AModuleOption moduleOpt : module.getOptions()) {
					if (moduleOpt instanceof ModuleKey) {

						JsonElement element = jsonModule.get(moduleOpt.getName());
						if (element != null) {
							moduleOpt.setValue(element.getAsInt());
						}
					}
				}
			}
		}
	}

	
	public void saveConfig() throws IOException {
		JsonObject jsonObject = new JsonObject();

		for (Module module : Elixe.INSTANCE.MODULE_MANAGER.getModules()) {
			JsonObject jsonMod = new JsonObject();
			// state
			jsonMod.addProperty("state", module.isToggled());
			// keys
			for (AModuleOption moduleOpt : module.getOptions()) {
				if (moduleOpt instanceof ModuleKey) {
					jsonMod.addProperty(moduleOpt.getName(), (Number) moduleOpt.getValue());
				}
			}
			jsonObject.add(module.getName(), jsonMod);
		}

		PrintWriter printWriter = new PrintWriter(new FileWriter(dir));
		printWriter.println(FileManager.GSON.toJson(jsonObject));
		printWriter.close();
	}

}

package elixe.file.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import elixe.Elixe;
import elixe.file.FileConfig;
import elixe.modules.Module;
import elixe.modules.AModuleOption;
import elixe.modules.option.ModuleArrayMultiple;
import elixe.modules.option.ModuleBoolean;
import elixe.modules.option.ModuleFloat;
import elixe.modules.option.ModuleKey;

public class ModuleConfig implements FileConfig {

	private File dir;

	public ModuleConfig(File dir) throws IOException {
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

				for (AModuleOption moduleOpt : module.getOptions()) {
					if (!(moduleOpt instanceof ModuleKey)) {

						JsonElement element = jsonModule.get(moduleOpt.getName());

						if (element != null) {
							try {
								if (moduleOpt instanceof ModuleBoolean) {
									moduleOpt.setValue(element.getAsBoolean());
								} else if (moduleOpt instanceof ModuleFloat) {
									moduleOpt.setValue(element.getAsFloat());
								} else if (moduleOpt instanceof ModuleArrayMultiple) {
									JsonArray indexesArray = element.getAsJsonArray();

									boolean[] selectedIndexes = (boolean[]) moduleOpt.getValue();

									for (int i = 0; i < selectedIndexes.length; i++) {
										JsonElement optEle = indexesArray.get(i);
										boolean optState = false;
										if (optEle != null) {
											optState = optEle.getAsBoolean();
										}

										selectedIndexes[i] = optState;
									}

									moduleOpt.setValue(selectedIndexes);
								} else {
									moduleOpt.setValue(element.getAsInt());
								}
							} catch (NumberFormatException e) {
								// format errado
							}
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

			for (AModuleOption moduleOpt : module.getOptions()) { // todos as opcoes
				if (!(moduleOpt instanceof ModuleKey)) { // nao é key. key vai no pessoal

					if (moduleOpt instanceof ModuleBoolean) {
						jsonMod.addProperty(moduleOpt.getName(), (Boolean) moduleOpt.getValue());
					} else if (moduleOpt instanceof ModuleArrayMultiple) {
						JsonArray selectedArray = new JsonArray();

						boolean[] optSelected = (boolean[]) moduleOpt.getValue();
						for (int i = 0; i < optSelected.length; i++) {
							JsonElement selectedIndexState = new JsonPrimitive(optSelected[i]);
							selectedArray.add(selectedIndexState);
						}

						jsonMod.add(moduleOpt.getName(), selectedArray);

					} else {
						jsonMod.addProperty(moduleOpt.getName(), (Number) moduleOpt.getValue());
					}
				}
			}

			jsonObject.add(module.getName(), jsonMod);
		}

		PrintWriter printWriter = new PrintWriter(new FileWriter(dir));
		printWriter.println(Elixe.INSTANCE.FILE_MANAGER.GSON.toJson(jsonObject));
		printWriter.close();
	}

}

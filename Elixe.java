package elixe;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.security.NoSuchAlgorithmException;

import elixe.file.FileManager;
import elixe.modules.ModuleManager;
import elixe.utils.player.ConditionalsUtils;
import me.zero.alpine.bus.EventBus;
import me.zero.alpine.bus.EventManager;
import net.minecraft.client.Minecraft;

public class Elixe {
	public static Elixe INSTANCE;
	public final EventBus EVENT_BUS = new EventManager();
	public final ModuleManager MODULE_MANAGER;
	public final FileManager FILE_MANAGER;
	public final ConditionalsUtils CONDITIONALS;

	public Minecraft mc;

	public String build = "betatest4";

	//startGame() : void - net.minecraft.client.Minecraft
	//L:552
	public Elixe(Minecraft mc) {
		INSTANCE = this;
		
		this.mc = mc;

		this.CONDITIONALS = new ConditionalsUtils();
		this.MODULE_MANAGER = new ModuleManager();
		this.FILE_MANAGER = new FileManager();

		initialize();
	}

	private void initialize() {

		EVENT_BUS.subscribe(MODULE_MANAGER);
	}


}

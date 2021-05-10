package elixe;

import elixe.file.FileManager;
import elixe.modules.ModuleManager;
import elixe.utils.player.PlayerConditionals;
import elixe.utils.player.Rotations;
import me.zero.alpine.bus.EventBus;
import me.zero.alpine.bus.EventManager;
import net.minecraft.client.Minecraft;

public class Elixe {
	public static Elixe INSTANCE;
	public final EventBus EVENT_BUS = new EventManager();
	public final ModuleManager MODULE_MANAGER;
	public final FileManager FILE_MANAGER;

	public final Rotations ROTATIONS;
	public final PlayerConditionals CONDITIONALS;

	public Minecraft mc;

	public String build = "betatest7";

	// startGame() : void - net.minecraft.client.Minecraft
	// L:552
	public Elixe(Minecraft mc) {
		INSTANCE = this;

		this.mc = mc;

		this.ROTATIONS = new Rotations();
		this.CONDITIONALS = new PlayerConditionals();

		this.MODULE_MANAGER = new ModuleManager();
		this.FILE_MANAGER = new FileManager();

		initialize();
	}

	private void initialize() {

		EVENT_BUS.subscribe(CONDITIONALS);
		EVENT_BUS.subscribe(ROTATIONS);

		EVENT_BUS.subscribe(MODULE_MANAGER);
	}

}

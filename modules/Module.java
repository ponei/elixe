package elixe.modules;

import java.util.ArrayList;

import elixe.Elixe;
import elixe.modules.option.ModuleKey;
import elixe.utils.player.PlayerConditionals;
import me.zero.alpine.listener.Listenable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.Entity;

public class Module implements Listenable, Comparable<Module> {
	private String name;
	private ModuleCategory category;
	private boolean toggled;

	private int key = 0;
	private ModuleKey keyOption = new ModuleKey(0) {
		public void valueChanged() {
			key = (int) this.getValue();
		}
	};

	protected PlayerConditionals conditionals = Elixe.INSTANCE.CONDITIONALS;
	protected Minecraft mc = Elixe.INSTANCE.mc;
	protected RenderManager renderManager = mc.getRenderManager();
	protected RenderItem renderItem = mc.getRenderItem();
	protected TextureManager textureManager = mc.getTextureManager();
	protected ArrayList<AModuleOption> moduleOptions = new ArrayList<AModuleOption>();

	protected void onEnable() {
		Elixe.INSTANCE.EVENT_BUS.subscribe(this);
	}

	protected void onDisable() {
		Elixe.INSTANCE.EVENT_BUS.unsubscribe(this);
	}

	public Module(String name, ModuleCategory category) {
		this.name = name;
		this.category = category;
		this.toggled = false;

		moduleOptions.add(this.keyOption);
	}

	public Module(String name, ModuleCategory category, int keyMod) {
		this.name = name;
		this.category = category;
		this.toggled = false;

		keyOption.setValue(keyMod);
		moduleOptions.add(this.keyOption);
	}

	public void toggle() {
		toggled = !toggled;

		if (toggled) {
			onEnable();
		} else {
			onDisable();
		}
	}

	public String getName() {
		return name;
	}

	public ModuleCategory getCategory() {
		return category;
	}

	public ArrayList<AModuleOption> getOptions() {
		return moduleOptions;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.keyOption.setValue(key);
	}

	public boolean isToggled() {
		return toggled;
	}

	public void updateVisibilityOfOptions(AModuleOption[][] multipleControls, int index) {
		for (int i = 0; i < multipleControls.length; i++) {
			for (int j = 0; j < multipleControls[i].length; j++) {
				if (i == index) {
					multipleControls[i][j].setShow(true);
				} else {
					multipleControls[i][j].setShow(false);
				}
			}
		}
	}

	public void updateVisibilityOfOptions(AModuleOption[] controls, boolean state) {
		for (int i = 0; i < controls.length; i++) {
			controls[i].setShow(state);
		}
	}

	@Override
	public int compareTo(Module arg0) {
		FontRenderer fontR = this.mc.fontRendererObj;
		return fontR.getStringWidth(arg0.getName().toLowerCase()) - fontR.getStringWidth(name.toLowerCase());
	}
}

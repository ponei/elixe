package elixe.modules.render;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import org.lwjgl.opengl.GL11;

import elixe.Elixe;
import elixe.events.OnRender2DEvent;
import elixe.events.OnTickEvent;
import elixe.modules.Module;
import elixe.modules.ModuleCategory;
import elixe.modules.ModuleManager;
import elixe.modules.option.ModuleBoolean;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;

public class HUD extends Module {

	private ModuleManager moduleManager;
	private ArrayList<Module> modules = new ArrayList<Module>();

	public HUD() {
		super("HUD", ModuleCategory.RENDER);
		
		moduleOptions.add(watermarkOption);
		moduleOptions.add(moduleListOption);
		moduleOptions.add(sprintingOption);
	}

	boolean watermark;
	ModuleBoolean watermarkOption = new ModuleBoolean("watermark", false) {
		public void valueChanged() {
			watermark = (boolean) this.getValue();
		}
	};

	boolean moduleList;
	ModuleBoolean moduleListOption = new ModuleBoolean("module list", false) {
		public void valueChanged() {
			moduleList = (boolean) this.getValue();
		}
	};

	boolean sprinting;
	ModuleBoolean sprintingOption = new ModuleBoolean("sprinting", false) {
		public void valueChanged() {
			sprinting = (boolean) this.getValue();
		}
	};

	public void setModuleManager(ModuleManager moduleManager) {
		this.moduleManager = moduleManager;
		modules = (ArrayList<Module>) moduleManager.getModules().clone();
		Collections.sort(modules);
	}

	String address = "";
	@EventHandler
	private Listener<OnRender2DEvent> onRender2DEvent = new Listener<>(e -> {
		if (!this.mc.gameSettings.showDebugInfo) {
			int ySpacing = 5;
			int xSpacing = 10;
			
			if (watermark) {
				mc.fontRendererObj.drawStringWithShadow("(" + address + ")", 10f, ySpacing + 14, 0.5f, 0.4f);
				GL11.glPushMatrix();
				GL11.glScalef(1.5f, 1.5f, 1.5f);
				mc.fontRendererObj.drawStringWithShadow("elixe", 7f, ySpacing, 1f, 1f);
				GL11.glPopMatrix();
				mc.fontRendererObj.drawStringWithShadow("(" + Elixe.INSTANCE.build + ")", 48f, ySpacing + 6, 0.5f, 0.8f);
				ySpacing += 23;
				xSpacing += 110;
			}

			if (moduleList) {
				int yModule = ySpacing;
				for (Module module : modules) {
					if (module.isToggled()) {
						mc.fontRendererObj.drawStringWithShadow(module.getName().toLowerCase(), 10f, yModule, 1f, 0.5f);
						yModule += mc.fontRendererObj.FONT_HEIGHT;
					}
				}
				if (!watermark) {
					xSpacing += 80;
				}
			}

			
			if (sprinting) {
				mc.fontRendererObj.drawStringWithShadow(mc.thePlayer.isSprinting() ? "[Sprinting (Toggled)]" : "", xSpacing, watermark ? 11f : 5f, 1f, 1f);
			}

		}
	});
	
	//channelActive(ChannelHandlerContext) : void - net.minecraft.network.NetworkManager
	//L:111
	public void setRemoteAddress(String addr) {
		
		if (addr.contains("local:")) {
			address = addr;
		} else {
			address = addr.split("/")[1];
		}
	}
}

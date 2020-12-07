package elixe.modules.render;

import elixe.Elixe;
import elixe.events.OnKeyEvent;
import elixe.events.OnRender3DEvent;
import elixe.modules.Module;
import elixe.modules.ModuleCategory;
import elixe.modules.option.ModuleKey;
import elixe.ui.clickgui.ElixeMenu;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;

public class ClickGUI extends Module {
	
	public ClickGUI() {
		super("ClickGUI", ModuleCategory.RENDER, 45); //x
	} 

	public ElixeMenu menu = new ElixeMenu(this);
	
	public void onEnable() {
		super.onEnable();
		mc.displayGuiScreen(menu);
	}
	
	public void onDisable() {
		super.onDisable();
		mc.displayGuiScreen(null);
	}
}

package elixe.modules.render;

import elixe.modules.Module;
import elixe.modules.ModuleCategory;
import elixe.ui.clickgui.ElixeMenu;

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

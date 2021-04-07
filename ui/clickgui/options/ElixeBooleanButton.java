package elixe.ui.clickgui.options;

import elixe.modules.Module;
import elixe.modules.option.ModuleBoolean;
import elixe.ui.base.ElixeButtonCheckboxBase;
import elixe.ui.clickgui.ElixeMenu;

public class ElixeBooleanButton extends ElixeButtonCheckboxBase {

	ModuleBoolean option;
	
	//menu ref
	ElixeMenu menu;

	public ElixeBooleanButton(ElixeMenu menu, String text, ModuleBoolean opt, int x, int y, int wid, int hei) {
		super(text, x, y, wid, hei);
		opt.setButton(this);
		this.option = opt;
		this.menu = menu;
		
		setEnabled((boolean) option.getValue());
	}

	public void setValue(Object v) {
		setEnabled((boolean) v);
	}
	
	public boolean mouseClick(int mouseX, int mouseY,  int mouseButton) {
		if (!checkMouseClick(mouseX, mouseY)) {
			return false;
		}
		setEnabled(!isEnabled());
		option.setValue(isEnabled());
		if (option.shouldUpdate()) {
			menu.addOptions(menu.CURRENT_MODULE, true);
		}
		return true;
	}
}

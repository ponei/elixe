package elixe.ui.clickgui.options;

import elixe.modules.Module;
import elixe.modules.option.ModuleBoolean;
import elixe.ui.base.ElixeButtonCheckboxBase;

public class ElixeBooleanButton extends ElixeButtonCheckboxBase {

	ModuleBoolean option;

	public ElixeBooleanButton(String text, ModuleBoolean opt, int x, int y, int wid, int hei) {
		super(text, x, y, wid, hei);
		opt.setButton(this);
		this.option = opt;
		
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
		return true;
	}
}

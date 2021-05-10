package elixe.modules;

import elixe.ui.clickgui.controls.base.ElixeButtonBase;

public abstract class AModuleOption {
	private boolean show = true;
	
	public boolean shouldShow() {
		return show;
	}
	
	public void setShow(boolean b) {
		show = b;
	}
	
	private boolean updateOnChange = false;
	
	public boolean shouldUpdate() {
		return updateOnChange;
	}
	
	public void setShouldUpdate(boolean b) {
		updateOnChange = b;
	}
	
	public void valueChanged() {
		
	}
	
	public abstract Object getValue();
	
	public abstract void setValue(Object v);

	protected String name;
	
	public String getName() {
		return name;
	}
	
	private ElixeButtonBase bt;
	
	public void setButton(ElixeButtonBase button) {
		this.bt = button;
	}
	
	public ElixeButtonBase getButton() {
		return bt;
	}
}

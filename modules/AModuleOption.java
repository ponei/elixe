package elixe.modules;

import java.lang.reflect.Type;

import elixe.Elixe;
import elixe.ui.base.ElixeButtonBase;
import net.minecraft.client.Minecraft;

public abstract class AModuleOption {
	private boolean show = true;
	
	public boolean shouldShow() {
		return show;
	}
	
	public void setShow(boolean b) {
		show = b;
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

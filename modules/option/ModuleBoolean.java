package elixe.modules.option;

import java.lang.reflect.Type;

import elixe.modules.IModuleOption;
import elixe.ui.base.ElixeButtonBase;

public class ModuleBoolean implements IModuleOption {
	private boolean state;
	private String name;
	
	private ElixeButtonBase bt;
	

	public ModuleBoolean(String name, boolean state) {
		super();
		this.state = state;
		this.name = name;
		valueChanged();
	}
	
	public void setButton(ElixeButtonBase bt) {
		this.bt = bt;
	}
	
	public ElixeButtonBase getButton() {
		return bt;
	}

	public Object getValue() {
		return state;
	}
	
	public void setValue(Object v) {
		this.state = (boolean) v;
		valueChanged();
	}

	public String getName() {
		return name;
	}

	public void valueChanged() {

	}
}

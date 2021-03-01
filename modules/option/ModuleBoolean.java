package elixe.modules.option;

import java.lang.reflect.Type;

import elixe.modules.AModuleOption;
import elixe.ui.base.ElixeButtonBase;

public class ModuleBoolean extends AModuleOption {
	private boolean state;


	public ModuleBoolean(String name, boolean state) {
		super();
		this.state = state;
		this.name = name;
		valueChanged();
	}
	
	public Object getValue() {
		return state;
	}
	
	public void setValue(Object v) {
		this.state = (boolean) v;
		valueChanged();
	}
}

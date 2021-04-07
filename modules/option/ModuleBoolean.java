package elixe.modules.option;

import java.lang.reflect.Type;

import elixe.modules.AModuleOption;
import elixe.ui.base.ElixeButtonBase;

public class ModuleBoolean extends AModuleOption {
	private boolean state;


	public ModuleBoolean(String name, boolean state) {
		this(name, state, false);
	}
	
	public ModuleBoolean(String name, boolean state, boolean update) {
		super();
		setShouldUpdate(update);
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

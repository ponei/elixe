package elixe.modules.option;

import java.lang.reflect.Type;

import elixe.modules.ModuleOption;

public class ModuleBoolean implements ModuleOption {
	private boolean state;
	private String name;

	public ModuleBoolean(String name, boolean state) {
		super();
		this.state = state;
		this.name = name;
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

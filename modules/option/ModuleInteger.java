package elixe.modules.option;

import java.lang.reflect.Type;

import elixe.modules.AModuleOption;
import elixe.ui.base.ElixeButtonBase;
import elixe.ui.clickgui.options.ElixeIntegerButton;

public class ModuleInteger extends AModuleOption {
	private int value;
	private int min, max;

	public ModuleInteger(String name, int value, int min, int max) {
		super();
		this.name = name;
		this.value = value;

		this.min = min;
		this.max = max;
		valueChanged();
	}

	
	public int getMin() {
		return min;
	}

	public int getMax() {
		return max;
	}
	
	public void setValueSilent(int i) {
		this.value = i;
	}

	
	public Object getValue() {
		return value;
	}
	
	public void setValue(Object v) {
		this.value = (int) v;
		valueChanged();
	}
}

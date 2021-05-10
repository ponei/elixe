package elixe.modules.option;

import elixe.modules.AModuleOption;

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

package elixe.modules.option;

import java.lang.reflect.Type;

import elixe.modules.AModuleOption;
import elixe.ui.base.ElixeButtonBase;
import elixe.ui.clickgui.options.ElixeFloatButton;

public class ModuleFloat extends AModuleOption {
	private float value;
	private float min, max;

	public ModuleFloat(String name, float value, float min, float max) {
		super();
		this.name = name;
		this.value = value;

		this.min = min;
		this.max = max;
		valueChanged();
	}

	public float getMin() {
		return min;
	}

	public float getMax() {
		return max;
	}

	public void setValueSilent(float f) {
		this.value = f;
	}


	public Object getValue() {
		return value;
	}
	
	public void setValue(Object v) {
		this.value = (float) v;
		valueChanged();
	}
}

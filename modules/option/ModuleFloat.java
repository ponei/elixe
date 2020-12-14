package elixe.modules.option;

import java.lang.reflect.Type;

import elixe.modules.ModuleOption;
import elixe.ui.clickgui.options.ElixeFloatButton;

public class ModuleFloat implements ModuleOption {

	private String name;
	private float value;

	private float min, max;
	
	private ElixeFloatButton bt;

	public ModuleFloat(String name, float value, float min, float max) {
		super();
		this.name = name;
		this.value = value;

		this.min = min;
		this.max = max;
		valueChanged();
	}
	
	public void setButton(ElixeFloatButton bt) {
		this.bt = bt;
	}
	
	public ElixeFloatButton getButton() {
		return bt;
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
	
	public void valueChanged() {

	}

	public Object getValue() {
		return value;
	}
	
	public void setValue(Object v) {
		this.value = (float) v;
		valueChanged();
	}

	public String getName() {
		return name;
	}


}

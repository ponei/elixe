package elixe.modules.option;

import java.lang.reflect.Type;

import elixe.modules.IModuleOption;
import elixe.ui.base.ElixeButtonBase;
import elixe.ui.clickgui.options.ElixeIntegerButton;

public class ModuleInteger implements IModuleOption {

	private String name;
	private int value;

	private ElixeButtonBase bt;
	
	private int min, max;

	public ModuleInteger(String name, int value, int min, int max) {
		super();
		this.name = name;
		this.value = value;

		this.min = min;
		this.max = max;
		valueChanged();
	}

	public void setButton(ElixeButtonBase bt) {
		this.bt = bt;
	}
	
	public ElixeButtonBase getButton() {
		return bt;
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

	public void valueChanged() {

	}

	public Object getValue() {
		return value;
	}
	
	public void setValue(Object v) {
		this.value = (int) v;
		valueChanged();
	}

	public String getName() {
		return name;
	}


}

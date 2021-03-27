package elixe.modules.option;

import java.lang.reflect.Type;

import elixe.modules.AModuleOption;
import elixe.ui.base.ElixeButtonBase;

public class ModuleArray extends AModuleOption {
	private String[] array;
	private int selectedIndex;
	

	public ModuleArray(String name, int index, String[] array) {
		this(name, index, array, false);
	}

	public ModuleArray(String name, int index, String[] array, boolean b) {
		super();
		setShouldUpdate(b);
		this.selectedIndex = index;
		this.array = array;
		this.name = name;
		valueChanged();
	}

	public String[] getArray() {
		return array;
	}

	public String getSelected() {
		return array[selectedIndex];
	}


	public Object getValue() {
		return selectedIndex;
	}

	public void setValue(Object v) {
		this.selectedIndex = (int) v;
		valueChanged();
	}

}
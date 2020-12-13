package elixe.modules.option;

import java.lang.reflect.Type;

import elixe.modules.ModuleOption;

public class ModuleArray implements ModuleOption {
	private String[] array;
	private int selectedIndex;
	
	private String name;

	public ModuleArray(String name, int index, String[] array) {
		super();
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

	public String getName() {
		return name;
	}

	public void valueChanged() {

	}
}
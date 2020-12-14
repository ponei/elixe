package elixe.modules.option;

import java.lang.reflect.Type;

import elixe.modules.ModuleOption;

public class ModuleArrayMultiple implements ModuleOption {
	private String[] array;
	private boolean[] selectedIndexes;
	
	private String name;

	public ModuleArrayMultiple(String name, boolean[] selected, String[] array) {
		super();
		this.selectedIndexes = selected;
		this.array = array;
		this.name = name;
		valueChanged();
	}

	public String[] getArray() {
		return array;
	}
	
	
	public void changeIndex(int index, boolean state) {
		selectedIndexes[index] = state;
		valueChanged();
	}

	
	public Object getValue() {
		return selectedIndexes;
	}
	
	public void setValue(Object b) {
		this.selectedIndexes = (boolean[]) b;
		valueChanged();
	}

	public String getName() {
		return name;
	}

	public void valueChanged() {

	}
}
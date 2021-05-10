package elixe.modules.option;

import elixe.modules.AModuleOption;

public class ModuleArrayMultiple extends AModuleOption {
	private String[] array;
	private boolean[] selectedIndexes;

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

}
package elixe.modules.option;

import java.lang.reflect.Type;

import elixe.modules.IModuleOption;
import elixe.ui.base.ElixeButtonBase;

public class ModuleArrayMultiple implements IModuleOption {
	private String[] array;
	private boolean[] selectedIndexes;
	
	private String name;

	private ElixeButtonBase bt;
	
	
	public ModuleArrayMultiple(String name, boolean[] selected, String[] array) {
		super();
		this.selectedIndexes = selected;
		this.array = array;
		this.name = name;
		valueChanged();
	}
	
	public void setButton(ElixeButtonBase bt) {
		this.bt = bt;
	}
	
	public ElixeButtonBase getButton() {
		return bt;
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
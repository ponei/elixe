package elixe.modules.option;

import java.lang.reflect.Type;

import org.lwjgl.input.Keyboard;

import elixe.modules.AModuleOption;
import elixe.ui.base.ElixeButtonBase;

public class ModuleKey extends AModuleOption {

	private int key;

	public ModuleKey(int key) {
		super();
		this.name = "key";
		this.key = key;
		valueChanged();
	}

	public Object getValue() {
		return key;
	}
	
	public void setValue(Object v) {
		this.key = (int) v;
		valueChanged();
	}
}

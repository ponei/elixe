package elixe.modules.option;

import java.lang.reflect.Type;

import org.lwjgl.input.Keyboard;

import elixe.modules.ModuleOption;

public class ModuleKey implements ModuleOption {

	private int key;
	private String name = "key";

	public ModuleKey(int key) {
		super();
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

	public String getName() {
		return name;
	}


	public void valueChanged() {
		
	}

}

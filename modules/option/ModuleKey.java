package elixe.modules.option;

import java.lang.reflect.Type;

import org.lwjgl.input.Keyboard;

import elixe.modules.IModuleOption;
import elixe.ui.base.ElixeButtonBase;

public class ModuleKey implements IModuleOption {

	private int key;
	private String name = "key";
	private ElixeButtonBase bt;
	

	public ModuleKey(int key) {
		super();
		this.key = key;
		valueChanged();
	}
	
	public void setButton(ElixeButtonBase bt) {
		this.bt = bt;
	}
	
	public ElixeButtonBase getButton() {
		return bt;
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

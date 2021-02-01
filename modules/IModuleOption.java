package elixe.modules;

import java.lang.reflect.Type;

import elixe.Elixe;
import elixe.ui.base.ElixeButtonBase;
import net.minecraft.client.Minecraft;

public interface IModuleOption {	
	public void valueChanged();
	
	public Object getValue();
	
	public void setValue(Object v);

	public String getName();
	
	public void setButton(ElixeButtonBase button);
	
	public ElixeButtonBase getButton();
}

package elixe.modules;

import java.lang.reflect.Type;

public interface ModuleOption {
	public void valueChanged();
	
	public Object getValue();
	
	public void setValue(Object v);

	public String getName();
}

package elixe.modules.option;

import elixe.modules.IModuleOption;
import elixe.ui.base.ElixeButtonBase;

public class ModuleColor implements IModuleOption {
	private String name;
	private int color;

	private ElixeButtonBase bt;
	
	
	public ModuleColor(String name, int r, int g, int b) {
		super();
		this.name = name;
		RGBToInt(r, g, b);
		valueChanged();
	}

	public void setButton(ElixeButtonBase bt) {
		this.bt = bt;
	}
	
	public ElixeButtonBase getButton() {
		return bt;
	}
	
	public ModuleColor(String name, int color) {
		super();
		this.name = name;
		this.color = color;
		valueChanged();
	}

	// https://stackoverflow.com/questions/4801366/convert-rgb-values-to-integer#4801397
	private void RGBToInt(int r, int g, int b) {
		this.color = ((r & 0x0ff) << 16) | ((g & 0x0ff) << 8) | (b & 0x0ff);
	}
	
	private void GLRGBToInt(float r, float g, float b) {
		this.color = (((int)(r * 255f) & 0x0ff) << 16) | (((int)(g  * 255f) & 0x0ff) << 8) | ((int)(b  * 255f) & 0x0ff);
	}

	public float[] getGLRGB() {
		float r = ((color >> 16) & 0x0ff) / 255f;
		float g = ((color >> 8) & 0x0ff) / 255f;
		float b = ((color) & 0x0ff) / 255f;
		return new float[] { r, g, b };
	}
	
	public int[] getRGB() {
		int r = ((color >> 16) & 0x0ff);
		int g = ((color >> 8) & 0x0ff);
		int b = ((color) & 0x0ff);
		return new int[] { r, g, b };
	}

	public void setValueRGB(int r, int g, int b) {
		RGBToInt(r, g, b);
		valueChanged();
	}
	
	public void setValueGLRGB(float r, float g, float b) {
		GLRGBToInt(r, g, b);
		valueChanged();
	}

	public void valueChanged() {

	}

	public Object getValue() {
		return color;
	}

	public void setValue(Object v) {
		this.color = (int) v;
		valueChanged();
	}

	public String getName() {
		return name;
	}
}

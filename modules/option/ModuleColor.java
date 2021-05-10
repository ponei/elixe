package elixe.modules.option;

import elixe.modules.AModuleOption;

public class ModuleColor extends AModuleOption {
	private int color;

	
	public ModuleColor(String name, int r, int g, int b) {
		super();
		this.name = name;
		RGBToInt(255, r, g, b);
		valueChanged();
	}

	
	public ModuleColor(String name, int color) {
		super();
		this.name = name;
		this.color = color;
		valueChanged();
	}

	// https://stackoverflow.com/questions/4801366/convert-rgb-values-to-integer#4801397
	private void RGBToInt(int a, int r, int g, int b) {
		this.color = ((a & 0x0ff) << 24) | ((r & 0x0ff) << 16) | ((g & 0x0ff) << 8) | (b & 0x0ff);
	}
	
	private void GLRGBToInt(float a, float r, float g, float b) {
		this.color = (((int)(a * 255f) & 0x0ff) << 24) | (((int)(r * 255f) & 0x0ff) << 16) | (((int)(g  * 255f) & 0x0ff) << 8) | ((int)(b  * 255f) & 0x0ff);
	}

	public float[] getGLRGB() {
		float a = ((color >> 24) & 0x0ff) / 255f;
		float r = ((color >> 16) & 0x0ff) / 255f;
		float g = ((color >> 8) & 0x0ff) / 255f;
		float b = ((color) & 0x0ff) / 255f;
		return new float[] { r, g, b, a };
	}
	
	public int[] getRGB() {
		int a = ((color >> 24) & 0x0ff);
		int r = ((color >> 16) & 0x0ff);
		int g = ((color >> 8) & 0x0ff);
		int b = ((color) & 0x0ff);
		return new int[] { r, g, b, a };
	}

	public void setValueRGB(int r, int g, int b, int a) {
		RGBToInt(a, r, g, b);
		valueChanged();
	}
	
	public void setValueGLRGB(float r, float g, float b, float a) {
		GLRGBToInt(a, r, g, b);
		valueChanged();
	}


	
	public Object getValue() {
		return color;
	}

	
	public void setValue(Object v) {
		this.color = (int) v;
		valueChanged();
	}

}

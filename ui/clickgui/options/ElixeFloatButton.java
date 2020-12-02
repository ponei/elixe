package elixe.ui.clickgui.options;

import java.awt.Color;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import elixe.modules.option.ModuleFloat;
import elixe.modules.option.ModuleKey;
import elixe.ui.base.ElixeButtonBase;
import elixe.ui.base.ElixeButtonNumberBase;
import elixe.utils.render.GUIUtils;

public class ElixeFloatButton extends ElixeButtonNumberBase {
	// opt
	private ModuleFloat option;

	// floats
	private float value;
	private float min, max;

	public ElixeFloatButton(String text, ModuleFloat opt, int x, int y, int wid, int hei, int def) {
		super(text, x, y, wid, hei, def);

		this.option = opt;
		option.setButton(this);
		
		// slider floats
		this.value = (float) opt.getValue();
		this.min = opt.getMin();
		this.max = opt.getMax();

		// slider strings
		this.valueS = String.format("%.2f", value);
		this.valueWid = fontrenderer.getStringWidth(valueS);

		this.minS = String.format("%.1f", min);
		this.maxS = String.format("%.1f", max);
		minWid = fontrenderer.getStringWidth(minS) + 4;
		maxWid = fontrenderer.getStringWidth(maxS);

		// slider math
		startX = x + minWid;
		endX = x + width - maxWid - 4;
		difX = endX - startX;

		valueRealX = Math.round((difX * (value - min)) / (max - min));
		valueX = startX + valueRealX;

		// cache polygons
		cacheSlider();
		cacheSliderBackground();
	}
	
	public void setValue(float v) {
		value = v;
		valueRealX = Math.round((difX * (value - min)) / (max - min));
		valueX = startX + valueRealX;
		cacheSlider();
		this.valueS = String.format("%.2f", value);
		this.valueWid = fontrenderer.getStringWidth(valueS);
	}

	public void mouseReleased(int mouseX, int mouseY, int state) {
		if (dragging) {
			dragging = false;
			option.setValue(value);
		}
	}

	public void mouseClickMove(int mouseX, int mouseY) {
		if (dragging) {
			if (mouseX > endX) {
				value = max;
				valueX = startX + difX;
			} else if (startX > mouseX) {
				value = min;
				valueX = startX;
			} else {
				valueRealX = mouseX - startX;
				valueX = startX + valueRealX;

				// rule of 3
				value = min + (((max - min) * valueRealX) / difX);
			}

			// cache polygons
			cacheSlider();
			this.valueS = String.format("%.2f", value);
			this.valueWid = fontrenderer.getStringWidth(valueS);
		}
	}
}

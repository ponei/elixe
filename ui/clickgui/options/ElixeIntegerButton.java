package elixe.ui.clickgui.options;

import elixe.Elixe;
import elixe.modules.option.ModuleFloat;
import elixe.modules.option.ModuleInteger;
import elixe.ui.base.ElixeButtonNumberBase;
import elixe.utils.misc.ChatUtils;

public class ElixeIntegerButton extends ElixeButtonNumberBase {
	// opt
	private ModuleInteger option;

	// ints
	private int value;
	private int min, max;

	public ElixeIntegerButton(String text, ModuleInteger opt, int x, int y, int wid, int hei, int def) {
		super(text, x, y, wid, hei, def);

		this.option = opt;
		option.setButton(this);
		
		// slider floats
		this.value = (int) opt.getValue();
		this.min = opt.getMin();
		this.max = opt.getMax();

		// slider strings
		this.valueS = String.valueOf(value);
		this.valueWid = fontrenderer.getStringWidth(valueS);

		this.minS = String.valueOf(min);
		this.maxS = String.valueOf(max);
		minWid = fontrenderer.getStringWidth(minS) + 4;
		maxWid = fontrenderer.getStringWidth(maxS);

		// slider math
		startX = x + minWid;
		endX = x + width - maxWid - 4;
		difX = endX - startX;

		valueRealX = (int) ((difX * (value - min)) / (max - min));
		valueX = startX + valueRealX;

		// cache polygons
		cacheSlider();
		cacheSliderBackground();
	}
	
	public void setValue(Object v) {
		value = (int) v;
		valueRealX = (int) ((difX * (value - min)) / (max - min));
		valueX = startX + valueRealX;
		cacheSlider();
		this.valueS = String.valueOf(value);
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

				// rule of 3
				float valueFloat = min + ((((float)max - (float)min) * (float)valueRealX) / (float)difX);
				value = Math.round(valueFloat);
				
				//ChatUtils.message(Elixe.INSTANCE.mc, String.valueOf(valueFloat));
				valueRealX = (int) ((difX * (value - min)) / (max - min));
				valueX = startX + valueRealX;
			}

			// cache polygons
			cacheSlider();
			this.valueS = String.valueOf(value);
			this.valueWid = fontrenderer.getStringWidth(valueS);
		}
	}
}

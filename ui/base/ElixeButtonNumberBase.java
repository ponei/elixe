package elixe.ui.base;

import org.lwjgl.opengl.GL11;

import elixe.modules.option.ModuleFloat;
import elixe.utils.render.GUIUtils;

public class ElixeButtonNumberBase extends ElixeButtonBase {
	// name and line
	protected String text;
	protected int textWid;
	protected int def, mid, sliderMid;

	// strings
	protected String valueS;
	protected int valueWid;

	protected String minS, maxS;
	protected int minWid, maxWid;

	// math
	protected int startX, endX;
	protected int difX;

	protected int valueX, valueRealX;

	// etc
	protected int sliderHeight = 14;

	public ElixeButtonNumberBase(String text, int x, int y, int wid, int hei, int def) {
		// button base
		this.x = x;
		this.y = y;
		this.width = wid;
		this.height = hei;
		this.def = def;

		// text
		this.text = text;
		textWid = fontrenderer.getStringWidth(text);
		mid = y + def / 2;
		sliderMid = y + def + 6;
	}

	protected void cacheSlider() {
		slider = GUIUtils.getRoundedRect(startX, y + def - 2, valueX, y + def - 2 + sliderHeight, 7);
	}

	protected void cacheSliderBackground() {
		sliderBackground = GUIUtils.getRoundedRect(startX, y + def - 2, endX, y + def - 2 + sliderHeight, 7);
	}

	public void updatePosition(int xDif, int yDif) {
		mid = y + def / 2;
		sliderMid = y + def + 6;
		startX = x + minWid;
		endX = x + width - maxWid - 4;
		for (double[] s : slider) {
			s[0] += xDif;
			s[1] += yDif;
		}
		for (double[] sb : sliderBackground) {
			sb[0] += xDif;
			sb[1] += yDif;
		}
	}

	protected boolean dragging = false;

	public boolean mouseClick(int mouseX, int mouseY, int mouseButton) {
		if (!containsSlider(mouseX, mouseY)) {
			return false;
		}
		dragging = true;
		mouseClickMove(mouseX, mouseY);
		return true;
	}

	public void mouseReleased(int mouseX, int mouseY, int state) {
		dragging = false;
	}

	public void mouseClickMove(int mouseX, int mouseY) {

	}
 
	// use cached polygons
	protected double[][] sliderBackground, slider;

	public void drawButton(int mouseX, int mouseY) {
		// line
		if (dragging) {
			GL11.glColor4f(0.86f, 0.86f, 0.86f, 1f); // 220
		} else {
			GL11.glColor4f(0.47f, 0.47f, 0.47f, 1f); // 120
		}

		GL11.glLineWidth(2f);
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex2f(x + 2 + textWid, mid);
		GL11.glVertex2f(x + width, mid);
		GL11.glEnd();

		// slider
		GUIUtils.drawPolygon(sliderBackground, 0.47f, 1f); // 120
		GUIUtils.drawPolygon(slider, 0.86f, 1f); // 220
	}

	public void drawText(int mouseX, int mouseY) {
		fontrenderer.drawStringWithShadow(text, x, mid - fontrenderer.FONT_HEIGHT / 2, 0.86f, 1f); // 220
		fontrenderer.drawStringWithShadow(minS, x, sliderMid - fontrenderer.FONT_HEIGHT / 2, 0.7f, 1f); // 180
		fontrenderer.drawStringWithShadow(maxS, x + width - maxWid, sliderMid - fontrenderer.FONT_HEIGHT / 2, 0.7f, 1f); // 180
		fontrenderer.drawString(valueS, startX + difX / 2 - valueWid / 2, sliderMid - fontrenderer.FONT_HEIGHT / 2,
				0.07f, 1f); // 20
	}

	private boolean containsSlider(int mouseX, int mouseY) {
		return (mouseX >= startX && mouseX <= endX && mouseY >= y + def - 2 && mouseY <= y + def - 2 + sliderHeight);
	}
}

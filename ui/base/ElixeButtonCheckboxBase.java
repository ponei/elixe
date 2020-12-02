package elixe.ui.base;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import elixe.utils.render.GUIUtils;

public class ElixeButtonCheckboxBase extends ElixeButtonBase {
	private String text;
	private boolean enabled = false;

	private int textWid;
	private int mid;

	public static int checkBoxWidth = 30;
	public static int checkBoxHeight = 7;

	public ElixeButtonCheckboxBase(String text, int x, int y, int wid, int hei) {
		this.text = text;

		this.x = x;
		this.y = y;
		this.width = wid;
		this.height = hei;

		textWid = fontrenderer.getStringWidth(text);
		mid = y + height / 2;

		// cache polygons
		cacheBackground();
	}

	// cache polygons
	public void setEnabled(boolean b) {
		enabled = b;
		cacheCircle();
	}

	private void cacheCircle() {
		checkCircle = GUIUtils.getCircle(enabled ? x + width - 7 : x + width - checkBoxWidth + 7, y + height / 2, 5);
	}

	private void cacheBackground() {
		checkBackground = GUIUtils.getRoundedRect(x + width - checkBoxWidth, y + height / 2 - checkBoxHeight, x + width,
				y + height / 2 + checkBoxHeight, 7);
	}

	protected boolean isEnabled() {
		return enabled;
	}

	// update all values
	public void updatePosition(int xDif, int yDif) {
		mid = y + height / 2;
		for (double[] cc : checkCircle) {
			cc[0] += xDif;
			cc[1] += yDif;
		}
		for (double[] cb : checkBackground) {
			cb[0] += xDif;
			cb[1] += yDif;
		}
	}

	public void drawText(int mouseX, int mouseY) {
		fontrenderer.drawStringWithShadow(text, x, y + height / 2f - fontrenderer.FONT_HEIGHT / 2, 0.86f, 1f); // 220
	}

	// use cached polygons
	double[][] checkBackground, checkCircle;

	public void drawButton(int mouseX, int mouseY) {
		float c = enabled ? 0.86f : 0.47f; // 220 e 120
		GUIUtils.drawPolygon(checkBackground, c, 1f);
		GUIUtils.drawPolygon(checkCircle, 0.078f, 1f); // 20
		// line

		GL11.glColor4f(c, c, c, 1f);

		GL11.glLineWidth(2f);
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex2f(x + 2 + textWid, mid);
		GL11.glVertex2f(x + width - checkBoxWidth - 3, mid);
		GL11.glEnd();
	}

	public boolean mouseClick(int mouseX, int mouseY, int mouseButton) {
		if (!checkMouseClick(mouseX, mouseY)) {
			return false;
		}
		return true;
	}

	public boolean contains(int mouseX, int mouseY) {
		return (mouseX >= this.x + width - checkBoxWidth && mouseX <= this.x + width
				&& mouseY >= this.y + height / 2 - checkBoxHeight && mouseY <= this.y + height / 2 + checkBoxHeight);
	}
}

package elixe.ui.clickgui.options;

import java.awt.Color;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import elixe.modules.option.ModuleKey;
import elixe.ui.base.ElixeButtonBase;
import elixe.ui.base.ElixeButtonCheckboxBase;
import elixe.utils.render.GUIUtils;

public class ElixeKeyButton extends ElixeButtonBase {

	private String text;
	private int textWid;

	private ModuleKey option;
	private String keyName;
	private int keyNameTextWid;

	private int mid;

	public int boxWidth = 80;
	public int boxHeight = 7;

	public ElixeKeyButton(String text, ModuleKey opt, int x, int y, int wid, int hei) {
		this.text = text;
		this.textWid = fontrenderer.getStringWidth(text);

		this.x = x;
		this.y = y;
		this.width = wid;
		this.height = hei;

		this.option = opt;

		mid = y + height / 2;

		setTextCurrentKey();
		// cache polygons
		cacheBackground();
	}

	private void setTextCurrentKey() {
		int keyCode = (int) option.getValue();
		if (keyCode >= 0) {
			keyName = Keyboard.getKeyName(keyCode).toLowerCase();
		} else {
			keyName = Mouse.getButtonName(keyCode + 100).toLowerCase();
		}
	
		keyNameTextWid = fontrenderer.getStringWidth(keyName) / 2;
	}

	private void cacheBackground() {
		keyBackground = GUIUtils.getRoundedRect(x + width - boxWidth, mid - boxHeight, x + width, mid + boxHeight, 7);
	}

	public boolean mouseClick(int mouseX, int mouseY, int mouseButton) {
		if (!containsKeyButton(mouseX, mouseY)) {
			return false;
		}

		if (!waiting) {
			waiting = true;
			keyName = "waiting...";
			keyNameTextWid = fontrenderer.getStringWidth(keyName) / 2;
		} else {
			if (mouseButton > 1) {
				option.setValue(mouseButton - 100);				
			}
			waiting = false;
			setTextCurrentKey();
			
		}
		return true;
	}

	public void updatePosition(int xDif, int yDif) {
		mid = y + height / 2;

		for (double[] kb : keyBackground) {
			kb[0] += xDif;
			kb[1] += yDif;
		}
	}

	public void drawText(int mouseX, int mouseY) {
		fontrenderer.drawStringWithShadow(text, x, mid - fontrenderer.FONT_HEIGHT / 2, 0.86f, 1f); // 220
		fontrenderer.drawString(keyName, x + width - boxWidth / 2 - keyNameTextWid, mid - fontrenderer.FONT_HEIGHT / 2,
				0.07f, 1f); // 20
	}

	boolean waiting = false;
	// use cached polygons
	double[][] keyBackground;

	public void drawButton(int mouseX, int mouseY) {
		float c = waiting ? 0.86f : 0.47f;
		GUIUtils.drawPolygon(keyBackground, c, 1f); // 120

		GL11.glColor4f(c, c, c, 1f);

		GL11.glLineWidth(2f);
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex2f(x + 2 + textWid, mid);
		GL11.glVertex2f(x + width - boxWidth - 3, mid);
		GL11.glEnd();
	}

	public void keyClick(int keyCode) {
		if (keyCode == Keyboard.KEY_DELETE) {
			keyCode = 0;
		}
		if (waiting) {
			option.setValue(keyCode);
			setTextCurrentKey();
			waiting = false;
		}
	}

	public void guiClosed() {
		waiting = false;
		setTextCurrentKey();
	}

	private boolean containsKeyButton(int mouseX, int mouseY) {
		return (mouseX > x + width - boxWidth && mouseX < x + width && mouseY > y + height / 2 - boxHeight
				&& mouseY < y + height / 2 + boxHeight);
	}

}

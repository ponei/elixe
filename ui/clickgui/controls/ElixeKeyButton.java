package elixe.ui.clickgui.controls;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import elixe.modules.option.ModuleKey;
import elixe.ui.clickgui.controls.base.ElixeButtonBase;
import elixe.utils.render.GUIUtils;

public class ElixeKeyButton extends ElixeButtonBase {

	private ModuleKey option;
	private String keyName;
	private int keyNameTextWidth;

	final int boxWidth = 80;
	final int boxHeight = 7;

	public ElixeKeyButton(String text, ModuleKey opt, int x, int y, int wid, int hei) {
		super(text, x, y, wid, hei);
		
		this.option = opt;

		setTextCurrentKey();
		// cache polygons
		cacheBackground();
	}

	//pega key do module
	private void setTextCurrentKey() {
		int keyCode = (int) option.getValue();
		if (keyCode >= 0) {
			keyName = Keyboard.getKeyName(keyCode).toLowerCase();
		} else {
			keyName = Mouse.getButtonName(keyCode + 100).toLowerCase();
		}

		updateKeyWidth();
		}
	
	private void updateKeyWidth() {
		keyNameTextWidth = fontrenderer.getStringWidth(keyName) / 2;
	}

	private void cacheBackground() {
		keyBackground = GUIUtils.getRoundedRect(x + width - boxWidth, controlMiddle - boxHeight, x + width, controlMiddle + boxHeight, 7);
	}

	
	public boolean mouseClick(int mouseX, int mouseY, int mouseButton) {
		if (!waiting) {
			if (!containsKeyButton(mouseX, mouseY)) {
				return false;
			}
		}

		if (!waiting) {
			waiting = true;
			keyName = "waiting...";
			updateKeyWidth();
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
		updateControlMiddle();

		for (double[] kb : keyBackground) {
			kb[0] += xDif;
			kb[1] += yDif;
		}
	}

	
	public void drawText(int mouseX, int mouseY) {
		fontrenderer.drawStringWithShadow(text, x, controlMiddle - fontrenderer.FONT_HEIGHT / 2, 0.86f, 1f); // 220
		fontrenderer.drawString(keyName, x + width - boxWidth / 2 - keyNameTextWidth, controlMiddle - fontrenderer.FONT_HEIGHT / 2,
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
		GL11.glVertex2f(x + 2 + textWidth, controlMiddle);
		GL11.glVertex2f(x + width - boxWidth - 3, controlMiddle);
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
		return (mouseX > x + width - boxWidth && mouseX < x + width && mouseY > controlMiddle - boxHeight
				&& mouseY < controlMiddle + boxHeight);
	}

}

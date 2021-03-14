package elixe.ui.clickgui.options;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import elixe.modules.option.ModuleColor;
import elixe.ui.base.ElixeButtonBase;
import elixe.ui.clickgui.ElixeMenu;
import elixe.utils.render.GUIUtils;

public class ElixeColorButton extends ElixeButtonBase {
	// menu ref
	private ElixeMenu menu;

	private String text;

	// option
	private ModuleColor colorOption;
	// cor selecionada
	private float[] colorOptionGL;
	// cor com saturação e brightness no maximo. apenas o hue importa
	private float[] colorOptionHUEGL = new float[3];

	private int[] colorOptionRGB;

	private int textWid;
	private int mid;

	static int previewBoxWidth = 30;
	static int previewBoxHeight = 7;

	static int alphaSize = 10;
	static int pickerSize = 90;
	static int hueSize = 10;

	static int pickerSpacing = 4;
	static int realPickerSize = pickerSize - pickerSpacing * 2, realHueSize = hueSize - pickerSpacing, realAlphaSize = alphaSize - pickerSpacing;

	int alphaXStart, alphaYStart;
	int alphaXEnd, alphaYEnd;
	
	int pickerXStart, pickerXEnd;
	
	int hueXStart, hueXEnd;

	public ElixeColorButton(ElixeMenu menu, String text, ModuleColor opt, int x, int y, int wid, int hei) {
		this.menu = menu;

		this.text = text;

		opt.setButton(this);
		
		this.colorOption = opt;
		colorOptionGL = colorOption.getGLRGB();
		colorOptionRGB = colorOption.getRGB();

		float[] colorOptionHSV;
		colorOptionHSV = Color.RGBtoHSB(colorOptionRGB[0], colorOptionRGB[1], colorOptionRGB[2], null);

		hue = colorOptionHSV[0];
		saturation = colorOptionHSV[1];
		value = colorOptionHSV[2];

		updateBaseColor();

		this.x = x;
		this.y = y;
		this.width = wid;
		this.height = hei;

		textWid = fontrenderer.getStringWidth(text);
		mid = y + height / 2;
		overlayY = y + height;

		alphaXStart = x + width - pickerSize - hueSize - alphaSize + pickerSpacing;
		alphaYStart = y + height + pickerSpacing;
		alphaXEnd = alphaXStart + realAlphaSize;
		alphaYEnd = alphaYStart + realPickerSize; //size é width entao agnt pega o size do picker
		
		pickerXStart = alphaXEnd + pickerSpacing;
		pickerXEnd = pickerXStart + realPickerSize;

		hueXStart = pickerXEnd + pickerSpacing;
		hueXEnd = pickerXEnd + pickerSpacing + realHueSize;

		updatePositionPickerPoints();
		updatePositionHuePoint();

		// cache polygons
		cacheColorPreview();
		cacheColorPicker();
	}

	private void cacheColorPreview() {
		colorPreview = GUIUtils.getRoundedRect(x + width - previewBoxWidth, y + height / 2 - previewBoxHeight,
				x + width, y + height / 2 + previewBoxHeight, 7);
	}

	private void cacheColorPicker() {
		colorPicker = GUIUtils.getRoundedRect(x + width - alphaSize - pickerSize - hueSize, y + height, x + width,
				y + height + pickerSize, 7);
	}

	// update all values
	public void updatePosition(int xDif, int yDif) {
		mid = y + height / 2;
		overlayY = y + height;


		alphaXStart = x + width - pickerSize - hueSize - alphaSize + pickerSpacing;
		alphaYStart = y + height + pickerSpacing;
		alphaXEnd = alphaXStart + realAlphaSize;
		alphaYEnd = alphaYStart + realPickerSize;
		
		pickerXStart = alphaXEnd + pickerSpacing;
		pickerXEnd = pickerXStart + realPickerSize;

		hueXStart = pickerXEnd + pickerSpacing;
		hueXEnd = pickerXEnd + pickerSpacing + realHueSize;

		for (double[] cpre : colorPreview) {
			cpre[0] += xDif;
			cpre[1] += yDif;
		}
		for (double[] cp : colorPicker) {
			cp[0] += xDif;
			cp[1] += yDif;
		}
	}

	public void drawText(int mouseX, int mouseY) {
		fontrenderer.drawStringWithShadow(text, x, y + height / 2f - fontrenderer.FONT_HEIGHT / 2, 0.86f, 1f); // 220
	}

	protected boolean pickerOpen = false;

	// use cached polygons
	double[][] colorPreview;

	public void drawButton(int mouseX, int mouseY) {
		float c = pickerOpen ? 0.86f : 0.47f; // 220 e 120
		GUIUtils.drawPolygon(colorPreview, colorOptionGL[0], colorOptionGL[1], colorOptionGL[2], 1f);

		// line
		GL11.glColor4f(c, c, c, 1f);

		GL11.glLineWidth(2f);
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex2f(x + 2 + textWid, mid);
		GL11.glVertex2f(x + width - previewBoxWidth - 3, mid);
		GL11.glEnd();
	}

	public boolean mouseClick(int mouseX, int mouseY, int mouseButton) {
		if (!checkMouseClick(mouseX, mouseY)) {
			return false;
		}

		pickerOpen = !pickerOpen;
		if (pickerOpen) {
			menu.setOverlay(this);
		} else {
			if (menu.isOverlay(this)) {
				menu.setOverlay(null);
			}
		}

		return true;
	}

	public void setOverlayOpen(boolean b) {
		pickerOpen = b;
	}

	public boolean contains(int mouseX, int mouseY) {
		return (mouseX >= this.x + width - previewBoxWidth && mouseX <= this.x + width
				&& mouseY >= this.y + height / 2 - previewBoxHeight
				&& mouseY <= this.y + height / 2 + previewBoxHeight);
	}

	//////////// if picker is open
	double[][] colorPicker;
	float alpha, hue, saturation, value;

	float alphaY;
	float saturationX, valueY;
	float hueY;

	boolean holding = false;
	int selectedControl = 0; // 0 = picker, 1 = hue

	private void updatePositionAlphaPoint() {
		int alphaDif = alphaYEnd - alphaYStart;
		alphaY = (1f - alpha) * alphaDif;
	}
	
	private void updatePositionPickerPoints() {
		int satDif = pickerXEnd - pickerXStart;
		saturationX = saturation * satDif;

		int valDif = alphaYEnd - alphaYStart;
		valueY = (1f - value) * valDif;
	}

	private void updatePositionHuePoint() {
		int hueDif = alphaYEnd - alphaYStart;
		hueY = (hue) * hueDif;
	}

	private void updateColorFromHSV() {
		Color hueTemp = Color.getHSBColor(hue, saturation, value);
		colorOptionGL[0] = hueTemp.getRed() > 0 ? hueTemp.getRed() / 255f : 0f;
		colorOptionGL[1] = hueTemp.getGreen() > 0 ? hueTemp.getGreen() / 255f : 0f;
		colorOptionGL[2] = hueTemp.getBlue() > 0 ? hueTemp.getBlue() / 255f : 0f;
		
		colorOption.setValueGLRGB(colorOptionGL[0], colorOptionGL[1], colorOptionGL[2], alpha);
	}

	private void updateBaseColor() {
		Color hueTemp = Color.getHSBColor(hue, 1, 1);
		colorOptionHUEGL[0] = hueTemp.getRed() > 0 ? hueTemp.getRed() / 255f : 0f;
		colorOptionHUEGL[1] = hueTemp.getGreen() > 0 ? hueTemp.getGreen() / 255f : 0f;
		colorOptionHUEGL[2] = hueTemp.getBlue() > 0 ? hueTemp.getBlue() / 255f : 0f;
	}

	private void updateValues(int mouseX, int mouseY) {
		if (holding) {
			switch (selectedControl) {
			case 0:
				int alphaDif = alphaYEnd - alphaYStart;
				float newAlpha = 1f - ((mouseY - alphaYStart) / (float) alphaDif);
				if (newAlpha > 1f) {
					newAlpha = 1f;
				} else if (0f > newAlpha) {
					newAlpha = 0f;
				}
				alpha = newAlpha;
				
				// update
				updatePositionAlphaPoint();
				updateColorFromHSV();
				break;
			case 1:
				// sat
				int satDif = pickerXEnd - pickerXStart;
				float newSaturation = (mouseX - pickerXStart) / (float) satDif;

				if (newSaturation > 1f) {
					newSaturation = 1f;
				} else if (0f > newSaturation) {
					newSaturation = 0f;
				}
				saturation = newSaturation;

				// val
				int valDif = alphaYEnd - alphaYStart;
				float newValue = 1f - ((mouseY - alphaYStart) / (float) valDif);

				if (newValue > 1f) {
					newValue = 1f;
				} else if (0f > newValue) {
					newValue = 0f;
				}
				value = newValue;

				// update
				updatePositionPickerPoints();
				updateColorFromHSV();
				break;

			case 2:
				int hueDif = alphaYEnd - alphaYStart;
				float newHue = (mouseY - alphaYStart) / (float) hueDif;
				if (newHue > 1f) {
					newHue = 1f;
				} else if (0f > newHue) {
					newHue = 0f;
				}
				hue = newHue;
				
				// update
				updatePositionHuePoint();
				updateColorFromHSV();
				updateBaseColor();
				break;
			}

		}
	}

	public void drawOverlay(int mouseX, int mouseY) {
		GUIUtils.drawPolygon(colorPicker, 0.24f, 1f);

		GL11.glShadeModel(GL11.GL_SMOOTH);

		GUIUtils.drawGradient90(alphaXStart, alphaYStart, alphaXEnd, alphaYEnd, new float[] { 1, 1, 1, 1 },
				new float[] { 1, 1, 1, 0 });
		GUIUtils.drawGradient(pickerXStart, alphaYStart, pickerXEnd, alphaYEnd, new float[] { 1, 1, 1, 1 },
				new float[] { colorOptionHUEGL[0], colorOptionHUEGL[1], colorOptionHUEGL[2], 1 });
		GUIUtils.drawGradient90(pickerXStart, alphaYStart, pickerXEnd, alphaYEnd, new float[] { 0, 0, 0, 0 },
				new float[] { 0, 0, 0, 1 });
		GUIUtils.drawRainbow(hueXStart, alphaYStart, hueXEnd, alphaYEnd);

		GL11.glShadeModel(GL11.GL_FLAT);

		updateValues(mouseX, mouseY);

		GL11.glColor4f(1, 1, 1, 1);
		GL11.glBlendFunc(GL11.GL_ONE_MINUS_DST_COLOR, GL11.GL_ONE_MINUS_SRC_COLOR);
		
		GL11.glPointSize(5f);
		GL11.glBegin(GL11.GL_POINTS);
		GL11.glVertex2f(pickerXStart + saturationX, alphaYStart + valueY);
		GL11.glEnd();
		GL11.glPointSize(1f);
		
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex2f(hueXStart, alphaYStart + hueY);
		GL11.glVertex2f(hueXEnd, alphaYStart + hueY);
		GL11.glVertex2f(alphaXStart, alphaYStart + alphaY);
		GL11.glVertex2f(alphaXEnd, alphaYStart + alphaY);
		GL11.glEnd();
	}

	public void drawOverlayText(int mouseX, int mouseY) {

	}

	public boolean overlayClick(int mouseX, int mouseY, int mouseButton) {
		if (!containsPicker(mouseX, mouseY)) {
			return false;
		}
		getSelectedControl(mouseX, mouseY);

		holding = true;
		return true;
	}

	public void overlayClickReleased(int mouseX, int mouseY, int state) {
		holding = false;
	}

	public void getSelectedControl(int mouseX, int mouseY) {
		if (mouseX >= alphaXStart && mouseX <= alphaXEnd) {
			selectedControl = 0;
		} else if (mouseX >= pickerXStart && mouseX <= pickerXEnd) {
			selectedControl = 1;
		} else if (mouseX >= hueXStart && mouseX <= hueXEnd) {
			selectedControl = 2;
		} else {
			selectedControl = -1;
		}
	}

	public boolean containsPicker(int mouseX, int mouseY) {
		return (mouseX >= alphaXStart - pickerSpacing && mouseX <= this.x + width && mouseY >= y + height
				&& mouseY <= y + height + pickerSize);
	}
}

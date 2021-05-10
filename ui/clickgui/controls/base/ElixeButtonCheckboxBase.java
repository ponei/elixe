package elixe.ui.clickgui.controls.base;

import org.lwjgl.opengl.GL11;

import elixe.utils.render.GUIUtils;

public class ElixeButtonCheckboxBase extends ElixeButtonBase {
	
	private boolean enabled = false;

	protected final int checkBoxWidth = 19;
	protected final int checkBoxHeight = 5;
	private final int circleSpacing = 5;

	public ElixeButtonCheckboxBase(String text, int x, int y, int wid, int hei) {
		super(text, x, y, wid, hei);
		
		// cache polygons
		cacheBackground();
	}

	// cache polygons
	public void setEnabled(boolean b) {
		enabled = b;
		cacheCircle();
	}

	private void cacheCircle() {
		checkCircle = GUIUtils.getCircle(enabled ? x + width - circleSpacing : x + width - checkBoxWidth + circleSpacing, controlMiddle, 3);
	}

	private void cacheBackground() {
		checkBackground = GUIUtils.getRoundedRect(x + width - checkBoxWidth, controlMiddle - checkBoxHeight, x + width,
				controlMiddle + checkBoxHeight, 5);
	}

	protected boolean isEnabled() {
		return enabled;
	}

	// update all values
	
	public void updatePosition(int xDif, int yDif) {
		updateControlMiddle();
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
		fontrenderer.drawStringWithShadow(text, x, controlMiddle - fontrenderer.FONT_HEIGHT / 2, 0.86f, 1f); // 220
	}

	// use cached polygons
	double[][] checkBackground, checkCircle;

	
	public void drawButton(int mouseX, int mouseY) {
		float c = enabled ? ENABLED_COLOR : DISABLED_COLOR;
		GUIUtils.drawPolygon(checkBackground, c, 1f);
		GUIUtils.drawPolygon(checkCircle, 0.078f, 1f);
		
		// line
		GL11.glColor4f(c, c, c, 1f);

		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glLineWidth(2f);
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex2f(x + 2 + textWidth, controlMiddle);
		GL11.glColor4f(c, c, c, 0f);
		GL11.glVertex2f(x + width - checkBoxWidth - 3, controlMiddle);
		GL11.glEnd();
	}

	
	public boolean checkMouseOver(int mouseX, int mouseY) {
		return (mouseX >= this.x + width - checkBoxWidth && mouseX <= this.x + width
				&& mouseY >= controlMiddle - checkBoxHeight && mouseY <= controlMiddle + checkBoxHeight);
	}
}

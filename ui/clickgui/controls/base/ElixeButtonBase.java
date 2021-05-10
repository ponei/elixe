package elixe.ui.clickgui.controls.base;

import elixe.Elixe;
import net.minecraft.client.gui.FontRenderer;

public abstract class ElixeButtonBase {
	//fontrenderer obj
	public FontRenderer fontrenderer = Elixe.INSTANCE.mc.fontRendererObj;
	protected final float DISABLED_COLOR = 0.38f;
	protected final float ENABLED_COLOR = 0.86f;
	
	protected String text;
	protected int textWidth;
	
	//upper left location
	public int x, y;
	//size
	public int width, height;
	public int controlMiddle;
	
	///overlay - optional
	//upper left location
	public int overlayY, overlayX;
	//size
	public int overlayWidth, overlayHeight;
	
	

	public ElixeButtonBase(String text, int x, int y, int width, int height) {
		super();
		this.text = text;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		controlMiddle = y + height / 2;
		textWidth = fontrenderer.getStringWidth(text);
	}

	//defined by control
	public abstract void drawButton(int mouseX, int mouseY);

	public abstract void drawText(int mouseX, int mouseY);

	public boolean mouseClick(int mouseX, int mouseY, int mouseButton) {
		if (!checkMouseOver(mouseX, mouseY)) {
			return false;
		}
		return true;
	}
	
	public void updateControlMiddle() {
		controlMiddle = y + height / 2;
	}
	
	public void keyClick(int keyCode) {
		
	}

	//contains control
	public boolean checkMouseOver(int mouseX, int mouseY) {
		return (mouseX > this.x && mouseX < this.x + width && mouseY > this.y && mouseY < this.y + height);
	}
	
	//contais overlay
	public boolean checkOverlayMouseOver(int mouseX, int mouseY) {
		return (mouseX > this.overlayX && mouseX < this.overlayX + overlayWidth && mouseY > this.overlayY && mouseY < this.overlayY + overlayHeight);
	}


	public void mouseReleased(int mouseX, int mouseY, int state) {

	}

	public void mouseClickMove(int mouseX, int mouseY) {

	}

	protected void updatePosition(int xDif, int yDif) {

	}

	public void setPositionDifference(int xDif, int yDif) {
		y += yDif;
		x += xDif;
		updatePosition(xDif, yDif);
	}

	public void setOverlayOpen(boolean b) {

	}
	
	public void drawOverlay(int mouseX, int mouseY) {

	}

	public void drawOverlayText(int mouseX, int mouseY) {

	}
	
	public void overlayClickReleased(int mouseX, int mouseY, int state) {

	}
	
	public void setValue(Object value) {
		
	}
	
	public boolean overlayClick(int mouseX, int mouseY,  int mouseButton) {
		return false;
	}
	
	public void guiClosed() {
		
	}
}

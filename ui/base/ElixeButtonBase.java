package elixe.ui.base;

import elixe.Elixe;
import net.minecraft.client.gui.FontRenderer;

public class ElixeButtonBase {
	public FontRenderer fontrenderer = Elixe.INSTANCE.mc.fontRendererObj;

	public int x, y;
	public int baseY;
	
	public int overlayY;

	public int width, height;

	public void drawButton(int mouseX, int mouseY) {

	}

	public void drawText(int mouseX, int mouseY) {

	}

	public boolean mouseClick(int mouseX, int mouseY, int mouseButton) {
		if (!checkMouseClick(mouseX, mouseY)) {
			return false;
		}
		return true;
	}
	
	public void keyClick(int keyCode) {
		
	}

	public boolean checkMouseClick(int mouseX, int mouseY) {
		if (contains(mouseX, mouseY)) {
			return true;
		}
		return false;
	}

	public boolean contains(int mouseX, int mouseY) {
		return (mouseX > this.x && mouseX < this.x + width && mouseY > this.y && mouseY < this.y + height);
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
	
	public boolean overlayClick(int mouseX, int mouseY,  int mouseButton) {
		return false;
	}
	
	public void guiClosed() {
		
	}
}

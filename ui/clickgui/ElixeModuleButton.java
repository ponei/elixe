package elixe.ui.clickgui;

import org.lwjgl.opengl.GL11;

import elixe.modules.Module;
import elixe.ui.base.ElixeButtonCheckboxBase;

public class ElixeModuleButton extends ElixeButtonCheckboxBase {

	private Module mod;

	public ElixeModuleButton(String text, Module mod, int x, int y, int wid, int hei) {
		super(text, x, y, wid, hei);

		this.mod = mod;
		setEnabled(mod.isToggled());
	}

	private static int arrowWidth = 10;

	public Module getModule() {
		return mod;
	}

	public boolean mouseClick(int mouseX, int mouseY,  int mouseButton) {
		if (!checkMouseClick(mouseX, mouseY)) {
			return false;
		}
		mod.toggle();
		setEnabled(mod.isToggled());
		return true;
	}

	public void drawButton(int mouseX, int mouseY) {
		super.drawButton(mouseX, mouseY);
		// arrow
		if (containsArrow(mouseX, mouseY)) {
			GL11.glColor4f(0.86f, 0.86f, 0.86f, 1f); //220
		} else {
			GL11.glColor4f(0.47f, 0.47f, 0.47f, 1f); //120
		}		
		GL11.glLineWidth(2f);
		GL11.glBegin(GL11.GL_LINE_STRIP);
		GL11.glVertex2f(x + width + 4, y + height / 2 - 3);
		GL11.glVertex2f(x + width + arrowWidth - 3, y + height / 2);
		GL11.glVertex2f(x + width + 4, y + height / 2 + 3);
		GL11.glEnd();
	}

	public boolean containsArrow(int mouseX, int mouseY) {
		return (mouseX > this.x + width && mouseX < this.x + width + arrowWidth
				&& mouseY > this.y + height / 2 - checkBoxHeight && mouseY < this.y + height / 2 + checkBoxHeight);
	}

}

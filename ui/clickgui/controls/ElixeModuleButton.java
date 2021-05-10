package elixe.ui.clickgui.controls;

import org.lwjgl.opengl.GL11;

import elixe.modules.Module;
import elixe.ui.clickgui.controls.base.ElixeButtonCheckboxBase;

public class ElixeModuleButton extends ElixeButtonCheckboxBase {

	private Module mod;
	private final int arrowWidth = 10;
	
	public ElixeModuleButton(String text, Module mod, int x, int y, int wid, int hei) {
		super(text, x, y, wid, hei);

		this.mod = mod;
		setEnabled(mod.isToggled());
	}

	public Module getModule() {
		return mod;
	}

	
	public boolean mouseClick(int mouseX, int mouseY, int mouseButton) {
		if (!checkMouseOver(mouseX, mouseY)) {
			return false;
		}
		mod.toggle();
		setEnabled(mod.isToggled());
		return true;
	}

	
	public void drawButton(int mouseX, int mouseY) {
		super.drawButton(mouseX, mouseY);

		float c = containsArrow(mouseX, mouseY) ? ENABLED_COLOR : DISABLED_COLOR;
		// arrow
		GL11.glColor4f(c, c, c, 1f);

		GL11.glLineWidth(2f);
		GL11.glBegin(GL11.GL_LINE_STRIP);
		GL11.glVertex2f(x + width + 4, controlMiddle - 3);
		GL11.glVertex2f(x + width + arrowWidth - 3, controlMiddle);
		GL11.glVertex2f(x + width + 4, controlMiddle + 3);
		GL11.glEnd();
	}

	public boolean containsArrow(int mouseX, int mouseY) {
		return (mouseX > this.x + width && mouseX < this.x + width + arrowWidth && mouseY > controlMiddle - checkBoxHeight
				&& mouseY < controlMiddle + checkBoxHeight);
	}

}

package elixe.ui.clickgui.controls;

import elixe.modules.ModuleCategory;
import elixe.ui.clickgui.controls.base.ElixeButtonBase;
import elixe.utils.render.GUIUtils;

public class ElixeCategoryButton extends ElixeButtonBase {

	private ModuleCategory cat;

	public ModuleCategory getCategory() {
		return cat;
	}

	public ElixeCategoryButton(String text, ModuleCategory cat, int x, int y, int wid, int hei) {
		super(text, x, y, wid, hei);
		this.cat = cat;
	}

	
	public void drawButton(int mouseX, int mouseY) {
		float c = checkMouseOver(mouseX, mouseY) ? 0.137f : 0.098f; // 35 e 25

		GUIUtils.drawRect(x, y, x + width, y + height, c, 1f);
	}

	
	public void drawText(int mouseX, int mouseY) {
		fontrenderer.drawStringWithShadow(text, x + (width / 2) - textWidth,
				controlMiddle - fontrenderer.FONT_HEIGHT / 2, 0.62f, 1f); // 160
	}
}

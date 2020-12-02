package elixe.ui.clickgui;

import java.awt.Color;

import elixe.Elixe;
import elixe.modules.ModuleCategory;
import elixe.ui.base.ElixeButtonBase;
import elixe.utils.render.GUIUtils;
import net.minecraft.client.gui.FontRenderer;

public class ElixeCategoryButton extends ElixeButtonBase {

	private String text;
	private ModuleCategory cat;
	private int textWid;

	public ModuleCategory getCategory() {
		return cat;
	}

	public ElixeCategoryButton(String text, ModuleCategory cat, int x, int y, int wid, int hei) {
		this.text = text;
		textWid = fontrenderer.getStringWidth(text) / 2;

		this.x = x;
		this.y = y;
		this.width = wid;
		this.height = hei;
		this.cat = cat;
	}

	public void drawButton(int mouseX, int mouseY) {
		float c = contains(mouseX, mouseY) ? 0.137f : 0.098f; // 35 e 25

		GUIUtils.drawRect(x, y, x + width, y + height, c, 1f);
	}

	public void drawText(int mouseX, int mouseY) {
		fontrenderer.drawStringWithShadow(text, x + (width / 2) - textWid,
				y + height / 2f - fontrenderer.FONT_HEIGHT / 2, 0.62f, 1f); // 160
	}
}

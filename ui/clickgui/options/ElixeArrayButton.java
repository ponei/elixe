package elixe.ui.clickgui.options;

import org.lwjgl.opengl.GL11;

import elixe.modules.option.ModuleArray;
import elixe.ui.base.ElixeButtonBase;
import elixe.ui.clickgui.ElixeMenu;
import elixe.utils.render.GUIUtils;

public class ElixeArrayButton extends ElixeButtonBase {
	// menu ref
	private ElixeMenu menu;

	// name and line
	protected String text;
	protected int textWid;
	protected int def, mid, comboMid;

	protected int realHei;
	
	protected String selectedText;
	protected int selectedIndex;
	protected int selectedTextWid;

	protected String[] listOptions;
	protected int[] listOptionsWid;

	// option
	ModuleArray arrayOpt;

	// math
	protected int maxY;

	// etc
	protected int comboHeight = 14;

	public ElixeArrayButton(ElixeMenu menu, String text, ModuleArray opt, int x, int y, int wid, int hei, int def) {
		this.menu = menu;

		opt.setButton(this);
		
		// button base
		this.x = x;
		this.y = y;
		this.width = wid;
		this.height = hei;
		this.def = def;

		// option
		this.arrayOpt = opt;
		selectedIndex = (int) opt.getValue();
		selectedText = opt.getSelected();
		selectedTextWid = fontrenderer.getStringWidth(selectedText);

		listOptions = opt.getArray();
		listOptionsWid = new int[listOptions.length];
		for (int i = 0; i < listOptions.length; i++) {
			listOptionsWid[i] = fontrenderer.getStringWidth(listOptions[i]);
		}

		itemBackground = new double[listOptions.length][][];

		// text
		this.text = text;
		textWid = fontrenderer.getStringWidth(text);
		mid = y + def / 2;
		comboMid = y + def + 6;
		overlayY = y + def + comboHeight;
		maxY = overlayY + comboHeight * listOptions.length;
		
		realHei = maxY - overlayY;

		// math

		cacheComboBackground();
		cacheItemsBackgrounds();
	}

	protected void cacheComboBackground() {
		comboBackground = GUIUtils.getRoundedRect(x, y + def - 2, x + width, y + def - 2 + comboHeight, 7);
	}

	protected void cacheItemsBackgrounds() {
		for (int i = 0; i < listOptions.length; i++) {
			int type = 1;
			if (i == 0) {
				type = 0;
			} else if (listOptions.length == i + 1) {
				type = 2;
			}

			itemBackground[i] = GUIUtils.getItemRect(type, x, overlayY + comboHeight * i, x + width,
					overlayY + comboHeight * (i + 1), 7);
		}
	}

	public void updatePosition(int xDif, int yDif) {
		mid = y + def / 2;
		comboMid = y + def + 6;
		overlayY = y + def + comboHeight;
		maxY = overlayY + comboHeight * listOptions.length;
		
		realHei = maxY - overlayY;

		for (double[] cb : comboBackground) {
			cb[0] += xDif;
			cb[1] += yDif;
		}

		for (double[][] ib : itemBackground) {
			for (double[] ibb : ib) {
				ibb[0] += xDif;
				ibb[1] += yDif;
			}
		}
	}

	public boolean mouseClick(int mouseX, int mouseY,  int mouseButton) {
		if (!containsCombo(mouseX, mouseY)) {
			return false;
		}

		comboOpen = !comboOpen;
		if (comboOpen) {
			menu.setOverlay(this);
		} else {
			if (menu.isOverlay(this)) {
				menu.setOverlay(null);
			}
		}

		return true;
	}

	public void setOverlayOpen(boolean b) {
		comboOpen = b;
	}

	protected boolean comboOpen = false;

	// use cached polygons
	protected double[][][] itemBackground;
	protected double[][] comboBackground;

	public void drawButton(int mouseX, int mouseY) {
		// line
		if (comboOpen) {
			GL11.glColor4f(0.86f, 0.86f, 0.86f, 1f); // 220
		} else {
			GL11.glColor4f(0.47f, 0.47f, 0.47f, 1f); // 120
		}

		GL11.glLineWidth(2f);
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex2f(x + 2 + textWid, mid);
		GL11.glVertex2f(x + width, mid);
		GL11.glEnd();

		// combo background
		GUIUtils.drawPolygon(comboBackground, comboOpen ? 0.86f : 0.47f, 1f); // 220, 120
	}

	public void drawText(int mouseX, int mouseY) {
		fontrenderer.drawStringWithShadow(text, x, mid - fontrenderer.FONT_HEIGHT / 2, 0.86f, 1f); // 220
		fontrenderer.drawString(selectedText, x + width / 2 - selectedTextWid / 2,
				comboMid - fontrenderer.FONT_HEIGHT / 2, 0.07f, 1f); // 20
	}
	
	private boolean containsCombo(int mouseX, int mouseY) {
		return (mouseX >= x && mouseX <= x + width && mouseY >= y + def - 2 && mouseY <= y + def - 2 + comboHeight);
	}

	/////////////if combo is open
	public void drawOverlay(int mouseX, int mouseY) {		
		for (int i = 0; i < itemBackground.length; i++) {
			int overIndex = getOverIndex(mouseX, mouseY);
			GUIUtils.drawPolygon(itemBackground[i], i == overIndex ? 0.86f : 0.47f, 1f); // 120
			
		}	
	}

	public void drawOverlayText(int mouseX, int mouseY) {
		for (int i = 0; i < listOptions.length; i++) {
			fontrenderer.drawString(listOptions[i], x + width / 2 - listOptionsWid[i] / 2,
					overlayY + comboHeight * i + fontrenderer.FONT_HEIGHT / 2, 0.07f, 1f); // 20
		}
	}

	private int getOverIndex(int mouseX, int mouseY) {
		int index = -1;
		if (mouseY > overlayY && maxY > mouseY) {
			if (mouseX > x && x + width > mouseX) {
				int mouseDif = mouseY - overlayY;
				index = (listOptions.length * mouseDif) / realHei;
			}
		}
		
		return index;
	}
	
	public boolean overlayClick(int mouseX, int mouseY, int mouseButton) {
		int overIndex = getOverIndex(mouseX, mouseY);
		if (overIndex != -1) {
			selectedText = listOptions[overIndex];
			selectedTextWid = fontrenderer.getStringWidth(selectedText);
			arrayOpt.setValue(overIndex);
			return true;
		}		
		return false;
	}
}
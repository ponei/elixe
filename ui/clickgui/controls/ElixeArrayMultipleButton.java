package elixe.ui.clickgui.controls;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import elixe.modules.option.ModuleArrayMultiple;
import elixe.ui.clickgui.ElixeMenu;
import elixe.ui.clickgui.controls.base.ElixeButtonBase;
import elixe.utils.render.GUIUtils;

public class ElixeArrayMultipleButton extends ElixeButtonBase {
	// menu ref
	private ElixeMenu menu;

	// name and line
	protected int def, comboMid;

	protected int realHei;

	protected boolean[] selectedIndexes;

	protected String selectedText;
	protected int selectedTextWid;

	protected String[] listOptions;
	protected int[] listOptionsWid;

	// option
	ModuleArrayMultiple arrayMultipleOpt;

	// math
	protected int maxY;

	// etc
	protected int comboHeight = 14;

	public ElixeArrayMultipleButton(ElixeMenu menu, String text, ModuleArrayMultiple opt, int x, int y, int wid,
			int hei, int def) {
		super(text, x, y, wid, hei);
		
		this.menu = menu;

		opt.setButton(this);
		
		// button base
		this.def = def;

		// option
		this.arrayMultipleOpt = opt;
		selectedIndexes = (boolean[]) opt.getValue();

		listOptions = opt.getArray();
		listOptionsWid = new int[listOptions.length];
		for (int i = 0; i < listOptions.length; i++) {
			listOptionsWid[i] = fontrenderer.getStringWidth(listOptions[i]);
		}
		createSelectedText();

		itemBackground = new double[listOptions.length][][];

		// text
		comboMid = y + def + 6;
		overlayY = y + def + comboHeight;
		maxY = overlayY + comboHeight * listOptions.length;

		realHei = maxY - overlayY;

		// math

		cacheComboBackground();
		cacheItemsBackgrounds();
	}

	protected void createSelectedText() {
		ArrayList<String> selectedTexts = new ArrayList<String>();
		for (int i = 0; i < listOptions.length; i++) {
			if (selectedIndexes[i]) {
				selectedTexts.add(listOptions[i]);
			}
		}
		
		String textsJoin = String.join(", ", selectedTexts);
		String newSelectedText = fontrenderer.trimStringToWidth(textsJoin, width - 12);
		
		int textsJoinWid = fontrenderer.getStringWidth(textsJoin);
		int newTextWid = fontrenderer.getStringWidth(newSelectedText);
		
		if (textsJoinWid > newTextWid) {
			selectedText = newSelectedText + "...";
			selectedTextWid = fontrenderer.getStringWidth(selectedText);;
		} else {
			selectedText = textsJoin;
			selectedTextWid = textsJoinWid;
		}
		
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
updateControlMiddle();
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
		GL11.glVertex2f(x + 2 + textWidth, controlMiddle);
		GL11.glVertex2f(x + width, controlMiddle);
		GL11.glEnd();

		// combo background
		GUIUtils.drawPolygon(comboBackground, comboOpen ? 0.86f : 0.47f, 1f); // 220, 120
	}

	
	public void drawText(int mouseX, int mouseY) {
		fontrenderer.drawStringWithShadow(text, x, controlMiddle - fontrenderer.FONT_HEIGHT / 2, 0.86f, 1f); // 220
		fontrenderer.drawString(selectedText, x + width / 2 - selectedTextWid / 2,
				comboMid - fontrenderer.FONT_HEIGHT / 2, 0.07f, 1f); // 20
	}

	private boolean containsCombo(int mouseX, int mouseY) {
		return (mouseX >= x && mouseX <= x + width && mouseY >= y + def - 2 && mouseY <= y + def - 2 + comboHeight);
	}

	///////////// if combo is open
	
	public void drawOverlay(int mouseX, int mouseY) {
		for (int i = 0; i < itemBackground.length; i++) {
			float itemColor = selectedIndexes[i] ? 0.67f : 0.47f;
			int overIndex = getOverIndex(mouseX, mouseY);
			GUIUtils.drawPolygon(itemBackground[i], i == overIndex ? 0.86f : itemColor, 1f); // 120

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
			boolean newState = !selectedIndexes[overIndex];
			selectedIndexes[overIndex] = newState;
			createSelectedText();
			arrayMultipleOpt.changeIndex(overIndex, newState);
			return true;
		}
		return false;
	}
}
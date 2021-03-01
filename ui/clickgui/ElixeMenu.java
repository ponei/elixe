package elixe.ui.clickgui;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import elixe.Elixe;
import elixe.modules.Module;
import elixe.modules.ModuleCategory;
import elixe.modules.AModuleOption;
import elixe.modules.option.ModuleArray;
import elixe.modules.option.ModuleArrayMultiple;
import elixe.modules.option.ModuleBoolean;
import elixe.modules.option.ModuleColor;
import elixe.modules.option.ModuleFloat;
import elixe.modules.option.ModuleInteger;
import elixe.modules.option.ModuleKey;
import elixe.modules.render.ClickGUI;
import elixe.ui.base.ElixeButtonBase;
import elixe.ui.clickgui.options.ElixeArrayButton;
import elixe.ui.clickgui.options.ElixeArrayMultipleButton;
import elixe.ui.clickgui.options.ElixeBooleanButton;
import elixe.ui.clickgui.options.ElixeColorButton;
import elixe.ui.clickgui.options.ElixeFloatButton;
import elixe.ui.clickgui.options.ElixeIntegerButton;
import elixe.ui.clickgui.options.ElixeKeyButton;
import elixe.utils.misc.ChatUtils;
import elixe.utils.misc.LoggingUtils;
import elixe.utils.render.GUIUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

public class ElixeMenu extends GuiScreen {
	int GUI_X = 0, GUI_Y = 0;

	int GUI_WIDTH = 300, GUI_HEIGHT = 200;

	int GUI_CATEGORY_WIDTH;
	int GUI_CATEGORY_HEIGHT = 20;

	int GUI_MODULE_HEIGHT = 20;

	public Module CURRENT_MODULE;

	boolean FIRST = true;

	int SCALE_FACTOR;

	private ModuleCategory SELECTED_CATEGORY = ModuleCategory.PLAYER;

	private ElixeCategoryButton[] catButtons = new ElixeCategoryButton[ModuleCategory.values().length];
	private ArrayList<ElixeModuleButton> modButtons = new ArrayList<ElixeModuleButton>();
	private ArrayList<ElixeButtonBase> modOptions = new ArrayList<ElixeButtonBase>();

	public ElixeMenu(ClickGUI ck) {
		// var setup
		CLICKGUI = ck;
		GUI_CATEGORY_WIDTH = GUI_WIDTH / ModuleCategory.values().length;
	}

	public void initGui() {
		if (FIRST) {
			setupMenu();
			FIRST = false;
		} else {

			if (GUI_X + GUI_WIDTH > this.width) {
				updateButtonsPosition(-((GUI_X + GUI_WIDTH) - this.width), 0);
				GUI_X = this.width - GUI_WIDTH;
			}

			if (GUI_Y + GUI_HEIGHT > this.height) {
				updateButtonsPosition(0, -((GUI_Y + GUI_HEIGHT) - this.height));
				GUI_Y = this.height - GUI_HEIGHT;
			}

			for (ElixeModuleButton bt : modButtons) {
				bt.setEnabled(bt.getModule().isToggled());

			}
		}
		ScaledResolution sr = new ScaledResolution(Elixe.INSTANCE.mc);
		SCALE_FACTOR = sr.getScaleFactor();
	}

	private void setupMenu() {
		GUI_X = this.width / 2 - GUI_WIDTH / 2;
		GUI_Y = 10;

		for (int i = 0; ModuleCategory.values().length > i; i++) {
			catButtons[i] = new ElixeCategoryButton(ModuleCategory.values()[i].toString().toLowerCase(), ModuleCategory.values()[i],
					GUI_X + GUI_CATEGORY_WIDTH * i, GUI_Y, GUI_CATEGORY_WIDTH, GUI_CATEGORY_HEIGHT);
		}

		changeCategory(SELECTED_CATEGORY);
	}

	private boolean isInOptionsArea(int btY) {
		return btY >= GUI_Y - 40 && GUI_Y + GUI_HEIGHT >= btY;
	}

	private boolean isInOptionsAreaStrict(int btY) {
		return btY >= GUI_Y + GUI_CATEGORY_HEIGHT && GUI_Y + GUI_HEIGHT >= btY;
	}

	private boolean isInGUIArea(int mouseX, int mouseY) {
		return (mouseX >= GUI_X && mouseX <= GUI_X + GUI_WIDTH && mouseY >= GUI_Y && mouseY <= GUI_Y + GUI_HEIGHT);
	}

	private boolean isInOptionsArea(int mouseX, int mouseY) {
		return (mouseX >= GUI_X + 140 && mouseX <= GUI_X + GUI_WIDTH && mouseY >= GUI_Y + GUI_CATEGORY_HEIGHT && mouseY <= GUI_Y + GUI_HEIGHT);
	}

	private ElixeButtonBase modOptionOverlay;

	public void setOverlay(ElixeButtonBase modOpt) {
		if (modOptionOverlay != null) {
			modOptionOverlay.setOverlayOpen(false);
		}

		modOptionOverlay = modOpt;
	}

	public boolean isOverlay(ElixeButtonBase modOpt) {
		return modOptionOverlay == modOpt;
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {

		dragIfPossible(mouseX, mouseY);

		GL11.glPushMatrix();
		GUIUtils.pre2D();

		int mWheel = Mouse.getDWheel() / 8;
		if (mWheel != 0 && isInOptionsArea(mouseX, mouseY)) {
			updateOptionsPosition(mWheel);
		}

		GUIUtils.drawRect(GUI_X, GUI_Y, GUI_X + GUI_WIDTH, GUI_Y + GUI_HEIGHT, 0.078f, 1f); // 20
		drawButtonsBase(mouseX, mouseY);

		GUIUtils.post2D();

		GlStateManager.disableBlend();
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

		drawButtonsText(mouseX, mouseY);

		if (modOptionOverlay != null) {
			if (isInOptionsAreaStrict(modOptionOverlay.overlayY)) {
				GlStateManager.enableBlend();
				GUIUtils.pre2D();

				modOptionOverlay.drawOverlay(mouseX, mouseY);

				GUIUtils.post2D();

				GlStateManager.disableBlend();
				GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

				modOptionOverlay.drawOverlayText(mouseX, mouseY);
			}
		}

		GL11.glPopMatrix();

	}

	private void scissorButtons() {
		glScissor(GUI_X + 140, GUI_Y + GUI_CATEGORY_HEIGHT, GUI_X + GUI_WIDTH, GUI_Y + GUI_HEIGHT);
	}

	private void glScissor(int x1, int y1, int x2, int y2) {
		int width = Math.abs(x1 - x2) * SCALE_FACTOR;
		int height = Math.abs(y1 - y2) * SCALE_FACTOR;
		int xStart = Math.min(x1, x2) * SCALE_FACTOR;
		int yStart = (this.height - Math.max(y1, y2)) * SCALE_FACTOR;
		GL11.glScissor(xStart, yStart, width, height);
	}

	boolean dragging = false;

	int clickX, clickY;

	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {

		if (modOptionOverlay != null) {
			if (isInOptionsAreaStrict(modOptionOverlay.overlayY)) {
				if (modOptionOverlay.overlayClick(mouseX, mouseY, mouseButton)) {
					return;
				}
			}
		}

		// category change
		for (int i = 0; catButtons.length > i; i++) {
			if (catButtons[i].checkMouseClick(mouseX, mouseY)) {
				changeCategory(catButtons[i].getCategory());
				return;
			}
		}
		// module toggle/options
		for (ElixeModuleButton bt : modButtons) {
			if (bt.mouseClick(mouseX, mouseY, mouseButton)) {
				return;
			}

			if (bt.containsArrow(mouseX, mouseY)) {
				addOptions(bt.getModule());
				return;
			}
		}

		// base
		for (ElixeButtonBase opt : modOptions) {
			if (opt.mouseClick(mouseX, mouseY, mouseButton)) {
				return;
			}
		}

		if (isInGUIArea(mouseX, mouseY)) {
			if (!dragging) {
				dragging = true;
				clickX = mouseX;
				clickY = mouseY;
			}
		}

	}

	protected void mouseReleased(int mouseX, int mouseY, int state) {
		if (dragging) {
			dragging = false;
			return;
		}
		if (modOptionOverlay != null) {

			modOptionOverlay.overlayClickReleased(mouseX, mouseY, state);

		}

		for (ElixeButtonBase opt : modOptions) {
			opt.mouseReleased(mouseX, mouseY, state);
		}
	}

	private void dragIfPossible(int mouseX, int mouseY) {
		if (dragging) {
			int xDif = mouseX - clickX, yDif = mouseY - clickY;

			int newX = GUI_X;
			newX += xDif;
			int newY = GUI_Y;
			newY += yDif;

			if (newX >= 0 && newY >= 0 && this.width >= newX + GUI_WIDTH && this.height >= newY + GUI_HEIGHT) {
				GUI_X = newX;
				GUI_Y = newY;
				updateButtonsPosition(xDif, yDif);
			}

			clickX = mouseX;
			clickY = mouseY;

		}

		for (ElixeButtonBase opt : modOptions) {
			opt.mouseClickMove(mouseX, mouseY);
		}
	}

	public boolean doesGuiPauseGame() {
		return false;
	}

	ClickGUI CLICKGUI;

	public void onGuiClosed() {
		if (CLICKGUI.isToggled()) {
			try {
				Elixe.INSTANCE.FILE_MANAGER.MODULE_CONFIG.saveConfig();
				Elixe.INSTANCE.FILE_MANAGER.MODULE_PERSONAL.saveConfig();
				LoggingUtils.out("saving module configurations...");
			} catch (IOException e) {
				LoggingUtils.out("error trying to save module configs");
			}
			CLICKGUI.toggle();
		}
	}

	public void addOptions(Module mod) {
		modOptions.clear();
		modOptionOverlay = null;

		CURRENT_MODULE = mod;
		optionsSpacing = GUI_MODULE_HEIGHT;
		for (AModuleOption opt : mod.getOptions()) {
			if (!opt.shouldShow()) {
				continue;
			}
			if (opt instanceof ModuleKey) {
				ElixeKeyButton kbt = new ElixeKeyButton(opt.getName(), (ModuleKey) opt, GUI_X + 140, GUI_Y + 5 + optionsSpacing, GUI_WIDTH - 150,
						GUI_MODULE_HEIGHT);
				optionsSpacing += GUI_MODULE_HEIGHT;
				modOptions.add(kbt);
			}
			if (opt instanceof ModuleBoolean) {
				ElixeBooleanButton bbt = new ElixeBooleanButton(opt.getName(), (ModuleBoolean) opt, GUI_X + 140, GUI_Y + 5 + optionsSpacing, GUI_WIDTH - 150,
						GUI_MODULE_HEIGHT);
				optionsSpacing += GUI_MODULE_HEIGHT;
				modOptions.add(bbt);
			}
			if (opt instanceof ModuleFloat) {
				ElixeFloatButton fbt = new ElixeFloatButton(opt.getName(), (ModuleFloat) opt, GUI_X + 140, GUI_Y + 5 + optionsSpacing, GUI_WIDTH - 150,
						GUI_MODULE_HEIGHT + 14, GUI_MODULE_HEIGHT);
				optionsSpacing += GUI_MODULE_HEIGHT + 16;
				modOptions.add(fbt);
			}
			if (opt instanceof ModuleInteger) {
				ElixeIntegerButton ibt = new ElixeIntegerButton(opt.getName(), (ModuleInteger) opt, GUI_X + 140, GUI_Y + 5 + optionsSpacing, GUI_WIDTH - 150,
						GUI_MODULE_HEIGHT + 14, GUI_MODULE_HEIGHT);
				optionsSpacing += GUI_MODULE_HEIGHT + 16;
				modOptions.add(ibt);
			}
			if (opt instanceof ModuleArray) {
				ElixeArrayButton abt = new ElixeArrayButton(this, opt.getName(), (ModuleArray) opt, GUI_X + 140, GUI_Y + 5 + optionsSpacing, GUI_WIDTH - 150,
						GUI_MODULE_HEIGHT + 14, GUI_MODULE_HEIGHT);
				optionsSpacing += GUI_MODULE_HEIGHT + 16;
				modOptions.add(abt);
			}
			if (opt instanceof ModuleArrayMultiple) {
				ElixeArrayMultipleButton ambt = new ElixeArrayMultipleButton(this, opt.getName(), (ModuleArrayMultiple) opt, GUI_X + 140,
						GUI_Y + 5 + optionsSpacing, GUI_WIDTH - 150, GUI_MODULE_HEIGHT + 14, GUI_MODULE_HEIGHT);
				optionsSpacing += GUI_MODULE_HEIGHT + 16;
				modOptions.add(ambt);
			}
			if (opt instanceof ModuleColor) {
				ElixeColorButton cbt = new ElixeColorButton(this, opt.getName(), (ModuleColor) opt, GUI_X + 140, GUI_Y + 5 + optionsSpacing, GUI_WIDTH - 150,
						GUI_MODULE_HEIGHT);
				optionsSpacing += GUI_MODULE_HEIGHT;
				modOptions.add(cbt);
			}
		}

		refreshScrollLogic();

	}

	int optionsSpacing;

	int optionsScroll, optionsScrollMax;

	private void refreshScrollLogic() {
		optionsScroll = 0;
		optionsScrollMax = (GUI_HEIGHT - GUI_CATEGORY_HEIGHT) - optionsSpacing;
		if (optionsScrollMax > 0) {
			optionsScrollMax = 0;
		}
	}

	private void updateOptionsPosition(int yDif) {
		int newScroll = optionsScroll + yDif;

		if (newScroll > 0) {
			yDif = -optionsScroll;
			optionsScroll = 0;
		} else {
			if (optionsScrollMax > newScroll) {
				yDif = optionsScrollMax - optionsScroll;
				optionsScroll = optionsScrollMax;
			} else {
				optionsScroll += yDif;
			}
		}

		for (ElixeButtonBase bti : modOptions) {
			bti.setPositionDifference(0, yDif);
		}
	}

	private void changeCategory(ModuleCategory cat) {
		SELECTED_CATEGORY = cat;

		modButtons.clear();
		modOptions.clear();
		modOptionOverlay = null;

		int i = 1;
		for (Module m : Elixe.INSTANCE.MODULE_MANAGER.getModulesByCategory(SELECTED_CATEGORY)) {
			ElixeModuleButton bt = new ElixeModuleButton(m.getName().toLowerCase(), m, GUI_X + 10, GUI_Y + GUI_CATEGORY_HEIGHT * i, 110, 30);
			modButtons.add(bt);
			i++;
		}
	}

	private void updateButtonsPosition(int xDif, int yDif) {
		for (int i = 0; catButtons.length > i; i++) {
			catButtons[i].setPositionDifference(xDif, yDif);
		}

		for (ElixeModuleButton bt : modButtons) {
			bt.setPositionDifference(xDif, yDif);
		}

		for (ElixeButtonBase bti : modOptions) {
			bti.setPositionDifference(xDif, yDif);
		}
	}

	private void drawButtonsBase(int mouseX, int mouseY) {
		for (int i = 0; catButtons.length > i; i++) {
			catButtons[i].drawButton(mouseX, mouseY);
		}

		for (ElixeModuleButton bt : modButtons) {
			bt.drawButton(mouseX, mouseY);
		}

		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		scissorButtons();
		for (ElixeButtonBase bti : modOptions) {
			if (isInOptionsArea(bti.y))
				bti.drawButton(mouseX, mouseY);
		}
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
	}

	private void drawButtonsText(int mouseX, int mouseY) {
		for (int i = 0; catButtons.length > i; i++) {
			catButtons[i].drawText(mouseX, mouseY);
		}

		for (ElixeModuleButton bt : modButtons) {
			bt.drawText(mouseX, mouseY);
		}

		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		scissorButtons();
		for (ElixeButtonBase bti : modOptions) {
			if (isInOptionsArea(bti.y))
				bti.drawText(mouseX, mouseY);
		}
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
	}

	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);
		for (ElixeButtonBase bti : modOptions) {
			bti.keyClick(keyCode);
		}
	}
}

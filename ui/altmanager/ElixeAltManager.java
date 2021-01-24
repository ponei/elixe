package elixe.ui.altmanager;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.EnumChatFormatting;
import thealtening.auth.TheAlteningAuthentication;
import thealtening.auth.service.AlteningServiceType;

public final class ElixeAltManager extends GuiScreen {
	private GuiTextField password;
	private GuiTextField username;
	
	private final GuiScreen previousScreen;
	private ElixeLoginThread thread;

	private String currentService = "Mojang";
	private TheAlteningAuthentication authServer = TheAlteningAuthentication.mojang();

	public ElixeAltManager(GuiScreen previousScreen) {
		this.previousScreen = previousScreen;
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		switch (button.id) {
		case 1:
			this.thread = new ElixeLoginThread(this.username.getText(), this.password.getText());
			this.thread.start();
			break;
		case 2:
			if (currentService.equals("Mojang")) {
				currentService = "TheAltening";
				authServer.updateService(AlteningServiceType.THEALTENING);
				thread.shouldIgnorePass(true);
			} else {
				currentService = "Mojang";
				authServer.updateService(AlteningServiceType.MOJANG);
				thread.shouldIgnorePass(false);
			}
			break;
		case 3:
			this.mc.displayGuiScreen(this.previousScreen);
			break;
		}
	}

	@Override
	public void drawScreen(int x2, int y2, float z2) {
		this.drawDefaultBackground();
		this.username.drawTextBox();
		this.password.drawTextBox();
		this.drawCenteredString(this.mc.fontRendererObj, "Alt Login", width / 2, 20, -1);
		this.drawCenteredString(this.mc.fontRendererObj, (Object) ((Object) EnumChatFormatting.GRAY) + currentService, width / 2, 29, -1);
		this.drawCenteredString(this.mc.fontRendererObj,
				this.thread == null ? "Idle..." : this.thread.getStatus(),
				width / 2, 49, -1);
		if (this.username.getText().isEmpty()) {
			this.drawString(this.mc.fontRendererObj, "Username / E-Mail", width / 2 - 96, 66, -7829368);
		}
		if (this.password.getText().isEmpty()) {
			this.drawString(this.mc.fontRendererObj, "Password", width / 2 - 96, 106, -7829368);
		}
		super.drawScreen(x2, y2, z2);
	}

	@Override
	public void initGui() {
		int var3 = height / 4 + 24;
		this.buttonList.add(new GuiButton(1, width / 2 - 100, var3 + 72 + 12, "Login"));
		this.buttonList.add(new GuiButton(2, width / 2 - 100, var3 + 72 + 12 + 24, "Switch service"));
		this.buttonList.add(new GuiButton(3, width / 2 - 100, var3 + 72 + 12 + 48, "Back"));
		this.username = new GuiTextField(4, this.mc.fontRendererObj, width / 2 - 100, 60, 200, 20);
		this.password = new GuiTextField(5, this.mc.fontRendererObj, width / 2 - 100, 100, 200, 20);
		this.username.setFocused(true);
		Keyboard.enableRepeatEvents(true);
	}

	@Override
	protected void keyTyped(char character, int key) {
		try {
			super.keyTyped(character, key);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (character == '\t') {
			if (!this.username.isFocused() && !this.password.isFocused()) {
				this.username.setFocused(true);
			} else {
				this.username.setFocused(this.password.isFocused());
				this.password.setFocused(!this.username.isFocused());
			}
		}
		if (character == '\r') {
			this.actionPerformed((GuiButton) this.buttonList.get(0));
		}
		this.username.textboxKeyTyped(character, key);
		this.password.textboxKeyTyped(character, key);
	}

	@Override
	protected void mouseClicked(int x2, int y2, int button) {
		try {
			super.mouseClicked(x2, y2, button);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.username.mouseClicked(x2, y2, button);
		this.password.mouseClicked(x2, y2, button);
	}

	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	@Override
	public void updateScreen() {
		this.username.updateCursorCounter();
		this.password.updateCursorCounter();
	}
}

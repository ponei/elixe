package elixe.ui.altmanager;

import java.net.Proxy;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Session;

public final class ElixeLoginThread extends Thread {
	private String password;
	private String status;
	private String username;
	private Minecraft mc = Minecraft.getMinecraft();

	public ElixeLoginThread(String username, String password, String service) {
		super("Alt Login Thread");
		this.username = username;
		this.password = password;

		if (service.equals("TheAltening")) {
			ignorePass = true;
			this.password = "elixe";
		}
		this.status = (Object) ((Object) EnumChatFormatting.GRAY) + "Waiting...";
	}

	private Session createSession(String username, String password) {
		YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
		YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication) service
				.createUserAuthentication(Agent.MINECRAFT);
		auth.setUsername(username);
		auth.setPassword(password);
		try {
			auth.logIn();
			return new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(),
					auth.getAuthenticatedToken(), "mojang");
		} catch (AuthenticationException localAuthenticationException) {
			localAuthenticationException.printStackTrace();
			return null;
		}
	}

	public String getStatus() {
		return this.status;
	}

	boolean ignorePass = false;

	@Override
	public void run() {
		if (this.username.equals("")) {
			this.status = (Object) ((Object) EnumChatFormatting.RED) + "No username. (?)";
			return;
		} else {
			if (this.password.equals("") && !ignorePass) {
				this.mc.setSession(new Session(this.username, "", "", "mojang"));
				this.status = (Object) ((Object) EnumChatFormatting.GREEN) + "Logged in. (" + this.username
						+ " - offline name)";
				return;
			}
			this.status = (Object) ((Object) EnumChatFormatting.YELLOW) + "Logging in...";
			Session auth = this.createSession(this.username, this.password);
			if (auth == null) {
				this.status = (Object) ((Object) EnumChatFormatting.RED) + "Login failed!";
			} else {
				this.status = (Object) ((Object) EnumChatFormatting.GREEN) + "Logged in. (" + auth.getUsername() + ")";
				this.mc.setSession(auth);
			}
		}
	}

	public void setStatus(String status) {
		this.status = status;
	}
}

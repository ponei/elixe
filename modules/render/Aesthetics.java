package elixe.modules.render;

import elixe.events.OnChangeWorldEvent;
import elixe.events.OnFireFirstPersonEvent;
import elixe.events.OnNauseaScaleEvent;
import elixe.events.OnOverlayDrawEvent;
import elixe.events.OnPacketReceiveEvent;
import elixe.modules.AModuleOption;
import elixe.modules.Module;
import elixe.modules.ModuleCategory;
import elixe.modules.option.ModuleBoolean;
import elixe.modules.option.ModuleFloat;
import elixe.modules.option.ModuleInteger;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import net.minecraft.network.play.server.S2BPacketChangeGameState;

public class Aesthetics extends Module {

	public Aesthetics() {
		super("Aesthetics", ModuleCategory.RENDER);

		moduleOptions.add(changeTimeOption);
		moduleOptions.add(timeOption);

		moduleOptions.add(rainOption);
		moduleOptions.add(rainStrengthOption);
		moduleOptions.add(thunderOption);
		moduleOptions.add(thunderStrengthOption);

		moduleOptions.add(hideScoreboardOption);
		moduleOptions.add(fireHeightOption);
		moduleOptions.add(ignorePumpkinOption);
		moduleOptions.add(ignoreNauseaOption);
	}

	long currentTime = 0;
	float currentRain = 0, currentThunder = 0;

	// trovao
	float thunderStrength;
	ModuleFloat thunderStrengthOption = new ModuleFloat("thunder strength", 0.5f, 0f, 4f) {
		
		public void valueChanged() {
			thunderStrength = (float) this.getValue();
			setCustomStates();
		}
	};

	boolean thunder;
	ModuleBoolean thunderOption = new ModuleBoolean("thundering", false, true) {
		
		public void valueChanged() {
			thunder = (boolean) this.getValue();
			setCustomStates();

			updateVisibilityOfOptions(new AModuleOption[] { thunderStrengthOption }, thunder);
		}
	};

	// chuva
	float rainStrength;
	ModuleFloat rainStrengthOption = new ModuleFloat("rain strength", 0.5f, 0f, 4f) {
		
		public void valueChanged() {
			rainStrength = (float) this.getValue();
			setCustomStates();
		}
	};

	boolean rain;
	ModuleBoolean rainOption = new ModuleBoolean("raining", false, true) {
		
		public void valueChanged() {
			rain = (boolean) this.getValue();
			setCustomStates();

			if (rain) {
				updateVisibilityOfOptions(new AModuleOption[] { rainStrengthOption, thunderOption }, rain);
				updateVisibilityOfOptions(new AModuleOption[] { thunderStrengthOption }, thunder);
			} else {
				updateVisibilityOfOptions(new AModuleOption[] { rainStrengthOption, thunderOption, thunderStrengthOption }, rain);
			}

		}
	};

	// tempo
	int time;
	ModuleInteger timeOption = new ModuleInteger("time value", 12000, 0, 24000) {
		
		public void valueChanged() {
			time = (int) this.getValue();
			setCustomStates();
		}
	};

	boolean changeTime;
	ModuleBoolean changeTimeOption = new ModuleBoolean("change time", false, true) {
		
		public void valueChanged() {
			changeTime = (boolean) this.getValue();
			setCustomStates();
			if (changeTime) {
				updateVisibilityOfOptions(new AModuleOption[] { timeOption, rainOption }, changeTime);
				updateVisibilityOfOptions(new AModuleOption[] { rainStrengthOption, thunderOption }, rain);
				updateVisibilityOfOptions(new AModuleOption[] { thunderStrengthOption }, thunder);
			} else {
				updateVisibilityOfOptions(new AModuleOption[] { timeOption, rainOption, rainStrengthOption, thunderOption, thunderStrengthOption }, changeTime);
			}

		}
	};
	//

	boolean hideScoreboard;
	ModuleBoolean hideScoreboardOption = new ModuleBoolean("hide scoreboard", false) {
		
		public void valueChanged() {
			hideScoreboard = (boolean) this.getValue();
		}
	};

	float fireHeight;
	ModuleFloat fireHeightOption = new ModuleFloat("fire height", -0.7f, -1f, 0f) {
		
		public void valueChanged() {
			fireHeight = (float) this.getValue();
		}
	};

	boolean ignoreNausea;
	ModuleBoolean ignoreNauseaOption = new ModuleBoolean("ignore nausea", false) {
		
		public void valueChanged() {
			ignoreNausea = (boolean) this.getValue();
		}
	};

	boolean ignorePumpkin;
	ModuleBoolean ignorePumpkinOption = new ModuleBoolean("ignore pumpkin", false) {
		
		public void valueChanged() {
			ignorePumpkin = (boolean) this.getValue();
		}
	};

	
	public void onEnable() {
		super.onEnable();
		saveCurrentState();
		setCustomStates();
	}

	
	public void onDisable() {
		super.onDisable();
		restoreOldState();
	}

	private void saveCurrentState() {
		if (mc.theWorld == null)
			return;
		currentTime = mc.theWorld.getWorldTime();
		currentRain = mc.theWorld.getRainStrength(1f);
		currentThunder = mc.theWorld.getThunderStrength(1f);
	}

	private void restoreOldState() {
		if (mc.theWorld == null)
			return;
		mc.theWorld.setWorldTime(currentTime);
		mc.theWorld.setRainStrength(currentRain);
		mc.theWorld.setThunderStrength(currentThunder);
	}

	private void setCustomStates() {
		if (mc.theWorld == null || !isToggled())
			return;
		if (changeTime) {
			mc.theWorld.setWorldTime(time);
			if (rain) {
				mc.theWorld.setRainStrength(rainStrength);
				if (thunder) {
					mc.theWorld.setThunderStrength(thunderStrength);
				} else {
					mc.theWorld.setThunderStrength(currentThunder);
				}
			} else {
				mc.theWorld.setRainStrength(currentRain);
				mc.theWorld.setThunderStrength(currentThunder);
			}
		} else {
			mc.theWorld.setWorldTime(currentTime);
			mc.theWorld.setRainStrength(currentRain);
			mc.theWorld.setThunderStrength(currentThunder);
		}
	}

	@EventHandler
	private Listener<OnPacketReceiveEvent> onPacketReceiveEvent = new Listener<>(e -> {
		if (e.getPacket() instanceof S03PacketTimeUpdate) {
			S03PacketTimeUpdate timepacket = (S03PacketTimeUpdate) e.getPacket();
			currentTime = timepacket.getWorldTime();
			if (changeTime) {
				timepacket.setWorldTime(time);
			}
		}

		if (e.getPacket() instanceof S2BPacketChangeGameState) {
			S2BPacketChangeGameState statepacket = (S2BPacketChangeGameState) e.getPacket();
			int i = statepacket.getGameState();
			float f = statepacket.func_149137_d();

			switch (i) {
			case 1:
				currentRain = 0f;
				if (rain) {
					e.cancel();
				}
				break;
			case 2:
				currentRain = 1f;
				if (rain) {
					e.cancel();
				}
				break;
			case 7:
				currentRain = f;
				if (rain) {
					e.cancel();
				}
				break;
			case 8:
				currentThunder = f;
				if (thunder) {
					e.cancel();
				}
				break;

			default:
				break;
			}
		}
	});

	@EventHandler
	private Listener<OnChangeWorldEvent> onChangeWorldEvent = new Listener<>(e -> {
		if (mc.theWorld == null)
			return;
		// entramos no mundo
		saveCurrentState();
		setCustomStates();
	});

	@EventHandler
	private Listener<OnFireFirstPersonEvent> onFireFirstPersonEvent = new Listener<>(e -> {
		e.setHeight(fireHeight);
	});

	@EventHandler
	private Listener<OnOverlayDrawEvent> onOverlayDrawEvent = new Listener<>(e -> {
		switch (e.getType()) {
		case 0:
			if (ignorePumpkin) {
				e.skip();
			}
			break;

		case 1:
			if (hideScoreboard) {
				e.skip();
			}
			break;
		}

	});

	@EventHandler
	private Listener<OnNauseaScaleEvent> onNauseaScaleEvent = new Listener<>(e -> {
		if (ignoreNausea) {
			e.cancel();
		}
	});
}

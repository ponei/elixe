package elixe.modules;

import java.util.ArrayList;

import elixe.events.OnKeyEvent;
import elixe.events.OnMouseEvent;
import elixe.modules.combat.AimAssist;
import elixe.modules.combat.AutoClicker;
import elixe.modules.combat.AutoSoup;
import elixe.modules.combat.Hitbox;
import elixe.modules.combat.KillAura;
import elixe.modules.combat.Misplace;
import elixe.modules.combat.Reach;
import elixe.modules.combat.Velocity;
import elixe.modules.misc.AntiBot;
import elixe.modules.misc.Commands;
import elixe.modules.misc.MushExploit;
import elixe.modules.misc.OldAnimations;
import elixe.modules.movement.InventoryMove;
import elixe.modules.movement.SafeWalk;
import elixe.modules.movement.Sprint;
import elixe.modules.player.Derp;
import elixe.modules.player.Phase;
import elixe.modules.render.Aesthetics;
import elixe.modules.render.Camera;
import elixe.modules.render.Chams;
import elixe.modules.render.ClickGUI;
import elixe.modules.render.ESP;
import elixe.modules.render.HUD;
import elixe.modules.render.HealthLog;
import elixe.modules.render.NameProtect;
import elixe.modules.render.Skeletal;
import elixe.modules.world.FastPlace;
import elixe.modules.world.MLG;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listenable;
import me.zero.alpine.listener.Listener;

public class ModuleManager implements Listenable {
	private ArrayList<Module> modules = new ArrayList<Module>();

	// player
	public Phase PHASE = new Phase();
	public Derp DERP = new Derp();
	// render
	public HUD HUD = new elixe.modules.render.HUD();
	public Chams CHAMS = new Chams();
	public ESP ESP = new elixe.modules.render.ESP();
	public ClickGUI CLICKGUI = new ClickGUI();
	public NameProtect NAMEPROTECT = new NameProtect();
	public Camera CAMERA = new Camera();
	public Aesthetics AESTHETICS = new Aesthetics();
	public HealthLog HEALTHLOG = new HealthLog();
	public Skeletal SKELETAL = new Skeletal();
	// combat
	public KillAura KILLAURA = new KillAura();
	public AimAssist AIMASSIST = new AimAssist();
	public AutoClicker AUTOCLICKER = new AutoClicker();
	public Reach REACH = new Reach();
	public Hitbox HITBOX = new Hitbox();
	public Velocity VELOCITY = new Velocity();
	public AutoSoup AUTOSOUP = new AutoSoup();
	public Misplace MISPLACE = new Misplace();
	// world
	public MLG MLG = new MLG();
	public FastPlace FASTPLACE = new FastPlace();
	// movement
	public Sprint SPRINT = new Sprint();
	public InventoryMove INVENTORYMOVE = new InventoryMove();
	public SafeWalk SAFEWALK = new SafeWalk();
	// misc
	public AntiBot ANTIBOT = new AntiBot();
	public Commands COMMANDS = new Commands();
	public OldAnimations OLDANIMATIONS = new OldAnimations();
	public MushExploit MUSHEXPLOIT = new MushExploit();

	public ModuleManager() {
		// player
		modules.add(PHASE);
		modules.add(DERP);
		// render
		modules.add(HUD);
		modules.add(CHAMS);
		modules.add(ESP);
		modules.add(CLICKGUI);
		modules.add(NAMEPROTECT);
		modules.add(CAMERA);
		modules.add(AESTHETICS);
		modules.add(SKELETAL);
		// modules.add(HEALTHLOG);
		// combat
		modules.add(AIMASSIST);
		modules.add(AUTOCLICKER);
		modules.add(HITBOX);
		// modules.add(MISPLACE);
		modules.add(REACH);
		modules.add(VELOCITY);
		modules.add(AUTOSOUP);
		// world
		modules.add(MLG);
		modules.add(FASTPLACE);
		// movement
		modules.add(SPRINT);
		modules.add(INVENTORYMOVE);
		modules.add(SAFEWALK);
		// misc
		modules.add(COMMANDS);
		modules.add(OLDANIMATIONS);
		modules.add(MUSHEXPLOIT);
		// modules.add(ANTIBOT);

		// post
		HUD.setModuleManager(this);
	}

	private void toggleByKey(int key) {
		for (Module m : modules) {
			if (m.getKey() == key) {
				m.toggle();
			}
		}
	}

	public ArrayList<Module> getModulesByCategory(ModuleCategory cat) {
		ArrayList<Module> mods = new ArrayList<Module>();

		for (Module m : modules) {
			if (m.getCategory() == cat) {

				mods.add(m);
			}
		}

		return mods;
	}

	public Module getModuleByName(String name) {
		for (Module m : modules) {
			if (m.getName().equals(name)) {
				return m;
			}
		}
		return null;
	}

	public ArrayList<Module> getModules() {
		return modules;
	}

	@EventHandler
	private Listener<OnMouseEvent> onMouseEvent = new Listener<>(e -> {
		if (!e.isState()) {
			toggleByKey(e.getButton());
		}
	});

	@EventHandler
	private Listener<OnKeyEvent> onKeyListener = new Listener<>(e -> {
		if (!e.isState()) {
			toggleByKey(e.getKey());
		}
	});
}

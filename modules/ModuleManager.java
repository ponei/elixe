package elixe.modules;

import java.util.ArrayList;

import elixe.events.OnKeyEvent;
import elixe.events.OnMouseEvent;
import elixe.modules.combat.*;
import elixe.modules.misc.*;
import elixe.modules.movement.*;
import elixe.modules.render.*;
import elixe.modules.world.*;
import elixe.modules.player.*;

import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listenable;
import me.zero.alpine.listener.Listener;

public class ModuleManager implements Listenable {
    private ArrayList<Module> modules = new ArrayList<Module>();

    //player    
    public Phase PHASE = new Phase();
    public Derp DERP = new Derp();
    //render
    public HUD HUD = new elixe.modules.render.HUD();
    public Chams CHAMS = new Chams();
    public ESP ESP = new elixe.modules.render.ESP();
    public ClickGUI CLICKGUI = new ClickGUI();
    public NameProtect NAMEPROTECT = new NameProtect();
    public Camera CAMERA = new Camera();
    public ClearView CLEARVIEW = new ClearView();
    public HealthLog HEALTHLOG = new HealthLog();
    //combat
    public KillAura KILLAURA = new KillAura();
    public AimAssist AIMASSIST = new AimAssist();
    public AutoClicker AUTOCLICKER = new AutoClicker();
    public Reach REACH = new Reach();
    public Hitbox HITBOX = new Hitbox();
    public Velocity VELOCITY = new Velocity();
    public AutoSoup AUTOSOUP = new AutoSoup(); 
    public Misplace MISPLACE = new Misplace();
    //world
    public MLG MLG = new MLG();
    //movement
    public Sprint SPRINT = new Sprint();
    public InventoryMove INVENTORYMOVE = new InventoryMove();
    public SafeWalk SAFEWALK = new SafeWalk();
    //misc
    public Commands COMMANDS = new Commands();
    public OldAnimations OLDANIMATIONS = new OldAnimations();
    public MushExploit MUSHEXPLOIT = new MushExploit();
    
    public ModuleManager() {
    	//player    	
    	modules.add(PHASE);
    	modules.add(DERP);
        //render
    	modules.add(HUD);
    	modules.add(CHAMS);
    	modules.add(ESP);
    	modules.add(CLICKGUI);
    	modules.add(NAMEPROTECT);
    	modules.add(CAMERA);
    	modules.add(CLEARVIEW);
    	//modules.add(HEALTHLOG);
    	//combat
    	modules.add(AIMASSIST);
    	modules.add(AUTOCLICKER);
    	modules.add(HITBOX);
    	//modules.add(MISPLACE);
    	modules.add(REACH);
    	modules.add(VELOCITY);
    	modules.add(AUTOSOUP);  	
        //world
    	modules.add(MLG);  
        //movement
    	modules.add(SPRINT);
    	modules.add(INVENTORYMOVE);
    	modules.add(SAFEWALK);
        //misc
    	modules.add(COMMANDS);
    	modules.add(OLDANIMATIONS);
    	modules.add(MUSHEXPLOIT);  	
    	
    	//post
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

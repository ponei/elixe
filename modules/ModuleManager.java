package elixe.modules;

import java.util.ArrayList;

import elixe.events.OnKeyEvent;
import elixe.events.OnMouseEvent;
import elixe.modules.combat.*;
import elixe.modules.misc.*;
import elixe.modules.movement.*;
import elixe.modules.render.*;
import elixe.modules.player.*;

import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listenable;
import me.zero.alpine.listener.Listener;

public class ModuleManager implements Listenable {
    private ArrayList<Module> modules = new ArrayList<Module>();

    //player    
    public Phase PHASE = new Phase();
    //render
    public elixe.modules.render.HUD HUD = new elixe.modules.render.HUD();
    public Chams CHAMS = new Chams();
    public elixe.modules.render.ESP ESP = new elixe.modules.render.ESP();
    public ClickGUI CLICKGUI = new ClickGUI();
    public NameProtect NAMEPROTECT = new NameProtect();
    public HealthLog HEALTHLOG = new HealthLog();
    //combat
    public AimAssist AIMASSIST = new AimAssist();
    public AutoClicker AUTOCLICKER = new AutoClicker();
    public Reach REACH = new Reach();
    public Velocity VELOCITY = new Velocity();
    public AutoSoup AUTOSOUP = new AutoSoup();    
    //world
    //movement
    public Sprint SPRINT = new Sprint();
    public InventoryMove INVENTORYMOVE = new InventoryMove();
    //misc
    public OldAnimations OLDANIMATIONS = new OldAnimations();
    public MushExploit MUSHEXPLOIT = new MushExploit();
    
    public ModuleManager() {
    	//player    	
    	modules.add(PHASE);
        //render
    	modules.add(HUD);
    	modules.add(CHAMS);
    	modules.add(ESP);
    	modules.add(CLICKGUI);
    	modules.add(NAMEPROTECT);
    	//modules.add(HEALTHLOG);
    	//combat
    	modules.add(AIMASSIST);
    	modules.add(AUTOCLICKER);
    	modules.add(REACH);
    	modules.add(VELOCITY);
    	modules.add(AUTOSOUP);  	
        //world
        //movement
    	modules.add(SPRINT);
    	modules.add(INVENTORYMOVE);
        //misc
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

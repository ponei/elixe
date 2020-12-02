package elixe.modules.render;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.opengl.GL11;

import elixe.Elixe;
import elixe.events.OnBrightnessEntityEvent;
import elixe.events.OnPacketReceiveEvent;
import elixe.events.OnRender2DEvent;
import elixe.modules.Module;
import elixe.modules.ModuleCategory;
import elixe.utils.transitions.ITransition;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.DataWatcher.WatchableObject;
import net.minecraft.network.play.server.S1CPacketEntityMetadata;

public class HealthLog extends Module {

	public HealthLog() {
		super("HealthLog", ModuleCategory.RENDER);

	}
	

	public void onDisable() {
		super.onDisable();
		healthLogs.clear();
		ySpacing = 10f;
	}
	
	List<LogEntry> healthLogs = new CopyOnWriteArrayList<>();
	private float ySpacing;
	@EventHandler
	private Listener<OnPacketReceiveEvent> onPacketReceiveEvent = new Listener<>(e -> {
		if (e.getPacket() instanceof S1CPacketEntityMetadata) {
			S1CPacketEntityMetadata meta = (S1CPacketEntityMetadata) e.getPacket();
			if (meta.getEntityId() == mc.thePlayer.getEntityId()) {
				for (WatchableObject data : meta.func_149376_c()) {
					if (data.getDataValueId() == 6) {
						healthLogs.add(new LogEntry(100f, ySpacing, new Date().getTime(), 2000, "cu -> " + data.getObject()));
						ySpacing += 10f;
					}
				}
			}
		}
	});
	
	
	
	@EventHandler
	private Listener<OnRender2DEvent> onRender2DEvent = new Listener<>(e -> {
		updateEntries();
		if (!this.mc.gameSettings.showDebugInfo) {

			Iterator<LogEntry> logIterator = healthLogs.iterator();
			while (logIterator.hasNext()) {
				LogEntry log = logIterator.next();
				mc.fontRendererObj.drawStringWithShadow(log.text, log.x, log.y, 1f, 1f);
			}
		}
	});
	
	private void updateEntries() {
		long actualTime = new Date().getTime();
		
		int spacingChange = 0;
		Iterator<LogEntry> logIterator = healthLogs.iterator();
		while (logIterator.hasNext()) {
			LogEntry log = logIterator.next();		
			if (log.timePassed(actualTime)) {
				ySpacing -= 10f;
				healthLogs.remove(log);
			} else {
			}
		}

	}

	public class LogEntry {
		ArrayList<ITransition> entryTransitions = new ArrayList<ITransition>();
		public float x,y;
		
		public long startTime;
		public int logTime;
		public String text;
	
		public LogEntry(float x, float y, long startTime, int logTime, String text) {
			super();
			this.x = x;
			this.y = y;
			this.startTime = startTime;
			this.logTime = logTime;
			this.text = text;
		}

		public boolean timePassed(long actualTime) {
			if (actualTime - startTime > logTime) {
				return true;
			}
			return false;
		}
	}
}

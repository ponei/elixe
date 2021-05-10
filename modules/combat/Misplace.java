package elixe.modules.combat;

import elixe.events.OnPacketReceiveEvent;
import elixe.modules.Module;
import elixe.modules.ModuleCategory;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S18PacketEntityTeleport;

public class Misplace extends Module {
	public Misplace() {
		super("Misplace", ModuleCategory.COMBAT);
	}

	@EventHandler
	private Listener<OnPacketReceiveEvent> onPacketReceiveEvent = new Listener<>(e -> {
		Packet p = e.getPacket();
		if (p instanceof S18PacketEntityTeleport) {
			System.out.println(((S18PacketEntityTeleport) p).getEntityId());
		}
	});
	
}

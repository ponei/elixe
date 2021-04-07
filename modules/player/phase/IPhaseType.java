package elixe.modules.player.phase;

import elixe.events.OnLivingUpdateEvent;
import elixe.events.OnMoveEvent;
import elixe.events.OnPacketSendEvent;
import elixe.modules.AModuleOption;

public interface IPhaseType {
	public String getName();
	
	public void OnMove(OnMoveEvent event);

	public void OnLivingUpdate(OnLivingUpdateEvent event);

	public void OnPacket(OnPacketSendEvent event);
}

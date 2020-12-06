package elixe.modules.player.phase;

import elixe.events.OnLivingUpdateEvent;
import elixe.events.OnMoveEvent;
import elixe.events.OnPacketSendEvent;

public class OffsetPhase implements IPhaseType {

	@Override
	public void AddOptions() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnMove(OnMoveEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnLivingUpdate(OnLivingUpdateEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnPacket(OnPacketSendEvent event) {
		// TODO Auto-generated method stub
		
	}

	public String getName() {
		return "offset";
	}
}

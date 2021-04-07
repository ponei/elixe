package elixe.modules.player;

import elixe.events.OnLivingUpdateEvent;
import elixe.events.OnMoveEvent;
import elixe.events.OnPacketSendEvent;
import elixe.events.OnPushOutBlocksEvent;
import elixe.modules.AModuleOption;
import elixe.modules.Module;
import elixe.modules.ModuleCategory;
import elixe.modules.option.ModuleArray;
import elixe.modules.player.phase.IPhaseType;
import elixe.modules.player.phase.OffsetPhase;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;

public class Phase extends Module {
	
	IPhaseType[] phaseTypes = {new OffsetPhase()};
	IPhaseType selectedPhase;
	
	public Phase() {
		super("Phase", ModuleCategory.PLAYER);
		
		String[] phaseNames = new String[phaseTypes.length];
		for (int i = 0; i < phaseNames.length; i++) {
			phaseNames[i] = phaseTypes[i].getName();
		}
		
		phaseTypeIndexOption = new ModuleArray("type", 0, phaseNames) {
			public void valueChanged() {
				phaseTypeIndex = (int) this.getValue();
				updatePhaseType();
			}
		};
		moduleOptions.add(phaseTypeIndexOption);
		
		updatePhaseType();	
	}
	
	int phaseTypeIndex;
	ModuleArray phaseTypeIndexOption;
	
	private void updatePhaseType() {
		selectedPhase = phaseTypes[phaseTypeIndex];
	}
	
	

	@EventHandler
	private Listener<OnLivingUpdateEvent> onLivingUpdateEvent = new Listener<>(e -> {
		selectedPhase.OnLivingUpdate(e);
	});
	
	@EventHandler
	private Listener<OnMoveEvent> onMoveEvent = new Listener<>(e -> {
		selectedPhase.OnMove(e);
	});
	
	@EventHandler
	private Listener<OnPacketSendEvent> onPacketSendEvent = new Listener<>(e -> {
		selectedPhase.OnPacket(e);
	});
	
	@EventHandler
	private Listener<OnPushOutBlocksEvent> onPushOutBlocksEvent = new Listener<>(e -> {
		e.cancel();
	});
	
}

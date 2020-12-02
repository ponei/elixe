package elixe.events;

import net.minecraft.entity.Entity;

//attackEntity(EntityPlayer, Entity) : void - net.minecraft.client.multiplayer.PlayerControllerMP
//L:503
public class OnAttackEntityEvent {
	private Entity attackedEntity;

	public OnAttackEntityEvent(Entity attackedEntity) {
		super();
		this.attackedEntity = attackedEntity;
	}

	public Entity getAttackedEntity() {
		return attackedEntity;
	}
	
	
}

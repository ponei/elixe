package elixe.utils.player;

import org.lwjgl.input.Mouse;

import elixe.Elixe;
import elixe.events.OnTickEvent;
import me.zero.alpine.event.EventPriority;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listenable;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.Potion;

public class PlayerConditionals implements Listenable {
	Minecraft mc = Elixe.INSTANCE.mc;

	private boolean sprinting, holdingWeapon, holdingAttack, holdingUse, inWater, onAir, speed;

	@EventHandler
	private Listener<OnTickEvent> onTickEvent = new Listener<>(e -> {
		if (mc.currentScreen == null) {
			onAir = !mc.thePlayer.onGround;
			
			speed = this.mc.thePlayer.isPotionActive(Potion.moveSpeed);
			
			sprinting = mc.thePlayer.isSprinting();

			holdingAttack = Mouse.isButtonDown(mc.gameSettings.keyBindAttack.getKeyCode() + 100);
			
			holdingUse = Mouse.isButtonDown(mc.gameSettings.keyBindUseItem.getKeyCode() + 100);

			inWater = mc.thePlayer.isInWater();

			ItemStack item = mc.thePlayer.getCurrentEquippedItem();
			
			if (item != null) {
				holdingWeapon = (item.getItem() instanceof ItemSword || item.getItem() instanceof ItemAxe
						|| item.getItem() instanceof ItemPickaxe || item.getItem() instanceof ItemSpade
						|| item.getItem() instanceof ItemHoe);

			} else {
				holdingWeapon = false;
			}
		}
	}, EventPriority.HIGHEST);

	public boolean hasSpeed() {
		return speed;
	}
	
	public boolean isOnAir() {
		return onAir;
	}
	
	public boolean isSprinting() {
		return sprinting;
	}

	public boolean isHoldingWeapon() {
		return holdingWeapon;
	}

	public boolean isHoldingAttack() {
		return holdingAttack;
	}
	
	public boolean isHoldingUse() {
		return holdingUse;
	}

	public boolean isInWater() {
		return inWater;
	}

}

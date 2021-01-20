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

public class ConditionalsUtils implements Listenable {
	Minecraft mc = Elixe.INSTANCE.mc;

	private boolean sprinting, holdingWeapon, holdingAttack, inWater;

	@EventHandler
	private Listener<OnTickEvent> onTickEvent = new Listener<>(e -> {
		if (mc.currentScreen == null) {
			sprinting = mc.thePlayer.isSprinting();

			int attack = mc.gameSettings.keyBindAttack.getKeyCode() + 100;
			holdingAttack = Mouse.isButtonDown(attack);

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

	public boolean isSprinting() {
		return sprinting;
	}

	public boolean isHoldingWeapon() {
		return holdingWeapon;
	}

	public boolean isHoldingAttack() {
		return holdingAttack;
	}

	public boolean isInWater() {
		return inWater;
	}

}

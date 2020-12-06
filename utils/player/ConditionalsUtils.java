package elixe.utils.player;

import org.lwjgl.input.Mouse;

import elixe.Elixe;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

public class ConditionalsUtils {
	public static ConditionalsUtils INSTANCE;
	Minecraft mc = Elixe.INSTANCE.mc;

	public ConditionalsUtils() {
		INSTANCE = this;
	}

	private boolean sprinting, holdingWeapon, holdingAttack, inWater;

	//runTick() : void - net.minecraft.client.Minecraft
	//L:1579
	//antes do event de tick
	public void updateConditionals() {
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

	public boolean isInWater() {
		return inWater;
	}

}

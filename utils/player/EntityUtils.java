package elixe.utils.player;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public class EntityUtils {
	public static boolean isNaked(EntityLivingBase ent) {
		for (int i = 1; 4 >= i; i++) {
			ItemStack armor = ent.getEquipmentInSlot(i);
			if (armor != null) {
				return false;
			}
		}
		return true;
	}
}

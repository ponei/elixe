package elixe.utils.player;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class InventoryItem implements Comparable<InventoryItem> {
	private int slot, size;
	private Block invBlock;
	private Item invItem;

	public InventoryItem(int slot, int size, Block invBlock) {
		this.slot = slot;
		this.size = size;
		this.invBlock = invBlock;
	}

	public InventoryItem(int slot, int size, Item invItem) {
		this.slot = slot;
		this.size = size;
		this.invItem = invItem;
	}

	public int getSlot() {
		return slot;
	}

	public int getSize() {
		return size;
	}

	public Object getObject() {
		if (invBlock != null) {
			return invBlock;
		} else {
			return invItem;
		}
	}

	static Random r = new Random();
	
	public static int findItem(int startSlot, int endSlot, Item item, Minecraft mc) {
		for (int i = startSlot; i < endSlot; i++) {
			ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

			if (stack != null && stack.getItem() == item)
				return i;
		}
		return -1;
	}

	public static int findItemRandomized(int startSlot, int endSlot, Item item, Minecraft mc) {
		ArrayList<Integer> itemList = new ArrayList<Integer>();
		for (int i = startSlot; i < endSlot; i++) {
			ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

			if (stack != null && stack.getItem() == item) {
				itemList.add(i);
			}
		}

		if (itemList.size() > 0) {
			return itemList.get(r.nextInt(itemList.size()));
		} else {
			return -1;
		}
	}

	public static boolean hasSpaceHotbar(Minecraft mc) {
		for (int i = 36; i < 45; i++) {
			ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

			if (itemStack == null)
				return true;
		}
		return false;
	}

	int useItem;

	public static InventoryItem findBlockHighest(int startSlot, int endSlot, Block block, Minecraft mc) {
		int slot = -1;
		int size = 0;

		Item blockItem = Item.getItemFromBlock(block);
		for (int i = startSlot; i < endSlot; i++) {
			ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
			if (itemStack != null) {
				if (itemStack.getItem() == blockItem) {
					if (itemStack.stackSize > size) {
						size = itemStack.stackSize;
						slot = i;
					}

				}
			}
		}
		return new InventoryItem(slot, size, block);
	}

	public static InventoryItem findItemHighest(int startSlot, int endSlot, Item item, int meta, Minecraft mc) {
		int slot = -1;
		int size = 0;

		for (int i = startSlot; i < endSlot; i++) {
			ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
			if (itemStack != null) {
				if (itemStack.getItem() == item && itemStack.getItemDamage() == meta) {
					if (itemStack.stackSize > size) {
						size = itemStack.stackSize;
						slot = i;
					}

				}
			}
		}
		return new InventoryItem(slot, size, item);
	}
	
	// maior fica na frente
	
	public int compareTo(InventoryItem item) {
		return item.getSize() - size;
	}

}

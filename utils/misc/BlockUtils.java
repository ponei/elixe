package elixe.utils.misc;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;

public class BlockUtils {
	private Item[] placeableBlocks = { Item.getItemFromBlock(Blocks.stone), Item.getItemFromBlock(Blocks.grass),
			Item.getItemFromBlock(Blocks.dirt), Item.getItemFromBlock(Blocks.cobblestone), Item.getItemFromBlock(Blocks.planks),
			Item.getItemFromBlock(Blocks.bedrock), Item.getItemFromBlock(Blocks.gold_ore), Item.getItemFromBlock(Blocks.iron_ore),
			Item.getItemFromBlock(Blocks.coal_ore), Item.getItemFromBlock(Blocks.log), Item.getItemFromBlock(Blocks.log2), Item.getItemFromBlock(Blocks.leaves),
			Item.getItemFromBlock(Blocks.leaves2), Item.getItemFromBlock(Blocks.sponge), Item.getItemFromBlock(Blocks.glass),
			Item.getItemFromBlock(Blocks.lapis_ore), Item.getItemFromBlock(Blocks.lapis_block), Item.getItemFromBlock(Blocks.sandstone),
			Item.getItemFromBlock(Blocks.sticky_piston), Item.getItemFromBlock(Blocks.piston), Item.getItemFromBlock(Blocks.wool),
			Item.getItemFromBlock(Blocks.gold_block), Item.getItemFromBlock(Blocks.iron_block), Item.getItemFromBlock(Blocks.double_stone_slab),
			Item.getItemFromBlock(Blocks.stone_slab), Item.getItemFromBlock(Blocks.brick_block), Item.getItemFromBlock(Blocks.tnt),
			Item.getItemFromBlock(Blocks.bookshelf), Item.getItemFromBlock(Blocks.mossy_cobblestone), Item.getItemFromBlock(Blocks.obsidian),
			Item.getItemFromBlock(Blocks.mob_spawner), Item.getItemFromBlock(Blocks.oak_stairs), Item.getItemFromBlock(Blocks.diamond_ore),
			Item.getItemFromBlock(Blocks.diamond_block), Item.getItemFromBlock(Blocks.stone_stairs), Item.getItemFromBlock(Blocks.redstone_ore),
			Item.getItemFromBlock(Blocks.lit_redstone_ore), Item.getItemFromBlock(Blocks.ice), Item.getItemFromBlock(Blocks.snow),
			Item.getItemFromBlock(Blocks.clay), Item.getItemFromBlock(Blocks.jukebox), Item.getItemFromBlock(Blocks.pumpkin),
			Item.getItemFromBlock(Blocks.netherrack), Item.getItemFromBlock(Blocks.soul_sand), Item.getItemFromBlock(Blocks.glowstone),
			Item.getItemFromBlock(Blocks.stonebrick), Item.getItemFromBlock(Blocks.brown_mushroom_block), Item.getItemFromBlock(Blocks.red_mushroom_block),
			Item.getItemFromBlock(Blocks.melon_block), Item.getItemFromBlock(Blocks.brick_stairs), Item.getItemFromBlock(Blocks.stone_brick_stairs),
			Item.getItemFromBlock(Blocks.mycelium), Item.getItemFromBlock(Blocks.nether_brick), Item.getItemFromBlock(Blocks.enchanting_table),
			Item.getItemFromBlock(Blocks.end_stone), Item.getItemFromBlock(Blocks.redstone_lamp), Item.getItemFromBlock(Blocks.lit_redstone_lamp),
			Item.getItemFromBlock(Blocks.double_wooden_slab), Item.getItemFromBlock(Blocks.wooden_slab), Item.getItemFromBlock(Blocks.sandstone_stairs),
			Item.getItemFromBlock(Blocks.emerald_ore), Item.getItemFromBlock(Blocks.emerald_block), Item.getItemFromBlock(Blocks.spruce_stairs),
			Item.getItemFromBlock(Blocks.birch_stairs), Item.getItemFromBlock(Blocks.jungle_stairs), Item.getItemFromBlock(Blocks.command_block),
			Item.getItemFromBlock(Blocks.beacon), Item.getItemFromBlock(Blocks.redstone_block), Item.getItemFromBlock(Blocks.quartz_ore),
			Item.getItemFromBlock(Blocks.quartz_block), Item.getItemFromBlock(Blocks.quartz_stairs), Item.getItemFromBlock(Blocks.stained_hardened_clay),
			Item.getItemFromBlock(Blocks.hay_block), Item.getItemFromBlock(Blocks.hardened_clay), Item.getItemFromBlock(Blocks.coal_block),
			Item.getItemFromBlock(Blocks.packed_ice), Item.getItemFromBlock(Blocks.acacia_stairs), Item.getItemFromBlock(Blocks.dark_oak_stairs),
			Item.getItemFromBlock(Blocks.slime_block), Item.getItemFromBlock(Blocks.stained_glass), Item.getItemFromBlock(Blocks.prismarine),
			Item.getItemFromBlock(Blocks.sea_lantern), Item.getItemFromBlock(Blocks.red_sandstone), Item.getItemFromBlock(Blocks.red_sandstone_stairs),
			Item.getItemFromBlock(Blocks.double_stone_slab2), Item.getItemFromBlock(Blocks.stone_slab2) };

	public boolean isBlockPlaceable(Item item) {
		for (Item block : placeableBlocks) {			
			if (block == item) {
				return true;
			}
		}
		return false;
	}
}

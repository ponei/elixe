package elixe.modules.combat;

import java.util.ArrayList;
import java.util.Random;

import elixe.events.OnKeybindActionEvent;
import elixe.events.OnPacketSendEvent;
import elixe.events.OnTickEvent;
import elixe.modules.Module;
import elixe.modules.ModuleCategory;
import elixe.modules.option.ModuleBoolean;
import elixe.modules.option.ModuleFloat;
import elixe.modules.option.ModuleInteger;
import elixe.utils.misc.TimerUtils;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class AutoSoup extends Module {

	public AutoSoup() {
		super("AutoSoup", ModuleCategory.COMBAT);

		moduleOptions.add(healthToSoupOption);
		moduleOptions.add(dropBowlOption);

		moduleOptions.add(refillOption);
		moduleOptions.add(refillDelayOption);

		moduleOptions.add(recraftOption);
		moduleOptions.add(recraftDelayOption);
	}

	float healthToSoup = 12f;
	ModuleFloat healthToSoupOption = new ModuleFloat("health to soup", 12f, 1f, 20f) {
		public void valueChanged() {
			healthToSoup = (float) this.getValue();
		}
	};

	boolean dropBowl = false;
	ModuleBoolean dropBowlOption = new ModuleBoolean("drop bowl", false) {
		public void valueChanged() {
			dropBowl = (boolean) this.getValue();
		}
	};

	boolean refill = false;
	ModuleBoolean refillOption = new ModuleBoolean("refill", false) {
		public void valueChanged() {
			refill = (boolean) this.getValue();
		}
	};

	int refillDelay = 100;
	ModuleInteger refillDelayOption = new ModuleInteger("refill delay", 100, 1, 300) {
		public void valueChanged() {
			refillDelay = (int) this.getValue();
		}
	};

	boolean recraft = false;
	ModuleBoolean recraftOption = new ModuleBoolean("recraft", false) {
		public void valueChanged() {
			recraft = (boolean) this.getValue();
		}
	};

	int recraftDelay = 100;
	ModuleInteger recraftDelayOption = new ModuleInteger("recraft delay", 100, 1, 300) {
		public void valueChanged() {
			recraftDelay = (int) this.getValue();
		}
	};

	Random r = new Random();

	boolean autoSouping = false;
	int autoSoupStep = 0;

	TimerUtils refillTimer = new TimerUtils();

	TimerUtils recraftTimer = new TimerUtils();
	boolean recrafting = false;
	int recraftStep = 0;
	InventoryItem bowlRecraft, redMushRecraft, brownMushRecraft;

	int lastItem;
	int soupInHotbar;
	int soupInInventory;

//	@EventHandler
//	private Listener<OnPacketSendEvent> onPacketSendEvent = new Listener<>(e -> {
//		if (e.getPacket() instanceof C09PacketHeldItemChange) {
//			System.out.println("mudança de slot -> " + ((C09PacketHeldItemChange) e.getPacket()).getSlotId());
//		} else if (e.getPacket() instanceof C08PacketPlayerBlockPlacement) {
//			C08PacketPlayerBlockPlacement placement = (C08PacketPlayerBlockPlacement) e.getPacket();
//			if (placement.getPosition().getY() == -1) {
//				System.out.println("item usado -> " + placement.getStack());
//			} else {
//				System.out.println("item usado em bloco -> " + placement.getStack());
//			}
//
//		} else if (e.getPacket() instanceof C07PacketPlayerDigging) {
//			System.out.println("ação no item -> " + ((C07PacketPlayerDigging) e.getPacket()).getStatus());
//		}
//
//	});

	@EventHandler
	private Listener<OnTickEvent> onTickEvent = new Listener<>(e -> {
		if (mc.currentScreen instanceof GuiInventory) {
			if (recrafting) {
				makeRecraftStep();
			} else {

				if (refill) { // opcao de refill ativada e nao ta fazendo recraft
					if (refillTimer.hasTimePassed(refillDelay)) { // timer passou
						soupInInventory = findItemRandomized(9, 36, Items.mushroom_stew); // tenta pegar sopa no inv
						if (soupInInventory != -1 && hasSpaceHotbar()) { // tem sopa e espaço na hotbar
							mc.playerController.windowClick(0, soupInInventory, 0, 1, mc.thePlayer);
							refillTimer.reset();
							return;
						}
					}
				}

				if (recraft) {
					int trySoup = findItem(9, 45, Items.mushroom_stew);
					if (trySoup == -1) {

						bowlRecraft = findItemHighest(9, 45, Items.bowl);
						redMushRecraft = findBlockHighest(9, 45, Blocks.red_mushroom);
						brownMushRecraft = findBlockHighest(9, 45, Blocks.brown_mushroom);

						if (bowlRecraft.getSlot() != -1 && redMushRecraft.getSlot() != -1
								&& brownMushRecraft.getSlot() != -1) {
							recraftStep = 1;
							recrafting = true;
						}
					}
				}
			}

		} else if (mc.currentScreen == null) { // sem nenhuma gui ativa
			if (autoSouping) { // loop do autosoup ativo
				makeAutoSoupStep();
			} else { // checa se é possivel iniciar loop do autosoup
				shouldAutoSoup(true);
			}
			recrafting = false;
		}
	});

	private void makeRecraftStep() {
		if (recraftTimer.hasTimePassed(recraftDelay)) {

			switch (recraftStep) {
			case 1: // pega cogumelo
				mc.playerController.windowClick(0, redMushRecraft.getSlot(), 0, 0, mc.thePlayer);
				if (redMushRecraft.getSize() == 1) { // se for só 1 cogu, pula step de botao direito
					recraftStep++;
				}
				break;

			case 2: // clica uma vez com o direito pra colocar um cogu no slot
				mc.playerController.windowClick(0, redMushRecraft.getSlot(), 1, 0, mc.thePlayer);
				break;

			case 3: // coloca os cogu na crafting
				mc.playerController.windowClick(0, 3, 0, 0, mc.thePlayer);
				break;

			case 4: // msm coisa mas
				mc.playerController.windowClick(0, brownMushRecraft.getSlot(), 0, 0, mc.thePlayer);
				if (brownMushRecraft.getSize() == 1) {
					recraftStep++;
				}
				break;

			case 5:
				mc.playerController.windowClick(0, brownMushRecraft.getSlot(), 1, 0, mc.thePlayer);
				break;

			case 6:
				mc.playerController.windowClick(0, 4, 0, 0, mc.thePlayer);
				break;

			case 7: // msm coisa mas com pote
				mc.playerController.windowClick(0, bowlRecraft.getSlot(), 0, 0, mc.thePlayer);
				if (bowlRecraft.getSize() == 1) {
					recraftStep++;
				}
				break;

			case 8:
				mc.playerController.windowClick(0, bowlRecraft.getSlot(), 1, 0, mc.thePlayer);
				break;

			case 9:
				mc.playerController.windowClick(0, 2, 0, 0, mc.thePlayer);
				break;

			case 10: // shift na crafting pra fazer tudo de sopa
				mc.playerController.windowClick(0, 0, 0, 1, mc.thePlayer);
				break;

			case 11: // shift na crafting pra tirar os itens
				if (mc.thePlayer.inventoryContainer.getSlot(2).getStack() != null) {
					mc.playerController.windowClick(0, 2, 0, 1, mc.thePlayer);
				}
				break;

			case 12:
				if (mc.thePlayer.inventoryContainer.getSlot(4).getStack() != null) {
					mc.playerController.windowClick(0, 4, 0, 1, mc.thePlayer);
				}
				break;

			case 13:
				if (mc.thePlayer.inventoryContainer.getSlot(3).getStack() != null) {
					mc.playerController.windowClick(0, 3, 0, 1, mc.thePlayer);
				}
				recrafting = false;
				break;
			}
			recraftStep++;
			recraftTimer.reset();

		}
	}

	boolean waitForUseItem = false;
	@EventHandler
	private Listener<OnKeybindActionEvent> onMouseActionEvent = new Listener<>(e -> {
		if (waitForUseItem) {
			int useItem = mc.gameSettings.keyBindUseItem.getKeyCode();
			if (e.getKey() == useItem) {
				if (!mc.playerController.func_181040_m()) {
					waitForUseItem = false;
					if (!e.isPressed()) {
						KeyBinding.onTick(useItem);						
					}
					if (!dropBowl) {
						autoSoupStep++;
					}
				}
			}
		}
	});

	private void makeAutoSoupStep() {
		switch (autoSoupStep) {
		case 1: // toma a sopa
			// tenta clicar com o direito
			autoSoupStep++;
			waitForUseItem = true;
			break;

		case 2: // dropa o pote
			// se o direito conseguir clicar
			if (!waitForUseItem) {
				mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(
						C07PacketPlayerDigging.Action.DROP_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
				autoSoupStep++;
			}
			break;

		case 3:
			if (!waitForUseItem) {
				if (!shouldAutoSoup(false)) { // se puder continuar o loop sem voltar pro item inicial
					mc.thePlayer.inventory.currentItem = lastItem;
					autoSoupStep = 1; // seta step pro inicio
					autoSouping = false; // para loop
				}
			}
			break;
		}
	}

	// retorna false se não conseguir/nao for a hora de iniciar o loop do autosoup
	// retorna true se loop estiver iniciando/continuando
	private boolean shouldAutoSoup(boolean first) {
		if (healthToSoup > mc.thePlayer.getHealth()) { // health menor do que setado
			soupInHotbar = findItem(36, 45, Items.mushroom_stew); // acha alguma sopa na hotbar
			if (soupInHotbar != -1) { // se tiver
				if (first) { // se for primeira vez dessa seção
					autoSouping = true; // ativa loop
					lastItem = mc.thePlayer.inventory.currentItem; // seta item pra voltar depois
				}
				mc.thePlayer.inventory.currentItem = soupInHotbar - 36; // seta item na sopa
				// mc.playerController.updateController();
				autoSoupStep = 1; // step vira 1
				return true;
			}
			return false;
		}
		return false;
	}

	private int findItem(int startSlot, int endSlot, Item item) {
		for (int i = startSlot; i < endSlot; i++) {
			ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

			if (stack != null && stack.getItem() == item)
				return i;
		}
		return -1;
	}

	private int findItemRandomized(int startSlot, int endSlot, Item item) {
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

	private boolean hasSpaceHotbar() {
		for (int i = 36; i < 45; i++) {
			ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

			if (itemStack == null)
				return true;
		}
		return false;
	}

	int useItem;

	private InventoryItem findBlockHighest(int startSlot, int endSlot, Block block) {
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
		return new InventoryItem(slot, size);
	}

	private InventoryItem findItemHighest(int startSlot, int endSlot, Item item) {
		int slot = -1;
		int size = 0;

		for (int i = startSlot; i < endSlot; i++) {
			ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
			if (itemStack != null) {
				if (itemStack.getItem() == item) {
					if (itemStack.stackSize > size) {
						size = itemStack.stackSize;
						slot = i;
					}

				}
			}
		}
		return new InventoryItem(slot, size);
	}

	private void useItemPress() {
		useItem = mc.gameSettings.keyBindUseItem.getKeyCode();
		KeyBinding.onTick(useItem);
	}

	public class InventoryItem {
		private int slot, size;

		public InventoryItem(int slot, int size) {
			this.slot = slot;
			this.size = size;
		}

		public int getSlot() {
			return slot;
		}

		public int getSize() {
			return size;
		}

	}
}

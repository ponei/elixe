package elixe.modules.combat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import elixe.events.OnKeybindActionEvent;
import elixe.events.OnPacketSendEvent;
import elixe.events.OnTickEvent;
import elixe.modules.Module;
import elixe.modules.ModuleCategory;
import elixe.modules.option.ModuleArrayMultiple;
import elixe.modules.option.ModuleBoolean;
import elixe.modules.option.ModuleFloat;
import elixe.modules.option.ModuleInteger;
import elixe.utils.misc.TimerUtils;
import elixe.utils.player.InventoryItem;
import me.zero.alpine.event.EventPriority;
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
		moduleOptions.add(recraftableItemsOption);
		moduleOptions.add(recraftDelayOption);

		moduleOptions.add(needAttackButtonOption);
	}

	float healthToSoup;
	ModuleFloat healthToSoupOption = new ModuleFloat("health to soup", 12f, 1f, 20f) {
		public void valueChanged() {
			healthToSoup = (float) this.getValue();
		}
	};

	boolean dropBowl;
	ModuleBoolean dropBowlOption = new ModuleBoolean("drop bowl", false) {
		public void valueChanged() {
			dropBowl = (boolean) this.getValue();
		}
	};

	boolean refill;
	ModuleBoolean refillOption = new ModuleBoolean("refill", false) {
		public void valueChanged() {
			refill = (boolean) this.getValue();
		}
	};

	int refillDelay;
	ModuleInteger refillDelayOption = new ModuleInteger("refill delay", 100, 1, 300) {
		public void valueChanged() {
			refillDelay = (int) this.getValue();
		}
	};

	boolean recraft;
	ModuleBoolean recraftOption = new ModuleBoolean("recraft", false) {
		public void valueChanged() {
			recraft = (boolean) this.getValue();
		}
	};

	boolean[] recraftableItems;
	ModuleArrayMultiple recraftableItemsOption = new ModuleArrayMultiple("recraftable items", new boolean[] { true, false, false },
			new String[] { "mushroom", "cocoa", "cactus" }) {
		public void valueChanged() {
			recraftableItems = (boolean[]) this.getValue();
		}
	};

	int recraftDelay;
	ModuleInteger recraftDelayOption = new ModuleInteger("recraft delay", 100, 1, 300) {
		public void valueChanged() {
			recraftDelay = (int) this.getValue();
		}
	};

	boolean needAttackButton;
	ModuleBoolean needAttackButtonOption = new ModuleBoolean("require attack button", false) {
		public void valueChanged() {
			needAttackButton = (boolean) this.getValue();
		}
	};

	// crafts
	Object[][] combinationItems = { { Blocks.red_mushroom, Blocks.brown_mushroom }, { Items.dye }, { Blocks.cactus } };
	Object[][] bakedCombinations = { { Blocks.red_mushroom, Blocks.brown_mushroom }, { Blocks.brown_mushroom, Blocks.red_mushroom }, { Items.dye }, { Blocks.cactus } };

	Random r = new Random();

	boolean autoSouping = false;
	int autoSoupStep = 0;

	TimerUtils.MilisecondTimer refillTimer = new TimerUtils().new MilisecondTimer();

	TimerUtils.MilisecondTimer recraftTimer = new TimerUtils().new MilisecondTimer();

	boolean recrafting = false;
	int recraftStep = 0;

	InventoryItem bowlRecraft;
	InventoryItem[] itemsToUse = new InventoryItem[2];

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
						soupInInventory = InventoryItem.findItemRandomized(9, 36, Items.mushroom_stew, mc); // tenta pegar sopa no inv
						if (soupInInventory != -1 && InventoryItem.hasSpaceHotbar(mc)) { // tem sopa e espaço na hotbar
							mc.playerController.windowClick(0, soupInInventory, 0, 1, mc.thePlayer);
							refillTimer.reset();
							return;
						}
					}
				}

				if (recraft) {
					int trySoup = InventoryItem.findItem(9, 45, Items.mushroom_stew, mc);
					if (trySoup == -1) {

						bowlRecraft = InventoryItem.findItemHighest(9, 45, Items.bowl, 0, mc);

						// tem pote
						if (bowlRecraft.getSlot() != -1) {
							ArrayList<InventoryItem> sortedItems = new ArrayList<InventoryItem>();

							// a array 'possibleItems' contem todos as combinações de recraft aceitaveis
							// tendo alguns crafts possuindo 2 itens e outros somente 1
							// isso tem que ser dinamico
							//
							// pra isso, checamos se cada item em cada combinação existe
							// se existe, guarda na array 'itemsToSort'
							for (int i = 0; i < combinationItems.length; i++) {
								if (recraftableItems[i]) {
									for (int j = 0; j < combinationItems[i].length; j++) {
										if (combinationItems[i][j] instanceof Block) { // block
											InventoryItem block = InventoryItem.findBlockHighest(9, 45, (Block) combinationItems[i][j], mc);
											if (block.getSlot() != -1) {
												sortedItems.add(block);
											}

										} else { // item
											// eu queria fazer a lista de itens totalmente dinamico
											// problema é, cocoa beans são "corantes" mas com metadata de dano 3
											// como nao tem outro item na lista de recraft (os outros sao blocos)
											// eu nao fiz uma classe especifica pra isso, entao assumo que só pode ser
											// cocoa bean
											// no futuro, se precisar, refatoro
											InventoryItem item = InventoryItem.findItemHighest(9, 45, (Item) combinationItems[i][j], 3, mc);
											if (item.getSlot() != -1) {
												sortedItems.add(item);
											}
										}
									}
								}
							}

							Collections.sort(sortedItems);

							// em cada item filtrado
							boolean shouldStop = false;
							for (int i = 0; i < sortedItems.size(); i++) {
								if (shouldStop) {
									break;
								}

								// loop pra cada combinacao
								for (int j = 0; j < bakedCombinations.length; j++) {
									boolean canCraft = true;
									// todos os items da combinacao atual
									for (int k = 0; k < bakedCombinations[j].length; k++) {
										//combinacao len + index do loop for maior que itens
										if (bakedCombinations[j].length + i > sortedItems.size()) {
											canCraft = false;
										} else {
											//se index atual do loop de itens + index do loop da combinacao nao sao da combinacao
											if (sortedItems.get(i + k).getObject() != bakedCombinations[j][k]) {
												canCraft = false;
											}
										}
									}
									// nenhum falso
									if (canCraft) {
										shouldStop = true;
										for (int k = 0; k < bakedCombinations[j].length; k++) {
											// como ja estamos checando em ordem ja dado sort, pegamos na ordem
											itemsToUse[k] = sortedItems.get(i + k);
										}
										recraftStep = 1;
										recrafting = true;
										break;
									}
								}
							}

						}
					}
				}
			}

		} else if (mc.currentScreen == null) { // sem nenhuma gui ativa
			recrafting = false;
			if (needAttackButton) {
				if (!conditionals.isHoldingAttack()) {
					return;
				}
			}
			if (autoSouping) { // loop do autosoup ativo
				makeAutoSoupStep();
			} else { // checa se é possivel iniciar loop do autosoup
				shouldAutoSoup(true);
			}
		}
	});

	private void makeRecraftStep() {
		if (recraftTimer.hasTimePassed(recraftDelay)) {

			switch (recraftStep) {
			case 1: // pega primeiro item do recraft
				mc.playerController.windowClick(0, itemsToUse[0].getSlot(), 0, 0, mc.thePlayer);
				if (itemsToUse[0].getSize() == 1) { // se for só 1 item, pula step de botao direito
					recraftStep++;
				}
				break;

			case 2: // clica uma vez com o direito pra colocar um item no slot
				mc.playerController.windowClick(0, itemsToUse[0].getSlot(), 1, 0, mc.thePlayer);
				break;

			case 3: // coloca o item na crafting
				mc.playerController.windowClick(0, 3, 0, 0, mc.thePlayer);
				// checa se craft é 1 item só
				if (itemsToUse[1] == null) {
					recraftStep += 3; // pula pro step 7
				}
				break;

			case 4:
				mc.playerController.windowClick(0, itemsToUse[1].getSlot(), 0, 0, mc.thePlayer);
				if (itemsToUse[1].getSize() == 1) {
					recraftStep++;
				}
				break;

			case 5:
				mc.playerController.windowClick(0, itemsToUse[1].getSlot(), 1, 0, mc.thePlayer);
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
				if (mc.thePlayer.inventoryContainer.getSlot(3).getStack() != null) {
					mc.playerController.windowClick(0, 3, 0, 1, mc.thePlayer);
				}
				break;

			case 13:
				if (mc.thePlayer.inventoryContainer.getSlot(4).getStack() != null) {
					mc.playerController.windowClick(0, 4, 0, 1, mc.thePlayer);
				}
				recrafting = false;
				itemsToUse = new InventoryItem[2];
				break;
			}
			recraftStep++;
			recraftTimer.reset();

		}
	}

	boolean waitForUseItem = false;
	@EventHandler
	private Listener<OnKeybindActionEvent> onKeybindActionEvent = new Listener<>(e -> {
		if (waitForUseItem) {
			int useItem = mc.gameSettings.keyBindUseItem.getKeyCode();
			if (e.getKey() == useItem) {
				if (!mc.playerController.isHittingABlock()) {
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
				mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.DROP_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
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
			soupInHotbar = InventoryItem.findItem(36, 45, Items.mushroom_stew, mc); // acha alguma sopa na hotbar
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

	

	
}

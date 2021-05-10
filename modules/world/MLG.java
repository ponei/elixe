package elixe.modules.world;

import elixe.events.OnKeybindActionEvent;
import elixe.events.OnTickEvent;
import elixe.modules.Module;
import elixe.modules.ModuleCategory;
import elixe.modules.option.ModuleFloat;
import elixe.utils.player.InventoryItem;
import elixe.utils.player.Rotations;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class MLG extends Module {
	public MLG() {
		super("MLG", ModuleCategory.WORLD);

		moduleOptions.add(fallDistanceOption);
		moduleOptions.add(searchRangeOption);
	}

	float searchRange;
	ModuleFloat searchRangeOption = new ModuleFloat("search range", 7f, 4.5f, 15f) {
		
		public void valueChanged() {
			searchRange = (float) this.getValue();
		}
	};

	float fallDistance;
	ModuleFloat fallDistanceOption = new ModuleFloat("fall distance", 8f, 3f, 30f) {
		
		public void valueChanged() {
			fallDistance = (float) this.getValue();
		}
	};

	boolean waitForVanillaRange = false;
	boolean refreshedFall = false;
	@EventHandler
	private Listener<OnTickEvent> onTickEvent = new Listener<>(e -> {
		if (mc.theWorld == null) {
			return;
		}

		//honestamente eu poderia refatorar esse module mas to com preguiça
		
		Entity player = mc.getRenderViewEntity();
		double headY = player.getEntityBoundingBox().minY + player.getEyeHeight();

		//esperando o bloco ficar na distancia vanilla
		if (waitForVanillaRange) {
			Vec3 vechead = new Vec3(player.posX, headY, player.posZ);
			MovingObjectPosition result = rayTraceDown(player, vechead, 4.5f);
			if (result != null) {
				if (result.sideHit == EnumFacing.UP) {
					waitForVanillaRange = false;
					waitForUseItem = true;
				}
			}
		} else {
			if (player.fallDistance == 0) {
				refreshedFall = true;
			} else if (player.fallDistance > fallDistance && refreshedFall) {
				int waterBucket = InventoryItem.findItem(36, 45, Items.water_bucket, mc); // acha treco de agua
				if (waterBucket != -1 && waterBucket != mc.thePlayer.inventory.currentItem - 36) {
					mc.thePlayer.inventory.currentItem = waterBucket - 36;
				}
		
				double playerWid = player.width * 0.49999d; // nao me pergunte porque, pura experimentacao. 0.5 pega mais que necessario

				// pontas da bounding box
				Vec3 vec31 = new Vec3(player.posX - playerWid, headY, player.posZ + playerWid);
				Vec3 vec32 = new Vec3(player.posX - playerWid, headY, player.posZ - playerWid);
				Vec3 vec33 = new Vec3(player.posX + playerWid, headY, player.posZ - playerWid);
				Vec3 vec34 = new Vec3(player.posX + playerWid, headY, player.posZ + playerWid);

				Vec3[] vecs = new Vec3[] { vec31, vec32, vec33, vec34 };
				double lowest = searchRange;
				Vec3 lowestBlockVec = null;
				//achando vec de distancia menor
				for (Vec3 vec : vecs) {
					MovingObjectPosition result = rayTraceDown(player, vec, searchRange);
					if (result != null) {
						if (result.sideHit == EnumFacing.UP) {
							double distToHit = vec.distanceTo(result.hitVec);
							if (lowest > distToHit) {
								lowestBlockVec = result.hitVec;
								lowest = distToHit;
							}
						}
					}
				}

				//se tiver vec, muda mira e coloca pra esperar a distancia vanilla
				if (lowestBlockVec != null) {
					refreshedFall = false;
					
					float[] requiredAngles = Rotations.rotationUntilXYZ(lowestBlockVec.xCoord, lowestBlockVec.yCoord, lowestBlockVec.zCoord, mc.thePlayer);
					float requiredYaw = Rotations.getAngleDifference(mc.thePlayer.rotationYaw, requiredAngles[0]);
					float requiredPitch = Rotations.getAngleDifference(mc.thePlayer.rotationPitch, requiredAngles[1]);
					
					mc.thePlayer.rotationYaw -= requiredYaw;
					mc.thePlayer.rotationPitch -= requiredPitch;
					waitForVanillaRange = true;
				}
			}
		}
	});
	
	private MovingObjectPosition rayTraceDown(Entity player, Vec3 initialVec, float range) {
		Vec3 vecRange = initialVec.addVector(0, -range, 0);
		MovingObjectPosition result = player.worldObj.rayTraceBlocks(initialVec, vecRange, false, true, true);
		return result;
	}

	boolean waitForUseItem = false;
	@EventHandler
	private Listener<OnKeybindActionEvent> onMouseActionEvent = new Listener<>(e -> {
		if (waitForUseItem) {
			int useItem = mc.gameSettings.keyBindUseItem.getKeyCode();
			if (e.getKey() == useItem) {
				if (!mc.playerController.isHittingABlock()) {
					waitForUseItem = false;
					if (!e.isPressed()) {
						KeyBinding.onTick(useItem);
					}
				}
			}
		}
	});

}

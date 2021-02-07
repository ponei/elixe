package elixe.modules.combat;

import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.input.Mouse;

import elixe.events.OnPlayerAnglesEvent;
import elixe.events.OnRender3DEvent;
import elixe.events.OnTickEvent;
import elixe.modules.Module;
import elixe.modules.ModuleCategory;
import elixe.modules.option.ModuleArrayMultiple;
import elixe.modules.option.ModuleBoolean;
import elixe.modules.option.ModuleFloat;
import elixe.utils.player.EntityUtils;
import elixe.utils.player.Rotations;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;

public class AimAssist extends Module {

	public AimAssist() {
		super("Aim Assist", ModuleCategory.COMBAT);

		moduleOptions.add(allowedEntitiesOption);

		moduleOptions.add(allowedRotationsOption);
		moduleOptions.add(aimSpeedOption);
		moduleOptions.add(aimMaxSpeedOption);
		moduleOptions.add(aimFovOption);
		moduleOptions.add(aimDistanceOption);

		moduleOptions.add(allowedRandomRotationsOption);
		moduleOptions.add(aimRandomSpeedOption);
		moduleOptions.add(randomDecreaseOption);
		moduleOptions.add(randomDecreaseFovOption);

		moduleOptions.add(needVisibleOption);
		moduleOptions.add(needSprintOption);
		moduleOptions.add(needAttackButtonOption);
		moduleOptions.add(needWeaponOption);

		moduleOptions.add(ignoreNakedOption);
		moduleOptions.add(ignoreOnRightClickOption);

		moduleOptions.add(stopOnHitboxOption);
	}

	boolean[] allowedEntities;
	ModuleArrayMultiple allowedEntitiesOption = new ModuleArrayMultiple("allowed entities", new boolean[] { true, false, false, false },
			new String[] { "player", "animal", "monster", "villager" }) {
		public void valueChanged() {
			allowedEntities = (boolean[]) this.getValue();
		}
	};

	boolean[] allowedRotations;
	ModuleArrayMultiple allowedRotationsOption = new ModuleArrayMultiple("allowed rotations", new boolean[] { true, false }, new String[] { "yaw", "pitch" }) {
		public void valueChanged() {
			allowedRotations = (boolean[]) this.getValue();
		}
	};

	float aimSpeed;
	ModuleFloat aimSpeedOption = new ModuleFloat("speed factor", 0.8f, 0f, 1f) {
		public void valueChanged() {
			aimSpeed = (float) this.getValue();
		}
	};

	float aimMaxSpeed;
	ModuleFloat aimMaxSpeedOption = new ModuleFloat("max speed", 5f, 1f, 40f) {
		public void valueChanged() {
			aimMaxSpeed = (float) this.getValue();
		}
	};

	float aimFov;
	ModuleFloat aimFovOption = new ModuleFloat("aim fov", 20f, 1f, 90f) {
		public void valueChanged() {
			aimFov = (float) this.getValue();
		}
	};

	float aimDistance;
	ModuleFloat aimDistanceOption = new ModuleFloat("aim distance", 5f, 0f, 10f) {
		public void valueChanged() {
			aimDistance = (float) this.getValue();
		}
	};

	boolean[] allowedRandomRotations;
	ModuleArrayMultiple allowedRandomRotationsOption = new ModuleArrayMultiple("allowed random rotations", new boolean[] { true, false },
			new String[] { "yaw", "pitch" }) {
		public void valueChanged() {
			allowedRandomRotations = (boolean[]) this.getValue();
		}
	};

	float aimRandomSpeed;
	ModuleFloat aimRandomSpeedOption = new ModuleFloat("random speed", 5f, 1f, 40f) {
		public void valueChanged() {
			aimRandomSpeed = (float) this.getValue();
		}
	};

	boolean randomDecrease;
	ModuleBoolean randomDecreaseOption = new ModuleBoolean("random decrease", false) {
		public void valueChanged() {
			randomDecrease = (boolean) this.getValue();
		}
	};

	float randomDecreaseFov;
	ModuleFloat randomDecreaseFovOption = new ModuleFloat("random decrease fov", 5f, 1f, 90f) {
		public void valueChanged() {
			randomDecreaseFov = (float) this.getValue();
		}
	};

	boolean afterAttack;
	ModuleBoolean afterAttackOption = new ModuleBoolean("after attack", false) {
		public void valueChanged() {
			afterAttack = (boolean) this.getValue();
		}
	};

	boolean needVisible;
	ModuleBoolean needVisibleOption = new ModuleBoolean("require visibility", false) {
		public void valueChanged() {
			needVisible = (boolean) this.getValue();
		}
	};

	boolean needSprint;
	ModuleBoolean needSprintOption = new ModuleBoolean("require sprint", false) {
		public void valueChanged() {
			needSprint = (boolean) this.getValue();
		}
	};

	boolean needAttackButton;
	ModuleBoolean needAttackButtonOption = new ModuleBoolean("require attack button", false) {
		public void valueChanged() {
			needAttackButton = (boolean) this.getValue();
		}
	};

	boolean needWeapon;
	ModuleBoolean needWeaponOption = new ModuleBoolean("require weapon", false) {
		public void valueChanged() {
			needWeapon = (boolean) this.getValue();
		}
	};

	boolean ignoreNaked;
	ModuleBoolean ignoreNakedOption = new ModuleBoolean("ignore naked", false) {
		public void valueChanged() {
			ignoreNaked = (boolean) this.getValue();
		}
	};

	boolean ignoreOnRightClick;
	ModuleBoolean ignoreOnRightClickOption = new ModuleBoolean("ignore on right click", false) {
		public void valueChanged() {
			ignoreOnRightClick = (boolean) this.getValue();
		}
	};

	boolean stopOnHitbox;
	ModuleBoolean stopOnHitboxOption = new ModuleBoolean("stop on hitbox", false) {
		public void valueChanged() {
			stopOnHitbox = (boolean) this.getValue();
		}
	};

	float angleChange = 0f;
	float yawStep;
	float pitchStep;

	int angleEvent = 0;

	Random r = new Random();

	// 0 = player, 1 = animal, 2 = monster, 3 = villager

	@EventHandler
	private Listener<OnTickEvent> onTickEvent = new Listener<>(e -> {
		yawStep = 0;
		pitchStep = 0;
		angleChange = 0;

		if (!shouldAim())
			return;

		ArrayList<EntityLivingBase> filteredEntities = new ArrayList<EntityLivingBase>();
		for (Entity ent : mc.theWorld.loadedEntityList) {
			if (!ent.rendered) {
				continue;
			}
			if (ent == mc.thePlayer) {
				continue;
			}
			if (needVisible) {
				if (!mc.thePlayer.canEntityBeSeen(ent)) {
					continue;
				}
			}

			if ((ent instanceof EntityPlayer && allowedEntities[0]) || (ent instanceof EntityAnimal && allowedEntities[1])
					|| ((ent instanceof EntityMob || ent instanceof EntitySlime) && allowedEntities[2])
					|| (ent instanceof EntityVillager && allowedEntities[3])) {
				filteredEntities.add((EntityLivingBase) ent);
			}
		}

		EntityLivingBase filteredEntity = getClosestEntity(filteredEntities);
		if (filteredEntity != null) {
			if (filteredEntity.deathTime == 0) {
				if (ignoreNaked) {
					if (EntityUtils.isNaked(filteredEntity)) {
						return;
					}
				}

				if (angleEvent != 0) {
					angleChange = 1f / angleEvent;
					angleEvent = 0;
				}

				float[] requiredAngles = Rotations.rotationUntilTarget(filteredEntity, mc.thePlayer);
				float requiredYaw = Rotations.getAngleDifference(mc.thePlayer.rotationYaw, requiredAngles[0]);
				float requiredPitch = Rotations.getAngleDifference(mc.thePlayer.rotationPitch, requiredAngles[1]);

				if (aimFov >= Math.abs(requiredYaw)) {
					float randomYaw = 0f;
					if (allowedRandomRotations[0]) {
						if (r.nextBoolean()) {
							randomYaw = r.nextFloat() * aimRandomSpeed;
						} else {
							randomYaw = r.nextFloat() * -aimRandomSpeed;
						}
						if (randomDecrease && randomDecreaseFov >= requiredYaw) {
							randomYaw *= requiredYaw / randomDecreaseFov;
						}
					}

					float randomPitch = 0f;
					if (allowedRandomRotations[1]) {
						if (r.nextBoolean()) {
							randomPitch = r.nextFloat() * aimRandomSpeed;
						} else {
							randomPitch = r.nextFloat() * -aimRandomSpeed;
						}

						if (randomDecrease && randomDecreaseFov >= requiredYaw) {
							randomPitch *= requiredYaw / randomDecreaseFov;
						}
					}

					requiredYaw = allowedRotations[0] ? clampFloat(requiredYaw) : 0f;
					requiredPitch = allowedRotations[1] ? clampFloat(requiredPitch) : 0f;

					yawStep = (requiredYaw + randomYaw) * angleChange;
					pitchStep = (requiredPitch + randomPitch) * angleChange;
				}
			}
		}

	});

	@EventHandler
	private Listener<OnPlayerAnglesEvent> onPlayerAnglesEvent = new Listener<>(e -> {
		angleEvent++;

		if (angleChange > 0f) {
			e.cancel();
			setAngles(e.getYaw(), e.getPitch());
		}
	});

	private boolean shouldAim() {
		if (mc.thePlayer == null || mc.theWorld == null) {
			return false;
		}

		if (mc.currentScreen != null) {
			return false;
		}

		if (needSprint) {
			if (!conditionals.isSprinting()) {
				return false;
			}
		}

		if (stopOnHitbox) {
			if (mc.objectMouseOver != null) {
				if (mc.objectMouseOver.typeOfHit == MovingObjectType.ENTITY) {
					return false;
				}
			}
		}

		if (needAttackButton) {
			if (!conditionals.isHoldingAttack()) {
				return false;
			}
		}
		if (needWeapon) {
			if (!conditionals.isHoldingWeapon()) {
				return false;
			}
		}

		if (ignoreOnRightClick) {
			if (conditionals.isHoldingUse()) {
				return false;
			}
		}

		return true;
	}

	private float clampFloat(float f) {
		float toClamp = Math.abs(f);
		boolean reverse = 0 > f;

		if (toClamp > aimMaxSpeed) {
			toClamp = aimMaxSpeed;
		}

		return reverse ? -toClamp : toClamp;
	}

	public void setAngles(float yaw, float pitch) {
		float f = mc.thePlayer.rotationPitch;
		float f1 = mc.thePlayer.rotationYaw;
		mc.thePlayer.rotationYaw = (float) ((double) mc.thePlayer.rotationYaw + (double) yaw * 0.15D + (-yawStep * aimSpeed));
		mc.thePlayer.rotationPitch = (float) ((double) mc.thePlayer.rotationPitch - (double) pitch * 0.15D - (pitchStep * aimSpeed));
		mc.thePlayer.rotationPitch = MathHelper.clamp_float(mc.thePlayer.rotationPitch, -90.0F, 90.0F);
		mc.thePlayer.prevRotationPitch += mc.thePlayer.rotationPitch - f;
		mc.thePlayer.prevRotationYaw += mc.thePlayer.rotationYaw - f1;
	}

	private EntityLivingBase getClosestEntity(ArrayList<EntityLivingBase> filteredEntities) {
		float distance = aimDistance;
		EntityLivingBase closestEntity = null;
		for (EntityLivingBase entity : filteredEntities) {
			float toEnt = mc.thePlayer.getDistanceToEntity(entity);
			if (distance > toEnt) {
				distance = toEnt;
				closestEntity = entity;
			}
		}
		return closestEntity;
	}

}

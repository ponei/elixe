package elixe.modules.combat;

import java.util.List;
import java.util.Random;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import elixe.events.OnGetMouseOverEvent;
import elixe.modules.Module;
import elixe.modules.ModuleCategory;
import elixe.modules.option.ModuleBoolean;
import elixe.modules.option.ModuleFloat;
import elixe.utils.player.EntityUtils;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class Reach extends Module {
	public Reach() {
		super("Reach", ModuleCategory.COMBAT);

		moduleOptions.add(activationChanceOption);

		moduleOptions.add(reachMinOption);
		moduleOptions.add(reachMaxOption);

		moduleOptions.add(needSprintOption);
		moduleOptions.add(needSpeedOption);
		moduleOptions.add(needWeaponOption);

		moduleOptions.add(ignoreBlocksOption);
		moduleOptions.add(ignoreNakedOption);

		moduleOptions.add(waterCheckOption);
		moduleOptions.add(yCheckOption);
		moduleOptions.add(airCheckOption);
	}

	float activationChance;
	ModuleFloat activationChanceOption = new ModuleFloat("chance to activate", 80f, 0f, 100f) {

		public void valueChanged() {
			activationChance = (float) this.getValue();
		}
	};

	float reachMin;
	ModuleFloat reachMinOption = new ModuleFloat("reach min", 3f, 3f, 6f) {
		public void valueChanged() {
			float newReach = (float) this.getValue();

			if (reachMaxOption != null) {
				if (newReach > reachMax) {
					// fix
					reachMax = newReach;
					if (reachMaxOption.getButton() != null) {
						reachMaxOption.getButton().setValue(newReach);
					}

					reachMaxOption.setValueSilent(newReach);
				}
			}

			reachMin = newReach;
		}
	};

	float reachMax;
	ModuleFloat reachMaxOption = new ModuleFloat("reach max", 3.5f, 3f, 6f) {
		public void valueChanged() {
			float newReach = (float) this.getValue();

			if (reachMinOption != null) {
				if (reachMin > newReach) {
					// fix
					reachMin = newReach;
					if (reachMinOption.getButton() != null) {
						reachMinOption.getButton().setValue(newReach);
					}
					reachMinOption.setValueSilent(newReach);
				}
			}

			reachMax = newReach;
		}
	};

	boolean needSprint;
	ModuleBoolean needSprintOption = new ModuleBoolean("require sprint", false) {

		public void valueChanged() {
			needSprint = (boolean) this.getValue();
		}
	};

	boolean needSpeed;
	ModuleBoolean needSpeedOption = new ModuleBoolean("require speed", false) {

		public void valueChanged() {
			needSpeed = (boolean) this.getValue();
		}
	};

	boolean needWeapon;
	ModuleBoolean needWeaponOption = new ModuleBoolean("require weapon", false) {

		public void valueChanged() {
			needWeapon = (boolean) this.getValue();
		}
	};

	boolean ignoreBlocks;
	ModuleBoolean ignoreBlocksOption = new ModuleBoolean("ignore blocks", false) {

		public void valueChanged() {
			ignoreBlocks = (boolean) this.getValue();
		}
	};

	boolean ignoreNaked;
	ModuleBoolean ignoreNakedOption = new ModuleBoolean("ignore naked", false) {

		public void valueChanged() {
			ignoreNaked = (boolean) this.getValue();
		}
	};

	boolean waterCheck;
	ModuleBoolean waterCheckOption = new ModuleBoolean("water check", false) {

		public void valueChanged() {
			waterCheck = (boolean) this.getValue();
		}
	};

	boolean yCheck;
	ModuleBoolean yCheckOption = new ModuleBoolean("y check", false) {

		public void valueChanged() {
			yCheck = (boolean) this.getValue();
		}
	};

	boolean airCheck;
	ModuleBoolean airCheckOption = new ModuleBoolean("air check", false) {

		public void valueChanged() {
			airCheck = (boolean) this.getValue();
		}
	};

	Random r = new Random();
	@EventHandler
	private Listener<OnGetMouseOverEvent> onGetMouseOverEvent = new Listener<>(e -> {
		if (mc.thePlayer == null) {
			return;
		}
		if (needSprint) {
			if (!conditionals.isSprinting()) {
				return;
			}
		}

		if (needSpeed) {
			if (!conditionals.hasSpeed()) {
				return;
			}
		}

		if (needWeapon) {
			if (!conditionals.isHoldingWeapon()) {
				return;
			}
		}

		if (waterCheck) {
			if (conditionals.isInWater()) {
				return;
			}
		}

		if (airCheck) {
			if (conditionals.isOnAir()) {
				return;
			}
		}

		e.cancel();
		EntityRenderer entityRenderer = e.getEntityRenderer();
		float partialTicks = e.getPartialTicks();

		Entity entity = mc.getRenderViewEntity();

		if (entity != null && this.mc.theWorld != null) {
			mc.mcProfiler.startSection("pick");
			mc.pointedEntity = null;

			// novo reach
			float customReach = reachMin + r.nextFloat() * (reachMax - reachMin);

			double d0 = mc.playerController.getBlockReachDistance();
			if (customReach > d0) {
				d0 = customReach;
			}
			mc.objectMouseOver = entity.rayTrace(d0, partialTicks); // miss ou block, nao leva entidade em consideracao

			double d1 = d0;
			Vec3 vec3 = entity.getPositionEyes(partialTicks);
			boolean flag = false;

			if (this.mc.playerController.extendedReach()) {
				d0 = 6.0D;
				d1 = 6.0D;
			} else if (d0 >= 3.0D) { // survival = flag
				flag = true;
			}

			if (this.mc.objectMouseOver != null && !ignoreBlocks) { // limita reach se tiver bloco na frente
				d1 = this.mc.objectMouseOver.hitVec.distanceTo(vec3);
			}

			Vec3 vec31 = entity.getLook(partialTicks); // player -> look
			Vec3 vec32 = vec3.addVector(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0); // vector com reach de
																									// bloco
			entityRenderer.pointedEntity = null;
			Vec3 vec33 = null;
			float f = 1.0F;

			List<Entity> list = this.mc.theWorld.getEntitiesInAABBexcluding(entity,
					entity.getEntityBoundingBox().addCoord(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0).expand(f, f, f),
					Predicates.and(EntitySelectors.NOT_SPECTATING, new Predicate<Entity>() {

						public boolean apply(Entity p_apply_1_) {
							return p_apply_1_.canBeCollidedWith();
						}
					}));

			double d2 = d1;

			for (int j = 0; j < list.size(); ++j) {
				Entity entity1 = list.get(j);
				float f1 = entity1.getCollisionBorderSize();
				AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand(f1, f1, f1); // hitbox normalizada
				MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec32); // hitbox
																											// contem
																											// intercept
				// olhos -> vetor maximo de hit

				if (axisalignedbb.isVecInside(vec3)) { // estamos dentro do mob
					if (d2 >= 0.0D) {
						entityRenderer.pointedEntity = entity1;
						vec33 = movingobjectposition == null ? vec3 : movingobjectposition.hitVec;
						d2 = 0.0D;
					}
				} else if (movingobjectposition != null) { // intercept n�o � nulo
					double d3 = vec3.distanceTo(movingobjectposition.hitVec);

					if (d3 < d2 || d2 == 0.0D) {
						boolean flag1 = false;

						if (!flag1 && entity1 == entity.ridingEntity) {
							if (d2 == 0.0D) {
								entityRenderer.pointedEntity = entity1;
								vec33 = movingobjectposition.hitVec;
							}
						} else {
							entityRenderer.pointedEntity = entity1;
							vec33 = movingobjectposition.hitVec;
							d2 = d3;
						}
					}
				}
			}

			if (entityRenderer.pointedEntity != null) {
				if (activationChance > r.nextFloat() * 100f) {
					if (yCheck) {
						if (isEntityInDifferentY(entityRenderer.pointedEntity, entity)) {
							customReach = 3f;
						}
					}
					if (ignoreNaked) {
						if (EntityUtils.isNaked((EntityLivingBase) entityRenderer.pointedEntity)) {
							customReach = 3f;
						}
					}
				} else {
					customReach = 3f;
				}
			}

			// olhos -> posicao do hit
			// d� miss caso entidade estiver mais de 3d
			if (entityRenderer.pointedEntity != null && flag && vec3.distanceTo(vec33) > customReach) {
				entityRenderer.pointedEntity = null;
				this.mc.objectMouseOver = new MovingObjectPosition(MovingObjectPosition.MovingObjectType.MISS, vec33, (EnumFacing) null, new BlockPos(vec33));
			}

			if (entityRenderer.pointedEntity != null && (d2 < d1 || this.mc.objectMouseOver == null)) {
				this.mc.objectMouseOver = new MovingObjectPosition(entityRenderer.pointedEntity, vec33);

				if (entityRenderer.pointedEntity instanceof EntityLivingBase || entityRenderer.pointedEntity instanceof EntityItemFrame) {
					this.mc.pointedEntity = entityRenderer.pointedEntity;
				}
			}

			this.mc.mcProfiler.endSection();
		}
	});

	private boolean isEntityInDifferentY(Entity ent, Entity player) {
		double maxUp = player.posY + 0.3;
		double maxDown = player.posY - 0.3;
		if (ent.posY > maxUp || maxDown > ent.posY) {
			return true;
		} else {
			return false;
		}
	}
}

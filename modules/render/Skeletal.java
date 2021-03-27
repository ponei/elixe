package elixe.modules.render;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Quaternion;

import elixe.events.OnRender3DEvent;
import elixe.events.OnRenderEntityEvent;
import elixe.events.OnSetModelAngles;
import elixe.events.OnTickEvent;
import elixe.modules.Module;
import elixe.modules.ModuleCategory;
import elixe.modules.option.ModuleArrayMultiple;
import elixe.modules.option.ModuleBoolean;
import elixe.modules.option.ModuleColor;
import elixe.modules.option.ModuleFloat;
import elixe.modules.option.ModuleInteger;
import elixe.utils.render.Interpolation;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.biome.BiomeGenBase.Height;

public class Skeletal extends Module {

	public Skeletal() {
		super("Skeletal", ModuleCategory.RENDER);

		moduleOptions.add(allowedEntitiesOption);
		moduleOptions.add(workOnSelfOption);
		moduleOptions.add(connectBonesOption);
		moduleOptions.add(lineWidthOption);
		moduleOptions.add(lineColorOption);
	}

	boolean[] allowedEntities;
	ModuleArrayMultiple allowedEntitiesOption = new ModuleArrayMultiple("allowed entities", new boolean[] { true, false, false, false },
			new String[] { "player", "animal", "monster", "villager" }) {
		public void valueChanged() {
			allowedEntities = (boolean[]) this.getValue();
		}
	};
	
	boolean workOnSelf;
	ModuleBoolean workOnSelfOption = new ModuleBoolean("work on self", false) {
		public void valueChanged() {
			workOnSelf = (boolean) this.getValue();			
		}
	};
	
	boolean connectBones;
	int bipedLength = 5;
	ModuleBoolean connectBonesOption = new ModuleBoolean("connect bones", false) {
		public void valueChanged() {
			connectBones = (boolean) this.getValue();
			if (connectBones) {
				bipedLength = 9;
			} else {
				bipedLength = 5;
			}
		}
	};

	float lineWidth;
	ModuleInteger lineWidthOption = new ModuleInteger("line width", 1, 1, 5) {
		public void valueChanged() {
			lineWidth = (int) this.getValue();
		}
	};

	float[] lineColor;
	ModuleColor lineColorOption = new ModuleColor("line color", 255, 255, 255) {
		public void valueChanged() {
			lineColor = this.getGLRGB();
		}
	};

	Map<Entity, ModelRotations[]> modelRotations = new HashMap<Entity, ModelRotations[]>();

	// 0 = player, 1 = animal, 2 = monster, 3 = villager
	ArrayList<EntityLivingBase> filteredEntities = new ArrayList<EntityLivingBase>();
	@EventHandler
	private Listener<OnTickEvent> onTickEvent = new Listener<>(e -> {
		filteredEntities.clear();
		if (mc.theWorld == null)
			return;
		for (Entity ent : mc.theWorld.loadedEntityList) {
			if (!ent.rendered) {
				continue;
			}
			if (ent == mc.thePlayer && !workOnSelf) {
				continue;
			}

			if (ent instanceof EntityLivingBase) {
				if ((ent instanceof EntityPlayer && allowedEntities[0]) || (ent instanceof EntityAnimal && allowedEntities[1])
						|| ((ent instanceof EntityMob || ent instanceof EntitySlime) && allowedEntities[2])
						|| (ent instanceof EntityVillager && allowedEntities[3])) {
					EntityLivingBase entLiving = (EntityLivingBase) ent;
					if (entLiving.deathTime != 0) {
						continue;
					}

					filteredEntities.add(entLiving);
				}

			}

		}
	});

	private static float rad_to_deg = 57.295776F; // (180F / (float)Math.PI)
	private static float half_pi = 1.5707963F; // pi / 2
	@EventHandler
	private Listener<OnRender3DEvent> onRender3DEvent = new Listener<>(e -> {
		// aparecer atras das parede
		GlStateManager.disableDepth();
		GlStateManager.disableTexture2D();
		for (EntityLivingBase entity : filteredEntities) {
			ModelRotations[] models = modelRotations.get(entity);
			if (models == null) {
				continue;
			}

			GL11.glPushMatrix();
			// linha; cor e largura
			GL11.glLineWidth(lineWidth);
			GlStateManager.enableBlend();
			GL11.glColor4f(lineColor[0], lineColor[1], lineColor[2], lineColor[3]);

			// render correta
			double[] interpEnt = Interpolation.interpolateEntity(mc, e.getTickDelta(), entity);
			double[] interpRender = Interpolation.compensateRenderManager(interpEnt, renderManager);
			GL11.glTranslated(interpRender[0], interpRender[1], interpRender[2]);

			// rotacao do corpo
			float f = entity.prevRenderYawOffset + (entity.renderYawOffset - entity.prevRenderYawOffset) * e.getTickDelta();
			
			if (entity.isRiding() && entity.ridingEntity instanceof EntityLivingBase) {
				EntityLivingBase entityRide = (EntityLivingBase) entity.ridingEntity;
				//rotacao da entidade sendo usada
				f = entityRide.prevRenderYawOffset + (entityRide.renderYawOffset - entityRide.prevRenderYawOffset) * e.getTickDelta();
				
				float f1 = entity.prevRotationYawHead + (entity.rotationYawHead - entity.prevRotationYawHead) * e.getTickDelta();
				
				float f2 = MathHelper.wrapAngleTo180_float(f1 - f);

				if (f2 < -85.0F) {
					f2 = -85.0F;
				}

				if (f2 >= 85.0F) {
					f2 = 85.0F;
				}

				f = f1 - f2;

				if (f2 * f2 > 2500.0F) {
					f += f2 * 0.2F;
				}
			}
			
			GL11.glRotatef((-f), 0.0F, 1.0F, 0.0F);
			GL11.glScalef(-1.0F, -1.0F, 1.0F);
			GL11.glTranslatef(0.0F, -1.5078125F, 0.0F); // tava no RendererLivingEntity, nao sei porque assim exatamente

			GL11.glPushMatrix();
			if (entity.isSneaking()) {
				// sneak fode as rotacao lol
				GL11.glTranslatef(0.0F, 0.2F, 0.0F);
				GL11.glScalef(-1.0F, 1.0F, -1.0F);
			}
			// linha nos modelo
			for (ModelRotations model : models) {
				renderModel(model.scale, model.height, model.memberAngleX, model.memberAngleY, model.memberAngleZ, model.memberRotPointX, model.memberRotPointY,
						model.memberRotPointZ);
			}
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.disableBlend();
			
			GL11.glPopMatrix();
			GL11.glPopMatrix();
		}
		GlStateManager.enableDepth();
		GlStateManager.enableTexture2D();
	});

	// metodo do ModelRenderer, mesmo calculo mas desenha as linha
	private void renderModel(float scale, float height, float rotateAngleX, float rotateAngleY, float rotateAngleZ, float rotationPointX, float rotationPointY,
			float rotationPointZ) {

		if (rotateAngleX == 0.0F && rotateAngleY == 0.0F && rotateAngleZ == 0.0F) {
			if (rotationPointX == 0.0F && rotationPointY == 0.0F && rotationPointZ == 0.0F) {

				drawLine(scale, height);

			} else {
				GL11.glTranslatef(rotationPointX * scale, rotationPointY * scale, rotationPointZ * scale);

				drawLine(scale, height);

				GL11.glTranslatef(-rotationPointX * scale, -rotationPointY * scale, -rotationPointZ * scale);
			}
		} else {

			GL11.glPushMatrix();
			GL11.glTranslatef(rotationPointX * scale, rotationPointY * scale, rotationPointZ * scale);

			if (rotateAngleZ != 0.0F) {
				GL11.glRotatef(rotateAngleZ * rad_to_deg, 0.0F, 0.0F, 1.0F);
			}

			if (rotateAngleY != 0.0F) {
				GL11.glRotatef(rotateAngleY * rad_to_deg, 0.0F, 1.0F, 0.0F);
			}

			if (rotateAngleX != 0.0F) {
				GL11.glRotatef(rotateAngleX * rad_to_deg, 1.0F, 0.0F, 0.0F);
			}

			drawLine(scale, height);

			GL11.glPopMatrix();
		}

	}

	private void drawLine(float scale, float height) {
		GL11.glPushMatrix();

		GL11.glScalef(scale, scale, scale);
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex3f(0.0F, 0.0F, 0.0F);
		GL11.glVertex3f(0.0F, height, 0.0F);
		GL11.glEnd();

		GL11.glPopMatrix();
	}

	private Vec3 calculateRotation(float rotateAngleX, float rotateAngleY) {
		float f = MathHelper.cos(-rotateAngleY); //yaw
        float f1 = MathHelper.sin(-rotateAngleY);
        float f2 = -MathHelper.cos(-half_pi + rotateAngleX); //pitch
        float f3 = MathHelper.sin(-half_pi + rotateAngleX);
        Vec3 rot = new Vec3((double)(f1 * f2), (double)f3, (double)(f * f2));
		
		return rot;
	}
	
	private float[] calculateAngleAndDistance(Vec3 point, float rotationPointX, float rotationPointY, float rotationPointZ) { 
		double xDifR = point.xCoord - rotationPointX;
		double yDifR = point.yCoord - rotationPointY;
		double zDifR = point.zCoord - rotationPointZ;
		float distR = (float) Math.sqrt( Math.pow(xDifR, 2) + Math.pow(yDifR, 2) + Math.pow(zDifR, 2));
		float angleXR = (float) Math.atan2(xDifR, yDifR);
		float angleYR = (float) Math.asin(zDifR / distR);
		
		return new float[] { distR, angleYR, angleXR };
	}

	@EventHandler
	private Listener<OnSetModelAngles> onSetModelAngles = new Listener<>(e -> {
		// so funciona com biped por enquanto
		ModelBiped model = e.getModel();
		float scale = e.getScale();
		boolean sneak = e.getEntity().isSneaking();

		ModelRotations[] modelsRot = new ModelRotations[bipedLength];

		if (model.isRiding) {
			// perna direita
			modelsRot[0] = new ModelRotations(scale, model.bipedRightLeg.height, -model.bipedLeftLeg.rotateAngleX, model.bipedLeftLeg.rotateAngleY,
					model.bipedLeftLeg.rotateAngleZ, model.bipedRightLeg.rotationPointX, model.bipedRightLeg.rotationPointY, model.bipedRightLeg.rotationPointZ);
			// perna esquerda
			modelsRot[1] = new ModelRotations(scale, model.bipedLeftLeg.height, -model.bipedRightLeg.rotateAngleX, model.bipedRightLeg.rotateAngleY,
					model.bipedRightLeg.rotateAngleZ, model.bipedLeftLeg.rotationPointX, model.bipedLeftLeg.rotationPointY, model.bipedLeftLeg.rotationPointZ);
		} else {
			// perna direita
			modelsRot[0] = new ModelRotations(scale, model.bipedRightLeg.height, model.bipedRightLeg.rotateAngleX, model.bipedRightLeg.rotateAngleY,
					model.bipedRightLeg.rotateAngleZ, model.bipedRightLeg.rotationPointX, model.bipedRightLeg.rotationPointY, model.bipedRightLeg.rotationPointZ);
			// perna esquerda
			modelsRot[1] = new ModelRotations(scale, model.bipedLeftLeg.height, model.bipedLeftLeg.rotateAngleX, model.bipedLeftLeg.rotateAngleY,
					model.bipedLeftLeg.rotateAngleZ, model.bipedLeftLeg.rotationPointX, model.bipedLeftLeg.rotationPointY, model.bipedLeftLeg.rotationPointZ);
		}
			

		if (sneak) {
			// corpo
			modelsRot[2] = new ModelRotations(scale, model.bipedBody.height, model.bipedBody.rotateAngleX, model.bipedBody.rotateAngleY,
					model.bipedBody.rotateAngleZ, model.bipedBody.rotationPointX, model.bipedBody.rotationPointY, model.bipedBody.rotationPointZ);
			// braço direito
			modelsRot[3] = new ModelRotations(scale, model.bipedRightArm.height - 2, model.bipedRightArm.rotateAngleX, model.bipedRightArm.rotateAngleY,
					model.bipedRightArm.rotateAngleZ, model.bipedRightArm.rotationPointX, model.bipedRightArm.rotationPointY,
					model.bipedRightArm.rotationPointZ);
			// braço esquerdo
			modelsRot[4] = new ModelRotations(scale, model.bipedLeftArm.height - 2, model.bipedLeftArm.rotateAngleX, model.bipedLeftArm.rotateAngleY,
					model.bipedLeftArm.rotateAngleZ, model.bipedLeftArm.rotationPointX, model.bipedLeftArm.rotationPointY, model.bipedLeftArm.rotationPointZ);
			
		} else {
			// corpo
			modelsRot[2] = new ModelRotations(scale, model.bipedBody.height, model.bipedBody.rotateAngleX, model.bipedBody.rotateAngleY,
					-model.bipedBody.rotateAngleZ, model.bipedBody.rotationPointX, model.bipedBody.rotationPointY, model.bipedBody.rotationPointZ);
			// braço direito
			modelsRot[3] = new ModelRotations(scale, model.bipedRightArm.height - 2, -model.bipedLeftArm.rotateAngleX, model.bipedLeftArm.rotateAngleY,
					-model.bipedLeftArm.rotateAngleZ, model.bipedRightArm.rotationPointX, model.bipedRightArm.rotationPointY,
					model.bipedRightArm.rotationPointZ);
			// braço esquerdo
			modelsRot[4] = new ModelRotations(scale, model.bipedLeftArm.height - 2, -model.bipedRightArm.rotateAngleX, model.bipedRightArm.rotateAngleY,
					-model.bipedRightArm.rotateAngleZ, model.bipedLeftArm.rotationPointX, model.bipedLeftArm.rotationPointY, model.bipedLeftArm.rotationPointZ);
			
		}
		
		if (connectBones) {
			//começo do corpo
			Vec3 vecBody = new Vec3(model.bipedBody.rotationPointX, model.bipedBody.rotationPointY, model.bipedBody.rotationPointZ);
			//vetor de direcao com os angulo. nao considera z porque fodase
	        Vec3 vecDirectional = calculateRotation(model.bipedBody.rotateAngleX, model.bipedBody.rotateAngleY);
		
	        //final do corpo
			Vec3 vecBodyEnd = vecBody.addVector(vecDirectional.xCoord * model.bipedBody.height, 
					-vecDirectional.yCoord * model.bipedBody.height, 
					-vecDirectional.zCoord * model.bipedBody.height);
			
			//começo do corpo + y dos braço
			float bodyArmDif = model.bipedLeftArm.rotationPointY - model.bipedBody.rotationPointY;
			Vec3 vecBodyArms = vecBody.addVector(vecDirectional.xCoord * bodyArmDif, 
					-vecDirectional.yCoord * bodyArmDif, 
					-vecDirectional.zCoord * bodyArmDif);
			 
			//perna direita -> final corpo
			float[] rightLeg = calculateAngleAndDistance(vecBodyEnd, model.bipedRightLeg.rotationPointX, model.bipedRightLeg.rotationPointY, model.bipedRightLeg.rotationPointZ);				
			modelsRot[5] = new ModelRotations(scale, rightLeg[0], rightLeg[1], model.bipedRightLeg.rotateAngleY,
					-rightLeg[2], model.bipedRightLeg.rotationPointX, model.bipedRightLeg.rotationPointY, model.bipedRightLeg.rotationPointZ);
			//perna esquerda -> final corpo
			float[] leftLeg = calculateAngleAndDistance(vecBodyEnd, model.bipedLeftLeg.rotationPointX, model.bipedLeftLeg.rotationPointY, model.bipedLeftLeg.rotationPointZ);				
			modelsRot[6] = new ModelRotations(scale, leftLeg[0], leftLeg[1], model.bipedLeftLeg.rotateAngleY,
					-leftLeg[2], model.bipedLeftLeg.rotationPointX, model.bipedLeftLeg.rotationPointY, model.bipedLeftLeg.rotationPointZ);
			//braço esquerdo -> corpo
			float[] leftArm = calculateAngleAndDistance(vecBodyArms, model.bipedLeftArm.rotationPointX, model.bipedLeftArm.rotationPointY, model.bipedLeftArm.rotationPointZ);				
			modelsRot[7] = new ModelRotations(scale, leftArm[0], leftArm[1], 0f,
					-leftArm[2], model.bipedLeftArm.rotationPointX, model.bipedLeftArm.rotationPointY, model.bipedLeftArm.rotationPointZ);
			//braço direito -> corpo
			float[] rightArm = calculateAngleAndDistance(vecBodyArms, model.bipedRightArm.rotationPointX, model.bipedRightArm.rotationPointY, model.bipedRightArm.rotationPointZ);				
			modelsRot[8] = new ModelRotations(scale, rightArm[0], rightArm[1], 0f,
					-rightArm[2], model.bipedRightArm.rotationPointX, model.bipedRightArm.rotationPointY, model.bipedRightArm.rotationPointZ);
		}

		modelRotations.put(e.getEntity(), modelsRot);
	});

	private class ModelRotations {
		float scale, height;
		float memberAngleX, memberAngleY, memberAngleZ;
		float memberRotPointX, memberRotPointY, memberRotPointZ;
		

		public ModelRotations(float scale, float height, float legAngleX, float legAngleY, float legAngleZ, float legRotationX, float legRotationY,
				float legRotationZ) {
			super();
			this.scale = scale;
			this.height = height;
			this.memberAngleX = legAngleX;
			this.memberAngleY = legAngleY;
			this.memberAngleZ = legAngleZ;
			this.memberRotPointX = legRotationX;
			this.memberRotPointY = legRotationY;
			this.memberRotPointZ = legRotationZ;
		}

	}
}

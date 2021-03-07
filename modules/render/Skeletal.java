package elixe.modules.render;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import elixe.events.OnRender3DEvent;
import elixe.events.OnRenderEntityEvent;
import elixe.events.OnSetModelAngles;
import elixe.events.OnTickEvent;
import elixe.modules.Module;
import elixe.modules.ModuleCategory;
import elixe.modules.option.ModuleArrayMultiple;
import elixe.modules.option.ModuleColor;
import elixe.modules.option.ModuleFloat;
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
import net.minecraft.util.Vec3;

public class Skeletal extends Module {

	public Skeletal() {
		super("Skeletal", ModuleCategory.RENDER);

		moduleOptions.add(allowedEntitiesOption);
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

	float lineWidth;
	ModuleFloat lineWidthOption = new ModuleFloat("line width", 1f, 0f, 5f) {
		public void valueChanged() {
			lineWidth = (float) this.getValue();
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
			GL11.glColor4f(lineColor[0], lineColor[1], lineColor[2], 1.0F);

			// render correta
			double[] interpEnt = Interpolation.interpolateEntity(mc, e.getTickDelta(), entity);
			double[] interpRender = Interpolation.compensateRenderManager(interpEnt, renderManager);
			GL11.glTranslated(interpRender[0], interpRender[1], interpRender[2]);

			// rotacao do corpo
			float bodyYawOffset = entity.prevRenderYawOffset + (entity.renderYawOffset - entity.prevRenderYawOffset) * e.getTickDelta();
			GL11.glRotatef((-bodyYawOffset), 0.0F, 1.0F, 0.0F);
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

	@EventHandler
	private Listener<OnSetModelAngles> onSetModelAngles = new Listener<>(e -> {
		//so funciona com biped por enquanto
		ModelBiped model = e.getModel();
		boolean sneak = e.getEntity().isSneaking();
		modelRotations.put(e.getEntity(), new ModelRotations[] {
				// perna direita
				new ModelRotations(e.getScale(), model.bipedRightLeg.height, model.bipedRightLeg.rotateAngleX, model.bipedRightLeg.rotateAngleY,
						model.bipedRightLeg.rotateAngleZ, model.bipedRightLeg.rotationPointX, model.bipedRightLeg.rotationPointY,
						model.bipedRightLeg.rotationPointZ),
				// perna esquerda
				new ModelRotations(e.getScale(), model.bipedLeftLeg.height, model.bipedLeftLeg.rotateAngleX, model.bipedLeftLeg.rotateAngleY,
						model.bipedLeftLeg.rotateAngleZ, model.bipedLeftLeg.rotationPointX, model.bipedLeftLeg.rotationPointY,
						model.bipedLeftLeg.rotationPointZ),
				// sneak fode as rotacoes dos braços (??)
				// braço direito
				(sneak ? new ModelRotations(e.getScale(), model.bipedRightArm.height - 1, model.bipedRightArm.rotateAngleX, model.bipedRightArm.rotateAngleY,
						model.bipedRightArm.rotateAngleZ, model.bipedRightArm.rotationPointX, model.bipedRightArm.rotationPointY,
						model.bipedRightArm.rotationPointZ)
						: new ModelRotations(e.getScale(), model.bipedRightArm.height - 1, -model.bipedLeftArm.rotateAngleX, model.bipedLeftArm.rotateAngleY,
								-model.bipedLeftArm.rotateAngleZ, model.bipedRightArm.rotationPointX, model.bipedRightArm.rotationPointY,
								model.bipedRightArm.rotationPointZ)),
				// braço esquerdo
				(sneak ? new ModelRotations(e.getScale(), model.bipedLeftArm.height - 1, model.bipedLeftArm.rotateAngleX, model.bipedLeftArm.rotateAngleY,
						model.bipedLeftArm.rotateAngleZ, model.bipedLeftArm.rotationPointX, model.bipedLeftArm.rotationPointY,
						model.bipedLeftArm.rotationPointZ)
						: new ModelRotations(e.getScale(), model.bipedLeftArm.height - 1, -model.bipedRightArm.rotateAngleX, model.bipedRightArm.rotateAngleY,
								-model.bipedRightArm.rotateAngleZ, model.bipedLeftArm.rotationPointX, model.bipedLeftArm.rotationPointY,
								model.bipedLeftArm.rotationPointZ)),
				// corpo
				new ModelRotations(e.getScale(), model.bipedBody.height, model.bipedBody.rotateAngleX, model.bipedBody.rotateAngleY,
						model.bipedBody.rotateAngleZ, model.bipedBody.rotationPointX, model.bipedBody.rotationPointY, model.bipedBody.rotationPointZ) });
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

package elixe.modules.render;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import elixe.Elixe;
import elixe.events.OnKeyEvent;
import elixe.events.OnRender3DEvent;
import elixe.events.OnRenderNameEvent;
import elixe.events.OnTickEvent;
import elixe.modules.Module;
import elixe.modules.ModuleCategory;
import elixe.modules.ModuleOption;
import elixe.modules.option.ModuleArray;
import elixe.modules.option.ModuleArrayMultiple;
import elixe.modules.option.ModuleBoolean;
import elixe.modules.option.ModuleColor;
import elixe.modules.option.ModuleFloat;
import elixe.modules.option.ModuleInteger;
import elixe.modules.option.ModuleKey;
import elixe.utils.render.ESPSetup;
import elixe.utils.render.WorldToScreen;
import joptsimple.HelpFormatter;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listenable;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.culling.ClippingHelperImpl;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.src.Config;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Timer;
import net.optifine.shaders.Shaders;

public class ESP extends Module {
	ESPSetup util = new ESPSetup();

	public ESP() {
		super("ESP", ModuleCategory.RENDER);
		moduleOptions.add(allowedEntitiesOption);

		moduleOptions.add(boxOption);
		moduleOptions.add(boxColorOption);
		moduleOptions.add(boxStyleOption);

		moduleOptions.add(healthOption);
		moduleOptions.add(healthColorOption);
		moduleOptions.add(healthLevelColorOption);
		moduleOptions.add(healthLocationOption);
		moduleOptions.add(healthLinesOption);
		
		moduleOptions.add(armorOption);
		moduleOptions.add(armorLocationOption);
		moduleOptions.add(armorColorOption);

		moduleOptions.add(nameOption);
		moduleOptions.add(nameLocationOption);

		moduleOptions.add(itemNameOption);
		moduleOptions.add(itemNameLocationOption);

		moduleOptions.add(itemIconOption);
		moduleOptions.add(itemIconLocationOption);
		moduleOptions.add(rotateWeaponIconOption);
	}

	boolean[] allowedEntities;
	ModuleArrayMultiple allowedEntitiesOption = new ModuleArrayMultiple("allowed entities",
			new boolean[] { true, false, false, false }, new String[] { "player", "animal", "monster", "villager" }) {
		public void valueChanged() {
			allowedEntities = (boolean[]) this.getValue();
		}
	};

	boolean drawBox;
	ModuleBoolean boxOption = new ModuleBoolean("draw box", false) {
		public void valueChanged() {
			drawBox = (boolean) this.getValue();
		}
	};

	float[] boxColor;
	ModuleColor boxColorOption = new ModuleColor("box color", 255, 255, 255) {
		public void valueChanged() {
			boxColor = this.getGLRGB();
		}
	};

	int boxStyle;
	ModuleArray boxStyleOption = new ModuleArray("box style", 0, new String[] { "closed", "up and down" }) {
		public void valueChanged() {
			boxStyle = (int) this.getValue();
		}
	};

	boolean drawHealth;
	ModuleBoolean healthOption = new ModuleBoolean("draw health", false) {
		public void valueChanged() {
			drawHealth = (boolean) this.getValue();
		}
	};

	int healthLocation;
	ModuleArray healthLocationOption = new ModuleArray("health location", 0,
			new String[] { "left", "right", "up", "down" }) {
		public void valueChanged() {
			healthLocation = (int) this.getValue();
		}
	};

	int healthLines;
	ModuleInteger healthLinesOption = new ModuleInteger("health lines", 3, 0, 8) {
		public void valueChanged() {
			healthLines = (int) this.getValue();
		}
	};

	float[] healthColor;
	ModuleColor healthColorOption = new ModuleColor("health color", 255, 255, 255) {
		public void valueChanged() {
			healthColor = this.getGLRGB();
		}
	};
	
	boolean healthLevelColor;
	ModuleBoolean healthLevelColorOption = new ModuleBoolean("health level color", false) {
		public void valueChanged() {
			healthLevelColor = (boolean) this.getValue();
		}
	};

	boolean drawArmor;
	ModuleBoolean armorOption = new ModuleBoolean("draw armor", false) {
		public void valueChanged() {
			drawArmor = (boolean) this.getValue();
		}
	};

	int armorLocation;
	ModuleArray armorLocationOption = new ModuleArray("armor location", 0,
			new String[] { "left", "right", "up", "down" }) {
		public void valueChanged() {
			armorLocation = (int) this.getValue();
		}
	};

	boolean armorColor;
	ModuleBoolean armorColorOption = new ModuleBoolean("armor color", false) {
		public void valueChanged() {
			armorColor = (boolean) this.getValue();
		}
	};

	boolean drawName;
	ModuleBoolean nameOption = new ModuleBoolean("draw name", false) {
		public void valueChanged() {
			drawName = (boolean) this.getValue();
		}
	};

	int nameLocation;
	ModuleArray nameLocationOption = new ModuleArray("name location", 0, new String[] { "up", "down" }) {
		public void valueChanged() {
			nameLocation = (int) this.getValue();
		}
	};

	boolean drawItemName;
	ModuleBoolean itemNameOption = new ModuleBoolean("draw item name", false) {
		public void valueChanged() {
			drawItemName = (boolean) this.getValue();
		}
	};

	int itemNameLocation;
	ModuleArray itemNameLocationOption = new ModuleArray("item name location", 0, new String[] { "up", "down" }) {
		public void valueChanged() {
			itemNameLocation = (int) this.getValue();
		}
	};

	boolean drawItemIcon;
	ModuleBoolean itemIconOption = new ModuleBoolean("draw item icon", false) {
		public void valueChanged() {
			drawItemIcon = (boolean) this.getValue();
		}
	};

	int itemIconLocation;
	ModuleArray itemIconLocationOption = new ModuleArray("item icon location", 0, new String[] { "up", "down" }) {
		public void valueChanged() {
			itemIconLocation = (int) this.getValue();
		}
	};

	boolean rotateWeaponIcon;
	ModuleBoolean rotateWeaponIconOption = new ModuleBoolean("rotate weapon icons", false) {
		public void valueChanged() {
			rotateWeaponIcon = (boolean) this.getValue();
		}
	};

	@EventHandler
	private Listener<OnRenderNameEvent> onRenderNameEvent = new Listener<>(e -> {
		e.cancel();
	});

	// 0 = player, 1 = animal, 2 = monster, 3 = villager
	ArrayList<Entity> filteredEntities = new ArrayList<Entity>();
	@EventHandler
	private Listener<OnTickEvent> onTickEvent = new Listener<>(e -> {
		filteredEntities.clear();
		if (mc.theWorld == null)
			return;
		for (Entity ent : mc.theWorld.loadedEntityList) {
			if (!ent.rendered) {
				continue;
			}
			if (ent == mc.thePlayer) {
				continue;
			}
			if ((ent instanceof EntityPlayer && allowedEntities[0])
					|| (ent instanceof EntityAnimal && allowedEntities[1])
					|| ((ent instanceof EntityMob || ent instanceof EntitySlime) && allowedEntities[2])
					|| (ent instanceof EntityVillager && allowedEntities[3])) {
				filteredEntities.add(ent);
				continue;
			}

		}
	});

	Matrix4f mvMatrix, projectionMatrix;
	int spacingYUp = 0, spacingYDown = 0, spacingXLeft = 0, spacingXRight = 0;
	int espWid = 0;
	@EventHandler
	private Listener<OnRender3DEvent> onRender3DEvent = new Listener<>(e -> {
		mvMatrix = WorldToScreen.getMatrix(GL11.GL_MODELVIEW_MATRIX);
		projectionMatrix = WorldToScreen.getMatrix(GL11.GL_PROJECTION_MATRIX);

		util.setup2DStart();

		float tick = e.getTickDelta();
		for (Entity ent : filteredEntities) {
			EntityLivingBase entLiving = (EntityLivingBase) ent;

			float[] points = calculatePoints(ent, tick);
			float minX = points[0], minY = points[1], maxX = points[2], maxY = points[3];
			if (((minX > 0 && mc.displayWidth > minX) || (maxX > 0 && mc.displayWidth > maxX))
					&& ((minY > 0 && mc.displayHeight > minY) || (maxY > 0 && mc.displayHeight > maxY))) {
				// reset math utils
				spacingYUp = 0;
				spacingYDown = 0;
				spacingXLeft = 0;
				spacingXRight = 0;
				espWid = (int) ((maxX - minX) / 2);

				if (drawBox) {
					drawBox(minX, maxX, minY, maxY);
				}

				if (drawHealth) {
					drawHealth(minX, maxX, minY, maxY, entLiving);
				}

				if (drawArmor) {
					drawArmor(minX, maxX, minY, maxY, entLiving);
				}

				if (drawName) {
					drawName(minX, maxX, minY, maxY, entLiving);
				}

				if (drawItemName) {
					drawItemName(minX, maxX, minY, maxY, entLiving);
				}

				if (drawItemIcon) {
					drawItemIcon(minX, maxX, minY, maxY, entLiving);
				}
			}
		}

		util.setup2DEnd();
	});

	private void drawLinePoints(float[][] points) {
		GL11.glBegin(GL11.GL_LINES);
		for (float[] p : points) {
			GL11.glVertex2f(p[0], p[1]);
			GL11.glVertex2f(p[2], p[3]);
		}
		GL11.glEnd();
	}

	private void drawBox(float minX, float maxX, float minY, float maxY) {
		minX++;
		maxX--;
		minY++;
		maxY--;

		GL11.glColor3f(0f, 0f, 0f);
		GL11.glLineWidth(3f);
		switch (boxStyle) {
		case 0: //closed
			drawLinePoints(new float[][] { { minX, minY - 1, minX, maxY + 1 }, { minX - 1, maxY, maxX + 1, maxY },
				{ maxX, maxY + 1, maxX, minY - 1 }, { maxX + 1, minY, minX - 1, minY } });
			
			GL11.glColor3f(boxColor[0], boxColor[1], boxColor[2]);
			GL11.glLineWidth(1f);

			drawLinePoints(new float[][] { { minX, minY, minX, maxY }, { minX, maxY, maxX, maxY },
					{ maxX, maxY, maxX, minY }, { maxX, minY, minX, minY } });
			break;

		case 1: //up and down
			drawLinePoints(new float[][] { { minX, minY + 6, minX, minY - 1 }, { minX - 1, minY, maxX + 1, minY },
				{ maxX, minY - 1, maxX, minY + 6 }, { minX, maxY - 6, minX, maxY + 1 }, { minX - 1, maxY, maxX + 1, maxY },
				{ maxX, maxY + 1, maxX, maxY - 6 } });
			
			GL11.glColor3f(boxColor[0], boxColor[1], boxColor[2]);
			GL11.glLineWidth(1f);

			drawLinePoints(new float[][] { { minX, minY + 5, minX, minY }, { minX, minY, maxX, minY },
				{ maxX, minY, maxX, minY + 5 }, { minX, maxY - 5, minX, maxY }, { minX, maxY, maxX, maxY },
				{ maxX, maxY, maxX, maxY - 5 } });
			break;
		}
	}

	private void getHealthColor(float health, float max) {
		if (healthLevelColor) {
			float perc = health / max;
			if (0.33f > perc) {
				GL11.glColor3f(0.95f, 0f, 0f); // vermleho
			} else if (0.66f > perc) {
				GL11.glColor3f(0.96f, 0.8f, 0); // amarelo
			} else if (1f >= perc) {
				GL11.glColor3f(0.26f, 0.88f, 0f); // verde
			} else {
				GL11.glColor3f(1f, 1f, 0f); // amarelo forte
			}
		} else {
			GL11.glColor3f(healthColor[0], healthColor[1], healthColor[2]);
		}
	}

	private void drawHealth(float minX, float maxX, float minY, float maxY, EntityLivingBase entLiving) {

		float healthHeight = 0f;
		float base = 0f;
		float h = entLiving.getHealth();
		float maxH = entLiving.getMaxHealth();

		switch (healthLocation) {
		case 0: // left
			base = minX - 5;

			healthHeight = h > maxH ? maxY - minY - 2 : ((h * (maxY - minY - 2)) / maxH);

			GL11.glColor3f(0f, 0f, 0f);
			GL11.glLineWidth(3f);
			GL11.glBegin(GL11.GL_LINES);

			GL11.glVertex2f(base, minY);
			GL11.glVertex2f(base, maxY);

			GL11.glEnd();

			getHealthColor(h, maxH);
			GL11.glLineWidth(1f);
			GL11.glBegin(GL11.GL_LINES);

			GL11.glVertex2f(base, maxY - healthHeight - 1);
			GL11.glVertex2f(base, maxY - 1);

			GL11.glEnd();
			if (healthLines > 0) {
				GL11.glColor3f(0f, 0f, 0f);
				float linhasDiv = ((maxY - minY) / (healthLines + 1));
				if (linhasDiv > 3f) {
					GL11.glBegin(GL11.GL_POINTS);
					for (int i = 1; i <= healthLines; i++) {
						GL11.glVertex2f(base, minY + (i * linhasDiv));
					}
					GL11.glEnd();
				}
			}

			spacingXLeft += 4;
			break;

		case 1: // right
			base = maxX + 5;

			healthHeight = h > maxH ? maxY - minY - 2 : ((h * (maxY - minY - 2)) / maxH);
			GL11.glColor3f(0f, 0f, 0f);
			GL11.glLineWidth(3f);
			GL11.glBegin(GL11.GL_LINES);

			GL11.glVertex2f(base, minY);
			GL11.glVertex2f(base, maxY);

			GL11.glEnd();

			getHealthColor(h, maxH);
			GL11.glLineWidth(1f);
			GL11.glBegin(GL11.GL_LINES);

			GL11.glVertex2f(base, maxY - healthHeight - 1);
			GL11.glVertex2f(base, maxY - 1);

			GL11.glEnd();
			if (healthLines > 0) {
				GL11.glColor3f(0f, 0f, 0f);
				float linhasDiv = ((maxY - minY) / (healthLines + 1));
				if (linhasDiv > 3f) {
					GL11.glBegin(GL11.GL_POINTS);
					for (int i = 1; i <= healthLines; i++) {
						GL11.glVertex2f(base, minY + (i * linhasDiv));
					}
					GL11.glEnd();
				}
			}
			spacingXRight += 4;
			break;
		case 2: // up
			base = minY - 5;
			healthHeight = h > maxH ? maxX - minX - 2 : ((h * (maxX - minX - 2)) / maxH);
			GL11.glColor3f(0f, 0f, 0f);
			GL11.glLineWidth(3f);
			GL11.glBegin(GL11.GL_LINES);

			GL11.glVertex2f(minX, base);
			GL11.glVertex2f(maxX, base);

			GL11.glEnd();

			getHealthColor(h, maxH);
			GL11.glLineWidth(1f);
			GL11.glBegin(GL11.GL_LINES);

			GL11.glVertex2f(maxX - healthHeight - 1, base);
			GL11.glVertex2f(maxX - 1, base);

			GL11.glEnd();
			if (healthLines > 0) {
				GL11.glColor3f(0f, 0f, 0f);
				float linhasDiv = ((maxX - minX) / (healthLines + 1));
				if (linhasDiv > 3f) {
					GL11.glBegin(GL11.GL_POINTS);
					for (int i = 1; i <= healthLines; i++) {
						GL11.glVertex2f(minX + (i * linhasDiv), base);
					}
					GL11.glEnd();
				}
			}
			spacingYUp += 5;
			break;
		case 3: // down
			base = maxY + 5;
			healthHeight = h > maxH ? maxX - minX - 2 : ((h * (maxX - minX - 2)) / maxH);
			GL11.glColor3f(0f, 0f, 0f);
			GL11.glLineWidth(3f);
			GL11.glBegin(GL11.GL_LINES);

			GL11.glVertex2f(minX, base);
			GL11.glVertex2f(maxX, base);

			GL11.glEnd();

			getHealthColor(h, maxH);
			GL11.glLineWidth(1f);
			GL11.glBegin(GL11.GL_LINES);

			GL11.glVertex2f(maxX - healthHeight - 1, base);
			GL11.glVertex2f(maxX - 1, base);

			GL11.glEnd();
			if (healthLines > 0) {
				GL11.glColor3f(0f, 0f, 0f);
				float linhasDiv = ((maxX - minX) / (healthLines + 1));
				if (linhasDiv > 3f) {
					GL11.glBegin(GL11.GL_POINTS);
					for (int i = 1; i <= healthLines; i++) {
						GL11.glVertex2f(minX + (i * linhasDiv), base);
					}
					GL11.glEnd();
				}
			}
			spacingYDown += 5;
			break;
		}
	}

	private void getArmorColor(ArmorMaterial material) {
		if (armorColor) {
			switch (material) {
			case DIAMOND:
				GL11.glColor3f(0.19f, 0.89f, 0.76f);
				break;
			case IRON:
				GL11.glColor3f(0.7f, 0.7f, 0.7f);
				break;
			case CHAIN:
				GL11.glColor3f(0.39f, 0.39f, 0.39f);
				break;
			case GOLD:
				GL11.glColor3f(0.84f, 0.84f, 0.24f);
				break;
			case LEATHER:
				GL11.glColor3f(0.59f, 0.36f, 0.23f);
				break;
			}
		} else {
			GL11.glColor3f(1f, 1f, 1f);
		}
	}

	private void drawArmor(float minX, float maxX, float minY, float maxY, EntityLivingBase entLiving) {
		float base = 0f, linhasDiv = 0f;
		boolean hasArmor = false;

		switch (armorLocation) {
		case 0: // left
			linhasDiv = ((maxY - minY - 2) / 4f);
			base = minX - 5 - spacingXLeft;

			for (int i = 1; 4 >= i; i++) {
				ItemStack armor = entLiving.getEquipmentInSlot(i);
				if (armor != null) {
					if (armor.getItem() instanceof ItemArmor) {
						if (!hasArmor) {
							GL11.glColor3f(0f, 0f, 0f);
							GL11.glLineWidth(3f);
							GL11.glBegin(GL11.GL_LINES);

							GL11.glVertex2f(base, minY);
							GL11.glVertex2f(base, maxY);

							GL11.glEnd();

							GL11.glLineWidth(1f);
						}
						hasArmor = true;
						ItemArmor entArmor = (ItemArmor) armor.getItem();

						int armorType = entArmor.armorType;
						float minYArmor = minY + (linhasDiv * (armorType + 1));

						ArmorMaterial material = entArmor.getArmorMaterial();
						float armorDamageReduction = armorColor ? linhasDiv
								: (linhasDiv * material.getDamageReductionAmount(armorType))
										/ ArmorMaterial.DIAMOND.getDamageReductionAmount(armorType);

						getArmorColor(material);
						GL11.glBegin(GL11.GL_LINES);
						GL11.glVertex2f(base, minYArmor - armorDamageReduction + 1);
						GL11.glVertex2f(base, minYArmor + 1);
						GL11.glEnd();

					}
				}
			}
			if (hasArmor) {
				GL11.glColor3f(0f, 0f, 0f);
				GL11.glBegin(GL11.GL_POINTS);
				for (float i = 1; i < 4; i++) {
					GL11.glVertex2f(base, minY + 1 + (linhasDiv * i));
				}
				GL11.glEnd();
				spacingXLeft += 6;
			}

			break;

		case 1: // right
			linhasDiv = ((maxY - minY - 2) / 4f);

			base = maxX + 5 + spacingXRight;

			for (int i = 1; 4 >= i; i++) {
				ItemStack armor = entLiving.getEquipmentInSlot(i);
				if (armor != null) {
					if (armor.getItem() instanceof ItemArmor) {
						if (!hasArmor) {
							GL11.glColor3f(0f, 0f, 0f);
							GL11.glLineWidth(3f);
							GL11.glBegin(GL11.GL_LINES);

							GL11.glVertex2f(base, minY);
							GL11.glVertex2f(base, maxY);

							GL11.glEnd();

							GL11.glLineWidth(1f);
						}
						hasArmor = true;
						ItemArmor entArmor = (ItemArmor) armor.getItem();

						int armorType = entArmor.armorType;
						float minYArmor = minY + (linhasDiv * (armorType + 1));

						ArmorMaterial material = entArmor.getArmorMaterial();
						float armorDamageReduction = armorColor ? linhasDiv
								: (linhasDiv * material.getDamageReductionAmount(armorType))
										/ ArmorMaterial.DIAMOND.getDamageReductionAmount(armorType);

						getArmorColor(material);
						GL11.glBegin(GL11.GL_LINES);
						GL11.glVertex2f(base, minYArmor - armorDamageReduction + 1);
						GL11.glVertex2f(base, minYArmor + 1);
						GL11.glEnd();
					}
				}
			}
			if (hasArmor) {
				GL11.glColor3f(0f, 0f, 0f);
				GL11.glBegin(GL11.GL_POINTS);
				for (float i = 1; i < 4; i++) {
					GL11.glVertex2f(base, minY + 1 + (linhasDiv * i));
				}
				GL11.glEnd();
				spacingXRight += 6;
			}

			break;
		case 2: // up
			linhasDiv = ((maxX - minX - 2) / 4f);

			base = minY - 5 - spacingYUp;

			for (int i = 1; 4 >= i; i++) {
				ItemStack armor = entLiving.getEquipmentInSlot(i);
				if (armor != null) {
					if (armor.getItem() instanceof ItemArmor) {
						if (!hasArmor) {
							GL11.glColor3f(0f, 0f, 0f);
							GL11.glLineWidth(3f);
							GL11.glBegin(GL11.GL_LINES);

							GL11.glVertex2f(minX, base);
							GL11.glVertex2f(maxX, base);

							GL11.glEnd();

							GL11.glLineWidth(1f);
						}

						hasArmor = true;
						ItemArmor entArmor = (ItemArmor) armor.getItem();

						int armorType = entArmor.armorType;
						float minXArmor = minX + (linhasDiv * (armorType + 1));

						ArmorMaterial material = entArmor.getArmorMaterial();
						float armorDamageReduction = armorColor ? linhasDiv
								: (linhasDiv * material.getDamageReductionAmount(armorType))
										/ ArmorMaterial.DIAMOND.getDamageReductionAmount(armorType);

						getArmorColor(material);
						GL11.glBegin(GL11.GL_LINES);
						GL11.glVertex2f(minXArmor - armorDamageReduction + 1, base);
						GL11.glVertex2f(minXArmor + 1, base);
						GL11.glEnd();
					}
				}
			}
			if (hasArmor) {
				GL11.glColor3f(0f, 0f, 0f);
				GL11.glBegin(GL11.GL_POINTS);
				for (float i = 1; i < 4; i++) {
					GL11.glVertex2f(minX + 1 + (linhasDiv * i), base);
				}
				GL11.glEnd();
				spacingYUp += 6;
			}

			break;
		case 3: // down
			linhasDiv = ((maxX - minX - 2) / 4f);

			base = maxY + 5 + spacingYDown;

			for (int i = 1; 4 >= i; i++) {
				ItemStack armor = entLiving.getEquipmentInSlot(i);
				if (armor != null) {
					if (armor.getItem() instanceof ItemArmor) {
						if (!hasArmor) {
							GL11.glColor3f(0f, 0f, 0f);
							GL11.glLineWidth(3f);
							GL11.glBegin(GL11.GL_LINES);

							GL11.glVertex2f(minX, base);
							GL11.glVertex2f(maxX, base);

							GL11.glEnd();

							GL11.glLineWidth(1f);
						}

						hasArmor = true;
						ItemArmor entArmor = (ItemArmor) armor.getItem();

						int armorType = entArmor.armorType;
						float minXArmor = minX + (linhasDiv * (armorType + 1));

						ArmorMaterial material = entArmor.getArmorMaterial();
						float armorDamageReduction = armorColor ? linhasDiv
								: (linhasDiv * material.getDamageReductionAmount(armorType))
										/ ArmorMaterial.DIAMOND.getDamageReductionAmount(armorType);

						getArmorColor(material);
						GL11.glBegin(GL11.GL_LINES);
						GL11.glVertex2f(minXArmor - armorDamageReduction + 1, base);
						GL11.glVertex2f(minXArmor + 1, base);
						GL11.glEnd();
					}
				}
			}
			if (hasArmor) {
				GL11.glColor3f(0f, 0f, 0f);
				GL11.glBegin(GL11.GL_POINTS);
				for (float i = 1; i < 4; i++) {
					GL11.glVertex2f(minX + 1 + (linhasDiv * i), base);
				}
				GL11.glEnd();
				spacingYDown += 6;
			}

			break;
		}
	}

	private void drawName(float minX, float maxX, float minY, float maxY, EntityLivingBase entLiving) {
		String entName = entLiving.getDisplayName().getUnformattedText().toLowerCase();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		int textWidth = mc.fontRendererObj.getStringWidth(entName) / 2;

		switch (nameLocation) {
		case 0: // up
			mc.fontRendererObj.drawStringWithShadow(entName, minX + espWid - textWidth, minY - 10 - spacingYUp, 1f, 1f);
			spacingYUp += 10;
			break;
		case 1: // down
			mc.fontRendererObj.drawStringWithShadow(entName, minX + espWid - textWidth, maxY + 2 + spacingYDown, 1f,
					1f);
			spacingYDown += 10;
			break;
		}

		GL11.glDisable(GL11.GL_TEXTURE_2D);
	}

	private void drawItemName(float minX, float maxX, float minY, float maxY, EntityLivingBase entLiving) {
		ItemStack entItem = entLiving.getEquipmentInSlot(0);
		if (entItem != null) {
			String entItemName = entItem.getDisplayName().toLowerCase();
			GL11.glEnable(GL11.GL_TEXTURE_2D);

			int textWidth = mc.fontRendererObj.getStringWidth(entItemName) / 2;
			switch (itemNameLocation) {
			case 0: // up
				mc.fontRendererObj.drawStringWithShadow(entItemName, minX + espWid - textWidth, minY - 10 - spacingYUp,
						1f, 1f);
				spacingYUp += 10;
				break;
			case 1: // down
				mc.fontRendererObj.drawStringWithShadow(entItemName, minX + espWid - textWidth, maxY + 2 + spacingYDown,
						1f, 1f);
				spacingYDown += 10;
				break;
			}

			GL11.glDisable(GL11.GL_TEXTURE_2D);

		}
	}

	private void drawItemIcon(float minX, float maxX, float minY, float maxY, EntityLivingBase entLiving) {
		ItemStack item = entLiving.getEquipmentInSlot(0);
		if (item != null) {

			GL11.glEnable(GL11.GL_TEXTURE_2D);

			boolean shouldRotate = rotateWeaponIcon && (item.getItem() instanceof ItemSword
					|| item.getItem() instanceof ItemAxe || item.getItem() instanceof ItemPickaxe
					|| item.getItem() instanceof ItemSpade || item.getItem() instanceof ItemHoe || item.getItem() instanceof ItemBow);

			switch (itemIconLocation) {
			case 0: // up
				renderItem.renderItemIntoGUI(item, minX + espWid - 8, minY - 16 - spacingYUp, shouldRotate);
				break;

			case 1: // down
				renderItem.renderItemIntoGUI(item, minX + espWid - 8, maxY + 2 + spacingYDown, shouldRotate);
				break;
			}

			GL11.glDisable(GL11.GL_TEXTURE_2D);
		}
	}

	private float[] calculatePoints(Entity ent, float tick) {
		AxisAlignedBB bb = ent.getEntityBoundingBox().offset(-ent.posX, -ent.posY, -ent.posZ)
				.offset(ent.lastTickPosX + (ent.posX - ent.lastTickPosX) * tick,
						ent.lastTickPosY + (ent.posY - ent.lastTickPosY) * tick,
						ent.lastTickPosZ + (ent.posZ - ent.lastTickPosZ) * tick)
				.offset(-renderManager.renderPosX, -renderManager.renderPosY, -renderManager.renderPosZ);

		float minX = Float.MAX_VALUE;
		float minY = Float.MAX_VALUE;
		float maxX = -1f;
		float maxY = -1f;

		float bMinX = (float) bb.minX, bMaxX = (float) bb.maxX;
		float bMinY = (float) bb.minY, bMaxY = (float) bb.maxY;
		float bMinZ = (float) bb.minZ, bMaxZ = (float) bb.maxZ;

		float[][] boundingVertices = { { bMinX, bMinY, bMinZ }, { bMinX, bMaxY, bMinZ }, { bMaxX, bMaxY, bMinZ },
				{ bMaxX, bMinY, bMinZ }, { bMinX, bMinY, bMaxZ }, { bMinX, bMaxY, bMaxZ }, { bMaxX, bMaxY, bMaxZ },
				{ bMaxX, bMinY, bMaxZ } };

		for (float[] boundingVertex : boundingVertices) {
			Vector2f screenPos = WorldToScreen.worldToScreen(
					new Vector3f(boundingVertex[0], boundingVertex[1], boundingVertex[2]), mvMatrix, projectionMatrix,
					mc.displayWidth, mc.displayHeight);

			if (screenPos != null) {
				minX = Math.min(screenPos.x, minX);
				minY = Math.min(screenPos.y, minY);
				maxX = Math.max(screenPos.x, maxX);
				maxY = Math.max(screenPos.y, maxY);
			}
		}

		return new float[] { minX, minY, maxX, maxY };

	}
}

package elixe.utils.render;

import org.lwjgl.opengl.GL11;

import elixe.Elixe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;

public class ESPSetup {
	private final Minecraft mc = Elixe.INSTANCE.mc;

	public void setup2DStart() {
		GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		GL11.glOrtho(0.0, (double)mc.displayWidth, (double)mc.displayHeight, 0.0, -200, 10.0);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.enableTexture2D();
		GL11.glDepthMask(true);
		GL11.glLineWidth(1.0f);
	}

	public void setup2DEnd() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPopMatrix();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glPopMatrix();
		GL11.glPopAttrib();
	}
}

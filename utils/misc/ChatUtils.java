package elixe.utils.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class ChatUtils {
	public static void message(Minecraft mc, String text) {
        mc.thePlayer.addChatMessage(new ChatComponentText(text));
	}
}

package elixe.utils.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class ChatUtils {
	public static void message(Minecraft mc, boolean watermark, String text) {
        mc.thePlayer.addChatMessage(new ChatComponentText((watermark ? "[elixe] §7" : "§7") + text));
	}

	public static void message(Minecraft mc, String text) {
		message(mc, true, text);
	}
}

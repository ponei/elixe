package elixe.utils.misc;

import elixe.Elixe;

public class LoggingUtils {
	public static void out(String s) {
		System.out.println("[elixe " + Elixe.INSTANCE.build + "] " + s);
	}
}

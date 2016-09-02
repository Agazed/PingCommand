package net.agazed.ping;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.regex.Pattern;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Utils {
	private static Class<?> craftPlayer;
	private static Method handle;
	private static Field pingField;

	public static int getPing(Player player) throws Exception {
		int ping;
		Object converted = craftPlayer.cast(player);
		Object entityPlayer = handle.invoke(converted, new Object[0]);
		ping = pingField.getInt(entityPlayer);
		return ping;
	}

	static {
		try {
			String serverVersion = getServerVersion();
			craftPlayer = Class.forName("org.bukkit.craftbukkit."
					+ serverVersion + ".entity.CraftPlayer");
			Class<?> entityPlayer = Class.forName("net.minecraft.server."
					+ serverVersion + ".EntityPlayer");
			handle = craftPlayer.getMethod("getHandle", new Class[0]);
			pingField = entityPlayer.getField("ping");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getServerVersion() {
		Pattern oldBrand = Pattern.compile("(v|)[0-9][_.][0-9][_.][R0-9]*");
		Pattern newBrand = Pattern
				.compile("(v|)[0-9][_.][0-9][0-9][_.][R0-9]*");
		String pkg = Bukkit.getServer().getClass().getPackage().getName();
		String version = pkg.substring(pkg.lastIndexOf('.') + 1);
		if ((!oldBrand.matcher(version).matches())
				&& (!newBrand.matcher(version).matches())) {
			version = "";
		}
		return version;
	}
}
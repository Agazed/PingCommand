package net.agazed.ping;

import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Ping extends JavaPlugin {

	public void onEnable() {
		saveDefaultConfig();
		try {
			Metrics metrics = new Metrics(this);
			metrics.start();
		} catch (IOException e) {
			getLogger().info("Failed to submit metrics!");
		}
	}

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("ping")) {
			if (args.length == 0) {
				if (!(sender instanceof Player)) {
					sender.sendMessage("Command can only be run in-game!");
					return true;
				}
				try {
					Player player = (Player) sender;
					String formatself = getConfig().getString("format-self");
					String format = formatself.replace("%PING", Integer.toString(Utils.getPing(player)));
					format = ChatColor.translateAlternateColorCodes('&', format);
					sender.sendMessage(format);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return true;
			}
			if (args.length == 1) {
				Player target = getServer().getPlayerExact(args[0]);
				if (target == null) {
					sender.sendMessage(ChatColor.RED + "Player is offline or does not exist!");
					return true;
				}
				try {
					String formatplayer = getConfig().getString("format-player");
					String format = formatplayer.replace("%PLAYER", target.getName()).replace("%PING", Integer.toString(Utils.getPing(target)));
					format = ChatColor.translateAlternateColorCodes('&', format);
					sender.sendMessage(format);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return true;
			}
			sender.sendMessage(ChatColor.RED + "Usage: /ping [player]");
			return true;
		}
		if (cmd.getName().equalsIgnoreCase("pingreload")) {
			if (!sender.hasPermission("ping.reload")) {
				sender.sendMessage(ChatColor.RED + "No permission!");
				return true;
			}
			reloadConfig();
			sender.sendMessage(ChatColor.GREEN + "Successfully reloaded config!");
			return true;
		}
		return true;
	}
}
package com.allocinit.publicwarp;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class PublicWarp extends JavaPlugin {
	private SubCommand setCommand = new SetCommand(this);
	private SubCommand meCommand = new MeCommand(this);
	private SubCommand warpCommand = new WarpCommand(this);
	private SubCommand listCommand = new ListCommand(this);
	private SubCommand menuCommand = new MenuCommand(this);
	private SubCommand reloadCommand = new ReloadCommand(this);

	private PublicWarpDB db = new PublicWarpDB(this);
	private EssentialsAPI essentials;

	public PublicWarpDB getDB() {
		return db;
	}

	@Override
	public void onEnable() {
		this.getCommand("publicwarp").setExecutor(warpCommand);
		this.getCommand("publicwarpset").setExecutor(setCommand);
		this.getCommand("publicwarpme").setExecutor(meCommand);
		this.getCommand("publicwarplist").setExecutor(listCommand);
		this.getCommand("publicwarpmenu").setExecutor(menuCommand);
		this.getCommand("publicwarpreload").setExecutor(reloadCommand);
	}

	public void writeUsage(CommandSender player) {
		player.sendMessage("[" + ChatColor.GOLD + "Public Warp" + ChatColor.WHITE + "]");
		player.sendMessage(ChatColor.RED + "/pw " + ChatColor.GREEN + "- Alias for /publicwarp.");

		setCommand.writeUsage(player);
		meCommand.writeUsage(player);
		warpCommand.writeUsage(player);
		listCommand.writeUsage(player);
		menuCommand.writeUsage(player);
	}

	@Override
	public void onDisable() {
	}

	public synchronized EssentialsAPI getEssentials() {
		if (this.essentials == null) {
			try {
				essentials = new EssentialsAPI(this);
			} catch (Throwable t) {
			}
		}

		return essentials;
	}
}